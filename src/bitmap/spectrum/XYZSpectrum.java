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
public class XYZSpectrum extends CoefficientSpectrum<XYZSpectrum>{
        
    public XYZSpectrum(RGBSpectrum spectrum)
    {
        super(3);
        convertRGBtoXYZ(spectrum);
    }
        
    protected void convertRGBtoXYZ(RGBSpectrum spectrum)
    {
        float r = spectrum.r();
        float g = spectrum.g();
        float b = spectrum.b();

        float X = 0.5893f * r + 0.1789f * g + 0.1831f * b;
        float Y = 0.2904f * r + 0.6051f * g + 0.1045f * b;
        float Z = 0.0000f * r + 0.0684f * g + 1.0202f * b;
        
        this.setTo(X, Y, Z);
    }
    
    protected RGBSpectrum convertXYZtoRGB()
    {
        float r, g, b;

        r =  1.967f * X() - 0.548f * Y() - 0.297f * Z();
        g = -0.955f * X() + 1.938f * Y() - 0.027f * Z();
        b =  0.064f * X() - 0.130f * Y() + 0.982f * Z();
        
        return new RGBSpectrum(r, g, b);
    }
    
    public float X()
    {
        return c[0];
    }
    
    public float Z()
    {
        return c[2];
    }
    
    // set luminance
    public void setY(float Y)
    {         
        //convert to chromaticities
        float XYZ = X() + Y() + Z();
        if (XYZ < 1e-6f)
            return;
        float s = 1f / XYZ;
        float x = X() * s;
        float y = Y() * s;
        
        //set luminance
        c[1] = Y;
        
        //convert chromaticities back to tristumulus -> xy to XY
        c[0] = y < 1e-6f ? 0 : Y*(x/y);  //X
        c[2] = y < 1e-6f ? 0 : (Y * (1 - x - y))/y; //Y
    }
       
    @Override
    public float Y() {
        return c[1];
    }

    @Override
    public RGBSpectrum getRGBSpectrum() {
        return convertXYZtoRGB();
    }
    
}
