/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery.panel;

import java.util.ArrayList;
import java.util.List;
import bitmap.RectBound;

/**
 *
 * @author user
 */
public class RectImageRow implements RectBound<RectImage, NodeImage> 
{
    public final List<RectImage> rects = new ArrayList<>();
    private final double maxWidth;
    
    private double offsetX = 0;
    private double offsetY = 0;
    private double padding = 0;
    
    private boolean isLimit = false;
    public boolean isLastAttemptFail = false;
    
    public RectImageRow(double offsetY, double maxWidth, double padding)
    {
        this.maxWidth = maxWidth;
        this.offsetY  = offsetY;
        this.padding = padding;    
        
    }

    @Override
    public double getMinX() {
        return offsetX;
    }

    @Override
    public double getMinY() {
        return offsetY;
    }

    @Override
    public void setMinX(double minX) {
        this.offsetX = minX;
    }

    @Override
    public void setMinY(double minY) {
        this.offsetY = minY;
    }

    @Override
    public double getMaxX() {
        return maxWidth;
    }

    @Override
    public double getMaxY() {
        if(rects.isEmpty()) return 0;
        return rects.get(0).getMaxY() + padding;
    }

    @Override
    public RectImage get(int index) {
        return rects.get(index);
    }

    @Override
    public List<NodeImage> getChildren() {
        List<NodeImage> list = new ArrayList<>();
        for(int i = 0; i<rects.size(); i++)
            list.add(rects.get(i).getDefault());
        
        return list;
    }

    @Override
    public List<RectImage> getList() {
        return rects;
    }

    @Override
    public RectImage getDefault() {
        if(rects.isEmpty()) return null;
        return rects.get(0);
    }
        
    public boolean add(RectImage rect) {
        
        rect.setMinX(offsetX); 
        rect.setMinY(offsetY + padding);
        offsetX += rect.getMaxX()+ padding;
                    
        if((offsetX) > maxWidth)
        {
            if(rects.isEmpty())  
            {
                rects.add(rect);
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
        this.rects.add(rect);
        
        return isLimit;
    }
    
    public void normalizeWidth()
    {                
        double sumWidth = rects.stream().map(rect -> rect.getMaxX()).reduce(0d, (a, b) -> a+b);
        double scale = maxWidth/sumWidth;
        if(scale < 0) return; 
        
        scale(scale);
        
        int offX = 0;
        for(RectImage image : rects)
        {
            image.setMinX(offX);
            offX += image.getMaxX() + padding;
        }        
    }
    
    public void scale( double scale)
    {
        rects.forEach((rect) -> {
            rect.setScale(scale);
        });
    }
    
    
    public boolean isLimit()
    {
        return isLimit;
    }
}
