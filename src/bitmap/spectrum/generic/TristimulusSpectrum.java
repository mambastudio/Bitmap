/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum.generic;

import bitmap.spectrum.XYZSpectrum;

/**
 *
 * @author jmburu
 * @param <T>
 */
public abstract class TristimulusSpectrum <T extends TristimulusSpectrum> extends CoefficientSpectrum<T>{
    protected double YSum;
    
    protected TristimulusSpectrum()
    {
        //5nm from 380nm to 780nm, meaning 81 slots * 3 color matching function channel
        super(81 * 3);
    }
    
    protected void setTristimulus(int index, float x, float y, float z)
    {
        c[index + 0  ] = x;
        c[index + 81 ] = y;
        c[index + 162] = z;
    }
    
    protected void setTristimulusFromLambda(float lambda, float x, float y, float z)
    {
        c[((int)lambda - 380) / 5] = x;
        c[((int)lambda - 380) / 5 + 81] = y;
        c[((int)lambda - 380) / 5 + 162] = z;
    }
    
    public float xBar(int lambda)
    {
        return c[(lambda - 380) / 5];
    }
    
    public float xBarAtIndex(int index)
    {
        return c[index + 0];
    }
    
    public float yBar(int lambda)
    {
        return c[(lambda - 380) / 5 + 81] ;
    }
    
    public float yBarAtIndex(int index)
    {
        return c[index + 81];
    }
    
    public float zBar(int lambda)
    {
        return c[(lambda - 380) / 5  + 162];
    }
    
    public float zBarAtIndex(int index)
    {
        return c[index + 162];
    }
    
    
    protected void calculateY() {
        YSum = 0;
        for(int lambda = 380; lambda<780; lambda++)
        {
            YSum += yBar(lambda);
        }
    }
    
    @Override
    public float Y() {
        return (float) YSum;
    }
    
    public XYZSpectrum spectrumToXYZ(IntensitySpectrum spectrum)
    {
        float X = 0, Y = 0, Z = 0;
        for(int i = 0; i<81; i++)
        {
            X += xBarAtIndex(i) * spectrum.getValueAtIndex(i); 
            Y += yBarAtIndex(i) * spectrum.getValueAtIndex(i); 
            Z += zBarAtIndex(i) * spectrum.getValueAtIndex(i);
        }
        X *= 1.f/YSum;
        Y *= 1.f/YSum;
        Z *= 1.f/YSum;
        
        return new XYZSpectrum(X, Y, Z);
    }
}
