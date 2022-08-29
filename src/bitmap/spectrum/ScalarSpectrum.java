/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum;

/**
 *
 * @author user
 */
public class ScalarSpectrum extends CoefficientSpectrum<RGBSpectrum>{
    
    public ScalarSpectrum()
    {
        super(1);
    }
    
    public ScalarSpectrum(float value)
    {
        super(new float[]{value});
    }
    
    public ScalarSpectrum(double value)
    {
        this((float)value);
    }
    
    public float getValue()
    {
        return c[0];
    }

    @Override
    public float Y() {
        return getValue();
    }
    
    @Override
    public ScalarSpectrum newInstance() {
        try {
            ScalarSpectrum that = (ScalarSpectrum) super.clone();           
            that.c = new float[c.length];
            return (ScalarSpectrum) that;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
    }
    
    @Override
    public String toString()
    {
        return "value " +c[0];
    }
}
