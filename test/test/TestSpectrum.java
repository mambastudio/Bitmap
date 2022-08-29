/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import bitmap.spectrum.CIESpectrum;
import bitmap.spectrum.RGBSpectrum;

/**
 *
 * @author user
 */
public class TestSpectrum {
    public static void main(String... args)
    {
        CIESpectrum spectrum = new CIESpectrum();
        
        int lambda = 380;
        for(int i= 0; i<81; i++)
        {
            System.out.println(spectrum.xBar(lambda)+ " " +spectrum.yBar(lambda)+ " " +spectrum.zBar(lambda));
            lambda += 5;
        }
    }
}
