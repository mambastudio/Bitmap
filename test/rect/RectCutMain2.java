/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rect;

import bitmap.display.gallery.panel.NodeImageSelection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import bitmap.display.gallery.panel.NodeImage;

/**
 *
 * @author user
 */
public class RectCutMain2  extends Application { 
    
    NodeImage nodeImage;
    NodeImageSelection selection;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Creating a Group object  
        Pane root = new Pane(); 

        //Creating a scene object 
        nodeImage = new NodeImage("C:\\Users\\user\\Pictures\\Irenne\\JOE_1183.jpg");
        
        root.getChildren().add(nodeImage);
        Scene scene = new Scene(root, 750, 700); 
        
        

        //Setting title to the Stage 
        primaryStage.setTitle("Drawing a Rectangle"); 

        //Adding scene to the stage 
        primaryStage.setScene(scene); 

        //Displaying the contents of the stage 
        primaryStage.show(); 
        
        
    }
    
    public static void main(String args[]){ 
        launch(args); 
    } 
}
