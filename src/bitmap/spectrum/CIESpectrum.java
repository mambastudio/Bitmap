/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum;

import static bitmap.CIE1931.xFit_1931;
import static bitmap.CIE1931.yFit_1931;
import static bitmap.CIE1931.zFit_1931;
import bitmap.spectrum.generic.TristimulusSpectrum;

/**
 *
 * @author user
 */
public class CIESpectrum extends TristimulusSpectrum<CIESpectrum>{
    
    public CIESpectrum()
    {
        super();
        //x color matching function        
        for (int lambda = 380; lambda <= 780; lambda+=5)
        {
            float x = xFit_1931(lambda);
            float y = yFit_1931(lambda);
            float z = zFit_1931(lambda);
            
            this.setTristimulusFromLambda(lambda, x, y, z);
        }
        
        calculateY();
    }
    
        
}
