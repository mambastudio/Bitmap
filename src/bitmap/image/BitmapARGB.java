/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bitmap.image;

import bitmap.Color;
import bitmap.ColorCoding;
import bitmap.core.AbstractBitmap;
import java.nio.IntBuffer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
/**
 *
 * @author user
 */
public class BitmapARGB extends AbstractBitmap
{
    private final int w, h;
    private int[] data;
     
    public BitmapARGB(int width, int height)
    {
        this.w = width; this.h = height;
        this.data = new int[w * h];
    }
    
    public BitmapARGB(int width, int height, boolean opaque)
    {
        this.w = width; this.h = height;
        this.data = new int[w * h];
        
        if(!opaque)        
            for(int i = 0; i<data.length; i++)
                data[i] = ColorCoding.toInt8(0, 0, 0, 0);   
        else
            for(int i = 0; i<data.length; i++)
                data[i] = ColorCoding.toInt8(1, 0, 0, 0);   
    }
    
    public void reset()
    {
        
    }
    
    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    @Override
    public Color readColor(int x, int y) {
        int index = index(x, y);
        float[] buf = ColorCoding.toFloat8(data[index]);
        return new Color(buf[1], buf[2], buf[3]); //buf[0] is alpha
    }

    @Override
    public float readAlpha(int x, int y) {
        int index = index(x, y);
        float[] buf = ColorCoding.toFloat8(data[index]);
        return buf[0];
    }

    @Override
    public void writeColor(Color color, float alpha, int x, int y) {
        int index = index(x, y);
        int value = ColorCoding.toInt8(alpha, color.r, color.g, color.b);
        data[index] = value;
    }

    @Override
    public Image getImage() {
        WritableImage wImage = new WritableImage(w, h);        
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance(); 
        wImage.getPixelWriter().setPixels(0, 0, w, h, pixelFormat, data, 0, w);
        return wImage;
    }

    @Override
    public void writeColor(Color color, float alpha, int x, int y, int w, int h) {
        int w1 = (int) (getWidth() - 1);
        int h1 = (int) (getHeight() - 1);
        
        for (int dx = Math.min(x + w , w1); dx >= x; dx--)
            for (int dy = Math.min(y + h, h1); dy >= y; dy--)
            {          
                //System.out.println(color);
                writeColor(color, alpha, dx, dy);                
            }
    }
    
    
    private int index(int x, int y)
    {
        return (x + y * w );
    }

    @Override
    public void writeColor(Color[] color, float[] alpha, int x, int y, int w, int h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeColor(int[] color, int x, int y, int w, int h) {
        if(color.length == data.length)
            data = color;
    }
}
