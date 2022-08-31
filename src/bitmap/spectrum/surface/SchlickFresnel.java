/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum.surface;

import bitmap.spectrum.generic.Spectrum;

/**
 *
 * @author user
 * @param <S>
 */
public class SchlickFresnel<S extends Spectrum> implements Fresnel<S> {

    private final float n1;
    private final float n2;
    private final S s;
    
    public SchlickFresnel(S s, float n1, float n2)
    {
        this.n1 = n1; this.n2 = n2;
        this.s = s;
    }
    
    @Override
    public S evaluateSpectrum(float cosI) {
        float r0 = (n1 - n2)/ (n1 + n2);
        r0 *= r0;        
        if(n1 > n2)
        {
            float nratio = n1/n2;
            float sinT2 = nratio * nratio * (1.f - cosI * cosI);
            if(sinT2 > 1.f)
                return s;
            cosI = (float)Math.sqrt(1.f - sinT2);
        }
        float x = 1.f - cosI;
        return (S) s.mul(r0 + (1.f - r0) * x * x * x * x * x);
    }
    
}
