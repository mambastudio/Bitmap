/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery;

import static bitmap.display.gallery.GalleryCanvas.ImageType.BMP;
import static bitmap.display.gallery.GalleryCanvas.ImageType.GIF;
import static bitmap.display.gallery.GalleryCanvas.ImageType.HDR;
import static bitmap.display.gallery.GalleryCanvas.ImageType.JPG;
import static bitmap.display.gallery.GalleryCanvas.ImageType.PNG;
import bitmap.display.gallery.util.RowContainer;
import bitmap.display.gallery.util.ImageContainer;
import static java.lang.Math.abs;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public final class GalleryCanvas extends Canvas {
    public enum ImageType{
        JPG("jpg"),
        JPG1("JPG"),
        PNG("png"),
        BMP("bmp"),
        HDR("hdr"),
        GIF("gif");
        

        public final String value;

        private ImageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    };
    
    private final double MAX_HEIGHT = 90;
    private final double MAX_WIDTH = 90;
    
    private final ObservableList<ImageContainer>    children    = FXCollections.observableArrayList();
    private final List<RowContainer>                containers  = new ArrayList<>();
    
    private final SimpleDoubleProperty scrollableHeight = new SimpleDoubleProperty();
    private double lastContainerHeight = 0;
    
    private double translateY = 0;
    private double fullSize = 0;
    
    BooleanProperty clearBoolean = new SimpleBooleanProperty(false);
    
    int lastSelection = -1;
    
    public GalleryCanvas(int width, int height)
    {
        super(width, height);
        this.setCache(true);
        this.setCacheHint(CacheHint.SPEED);
        repositionPicturesOnDemand(this.getGraphicsContext2D());        
        this.setOnMouseClicked(e->{
            double xi = e.getX();
            double yi = e.getY();
            
            children.stream().filter((container) -> (container.contains(xi, yi))).forEachOrdered((container) -> {
           
                //index of current selection
                int index = children.indexOf(container);
                
                //unselect based on user selecting twice
                if(index == lastSelection)
                {
                    container.unselect();
                    lastSelection = -1;
                    redraw(this.getGraphicsContext2D());  
                    return;
                }
                
                //unselect the last one
                if(isIndexWithin(lastSelection))
                    children.get(lastSelection).unselect();
                
                //set current selection and set last selection to current
                lastSelection = index;
                container.select();
                redraw(this.getGraphicsContext2D());                
            });            
        });
        
    }
    
    public boolean isIndexWithin(int index)
    {
        if(index < 0) return false;
        if(children.isEmpty()) return false;
        return index < children.size();
    }
    
    public SimpleDoubleProperty getScrollableHeight()
    {
        return scrollableHeight;
    }
    
    private Predicate<Path> getPredicateFileType(ImageType... types)
    {
        Predicate<Path> fileTypes = path -> {
            for(ImageType type : types)           
                if(path.toString().endsWith(type.value))
                    return true;
            return false;
        };
        return fileTypes;
    }
    
    private Predicate<Path> getDefaultPredicateFileType()
    {
        return path -> {
            if(path.toString().endsWith(JPG.value))
                return true;
            else if(path.toString().endsWith(PNG.value))
                return true;
            else if(path.toString().endsWith(BMP.value))
                return true;            
            else return path.toString().endsWith(GIF.value);
        };
    }
    
    public void addFolderImages(Path pathFolder, ImageType... types)
    {
        clearBoolean.set(false);
        Predicate<Path> fileTypes = types.length != 0 ? getPredicateFileType(types) : getDefaultPredicateFileType();
        final List<Path> rootFolder = getSubfolders(pathFolder).parallelStream().filter(fileTypes).sequential().collect(Collectors.toList());
        addImages(MAX_HEIGHT, MAX_WIDTH, rootFolder);
        
        redrawAndSetScrollHeight(getGraphicsContext2D());
    }
    
    public Path getSelectedImagePath()
    {
        if(lastSelection > -1)
            return children.get(lastSelection).getImagePath();
        return null;
    }
    
    public void clear()
    {
        clearBoolean.set(true);
        children.clear();
        containers.clear();
        lastSelection = -1;
                
        redrawAndSetScrollHeight(getGraphicsContext2D());
    }
    
    private List<Path> getSubfolders(Path root) {
        final List<Path> roots = new ArrayList<>();
        try (DirectoryStream<Path> folders = Files.newDirectoryStream(root)) {
            // level one check, can have entries like floppy, so check level two
            for (final Path pathElement : folders) {
                roots.add(pathElement);
            }
        } catch (final Exception e) {
        }
        return roots;
    }
    
    public void scroll(double scrollY)
    {
        
        double totalHeight = getLastRowContainer(containers).getHeightSize()*containers.size();
        //System.out.println(totalHeight);
        //To prevent scroll if view port size is larger than total size of images
        double difference = totalHeight - this.getBoundsInParent().getHeight();        
        if(difference < 0)
            return;
        
        //scroll then
        translateY = scrollY;
        setupRows();
        renderCanvas(this.getGraphicsContext2D());
    }
    
    private void repositionPicturesOnDemand(final GraphicsContext gc)
    {
        this.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            if (oldSceneWidth.doubleValue() != newSceneWidth.doubleValue()) {
                //redraw
                redrawAndSetScrollHeight(gc);
            }
        });
        this.heightProperty().addListener((observableValue, oldSceneHight, newSceneHight) -> {
            if (oldSceneHight.doubleValue() != newSceneHight.doubleValue()) {
                //redraw
                redrawAndSetScrollHeight(gc);
            }
        });
        
    }
    
    public void redraw(final GraphicsContext gc)
    {
        setupRows();        
        renderCanvas(gc);        
    }
    
    public void redrawAndSetScrollHeight(final GraphicsContext gc)
    {
        setupRows();        
        renderCanvas(gc);
        scrollableHeight.set(fullSize - this.getBoundsInParent().getHeight());
    }

    
    private void setupRows()
    {
        scaleImages(1); //
        double offsetY = translateY;
        double padding = 3;
        containers.clear();        
        containers.add(new RowContainer(offsetY, getWidth(), padding, clearBoolean));
         
        for(ImageContainer image : children)
        {
            boolean isLimit = getLastRowContainer(containers).add(image);
            if(isLimit) //limit is when there is no space left to add in row
            {                
                //normalize row to fit width of frame
                getLastRowContainer(containers).normalizeWidth();
                //when you add more it fails definitely (this is always called if you add more)                
                if(getLastRowContainer(containers).isLastAttemptFail)
                {
                    offsetY += getLastRowContainer(containers).getHeightSize();    //increment offsetY
                    containers.add(new RowContainer(offsetY, getWidth(), padding, clearBoolean)); //add new row
                    getLastRowContainer(containers).add(image); //add image in new row
                }
            }            
        }
        //limit was not reached hence normalize width
        if(!getLastRowContainer(containers).isLimit())
            getLastRowContainer(containers).normalizeWidth();
        
        //reconfigure to accomodate offset
        lastContainerHeight = getLastRowContainer(containers).getHeightSize();
        fullSize = getLastRowContainer(containers).getOffsetY() + lastContainerHeight;
    }
    
    
    public void paintImages(final GraphicsContext gc) 
    {   
        //draw all
        containers.forEach((container) -> {                
            container.drawImageToCanvas(gc);         
        });        
    }
        
    private void addImages(double maxHeight, double maxWidth, List<Path> imageFolder) {        
        final List<ImageContainer> all = imageFolder.parallelStream().map(path -> new ImageContainer(path, maxHeight, maxWidth, clearBoolean)).collect(Collectors.toList());
        getChildren().addAll(all);
    }
    
    private void scaleImages(double scale)
    {
        getChildren().forEach((c) -> {
            c.setScaleFactor(scale);           
        });
    }
    
    private RowContainer getLastRowContainer(List<RowContainer> list)
    {
        if(list.isEmpty()) return null;
        return list.get(list.size()-1);
    }
        
    private void renderCanvas(GraphicsContext gc)
    {
        gc.clearRect(0, 0, getWidth(), getHeight());  
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());
        if(!clearBoolean.get())
            paintImages(gc);        
    }
    
    public ObservableList<ImageContainer> getChildren() {
        return children;
    }
    
    public int getAllImageHeight()
    {
        int i = 0;
        i = containers.stream().map(container -> (int)container.getHeightSize()).reduce(i, Integer::sum);
        return i;
    }
}
