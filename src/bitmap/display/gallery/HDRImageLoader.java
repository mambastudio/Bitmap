/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery;

import bitmap.image.BitmapRGBE;
import bitmap.reader.HDRBitmapReader;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author user
 */
public class HDRImageLoader extends ImageLoaderImpl{
    
    
    private static final int BYTES_PER_PIXEL = 3; // RGB

    private final InputStream input;
    
    
    
    protected HDRImageLoader(InputStream input) {
        super(HDRDescriptor.getInstance());

        if (input == null) {
                throw new IllegalArgumentException("input == null!");
        }

        this.input = input;
    }

    @Override
    public void dispose() {        
        
    }

    @Override
    public ImageFrame load(int imageIndex, int width, int height, boolean preserveAspectRatio, boolean smooth) throws IOException {
        
        if (0 != imageIndex) {
            return null;
        }
                
        HDRBitmapReader reader = new HDRBitmapReader();
        BitmapRGBE bitmap = reader.load(input);
              
        int imageWidth, imageHeight;
       
                
        //https://stackoverflow.com/questions/39408845/how-to-get-width-height-of-displayed-image-in-javafx-imageview
        
        imageWidth = width > 0 ? width :  bitmap.getWidth();
        imageHeight = height > 0 ? height :  bitmap.getHeight();
        
        //preserve ratio based on original
        float aspectRatio = (float)bitmap.getWidth() / bitmap.getHeight(); //to preserve ratio     
        imageWidth = (int) Math.min(imageWidth, imageHeight * aspectRatio);
        imageHeight = (int) Math.min(imageHeight, imageWidth / aspectRatio);
        
        byte[] imageData = bitmap.getByteArrayRGBScaled(imageWidth, imageHeight, 2.2);
        ByteBuffer buffer = ByteBuffer.wrap(imageData);        
        
        int stride = imageWidth * BYTES_PER_PIXEL;
                
        return new ImageFrame(ImageStorage.ImageType.RGB, buffer, imageWidth,
				imageHeight, stride, null, 1, null);
    }
    
}
