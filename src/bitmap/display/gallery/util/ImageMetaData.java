/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ImageMetaData {
    private int height;
    private int width;
    private String mimeType;

    private ImageMetaData() {

    }
    
    public ImageMetaData(File file) {
        if(!file.isFile())
            throw new UnsupportedOperationException("file not valid");
        
        InputStream is;
        try 
        {
            is = new FileInputStream(file);
            processStream(is);
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(ImageMetaData.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ImageMetaData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     /**
     *  
     * -> aw/(ow/oh) = ah
     * -> ow/oh = aspect ratio = r
     * -> hence 
     * -> aw/r = ah
     * 
     * @param targetWidth
     * @param targetHeight
     * @return 
     */
    public double[] getAdjustedSize(double targetWidth, double targetHeight)
    {
        double oldWidth = width;
        double oldHeight = height;
        
        // smaller ratio will ensure that the image fits in the view
        double ratio = Math.min(targetWidth / oldWidth, targetHeight / oldHeight);

        double newWidth = (int)(oldWidth * ratio);
        double newHeight = (int)(oldHeight * ratio);
        
        return new double[]{newWidth, newHeight};        
    }

    private void processStream(InputStream is) throws IOException {
        int c1 = is.read();
        int c2 = is.read();
        int c3 = is.read();
                
        mimeType = null;
        width = height = -1;

        if (c1 == 'G' && c2 == 'I' && c3 == 'F') { // GIF
            is.skip(3);
            width = readInt(is,2,false);
            height = readInt(is,2,false);
            mimeType = "image/gif";
        } else if (c1 == 0xFF && (c2 == 0xD8 ||
                                  c2 == 0xC4)) { // JPG
           // System.out.println("kubafu");
            while (c3 == 255) {
                int marker = is.read();
                int len = readInt(is,2,true);
                if (marker == 192 || marker == 193 || marker == 194) {
                    is.skip(1);
                    height = readInt(is,2,true);
                    width = readInt(is,2,true);
                    mimeType = "image/jpeg";
                    break;
                }
                is.skip(len - 2);
                c3 = is.read();
            }
        } else if (c1 == 137 && c2 == 80 && c3 == 78) { // PNG
            is.skip(15);
            width = readInt(is,2,true);
            is.skip(2);
            height = readInt(is,2,true);
            mimeType = "image/png";
        } else if (c1 == 66 && c2 == 77) { // BMP
            is.skip(15);
            width = readInt(is,2,false);
            is.skip(2);
            height = readInt(is,2,false);
            mimeType = "image/bmp";
        } else if (c1 == '#' && c2 == '?' && c3 == 'R') { // HDR
            is.mark(500); //
            int c4 = is.read();
            if(c4 == 'A')
            {
                
                byte[] bytes = new byte[400];
                is.read(bytes);
                String string = new String(bytes);                
                int index = string.indexOf("+X");                
                if(index >= 0)
                {
                    String str = string.substring(index);
                    str = str.replaceAll("[^0-9]+", " ");
                    String[] strArr = str.trim().split(" ");
                    width = Integer.parseInt(strArr[0]);                    
                }
                index = string.indexOf("-X");                
                if(index >= 0)
                {
                    String str = string.substring(index);
                    str = str.replaceAll("[^0-9]+", " ");
                    String[] strArr = str.trim().split(" ");
                    width = Integer.parseInt(strArr[0]);                    
                }
                
                index = string.indexOf("+Y");                
                if(index >= 0)
                {
                    String str = string.substring(index);
                    str = str.replaceAll("[^0-9]+", " ");
                    String[] strArr = str.trim().split(" ");
                    height = Integer.parseInt(strArr[0]);                    
                }
                index = string.indexOf("-Y");                
                if(index >= 0)
                {
                    String str = string.substring(index);
                    str = str.replaceAll("[^0-9]+", " ");
                    String[] strArr = str.trim().split(" ");
                    height = Integer.parseInt(strArr[0]);                    
                }
                
                mimeType = "image/hdr";
            }
            else
                is.reset();
        } else {
            int c4 = is.read();
            if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42)
                    || (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) { //TIFF
                boolean bigEndian = c1 == 'M';
                int ifd = 0;
                int entries;
                ifd = readInt(is,4,bigEndian);
                is.skip(ifd - 8);
                entries = readInt(is,2,bigEndian);
                for (int i = 1; i <= entries; i++) {
                    int tag = readInt(is,2,bigEndian);
                    int fieldType = readInt(is,2,bigEndian);
                    long count = readInt(is,4,bigEndian);
                    int valOffset;
                    if ((fieldType == 3 || fieldType == 8)) {
                        valOffset = readInt(is,2,bigEndian);
                        is.skip(2);
                    } else {
                        valOffset = readInt(is,4,bigEndian);
                    }
                    if (tag == 256) {
                        width = valOffset;
                    } else if (tag == 257) {
                        height = valOffset;
                    }
                    if (width != -1 && height != -1) {
                        mimeType = "image/tiff";
                        break;
                    }
                }
            }
        }
        if (mimeType == null) {
            mimeType = "null file";
        }
    }

    private int readInt(InputStream is, int noOfBytes, boolean bigEndian) throws IOException {
        int ret = 0;
        int sv = bigEndian ? ((noOfBytes - 1) * 8) : 0;
        int cnt = bigEndian ? -8 : 8;
        for(int i=0;i<noOfBytes;i++) {
            ret |= is.read() << sv;
            sv += cnt;
        }
        return ret;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String toString() {
        return "MIME Type : " + mimeType + "\t Width : " + width + "\t Height : " + height;
    }
    
    public boolean isValid()
    {
        return !getMimeType().contains("null file");
    }
}
