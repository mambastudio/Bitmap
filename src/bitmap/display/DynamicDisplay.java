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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class DynamicDisplay extends StackPane implements AbstractDisplay
{
    public Canvas canvas;  
         
    //Adding listeners to these variables triggers frame resize
    public DoubleProperty viewportW = new SimpleDoubleProperty();
    public DoubleProperty viewportH = new SimpleDoubleProperty();
    
    //Adding listeners to these triggers mouse dragging events
    public DoubleProperty translationDepth = new SimpleDoubleProperty();
    public ObjectProperty<Point2D> translationXY = new SimpleObjectProperty<>();
        
    AbstractBitmap tile;
    
    int x, y, w = 1, h = 1;
    GraphicsContext gc;
    
    double mouseLocX, mouseLocY;  
    
    boolean useTileSize = true;
    
    
    public DynamicDisplay()
    {                
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
        
        this.setOnMousePressed(this::mousePressed);
        this.setOnMouseDragged(this::mouseDragged);           
    }
    
    public DynamicDisplay(boolean useTileSize)
    {
        this();
        this.useTileSize = useTileSize;
    }
        
    @Override
    protected void layoutChildren()
    {
        final int top = (int)snappedTopInset();
        final int right = (int)snappedRightInset();
        final int bottom = (int)snappedBottomInset();
        final int left = (int)snappedLeftInset();
        final int ww = (int)getWidth() - left - right;
        final int hh = (int)getHeight() - top - bottom;
        
        canvas.setLayoutX(left);
        canvas.setLayoutY(top);
        
        if (ww != canvas.getWidth() || hh != canvas.getHeight()) {
            canvas.setWidth(ww);
            canvas.setHeight(hh);
            
            viewportW.setValue(ww);
            viewportH.setValue(hh);
            
            //If you put clear rect in repaint, alot of flickering is experienced
            //Not sure why but putting it here to only be called during frame size change
            //solves the issue (flickering is still experienced but only during resizing)
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            
            calculateBounds();             
        }
    }
    
    public final void calculateBounds()
    {
        int width  = (int) getWidth();
        int height = (int) getHeight();
      
        w = (int)(width     * 0.9D);
        h = (int)(height    * 0.9D);
        if (w > h) 
            w = h;    
        if (w < h) 
            h = w;
        
        if(w <= 0) w = 1;
        if(h <= 0) h = 1;
            
        x = (width - w) / 2;
        y = (height - h) / 2;    
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

    @Override
    public void imagePaint() 
    {   
        Platform.runLater(() -> {
            if(w > 0 && h > 0 && tile != null)
                gc.drawImage(tile.getImage(), x, y, w, h);  
        });         
    }

    @Override
    public int getImageWidth() {
        return w;
    }

    @Override
    public int getImageHeight() {
        return h;
    }
    
    @Override
    public void imageFill(AbstractBitmap bitmap) {
        this.tile = bitmap;
        imagePaint();
    }
}
