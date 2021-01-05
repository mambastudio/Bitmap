/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.core;

import java.nio.file.Path;

/**
 *
 * @author user
 * @param <B>
 */
public interface BitmapReader<B extends BitmapInterface> {
    public B load(Path path);
}
