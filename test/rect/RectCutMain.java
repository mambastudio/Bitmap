/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rect;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import bitmap.display.gallery.panel.RectImageContainer;

/**
 *
 * @author user
 */
public class RectCutMain extends Application { 
    
    RectImageContainer container;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Creating a Group object  
        Pane root = new Pane(); 

        //Creating a scene object 
        ScrollPane spane = new ScrollPane(root);
        //spane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //spane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Scene scene = new Scene(spane, 750, 700);  

        //Setting title to the Stage 
        primaryStage.setTitle("Drawing a Rectangle"); 

        //Adding scene to the stage 
        primaryStage.setScene(scene); 

        //Displaying the contents of the stage 
        primaryStage.show(); 
        
                
        container = new RectImageContainer(spane, root);
        container.addAll(getFileImages(Paths.get("C:\\Users\\user\\Pictures")));
        

        spane.setFitToWidth(true);
        
    }

    public ImageView getImage(String url)
    {
        File file = new File(url);
        Image image = null;
        try {
             image = new Image(file.toURI().toURL().toExternalForm(), 1000, 1000, true, true, false);
             image.progressProperty().addListener((obs, ov, nv)->{
                 if(nv.intValue() == 1)
                 {
                     container.draw();
                 }
             });
        } catch (MalformedURLException ex) {
            Logger.getLogger(RectCutMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(image != null)
        {
            
            ImageView v = new ImageView(image);
            v.setFitWidth(200);
            v.setFitHeight(200);
            v.setPreserveRatio(true);
            
            return v;
        }
        else
            return null;
    }
    
    
    public static void main(String args[]){ 
        launch(args); 
    } 
    
    
    
    public File[] getFileImages(Path pathFolder)
    {        
        //Filter list of files
        File[] files = pathFolder.toFile().listFiles((dir, name)->{
            if(name.toLowerCase().endsWith(".jpg"))
                return true;
            else if(name.toLowerCase().endsWith(".jpeg"))
                return true;
            return false;
        });   
        
        return files;        
    } 
}
