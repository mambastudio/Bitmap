/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.image;

import bitmap.Color;
import bitmap.ColorCoding;
import bitmap.core.BitmapInterface;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

/**
 *
 * @author user
 */
public class BitmapRGBE implements BitmapInterface
{
    private final int w;
    private final int h;
    private final int[] rgbeData;
    private static final float[] EXPONENT = new float[256];
    
    static {
        EXPONENT[0] = 0;
        for (int i = 1; i < 256; i++) {
            float f = 1.0f;
            int e = i - (128 + 8);
            if (e > 0)
                for (int j = 0; j < e; j++)
                    f *= 2.0f;
            else
                for (int j = 0; j < -e; j++)
                    f *= 0.5f;
            EXPONENT[i] = f;
        }
    }

    
    public BitmapRGBE(int w, int h, int[] data) {
        this.w = w;
        this.h = h;
        this.rgbeData = data;
    }

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }
    
    public byte[] getByteArrayRGB()
    {
        byte[] dataByte = new byte[w * h * 3];
        for(int i = 0; i<rgbeData.length; i++)
        {
            int rgbe = rgbeData[i];
            byte[] tempDataByte = ColorCoding.rgbeToBytes(rgbe);
            dataByte[i*3 + 0] = tempDataByte[0];
            dataByte[i*3 + 1] = tempDataByte[1];
            dataByte[i*3 + 2] = tempDataByte[2];
        }
        return dataByte;
    }
    
    public byte[] getByteArrayRGBScaled(int w2, int h2, double gamma)
    {
        //scale the image to the defined w2, h2
        //http://tech-algorithm.com/articles/nearest-neighbor-image-scaling/
        int w1 = w; int h1 = h;
        int[] temp = new int[w2*h2] ;
        double x_ratio = w1/(double)w2 ;
        double y_ratio = h1/(double)h2 ;
        double px, py ; 
        
        for (int i=0;i<h2;i++) {
            for (int j=0;j<w2;j++) {
                px = Math.floor(j*x_ratio) ;
                py = Math.floor(i*y_ratio) ;
                temp[(i*w2)+j] = rgbeData[(int)((py*w1)+px)] ;
            }
        }
        
        //same as getByteArrayRGB method
        byte[] dataByte = new byte[w2 * h2 * 3];
        for(int i = 0; i<temp.length; i++)
        {
            int rgbe = temp[i];
            byte[] tempDataByte = ColorCoding.rgbeToBytes(rgbe, gamma); 
            dataByte[i*3 + 0] = tempDataByte[0];
            dataByte[i*3 + 1] = tempDataByte[1];
            dataByte[i*3 + 2] = tempDataByte[2];
        }
        return dataByte;
    }
    
    public float[] getLuminanceArray()
    {
        float lumMax = 0;
        float[] lum = new float[w * h];
        for (int y=0; y<h; y++) {
            for (int x=0; x<w; x++) {
                int index = x + w*y;
                float[] floatRGB = ColorCoding.toFloatRGBE(rgbeData[index]);
                Color color = new Color(floatRGB[0], floatRGB[1], floatRGB[2]);
                lum[index] = color.luminance();
                if(lum[index] > lumMax)
                    lumMax = lum[index];
            }
        }
        
        return lum;
    }
    
    public float[] getScaledLuminanceArray(int w2, int h2)
    {
        float lumMax = 0;
        
        //scale the image to the defined w2, h2
        //http://tech-algorithm.com/articles/nearest-neighbor-image-scaling/
        int w1 = w; int h1 = h;
        float[] temp = new float[w2*h2] ;
        double x_ratio = w1/(double)w2 ;
        double y_ratio = h1/(double)h2 ;
        double px, py ; 
        
        for (int i=0;i<h2;i++) {
            for (int j=0;j<w2;j++) {
                px = Math.floor(j*x_ratio) ;
                py = Math.floor(i*y_ratio) ;
                float[] floatRGB = ColorCoding.toFloatRGBE(rgbeData[(int)((py*w1)+px)]);
                Color color = new Color(floatRGB[0], floatRGB[1], floatRGB[2]);
                temp[(i*w2)+j] = color.luminance();
                if(color.luminance() > lumMax)
                    lumMax = color.luminance();
            }
        }
        System.out.println("maximum luminance32 " +lumMax);
        return temp;
    }
    
    public Color getColor(int index)
    {
        Color color = new Color();
        color.setRGBE(getRGBE(index));
        return color;
    }
    
    public int getRGBE(int index)
    {
        try{
            return rgbeData[index];
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            Logger.getLogger(BitmapRGBE.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;        
    }
    
    public int getRGBE(int x, int y)
    {
        try{
            int index = x + w*y;
            return rgbeData[index];
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            Logger.getLogger(BitmapRGBE.class.getName()).log(Level.SEVERE, null, ex);        
        }
        return 0;        
        
    }
        
    ////https://stackoverflow.com/questions/39408845/how-to-get-width-height-of-displayed-image-in-javafx-imageview
    private int[] getScaledSize(int width, int height)
    {
        int imageWidth = width > 0 ? width :  getWidth();
        int imageHeight = height > 0 ? height :  getHeight();
        
        //preserve ratio based on original
        float aspectRatio = (float)getWidth() / getHeight(); //to preserve ratio     
        imageWidth = (int) Math.min(imageWidth, imageHeight * aspectRatio);
        imageHeight = (int) Math.min(imageHeight, imageWidth / aspectRatio);
        
        return new int[]{imageWidth, imageHeight};
    }
    
    public Image getScaledImage(int width, int height, double gamma)
    {
        int[] newsize = getScaledSize(width, height);
        int w2 = newsize[0]; int h2 = newsize[1];
        
        //http://tech-algorithm.com/articles/nearest-neighbor-image-scaling/
        int w1 = w; int h1 = h;
        int[] temp = new int[w2*h2] ;
        double x_ratio = w1/(double)w2 ;
        double y_ratio = h1/(double)h2 ;
        double px, py ; 
        
        for (int i=0;i<h2;i++) {
            for (int j=0;j<w2;j++) {
                px = Math.floor(j*x_ratio) ;
                py = Math.floor(i*y_ratio) ;
                temp[(i*w2)+j] = rgbeData[(int)((py*w1)+px)] ;
            }
        }
        
        //argb
        int[] argb = new int[w2 * h2];
        for(int i = 0; i<temp.length; i++)
        {
            int rgbe = temp[i];
            argb[i] = ColorCoding.rgbeToArgb(rgbe, gamma);
        }
        
        WritableImage wImage = new WritableImage(w2, h2);        
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance(); 
        wImage.getPixelWriter().setPixels(0, 0, w2, h2, pixelFormat, argb, 0, w2);
        return wImage;
    }
    
    public float[] getFloat4Data()
    {
        float[] rgb4 = new float[w * h * 4];
        
        int length = w * h;
        for(int i = 0; i< length; i++)
        {
            int rgbe = rgbeData[i];
            float[] floatRGB = ColorCoding.toFloatRGBE(rgbe); //returns [r, g, b]
            int index = i * 4;
            rgb4[index + 0] = floatRGB[0];
            rgb4[index + 1] = floatRGB[1];
            rgb4[index + 2] = floatRGB[2];
            rgb4[index + 3] = 1;
        }
        
        return rgb4;
    }
}
