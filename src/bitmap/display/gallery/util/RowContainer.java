/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery.util;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author user
 */
public class RowContainer {
    public final List<ImageContainer> images = new ArrayList<>();
    private double maxWidth;
    
    private double offsetX = 0;
    private double offsetY = 0;
    private double padding = 0;
    
    private boolean isLimit = false;
    public boolean isLastAttemptFail = false;
    
    private final BooleanProperty clearBoolean;
    
    public RowContainer(double offsetY, double maxWidth, double padding, BooleanProperty clearBoolean)
    {
        this.maxWidth = maxWidth;
        this.offsetY  = offsetY;
        this.padding = padding;
        this.clearBoolean = clearBoolean;
    }
    
    public double getMaxWidth()
    {
        return maxWidth;
    }
    
    public void setMaxWidth(double maxWidth)
    {
        this.maxWidth = maxWidth;
    }
    
    public boolean add(ImageContainer image) {
        image.setStartX(offsetX); //set x position        
        offsetX += image.getScaledX()+ padding;
        
            
        if((offsetX) > maxWidth)
        {
            if(images.isEmpty())  
            {
                images.add(image);
                isLimit = true;
                return isLimit;
            }
            else
            {
                isLastAttemptFail = true;
                isLimit = true;
                return isLimit;
            }
        }
        this.images.add(image);
        return isLimit;
    }
    
    public boolean isLimit()
    {
        return isLimit;
    }
    
    public void drawImageToCanvas(final GraphicsContext gc)
    {        
        images.forEach((image) -> {
            if(clearBoolean.get())
                return;
            image.drawImageToCanvas(gc, offsetY + padding);
        });
    }
    
    public double rowSize()
    {
        return offsetX;
    }
    
    public double getOffsetY()
    {
        return offsetY ;
    }
    
    public double getHeightSize()
    {
        if(images.isEmpty()) return 0;
        return images.get(0).getScaledY() + padding;
    }
    
    public void normalizeWidth()
    {
        
        //TODO
        double sumWidth = images.stream().map(image -> image.getScaledX()).reduce(0d, (a, b) -> a+b);
        double scale = maxWidth/sumWidth;
        if(scale < 0) return; 
        
        scale(images, scale);
        
        int offX = 0;
        for(ImageContainer image : images)
        {
            image.setStartX(offX);
            offX += image.getScaledX() + padding;
        }        
    }
    
    public void scale(List<ImageContainer> images, double scale)
    {
        images.forEach((image) -> {
            image.setScaleFactor(scale);
        });
    }
}
