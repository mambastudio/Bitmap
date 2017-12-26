/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display;

import bitmap.core.AbstractBitmap;
import bitmap.core.AbstractDisplay;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class StaticDisplay extends StackPane implements AbstractDisplay
{
    //Adding listeners to these triggers mouse dragging events
    public DoubleProperty translationDepth = new SimpleDoubleProperty();
    public ObjectProperty<Point2D> translationXY = new SimpleObjectProperty<>();
    
    //Image view
    private final ImageView imageView;
    
    //Mouse location
    double mouseLocX, mouseLocY; 
    
    //Bitmap image
    AbstractBitmap bitmap;
    
    public StaticDisplay()
    {
        imageView = new ImageView();
        getChildren().add(imageView);
        
        this.setOnMousePressed(this::mousePressed);
        this.setOnMouseDragged(this::mouseDragged);    
    }
    @Override
    public int getImageWidth() {
        if(bitmap != null)
            return bitmap.getWidth();
        else
            return -1;
    }

    @Override
    public int getImageHeight() {
        if(bitmap != null)
            return bitmap.getHeight();
        else
            return -1;
    }
    
    @Override
    public void imageFill(AbstractBitmap bitmap) {
        Platform.runLater(() -> {
            this.imageView.setImage(bitmap.getImage());        
            this.bitmap = bitmap;
        });
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
