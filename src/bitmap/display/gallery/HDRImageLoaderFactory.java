/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import com.sun.javafx.iio.ImageStorage;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author user
 */
public class HDRImageLoaderFactory implements ImageLoaderFactory {
    
    private static final HDRImageLoaderFactory instance = new HDRImageLoaderFactory();


    public static final void install() {           
            ImageStorage.addImageLoaderFactory(instance);
    }

    @Override
    public ImageFormatDescription getFormatDescription() {
        return HDRDescriptor.getInstance();
    }

    @Override
    public ImageLoader createImageLoader(InputStream in) throws IOException {        
        return new HDRImageLoader(in);
    }
    
}
