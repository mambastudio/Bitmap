/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum;

import bitmap.spectrum.generic.IntensitySpectrum;

/**
 *
 * @author user
 */
public class D65Spectrum extends IntensitySpectrum<D65Spectrum>{
    float Ysum;
    public D65Spectrum()
    {
        float[] data = new float[]{
            49.9755f, 52.3118f, 54.6482f, 68.7015f, 82.7549f,87.1204f, 91.486f, 
            92.4589f, 93.4318f, 90.057f,86.6823f, 95.7736f, 104.865f, 110.936f, 
            117.008f,117.41f, 117.812f,116.336f,  114.861f,115.392f,  115.923f,
            112.367f, 108.811f,109.082f,  109.354f,108.578f, 107.802f,106.296f,  
            104.79f, 106.239f, 107.689f,106.047f,  104.405f,104.225f, 104.046f,102.023f,  
            100f,    98.1671f, 96.3342f, 96.0611f, 95.788f, 92.2368f, 88.6856f, 89.3459f, 
            90.0062f,89.8026f, 89.5991f,  88.6489f, 87.6987f,85.4936f, 83.2886f, 
            83.4939f, 83.6992f,81.863f,  80.0268f, 80.1207f, 80.2146f,81.2462f, 
            82.2778f, 80.281f,  78.2842f,74.0027f, 69.7213f,  70.6652f, 71.6091f, 
            72.979f, 74.349f, 67.9765f, 61.604f, 65.7448f, 69.8856f, 72.4863f, 
            75.087f, 69.3398f, 63.5927f,55.0054f, 46.4182f, 56.6118f, 66.8054f,  
            65.0941f, 63.3828f
        };
        
        this.setTo(data);
        
        Ysum = 0;
        for(int i = 0; i<data.length; i++)
            Ysum += data[i];
    }
    
    @Override
    public float Y() {
        return Ysum;
    }

}
