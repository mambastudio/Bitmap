/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazyimage;

import bitmap.display.gallery.HDRImageLoaderFactory;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class TestLazyImage extends Application {
    
    static 
    {
        HDRImageLoaderFactory.install();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Lazy Image");
        StackPane layout = new StackPane();
        
        ImageView imageView = new ImageView();
        imageView.setImage(new LazyImage(Paths.get("C:\\Users\\user\\Pictures\\Saved Pictures\\gym_entrance_8k.hdr")));
        //imageView.setPreserveRatio(true);
        imageView.setFitWidth(500);
        imageView.setFitHeight(500);
        layout.getChildren().addAll(imageView);
        
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.setScene(new Scene(layout));
        primaryStage.show();
    }
}