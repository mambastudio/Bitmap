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
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class SimpleHDRViewer extends Application {
    static 
    {
        HDRImageLoaderFactory.install();
    }
    
    @Override
    public void start(Stage primaryStage) throws MalformedURLException 
    {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 700, 450);
        
        
        String path = "C:\\Users\\user\\Pictures\\Saved Pictures\\memorial.hdr";
     
        Image image = new Image(new File(path).toURI().toURL().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        
        root.getChildren().add(imageView);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
    }
    
    public static void main(String[] args) 
    {
        launch(args);
    }
}
