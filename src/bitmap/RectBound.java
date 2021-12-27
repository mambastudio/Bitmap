/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 * @param <T>
 * @param <C> child
 */
public interface RectBound<T, C> {
    
    public double getMinX();
    public double getMinY();    
    public void setMinX(double minX);
    public void setMinY(double minY);
    public double getMaxX();
    public double getMaxY();
    
    default T get(int index)
    {
        return getDefault();
    }
    
    default List<C> getChildren()
    {
        return new ArrayList();
    }
    
    default List<T> getList()
    {
        return new ArrayList();
    }
    
    public T getDefault();
            
}
