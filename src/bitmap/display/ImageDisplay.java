/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display;

import bitmap.Color;
import bitmap.core.AbstractBitmap;
import bitmap.core.AbstractDisplay;
import bitmap.util.ZoomUtility;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;


/**
 *
 * @author user
 */
public class ImageDisplay extends StackPane implements AbstractDisplay{
    private int w, h;    
    private ImageView imageView;
    private AbstractBitmap bitmap;
    private boolean imageMoveable;
    
    public ImageDisplay()
    {
        this(null, true);
    }
        
    public ImageDisplay(AbstractBitmap bitmap)
    {
        this(bitmap, true);
    }
    
    public ImageDisplay(AbstractBitmap bitmap, boolean moveable)
    {
        this.imageView = new ImageView();
    
        if(bitmap != null)        
            imageFill(bitmap);
                
        getChildren().add(imageView);
        this.imageMoveable = moveable;
        
        if(imageMoveable)
        {
            ZoomUtility.reset();
        
            setOnMousePressed((MouseEvent e) -> {
                ZoomUtility.setOnMousePressed(e.getX(), e.getY());                
            });
        
            setOnMouseDragged((MouseEvent e) -> {
                ZoomUtility.pan(imageView, e);            
            });
        
            setOnScroll((ScrollEvent event) -> {
                ZoomUtility.zoom(imageView, event);          
            });   
        }
        
        imageView.setScaleX(1);
        imageView.setScaleY(1);
       
        imageView.setTranslateX(0);
        imageView.setTranslateY(0);     
    }
    
    @Override
    public final void imageFill(AbstractBitmap bitmap)
    {
        this.imageView.setImage(bitmap.getImage());
        this.w = bitmap.getWidth(); this.h = bitmap.getHeight();
        this.bitmap = bitmap;
    }
    
    @Override
    public void imageFill(float x, float y, Color c)
    {
        this.bitmap.writeColor(c, 1, (int)x, (int)y);        
    }
    
    @Override
    public void imagePaint()
    {
        this.imageView.setImage(bitmap.getImage());
    }
    
    @Override
    public int getImageWidth() {
        return w;
    }

    @Override
    public int getImageHeight() {
        return h;
    }
    
}
