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
public class ConductorFresnel<S extends Spectrum> implements Fresnel<S> {
    private final S eta;
    private final S k;
    
    public ConductorFresnel(S eta, S k)
    {
        this.eta = eta;
        this.k = k;
    }
    
    //https://github.com/mitsuba-renderer/mitsuba/blob/cfeb7766e7a1513492451f35dc65b86409655a7b/src/libcore/util.cpp
    @Override
    public S evaluateSpectrum(float cosI) {
        S temp = (S) k.newInstance();
        
        float cosThetaI2    = cosI*cosI,
              sinThetaI2    = 1-cosThetaI2,
              sinThetaI4    = sinThetaI2*sinThetaI2;
               
        S   temp1   = (S) eta.mul(eta).sub(k.mul(k)).sub(temp.newInstance().setTo(sinThetaI2)),
            a2pb2   = (S) (temp1.mul(temp1).add(k.mul(k).mul(eta).mul(eta).mul(4))).sqrt(),
            a       = (S) ((a2pb2.add(temp1)).mul(0.5f)).sqrt();
        
        S   term1   = (S) a2pb2.add(temp.newInstance().setTo(cosThetaI2)),
            term2   = (S) a.mul(2 * cosI);
        
        S   Rs2     = (S) term1.sub(term2).div(term1.add(term2));
        
        S   term3   = (S) a2pb2.mul(cosThetaI2).add(temp.newInstance().setTo(sinThetaI4)),
            term4   = (S) term2.mul(sinThetaI2);
        
        S Rp2       = (S) Rs2.mul(term3.sub(term4)).div(term3.add(term4));
        
        return (S) Rp2.add(Rs2).mul(0.5f);       
    }
}
