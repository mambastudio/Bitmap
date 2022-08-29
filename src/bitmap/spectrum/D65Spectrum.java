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
public class D65Spectrum extends CoefficientSpectrum<D65Spectrum>{
    
    public D65Spectrum()
    {
        //5nm from 380nm to 780nm, meaning 81 slots
        super(81);
    }

    @Override
    public float Y() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
