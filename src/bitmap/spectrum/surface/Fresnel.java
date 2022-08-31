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
public interface Fresnel<S extends Spectrum> {
    public S evaluateSpectrum(float cosI);    
}
