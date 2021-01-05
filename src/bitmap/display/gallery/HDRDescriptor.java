/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery;

import com.sun.javafx.iio.common.ImageDescriptor;

/**
 *
 * @author user
 */
public class HDRDescriptor extends ImageDescriptor {
    private static final String formatName = "HDR";

    private static final String[] extensions = { "hdr" };

    private static final Signature[] signatures = {
                    new Signature(new byte[] { '#', '?', 'R', 'A', 'D', 'I', 'A', 'N', 'C', 'E'})};

    private static ImageDescriptor theInstance = null;

    public HDRDescriptor() {
        super(formatName, extensions, signatures);
    }
    
    public static synchronized ImageDescriptor getInstance() {
        if (theInstance == null) {
                theInstance = new HDRDescriptor();                
        }
        return theInstance;
    }
}
