/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import bitmap.display.ImageDisplay;
import bitmap.image.BitmapRGB;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author user
 */
public class StaticDisplayController implements Initializable {

    @FXML
    BorderPane pane;
    
     
    private ImageDisplay display;
    private BitmapRGB bitmap;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Display image
        bitmap = new BitmapRGB(100, 100);
        display = new ImageDisplay(bitmap);
        
        pane.setCenter(display); 
    }    
    
}
