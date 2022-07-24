/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display;

import bitmap.core.AbstractDisplay;
import bitmap.core.BitmapInterface;
import javafx.scene.image.ImageView;

/**
 *
 * @author user
 */
public class ImageViewDisplay extends ImageView implements AbstractDisplay {
    
    public ImageViewDisplay()
    {
        super();
    }

    @Override
    public int getImageWidth() {
        if(this.getImage() == null)
            return 0;
        return (int) this.getImage().getWidth();
    }

    @Override
    public int getImageHeight() {
        if(this.getImage() == null)
            return 0;
        return (int) this.getImage().getHeight();
    }
    
    @Override
    public final void imageFill(BitmapInterface bitmap)
    {
        this.setImage(bitmap.getImage());
        
    }
}
