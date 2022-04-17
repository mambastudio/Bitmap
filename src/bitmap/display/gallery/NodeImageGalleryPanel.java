/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery;

import bitmap.display.gallery.panel.NodeImage;
import bitmap.display.gallery.panel.NodeImageSelection.SelectionType;
import bitmap.display.gallery.panel.RectImageContainer;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author user
 */
public class NodeImageGalleryPanel extends ScrollPane{
    //type of images of interest
    public static enum ImageType{JPG, JPEG, PNG, HDR};
    
    //layout and display
    private final RectImageContainer container;
    private final Pane pane;
    
    //filter list
    private final Set<ImageType> imageSet = new LinkedHashSet<>(Arrays.asList(ImageType.values()));
    private FilenameFilter filter = null;
    
    public NodeImageGalleryPanel()
    {
        super();
        setContent(pane = new Pane());
        
        container = new RectImageContainer(this, pane);
        setFitToWidth(true);
        this.setStyle(
                  "-fx-focus-color: transparent;"
                + "-fx-faint-focus-color: transparent;"
                + "-fx-border-color: lightgray");
        
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            if(e.getClickCount() == 1)
            {
                if(e.getButton() == MouseButton.PRIMARY)
                    container.clearSelection();
            }
        });
        
        filter = (dir, name)->{
            for(ImageType type : imageSet)
            {                
                //both have to be tested in lower case to avoid issues type capitalisation
                if(name.toLowerCase().endsWith(type.name().toLowerCase()))
                    return true;
            }
            return false;
        };
    }
    
    public void setImageFilter(ImageType... types)
    {
        imageSet.clear();
        imageSet.addAll(Arrays.asList(types));
    }
    
    public void addImagesFromDirectory(Path pathFolder)
    {
        if(pathFolder.toFile().isDirectory())
        {
            container.addAll(getFileImages(pathFolder));
        }
    }
    
    public void addImagesFromDirectory(Supplier<File> supplier)
    {
        File file = supplier.get();
        if(file != null && file.isDirectory())
            container.addAll(getFileImages(file.toPath()));
    }
    
    public List<String> getSelectedPaths()
    {
        List<NodeImage> selectedNodeImages = container.getSelection();
        List<String> selectedPaths = new ArrayList<>();
        for(NodeImage nodeImage : selectedNodeImages)
            selectedPaths.add(nodeImage.getPath());
        return selectedPaths;
    }
    
    public List<File> getSelectedFiles()
    {
        List<NodeImage> selectedNodeImages = container.getSelection();
        List<File> selectedPaths = new ArrayList<>();
        for(NodeImage nodeImage : selectedNodeImages)
            selectedPaths.add(new File(nodeImage.getPath()));
        return selectedPaths;
    }
    
    public void deleteSelection()
    {
        container.deleteSelection();
    }
    
    public void setContextMenuSelection(ContextMenu contextMenu)
    {
        container.setContextMenuSelection(contextMenu);
    }
    
    public void setSelectionType(SelectionType selectionType)
    {
        container.setSelectionType(selectionType);
    }
    
    public void refresh()
    {
        container.draw();
    }
    
    private File[] getFileImages(Path pathFolder)
    {        
        //Filter list of files
        File[] files = pathFolder.toFile().listFiles(filter);           
        return files;        
    } 
}
