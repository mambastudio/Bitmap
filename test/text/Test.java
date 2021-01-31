/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package text;

import static bitmap.util.ImageUtility.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Test  extends Application {
  
    @Override
    public void start(Stage primaryStage) 
    {
        StackPane root = new StackPane();
        root.getChildren().add(new ImageView(stringToImage("No image", 200, 200)));
        
        Scene scene = new Scene(root, 500, 450);
       
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
