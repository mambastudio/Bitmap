/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import bitmap.display.gallery.HDRImageLoaderFactory;
import bitmap.ImageMetaData;
import bitmap.reader.HDRBitmapReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author user
 */
public class Test {
    static 
    {
        HDRImageLoaderFactory.install();
    }
    public static void main(String... args)
    {
        String path = "C:\\Users\\user\\Pictures\\Saved Pictures\\blaubeuren_night_1k.hdr";
        InputStream f  = null;
        try {
            f = new BufferedInputStream(new FileInputStream(path));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HDRBitmapReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Image image = new Image(f);
        System.out.println(image.getWidth());
        
    }
}
