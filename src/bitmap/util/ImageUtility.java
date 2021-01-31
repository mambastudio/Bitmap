/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.util;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 *
 * @author user
 */
public class ImageUtility {
    
    public static Image stringToImage(String string)
    {
        return stringToImage(string, 500, 500);
    }
    
    public static Image stringToImage(String string, int width, int height)
    {
        return stringToImage(string, Font.font("Verdana", 20), Color.DARKGREY, width, height);
    }
    
    public static Image stringToImage(String string, Font font, Color stringColor, int width, int height)
    {
        TextFlow textflow = new TextFlow(); 
        Text text = new Text(string);
        text.setFill(stringColor); 
        text.setFont(font);
        textflow.getChildren().add(text);
        textflow.setTextAlignment(TextAlignment.CENTER);
        
        //https://stackoverflow.com/questions/59720341/how-to-make-left-aligned-textflow-in-the-center-of-the-window-in-javafx
        FlowPane pane = new FlowPane(textflow);
        pane.setAlignment(Pos.CENTER);
        StackPane root = new StackPane(pane);
        root.setStyle("-fx-background-color: #000000;");
        
        Scene scene = new Scene(root, width, height);
        
        WritableImage img = new WritableImage(width, height) ;
        scene.snapshot(img);
        return img ;
    }
}
