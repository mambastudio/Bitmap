/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery.util;

import static bitmap.display.gallery.util.ThreadPoolTask.pool;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public class ImageContainer {
    
    private final Path imagePath;
    //manual start coordinates to avoid transform (good to calculate mouse point)
    private double startX;
    private double startY;
    
    private double endX;                //size of image width for view that is not greater than maxWidth
    private double endY;                //size of image height for view that is not greater than maxHeight
    //manual scaling to avoid transform
    private double scaledX;             //endX * scale, which is equal to endX if scale = 1
    private double scaledY;             //endY * scale, which is equal to endY if scale = 1
    private double scaleFactor = 1;
    
    private final double maxHeight;     //maximum height of view (irrespective of image height)
    private final double maxWidth;      //maximum width of view (irrespective of image width)
    private double lastDrawingStartPositionY; //translated position y (for scrolling)
    private boolean submitted = false;
    
    private boolean selected = false;
    
    
    /**
     * based on the above variables drawing should be - draw(startX(), startY(), getScaledX(), getScaledY())
     * if you put it in row container, it becomes - draw(startX(), startY() + strideY, getScaledX(), getScaledY())
     */
    
    private transient SoftReference<Image> imageRef = new SoftReference<Image>(null);
    //for future loading of original size
    //private transient SoftReference<Image> imageRefOrig = new SoftReference<Image>(null);
    
    //consider deleting this since I don't see the use of it
    private final BooleanProperty clearBoolean; //don't draw
    
    public ImageContainer(Path imagePath, double maxHeight, double maxWidth, BooleanProperty clearBoolean)
    {
        this.imagePath = imagePath;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.clearBoolean = clearBoolean;
        if(this.imagePath != null) {            
                ImageMetaData meta = new ImageMetaData(imagePath.toFile());                
                endX = getTargetWidth(meta, this.maxWidth); 
                endY = getTargetHeight(this.maxHeight);           
        }
    }
    
    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndY() {
        return endY;
    }

    public double getEndX() {
        return endX;
    }
    
    public double getScaledX() {
        return this.scaledX;
    }

    public double getScaledY() {
        return this.scaledY;
    }
    
    public double getScaleFactor() {
        return scaleFactor;
    }
    
    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        if(getScaleXFrom(scaleFactor) < endX) return;
        this.scaledX = this.endX * scaleFactor;
        this.scaledY = this.endY * scaleFactor;
    }
    
    public double getScaleXFrom(double scaleFactor)
    {
        return this.endX * scaleFactor;
    }
    
    public Path getImagePath() {
        return imagePath;
    }
    
    public boolean contains(double x, double y)
    {
        double xi = getStartX();
        double yi = lastDrawingStartPositionY;
        double wi = (int)getScaledX();
        double hi = (int)getScaledY();
        
        boolean xBoolean = (x >= xi && x < (xi + wi));
        boolean yBoolean = (y >= yi && y < (yi + hi));
        
        boolean successful = xBoolean == true && yBoolean == true;
        return successful;
    }
    
    public Bounds getBounds(double x, double y)
    {
        
        double xi = getStartX();
        double yi = lastDrawingStartPositionY;
        double wi = (int)getScaledX();
        double hi = (int)getScaledY();
        
        boolean xBoolean = (x >= xi && x < (xi + wi));
        boolean yBoolean = (y >= yi && y < (yi + hi));
        
        boolean successful = xBoolean == true && yBoolean == true;
        return null;
    }
    
    public void drawImageToCanvas(final GraphicsContext gc, final double start) {
        lastDrawingStartPositionY = start;
               
       
        if (imageRef.get()== null && !submitted) { //if no image, either not loaded or garbage collected
            submitted = true;
            pool.execute(()->{                
                
                final Image img = createImage(imagePath);       //load image in background thread    
                imageRef = new SoftReference(img);
                Platform.runLater(() -> drawImage(gc, img, getStartX(), lastDrawingStartPositionY, getScaledX(), getScaledY()));
                submitted = false; //to ensure garbage collection of removing image causes reload                
            });
        }
        
        if(clearBoolean.get())
            return;
        
        //draw if image loaded
        if(imageRef.get() == null)
        {            
            gc.setFill(Color.color(0.1f, 0.1f, 0.1f));
            gc.fillRect(getStartX(), lastDrawingStartPositionY, getScaledX(), getScaledY()); 
        }
        else
            drawImage(gc, imageRef.get(), getStartX(), lastDrawingStartPositionY, getScaledX(), getScaledY()); 
         
    }
    
    private void drawImage(final GraphicsContext gc, Image image, double x, double y, double width, double height)
    {
        gc.drawImage(image, x, y, width, height);
        if(selected)
            drawSelection(gc);
    }
    
    private void drawSelection(GraphicsContext gc)
    {       
        gc.setFill(Color.WHITE);
        gc.fillOval(getStartX(), lastDrawingStartPositionY, 20, 20); 
        gc.setFill(Color.RED);
        gc.fillOval(getStartX()+5, lastDrawingStartPositionY+5, 10, 10); 
        
    }
    
    public void selected(boolean selected)
    {
        this.selected = selected;
    }
    
    public void select()
    {
        this.selected = true;
    }
    
    public void unselect()
    {
        this.selected = false;
    }
           
    private Image createImage(Path imagePath){
        try {
            return new Image(
                    imagePath.toFile().toURI().toURL().toExternalForm(),
                    0,              //requested width
                    200,            //requested height (good resolution)
                    true,           //preserve ratio
                    false,          //smooth
                    false);          //background loading
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageContainer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private double getTargetHeight(double maxHeight) {
        return maxHeight;
    }

    //preserving width and height ratio
    private double getTargetWidth(ImageMetaData metadata, double maxWidth) {
        int nativeWidth = metadata.getWidth();
        int nativeHeight = metadata.getHeight();
        double targetHeight = getTargetHeight(maxWidth);
        double scalingFactor = nativeHeight / targetHeight; 
        return nativeWidth / scalingFactor;
    }
}
