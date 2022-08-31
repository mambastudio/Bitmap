/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum.generic;

import java.util.Arrays;

/**
 *
 * @author jmburu
 * @param <I>
 */
public abstract class IntensitySpectrum<I extends IntensitySpectrum> extends CoefficientSpectrum<I> {
    protected IntensitySpectrum()
    {
        //5nm from 380nm to 780nm, meaning 81 slots
        super(81);
    }
    
    public float getValueAtIndex(int index)
    {
        return c[index]; 
    }
   
     @Override
    public String toString()
    {        
        return Arrays.toString(c);
    }
    
}
