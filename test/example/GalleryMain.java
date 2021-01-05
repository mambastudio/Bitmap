/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import bitmap.display.gallery.GalleryLoader;
import bitmap.display.gallery.HDRImageLoaderFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class GalleryMain extends Application {
    static 
    {
        HDRImageLoaderFactory.install();       
    }
    @Override
    public void start(Stage primaryStage) 
    {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 700, 250);
        
        GalleryLoader gallery = new GalleryLoader(50, 50);
        root.getChildren().add(gallery);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        primaryStage.show();
    }
    
    public static void main(String[] args) 
    {
        launch(args);
    }
}
