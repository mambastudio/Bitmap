/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum;

import bitmap.Spectrum;

/**
 *
 * @author user
 */
public class RGBSpectrum extends CoefficientSpectrum<RGBSpectrum>{
    
    public RGBSpectrum() {
        super(3);
    }

    public RGBSpectrum(float r, float g, float b) {
        super(new float[]{r, g, b});
    }

    public RGBSpectrum(float v) {
        super(3, v);
    }

    public RGBSpectrum(float[] c) {
        super(c);
    }
    
    public float r()
    {
        return c[0];
    }
    
    public float g()
    {
        return c[1];
    }
    
    public float b()
    {
        return c[2];
    }
    
    @Override
    public RGBSpectrum convert(Spectrum s2) {
        if(s2 instanceof RGBSpectrum)
            return (RGBSpectrum)s2;
        throw new UnsupportedOperationException("format spectrum not recognised");
    }

    @Override
    public float y() {
        return 0.212671f * r() + 
               0.715160f * g() +
               0.072169f * b();
    }
    
    @Override
    public RGBSpectrum newInstance() {
        try {
            CoefficientSpectrum that = (CoefficientSpectrum) super.clone();           
            that.c = new float[c.length];
            return (RGBSpectrum) that;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
    }
    
    @Override
    public String toString()
    {
        return "r " +r()+ " g " +g()+ " b " +b();
    }
}
