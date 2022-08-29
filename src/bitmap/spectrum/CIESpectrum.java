/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum;

import static bitmap.CIE1931.xFit_1931;
import static bitmap.CIE1931.yFit_1931;
import static bitmap.CIE1931.zFit_1931;

/**
 *
 * @author user
 */
public class CIESpectrum extends CoefficientSpectrum<CIESpectrum>{
    
    public CIESpectrum()
    {
        //5nm from 380nm to 780nm, meaning 81 slots * 3 color matching function channel
        super(81 * 3);
        
        //x color matching function
        int index = 0;
        for (int i = 380; i <= 780; i+=5)
        {
            float x = xFit_1931(i);
            float y = yFit_1931(i);
            float z = zFit_1931(i);
            
            c[index + 0  ] = x;
            c[index + 81 ] = y;
            c[index + 162] = z;
            index++;
        }
    }
    
    public double xBar(int lambda)
    {
        return c[(lambda - 380) / 5];
    }
    
    public double yBar(int lambda)
    {
        return c[(lambda - 380) / 5 + 81] ;
    }
    
    public double zBar(int lambda)
    {
        return c[(lambda - 380) / 5  + 162];
    }
    

    @Override
    public float Y() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
