/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.spectrum.generic;

import bitmap.spectrum.generic.Spectrum;
import bitmap.util.ColorUtility;
import java.util.Arrays;

/**
 *
 * @author user
 * @param <T>
 */
public abstract class CoefficientSpectrum<T extends CoefficientSpectrum> implements Spectrum<T >{
    public float[] c;
    
    public CoefficientSpectrum(int nSamples) {
        this(nSamples, 0f);
    }

    public CoefficientSpectrum(int nSamples, float v) {
        c = new float[nSamples];
        for (int i = 0; i < c.length; i++) {
            c[i] = v;
        }
    }

    public CoefficientSpectrum(float... cs) {
        c = new float[cs.length];
        System.arraycopy(cs, 0, c, 0, cs.length);
    }
    
    @Override
    public boolean isBlack() {
        for (int i = 0; i < c.length; i++) {
            if (c[i] != 0) {
                return false;
            }
        }
        return true;
    }
    /** Returns true if all coefficients have value v.
     * @param v
     * @return  */
    @Override
    public boolean is(float v) {
        for (int i = 0; i < c.length; i++) {
            if (c[i] != v) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isDarkerThan(CoefficientSpectrum s2) {
        return Y() < s2.Y();
    }
    
    public boolean HasNaNs() {
        for (int i = 0; i < c.length; i++) {
            if (Float.isNaN(c[i])) {
                return true;
            }
        }
        return false;
    }

// Operators
    @Override
    public T setTo(Spectrum ss2) {
        CoefficientSpectrum s2 = (CoefficientSpectrum) ss2;
        assert (!s2.HasNaNs());
        if (c.length != s2.c.length) {
            c = Arrays.copyOf(s2.c, s2.c.length);
        } else {
            System.arraycopy(s2.c, 0, c, 0, c.length);
        }
        return (T)this;
    }

    @Override
    public T setTo(float v) {
        for (int i = c.length - 1; i >= 0; i--) {
            c[i] = v;
        }
        return (T)this;
    }
    
    @Override
    public T setTo(float... v) {
        for (int i = c.length - 1; i >= 0; i--) {
            System.arraycopy(v, 0, c, 0, v.length);
        }
        return (T)this;
    }

    @Override
    public T add(Spectrum ss2) {
        CoefficientSpectrum r = (CoefficientSpectrum) copy();
        CoefficientSpectrum s2 = (CoefficientSpectrum) ss2;
        for (int i = c.length - 1; i >= 0; i--) {
            r.c[i] += s2.c[i];
        }
        return (T)r;
    }

    @Override
    public T addAssign(Spectrum ss2) {
        CoefficientSpectrum s2 = (CoefficientSpectrum) ss2;
        for (int i = c.length - 1; i >= 0; i--) {
            c[i] += s2.c[i];
        }
        return (T)this;
    }

    @Override
    public T sub(Spectrum ss2) {
        CoefficientSpectrum r = (CoefficientSpectrum) copy();
        CoefficientSpectrum s2 = (CoefficientSpectrum) ss2;
        for (int i = c.length - 1; i >= 0; i--) {
            r.c[i] -= s2.c[i];
        }
        return (T)r;
    }

    @Override
    public T subAssign(Spectrum ss2) {
        CoefficientSpectrum s2 = (CoefficientSpectrum) ss2;
        for (int i = c.length - 1; i >= 0; i--) {
            c[i] -= s2.c[i];
        }
        return (T)this;
    }

    @Override
    public T mul(Spectrum ss2) {
        CoefficientSpectrum r = (CoefficientSpectrum) copy();
        CoefficientSpectrum s2 = (CoefficientSpectrum) ss2;
        for (int i = c.length - 1; i >= 0; i--) {
            r.c[i] *= s2.c[i];
        }
        return (T)r;
    }

    @Override
    public T mulAssign(Spectrum ss2) {
        CoefficientSpectrum s2 = (CoefficientSpectrum) ss2;
        for (int i = c.length - 1; i >= 0; i--) {
            c[i] *= s2.c[i];
        }
        return (T)this;
    }

    @Override
    public T div(Spectrum ss2) {
        CoefficientSpectrum r = (CoefficientSpectrum) copy();
        CoefficientSpectrum s2 = (CoefficientSpectrum) ss2;
        for (int i = c.length - 1; i >= 0; i--) {
            r.c[i] /= s2.c[i];
        }
        return (T) r;
    }

    @Override
    public T divAssign(Spectrum ss2) {
        CoefficientSpectrum s2 = (CoefficientSpectrum) ss2;
        for (int i = c.length - 1; i >= 0; i--) {
            c[i] /= s2.c[i];
        }
        return (T)this;
    }

    @Override
    public T mul(float f) {
        CoefficientSpectrum r = copy();
        for (int i = c.length - 1; i >= 0; i--) {
            r.c[i] *= f;
        }
        return (T)r;
    }

    @Override
    public T mulAssign(float f) {
        for (int i = c.length - 1; i >= 0; i--) {
            c[i] *= f;
        }
        return (T)this;
    }

    @Override
    public T div(float f) {
        CoefficientSpectrum r = copy();
        for (int i = c.length - 1; i >= 0; i--) {
            r.c[i] /= f;
        }
        return (T) r;
    }

    @Override
    public T divAssign(float f) {
        for (int i = c.length - 1; i >= 0; i--) {
            c[i] /= f;
        }
        return (T) this;
    }
    
    @Override
    public T neg() {
        CoefficientSpectrum s1 = copy();
        for (int i = 0; i < c.length; i++) {
            s1.c[i] = -c[i];
        }
        return (T)s1;
    }

    @Override
    public T pow(float n) {
        CoefficientSpectrum ret = copy();
        for (int i = 0; i < c.length; i++) {
            ret.c[i] = (float) Math.pow(c[i], n);
        }
        return (T)ret;
    }

    @Override
    public T pow(Spectrum ss2) {
        CoefficientSpectrum s2 = (CoefficientSpectrum) ss2;
        CoefficientSpectrum ret = copy();
        for (int i = 0; i < c.length; i++) {
            ret.c[i] = c[i] > 0 ? (float) Math.pow(c[i], s2.c[i]) : 0f;
        }
        return (T)ret;
    }

    @Override
    public T sqrt() {
        CoefficientSpectrum ret = copy();
        for (int i = 0; i < c.length; i++) {
            ret.c[i] = (float) Math.sqrt(c[i]);
        }
        return (T)ret;
    }
    
    @Override
    public T clamp(float low, float high) {
        CoefficientSpectrum ret = copy();
        for (int i = 0; i < c.length; ++i) {
            ret.c[i] = ColorUtility.clamp(c[i], low, high);
        }
        assert (!ret.HasNaNs());
        return (T)ret;
    }

// Equality
    @Override
    public boolean equals(Object o) {
        if (o instanceof CoefficientSpectrum) {
            return equals((CoefficientSpectrum) o);
        } else {
            return false;
        }
    }

    public boolean equals(CoefficientSpectrum that) {
        return Arrays.equals(this.c, that.c);
    }

    @Override
    public T clamp() {
        return clamp(0, Float.POSITIVE_INFINITY);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.c);
    }
    
    public void addWeighted(CoefficientSpectrum s2, float weight) {
        for (int i = 0; i < c.length; i++) {
            c[i] += weight * s2.c[i];
        }
    }

    public static CoefficientSpectrum exp(CoefficientSpectrum s) {
        CoefficientSpectrum ret = s.copy();
        for (int i = 0; i < s.c.length; ++i) {
            ret.c[i] = (float) Math.exp(s.c[i]);
        }
        assert (!ret.HasNaNs());
        return ret;
    }

    @Override
    public T exp() {
        CoefficientSpectrum ret = copy();
        for (int i = 0; i < c.length; ++i) {
            ret.c[i] = (float) Math.exp(c[i]);
        }
        assert (!ret.HasNaNs());
        return (T) ret;
    }
    @Override
    public T expAssign() {
        for (int i = 0; i < c.length; ++i) {
            c[i] = (float) Math.exp(c[i]);
        }
        assert (!HasNaNs());
        return (T) this;
    }

    @Override
    public T copy() {
        try {
            CoefficientSpectrum that = (CoefficientSpectrum) super.clone();
            that.c = Arrays.copyOf(c, c.length);
            return (T) that;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
    }
    
    @Override
    public T newInstance() {
        try {
            CoefficientSpectrum that = (CoefficientSpectrum) super.clone();           
            that.c = new float[c.length];
            return (T) that;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
    }
    
    @Override
    public boolean hasNaNs() {
        for (int i = 0; i < c.length; i++) {
            if (Float.isNaN(c[i])) {
                return true;
            }
        }
        return false;
    }
}
