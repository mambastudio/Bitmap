/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display;

import bitmap.core.AbstractDisplay;
import java.util.HashMap;
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
import bitmap.core.BitmapInterface;

/**
 *
 * @author user
 */
public class StaticDisplay extends StackPane implements AbstractDisplay
{
    //Adding listeners to these triggers mouse dragging events
    public DoubleProperty translationDepth = new SimpleDoubleProperty();
    public ObjectProperty<Point2D> translationXY = new SimpleObjectProperty<>();
      
    //Image view array
    private final HashMap<String, ImageView> imageArray;
    
    //Mouse location
    double mouseLocX, mouseLocY; 
            
    public StaticDisplay()
    {
        this.imageArray = new HashMap<>();
        this.imageArray.put("base", new ImageView());
        this.imageArray.put("selection", new ImageView()); get("selection").setBlendMode(BlendMode.SRC_OVER);
        
        getChildren().addAll(imageArray.get("base"), imageArray.get("selection"));
                
        this.setOnMousePressed(this::mousePressed);
        this.setOnMouseDragged(this::mouseDragged);    
        this.imageArray.get("base").setOnMouseMoved(this::mouseMoved);
       
    }
    
    public ImageView get(String name)
    {
        return imageArray.get(name);
    }
    
    public void imageFillSelection(BitmapInterface selectionBitmap)
    {
        Platform.runLater(() ->this.get("selection").setImage(selectionBitmap.getImage()));
    }
    
    @Override
    public int getImageWidth() {
        if(get("base") != null)
            return (int) get("base").getImage().getWidth();
        else
            return -1;
    }

    @Override
    public int getImageHeight() {
        if(get("base") != null)
            return (int) get("base").getImage().getHeight();
        else
            return -1;
    }
    
    @Override
    public void imageFill(BitmapInterface bitmap) {
        Platform.runLater(() -> this.get("base").setImage(bitmap.getImage()));
    }
    
    @Override
    public void imageFill(String name, BitmapInterface bitmap) {
        Platform.runLater(() -> this.get(name).setImage(bitmap.getImage()));
    }    
    
    public void mouseMoved(MouseEvent e)
    {
        
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
