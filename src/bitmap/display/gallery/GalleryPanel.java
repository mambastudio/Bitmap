/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery;

import java.nio.file.Path;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class GalleryPanel extends HBox{
    private final StackPane stackPane;
    private final GalleryCanvas galleryCanvas;
    private final ScrollBar scrollBarY;
    
    public GalleryPanel(int width, int height)
    {
        stackPane = new StackPane();
        galleryCanvas = new GalleryCanvas(width, height);
        
        scrollBarY = new ScrollBar();
        scrollBarY.setOrientation(Orientation.VERTICAL);
        
        stackPane.setMinSize(10, 10);
        init();
    }
    
    public void addFolderImages(Path pathFolder)
    {
        galleryCanvas.addFolderImages(pathFolder);
    }
    
    private void init()
    {
        stackPane.getChildren().add(galleryCanvas);
        getChildren().add(stackPane);
        getChildren().add(scrollBarY);
        
        galleryCanvas.widthProperty().bind(this.widthProperty().subtract(17));
        galleryCanvas.heightProperty().bind(this.heightProperty());
        
        galleryCanvas.setOnScroll(e->{
            if(e.getDeltaY() < 0)
              scrollBarY.increment();
            else
              scrollBarY.decrement();
        });
        
        galleryCanvas.getScrollableHeight().addListener((o, oldValue, newValue) -> {
            scrollBarY.setValue(0);   
            scrollBarY.setMax(newValue.doubleValue());
            scrollBarY.setUnitIncrement(0.1 * scrollBarY.getMax()); 
                      
        });
        
        scrollBarY.valueProperty().addListener((o, oldValue, newValue)->{            
            galleryCanvas.scroll(-newValue.doubleValue());
        });
    }
}
