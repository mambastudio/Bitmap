/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazyimage;

import bitmap.ImageMetaData;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

/**
 *
 * @author user
 */
public class LazyImage extends WritableImage {
    private ImageMetaData imageData;
    
    public LazyImage(Path path)
    {
        this(path, 200, 200);
    }
    
    public LazyImage(Path path, int targetWidth, int targetHeight)
    {
        this(new ImageMetaData(path.toFile()), targetWidth, targetHeight);
    }
    
    public LazyImage(ImageMetaData imageData, int targetWidth, int targetHeight)
    {
        super(
                (int)imageData.getAdjustedSize(targetWidth, targetHeight)[0], 
                (int)imageData.getAdjustedSize(targetWidth, targetHeight)[1]);
        
        this.imageData = imageData;
        
        System.out.println(getWidth()+ " " +getHeight());
        
        try {                 
            final Image image = new Image(imageData.getFile().toURI().toURL().toExternalForm(), this.getWidth(), getHeight(), true, true, true);
            image.progressProperty().addListener((obs, ov, nv)->{
                if(nv.intValue() == 1)         
                {                   
                    PixelReader pr = image.getPixelReader();                        
                    WritablePixelFormat wf = PixelFormat.getIntArgbInstance(); 
                    int[] buffer = new int[(int)(image.getWidth()*image.getHeight()*4)]; 

                    pr.getPixels(0, 0, (int)image.getWidth(), (int)image.getHeight(), wf, buffer, 0, (int)image.getWidth()*4);
                    this.getPixelWriter().setPixels(0, 0, (int) image.getWidth(), (int)image.getHeight(), wf, IntBuffer.wrap(buffer), (int)image.getWidth()*4);
                }
            });
            

        } catch (IOException ex) {
            Logger.getLogger(LazyImage.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    
}
