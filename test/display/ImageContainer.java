/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author user
 */
public class ImageContainer {
    
    private Path imagePath;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double scaledX;    
    private double scaledY;
    private double scaleFactor = 1;
    private double maxHeight;   
    private double maxWidth;
    private double lastDrawingStartPositionY;
    
    private transient SoftReference<Image> imageRef = new SoftReference<Image>(null);
    //for future loading of original size
    private transient SoftReference<Image> imageRefOrig = new SoftReference<Image>(null);
    
    public ImageContainer(Path imagePath, double maxHeight, double maxWidth)
    {
        this.imagePath = imagePath;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        if(this.imagePath != null) {
            try {
                ImageMetaData meta = new ImageMetaData(imagePath.toFile());                
                endX = getTargetWidth(meta, maxWidth); 
                endY = getTargetHeight(maxHeight); 
            } catch (IOException ex) {
                Logger.getLogger(ImageContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    
    public void drawImageToCanvas(final GraphicsContext gc, final double start) {
        lastDrawingStartPositionY = start;
        if (imageRef.get() == null) {
            try {
                final Image img = createImage(imagePath, maxWidth, maxHeight);                
                drawAsync(gc, img);
                drawSync(gc, img);
            } catch (Exception ex) {
                Logger.getLogger(ImageContainer.class.getName()).log(Level.SEVERE, null, ex);
            }            
            if (imageRef.get() == null)
                imageRef = new SoftReference<>(new Rectangle(getScaledX(), getScaledY()).snapshot(new SnapshotParameters(), null));
        }
        gc.drawImage(imageRef.get(), getStartX(), lastDrawingStartPositionY, getScaledX(), getScaledY());        
    }
    
    private void drawSync(GraphicsContext gc, Image img) {
        if (img.getProgress() >= 1.0) {
            drawImageWhenFinished(gc, img);
        }
    }

    private void drawAsync(GraphicsContext gc, Image img) {
        img.progressProperty().addListener((ov, oldVal, newVal) -> {
            if (newVal.doubleValue() >= 1.0) {
                drawImageWhenFinished(gc, img);
            }
        });
    }
    
    private void drawImageWhenFinished(final GraphicsContext gc, final Image img) {
        final Image image = postProcess(img, maxHeight, maxWidth);
        gc.drawImage(image, getStartX(), lastDrawingStartPositionY, getScaledX(), getScaledY());
        imageRef = new SoftReference<>(image);
    }
    
    Image postProcess(Image image,double maxHeight, double maxWidth) {
        return image;
    }
    
    public Image createImage(Path imagePath,double maxWidth, double maxHeight) throws Exception{
        return new Image(
                imagePath.toFile().toURI().toURL().toExternalForm(),
                0d,             //requested width
                500,            //requested height (good resolution)
                true,           //preserve ratio
                false, 
                true);
    }
    
    private double getTargetHeight(double maxHeight) {
        return maxHeight;
    }

    private double getTargetWidth(ImageMetaData metadata, double maxHeight) {
        int nativeWidth = metadata.getWidth();
        int nativeHeight = metadata.getHeight();
        double targetHeight = getTargetHeight(maxHeight);
        double scalingFactor = nativeHeight / targetHeight; 
        return nativeWidth / scalingFactor;
    }
}
