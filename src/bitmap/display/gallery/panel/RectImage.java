/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery.panel;

import bitmap.RectBound;

/**
 *
 * @author user
 */
public class RectImage implements RectBound<NodeImage, NodeImage> {
    private final NodeImage view;
        
    //manual start coordinates to avoid transform (good to calculate mouse point)
    private double minX;
    private double minY;
        
    private final double actualWidth;         //size of original image width for view that is not greater than maxWidth
    private final double actualHeight;        //size of original height for view that is not greater than maxHeight
    
    private double scale = 1;     //for scaling
    
    private double limitW = 120;  //limit width of scaling 
    private double limitH = 120;  //limit height of scaling 
    
    public RectImage(NodeImage view)
    {
        this.view = view;
        actualWidth     = getTargetWidth(view.getBoundsInLocal().getWidth(), view.getBoundsInParent().getHeight(), limitW); 
        actualHeight    = getTargetHeight(limitH);
        setScale(scale);
    }
    
    @Override
    public double getMinX() {
        return minX;
    }

    @Override
    public double getMinY() {
        return minY;
    }

    @Override
    public void setMinX(double minX) {
        this.minX = minX;
        reconfigureImage();
    }

    @Override
    public void setMinY(double minY) {
        this.minY = minY;
        reconfigureImage();
    }

    @Override
    public double getMaxX() {
        return actualWidth * scale;        
    }

    @Override
    public double getMaxY() {
        return actualHeight * scale;
    }

    @Override
    public NodeImage getDefault() {
        return view;
    }
      
    public void setLimitSize(double limitW, double limitH)
    {
        this.limitW = limitW; this.limitH = limitH;
    }
    
    public final void setScale(double scale) {
        
        if(getScaleXFrom(this.scale) < actualWidth) 
            return; //don't shrink further
        this.scale = scale;
        
        reconfigureImage();
    }
    
    public final double getScale()
    {
        return scale;
    }
    
    private void reconfigureImage()
    {
        view.setFitWidth(getMaxX());
        view.setFitHeight(getMaxY()); 
        view.setX(getMinX());
        view.setY(getMinY());
    }
    
    public double getScaleXFrom(double scaleFactor)
    {
        return this.actualWidth * scaleFactor;
    }
    
    //preserving width and height ratio based on maximum height
    private double getTargetWidth(double nativeWidth, double nativeHeight, double maxWidth) 
    {        
        double targetHeight = getTargetHeight(maxWidth);
        double scalingFactor = nativeHeight / targetHeight; 
        return nativeWidth / scalingFactor;
    }
    
    private double getTargetHeight(double maxHeight) 
    {
        return maxHeight;
    }
    
    @Override
    public String toString()
    {
        return String.format("(%3.2f, %3.2f, %3.2f, %3.2f)", getMinX(), getMinY(), getMaxX(), getMaxY());
    }
}
