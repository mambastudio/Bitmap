/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display;

import bitmap.core.AbstractBitmap;
import bitmap.core.AbstractDisplay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class BlendDisplay extends StackPane implements AbstractDisplay {
    //Adding listeners to these triggers mouse dragging events
    public DoubleProperty translationDepth = new SimpleDoubleProperty();
    public ObjectProperty<Point2D> translationXY = new SimpleObjectProperty<>();
      
    //Image view array
    private final HashMap<String, ImageView> imageArray;
    
    //Mouse location
    double mouseLocX, mouseLocY; 
    
    public BlendDisplay()
    {
        this.imageArray = new HashMap<>();     
        this.setOnMousePressed(this::mousePressed);
        this.setOnMouseDragged(this::mouseDragged);    
    } 
    
    public BlendDisplay(String... layers)
    {
        this();
        initDisplay(layers);
    }
    
    public final void initDisplay(String... layers)
    {
        for(int i = 0; i<layers.length; i++)
            if(i == 0)
                imageArray.put(layers[i], new ImageView());
            else
            {
                imageArray.put(layers[i], new ImageView());
                imageArray.get(layers[i]).setBlendMode(BlendMode.SRC_OVER);
            }
        
        for(String name : layers)        
            getChildren().add(imageArray.get(name));                    
    }
        
    public ImageView get(String name)
    {
        return imageArray.get(name);
    }
    
    public void set(String name, AbstractBitmap bitmap)
    {
        boolean namePresent = false;
        List<String> list = new ArrayList<>(imageArray.keySet());
        namePresent = list.stream().map((string) -> string.equals(name)).reduce(namePresent, (accumulator, _item) -> accumulator | _item);       
        if(namePresent)
            Platform.runLater(() ->this.get(name).setImage(bitmap.getImage()));
    }
        
    @Override
    public int getImageWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getImageHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void imageFill(String name, AbstractBitmap bitmap) {
        Platform.runLater(() -> this.get(name).setImage(bitmap.getImage()));
    }  
    
    public void mousePressed(MouseEvent e)
    {     
        mouseLocX = e.getX();
        mouseLocY = e.getY();        
    }
    
    public void mouseDragged(MouseEvent e)
    {        
        float currentLocX = (float) e.getX();
        float currentLocY = (float) e.getY();
        
        float dx = (float) (currentLocX - mouseLocX);
        float dy = (float) (currentLocY - mouseLocY);
      
        if (e.isSecondaryButtonDown())
        {
            translationDepth.setValue(dy * 0.1f);            
        }
        else
        {
            Point2D pointxy = new Point2D(-dx*0.5, -dy*0.5);
            translationXY.setValue(pointxy);    
            
        }
        this.mouseLocX = currentLocX;
        this.mouseLocY = currentLocY;                    
    }    
}
