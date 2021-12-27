/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rect;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import bitmap.display.gallery.panel.RectImageContainer;

/**
 * FXML Controller class
 *
 * @author user
 */
public class RectImageFXMLController implements Initializable {
    
    @FXML
    ScrollPane spane;
    @FXML
    Pane pane;
    
    DirectoryChooser directoryChooser = new DirectoryChooser();
    RectImageContainer container;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        container = new RectImageContainer(spane, pane);
        spane.setFitToWidth(true);
    } 
    
    public void loadAction(ActionEvent e)
    {        
        File selectedDirectory = directoryChooser.showDialog(spane.getScene().getWindow());
        if(selectedDirectory != null)
            container.addAll(getFileImages(selectedDirectory.toPath()));
       
    }
    
    public void removeAction(ActionEvent e)
    {        
        container.deleteSelection();
        container.draw();
       
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
