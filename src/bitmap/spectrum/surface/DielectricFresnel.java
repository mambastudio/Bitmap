/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum.surface;

import bitmap.spectrum.Spectrum;
import static bitmap.util.ColorUtility.clamp;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;

/**
 *
 * @author user
 * @param <S>
 */
public class DielectricFresnel<S extends Spectrum> implements Fresnel<S> {
    private float etaI, etaT;
    private final S s;
    
    public DielectricFresnel(S s, float etaI, float etaT)
    {
        this.etaI = etaI;
        this.etaT = etaT;
        this.s = s;
    }

    //https://github.com/mmp/pbrt-v3/blob/aaa552a4b9cbf9dccb71450f47b268e0ed6370e2/src/core/reflection.cpp#L47
    @Override
    public S evaluateSpectrum(float cosThetaI) {
        cosThetaI = clamp(cosThetaI, -1, 1);
        // Potentially swap indices of refraction
        boolean entering = cosThetaI > 0.f;
        if (!entering) {
            swapEta();
            cosThetaI = abs(cosThetaI);
        }
        
        // Compute _cosThetaT_ using Snell's law
        float sinThetaI = (float) sqrt(max((float)0, 1 - cosThetaI * cosThetaI));
        float sinThetaT = etaI / etaT * sinThetaI;
        
        // Handle total internal reflection
        if (sinThetaT >= 1) 
        {            
            return (S) s.newInstance().setTo(1); //return 1;
        }
        float cosThetaT = (float) sqrt(max((float)0, 1 - sinThetaT * sinThetaT));
        float Rparl = ((etaT * cosThetaI) - (etaI * cosThetaT)) /
                      ((etaT * cosThetaI) + (etaI * cosThetaT));
        float Rperp = ((etaI * cosThetaI) - (etaT * cosThetaT)) /
                      ((etaI * cosThetaI) + (etaT * cosThetaT));
        return (S) s.newInstance().setTo((Rparl * Rparl + Rperp * Rperp) / 2);
    }

    
    private void swapEta()
    {
        float temp = etaI;
        etaI = etaT;
        etaT = temp;
    }
}
