/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum;

/**
 *
 * @author user
 * @param <T>
 */
public interface Spectrum<T extends Spectrum> extends Cloneable {    
    public Spectrum add(Spectrum s2);
    public T addAssign(Spectrum s2);
    public T sub(Spectrum s2);
    public T subAssign(Spectrum s2);
    public T mul(Spectrum s2);
    public T mulAssign(Spectrum s2);
    public T div(Spectrum s2);
    public T divAssign(Spectrum s2);
    public T sqrt();
    public T copy();
    public T newInstance();
    public T clamp();
    public Spectrum setTo(Spectrum s);    
    public T setTo(float v);
    public T setTo(float... v);
    public boolean is(float value);
    public boolean hasNaNs();
    public boolean isBlack();  
    
    public T mul(float f);

    public T mulAssign(float f);

    public T div(float f);
    public T divAssign(float f);
    public T neg();
    
    public T clamp(float low, float high);    
    public T pow(float n);   
    public T pow(Spectrum s2);
    public T exp();
    public T expAssign();        
    public Spectrum convert(Spectrum s2);
    
    /** Converts this spectrum to the XYZ y coefficient.
     * <p>
     * The y coordinate is closely related to luminance, which measures
     * the perceived brightness of a color.
     *
     * @return the y coefficient of the spectrum.
     */
    public float y();

}
