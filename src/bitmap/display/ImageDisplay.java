/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display;

import bitmap.Color;
import bitmap.core.AbstractDisplay;
import bitmap.util.ZoomUtility;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import bitmap.core.BitmapInterface;
import javafx.scene.shape.Rectangle;


/**
 *
 * @author user
 */
public final class ImageDisplay extends StackPane implements AbstractDisplay{
    private int w, h;    
    private ImageView imageView;
    private BitmapInterface bitmap;
    private boolean imageMoveable;
    
    private final Rectangle outputClip = new Rectangle();
    
    private boolean firstTimeToAddNode = true;
    
    public ImageDisplay()
    {
        this(null, true);
    }
        
    public ImageDisplay(BitmapInterface bitmap)
    {
        this(bitmap, true);
    }
    
    public ImageDisplay(BitmapInterface bitmap, boolean moveable)
    {
        this.imageView = new ImageView();
        
    
        if(bitmap != null)        
            imageFill(bitmap);
                
        
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
        
        //http://news.kynosarges.org/2016/11/03/javafx-pane-clipping/
        setClip(outputClip);        
        layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
            //recenter
            int x = (int) (newValue.getWidth()/2 - w/2d);
            int y = (int) (newValue.getHeight()/2 - h/2d);            
            imageView.relocate(x, y);
        });  
        
        
       getChildren().add(imageView);
        
        
        //allow spilling outside layout        
        imageView.setManaged(false);
        
        
        
        
    }
    
    public void reset()
    {
        imageView.setScaleX(1);
        imageView.setScaleY(1);
       
        imageView.setTranslateX(0);
        imageView.setTranslateY(0); 
        //recenter
        int x = (int) (layoutBoundsProperty().get().getWidth()/2 - w/2d);
        int y = (int) (layoutBoundsProperty().get().getHeight()/2 - h/2d);
        imageView.relocate(x, y);
                
        ZoomUtility.reset();
    }
    
    @Override
    public final void imageFill(BitmapInterface bitmap)
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
