/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display;

import bitmap.core.AbstractDisplay;
import bitmap.core.BitmapInterface;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class PannableDisplay extends ScrollPane implements AbstractDisplay{
    private int w, h;    
    private ImageView imageView;
    
    Group group;
    StackPane content;
    
    public PannableDisplay()
    {
        super();
        this.imageView = new ImageView();
        this.group = new Group(imageView);
        this.content = new StackPane(group);
        this.setContent(content);
        
        content.setOnScroll(evt->{
            if (evt.isControlDown()) {
                evt.consume();
                
                // calculate adjustment of scroll position (pixels)
                Point2D posInZoomTarget = this.imageView.parentToLocal(group.parentToLocal(new Point2D(evt.getX(), evt.getY())));
                
                final double zoomFactor = evt.getDeltaY() > 0 ? 1.2 : 1 / 1.2;
                zoom(zoomFactor, posInZoomTarget);
            }
        });
        
        this.setPannable(true);
        this.viewportBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            // use vieport size, if not too small for zoomTarget
            content.setPrefSize(newBounds.getWidth(), newBounds.getHeight());
        });
        
        group.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            // keep it at least as large as the content
            content.setMinWidth(newBounds.getWidth());
            content.setMinHeight(newBounds.getHeight());
        });

    }
    
    public void setImage(Image image)
    {
        this.imageView.setImage(image);
        this.w = (int) image.getWidth();
        this.h = (int) image.getHeight();
    }
    
    public void zoom(double scale, Point2D point)
    {
        Bounds groupBounds = group.getLayoutBounds();
        final Bounds viewportBounds = getViewportBounds();

        // calculate pixel offsets from [0, 1] range
        double valX = getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
        double valY = getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());
                       
        // calculate adjustment of scroll position (pixels)
        Point2D adjustment = imageView.getLocalToParentTransform().deltaTransform(point.multiply(scale - 1));
        
        // do the resizing
        imageView.setScaleX(scale * imageView.getScaleX());
        imageView.setScaleY(scale * imageView.getScaleY());

        // refresh ScrollPane scroll positions & content bounds
        layout();

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        groupBounds = group.getLayoutBounds();
        setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
        setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
    }  
    
    public void toFitView(ActionEvent e)
    {
        toFitView();
    }
    
    public void toFitView()
    {
        Bounds groupBounds = group.getLayoutBounds();        
        Bounds viewportBounds = this.getLayoutBounds();
        
        double scale = Math.min(viewportBounds.getWidth()/groupBounds.getWidth(), viewportBounds.getHeight()/groupBounds.getHeight());
        zoom(scale * 0.90, Point2D.ZERO);
    }
    
    public void toOriginSize(ActionEvent e)
    {
        toOriginSize();
    }
    
    public void toOriginSize()
    {
        Bounds viewportBounds = group.getLayoutBounds();    
        double iWidth = imageView.getImage().getWidth();
        double iHeight = imageView.getImage().getHeight();
        
        double scale = Math.min(iWidth/viewportBounds.getWidth(), iHeight/viewportBounds.getHeight());
        
        zoom(scale, Point2D.ZERO);
    }
    
     @Override
    public final void imageFill(BitmapInterface bitmap)
    {        
        this.setImage(bitmap.getImage());        
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
