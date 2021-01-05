/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery;

import bitmap.display.gallery.GalleryCanvas.ImageType;
import static bitmap.display.gallery.GalleryCanvas.ImageType.JPG;
import static bitmap.display.gallery.GalleryCanvas.ImageType.PNG;
import java.io.File;
import java.nio.file.Path;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author user
 */
public class GalleryLoader extends VBox{
    private final StackPane stackPane;
    private final GalleryCanvas galleryCanvas;
    private final ScrollBar scrollBarY;
    
    DirectoryChooser directoryChooser = new DirectoryChooser();
    
    private final HBox hBoxCtrl = new HBox();
    private final HBox hBoxView = new HBox();
    
    private final Button addButton = new Button("add");
    private final Button clearButton = new Button("clear");
    
    private ImageType[] types = {JPG, PNG};
    
    public GalleryLoader(int width, int height)
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
        scrollBarY.setValue(0);   //this is not always called when not set here when this method is invoked
        galleryCanvas.addFolderImages(pathFolder, types);
    }
    
    public void setImageTypes(ImageType... types)
    {
        this.types = types;
    }
    

    private void init()
    {
        stackPane.getChildren().add(galleryCanvas);
        hBoxView.getChildren().add(stackPane);
        hBoxView.getChildren().add(scrollBarY);
        
        hBoxCtrl.getChildren().addAll(addButton, clearButton);
        hBoxCtrl.setAlignment(Pos.CENTER);
        hBoxCtrl.setPadding(new Insets(5, 0, 5, 0));
        hBoxCtrl.setSpacing(5);
        
        addButton.setPrefWidth(70);
        clearButton.setPrefWidth(70);
        
        getChildren().add(hBoxCtrl);
        getChildren().add(hBoxView);
        
        //set canvas size to window size change
        galleryCanvas.widthProperty().bind(this.widthProperty().subtract(17));
        galleryCanvas.heightProperty().bind(this.heightProperty().subtract(40));
        
        
        //mouse scroll action
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
        
        clearButton.setOnAction(e->{
            clear();
        });
        
        addButton.setOnAction(e->{
            
            File selectedDirectory = directoryChooser.showDialog(this.getScene().getWindow());
            if(selectedDirectory != null)
                addFolderImages(selectedDirectory.toPath());
            
        });
    }
    
    public void clear()
    {
        galleryCanvas.clear();
    }
    
    public Path getSelectedImagePath()
    {
        return galleryCanvas.getSelectedImagePath();
    }
}
