/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import bitmap.Color;
import bitmap.display.DynamicDisplay;
import bitmap.image.BitmapBGRA;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author user
 */
public class DynamicDisplayController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    BorderPane pane;
    
    private final DynamicDisplay display = new DynamicDisplay();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pane.setCenter(display);
        
        //Draw using a thread since it can't draw through repainting
        LambdaThread lambdaThread = new LambdaThread();
        lambdaThread.startExecution(() -> {
            while(true)
            {
                Platform.runLater(() -> draw());
                lambdaThread.chill();
                lambdaThread.pauseExecution();
            }
        });
        
        this.display.viewportW.addListener((observable, old_value, new_value) -> {
            lambdaThread.resumeExecution();
        });
        
        this.display.viewportH.addListener((observable, old_value, new_value) -> {
            lambdaThread.resumeExecution();
        });
    }    
    
    public void draw()
    {
        int w = display.getImageWidth();
        int h = display.getImageHeight();
        
        BitmapBGRA bitmap = new BitmapBGRA(w, h);
        
        for(int j = 0; j<h; j++)
            for(int i = 0; i<w; i++)
            {
                bitmap.writeColor(Color.RED, 1, i, j);
            }
        display.imageFill(bitmap);
        //System.out.println(w);
    }
}
