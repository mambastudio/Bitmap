/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import bitmap.spectrum.RGBSpectrum;

/**
 *
 * @author user
 */
public class TestSpectrum {
    public static void main(String... args)
    {
        RGBSpectrum spec = new RGBSpectrum(0.2f, 0.3f, 0.9f);
        RGBSpectrum specN = spec.newInstance().setTo(new RGBSpectrum(1, 1, 1));
        System.out.println(spec);
        
        System.out.println(specN);
    }
}
