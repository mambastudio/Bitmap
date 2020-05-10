/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public final class CanvasPanel extends Canvas {
    
    private final double MAX_HEIGHT = 90;
    private final double MAX_WIDTH = 90;
    
    private final ObservableList<ImageContainer> children = FXCollections.observableList(new ArrayList<>());
    
    private CanvasPanel(int x, int y, final List<Path> imageFolder)
    {
        super(x, y);
        registerScale(this.getGraphicsContext2D());
        addImages(MAX_HEIGHT, MAX_WIDTH, imageFolder);
    }
    
    private void registerScale(final GraphicsContext gc)
    {
        this.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            if (oldSceneWidth.doubleValue() != newSceneWidth.doubleValue()) {
                //redraw
                renderCanvas(gc);
            }
        });
        this.heightProperty().addListener((observableValue, oldSceneHight, newSceneHight) -> {
            if (oldSceneHight.doubleValue() != newSceneHight.doubleValue()) {
                //redraw
                renderCanvas(gc);
            }
        });
    }
    
    public void paintImages(final GraphicsContext gc, final List<ImageContainer> all) 
    {   
        scaleImages(1.0);
        double offsetY = 0;
        double padding = 3;
        List<RowContainer> containers = new ArrayList<>();
        containers.add(new RowContainer(offsetY, getWidth(), padding));
         
        for(ImageContainer image : all)
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
                    containers.add(new RowContainer(offsetY, getWidth(), padding)); //add new row
                    getLastRowContainer(containers).add(image); //add image in new row
                }
            }            
        }
        //limit was not reached hence normalize width
        if(!getLastRowContainer(containers).isLimit())
            getLastRowContainer(containers).normalizeWidth();
        
        //draw all
        containers.forEach((container) -> {               
            container.drawImageToCanvas(gc);         
        });
    }
    
    private void addImages(double maxHeight, double maxWidth, List<Path> imageFolder) {
        final List<ImageContainer> all = imageFolder.parallelStream().map(path -> new ImageContainer(path, maxHeight, maxWidth)).collect(Collectors.toList());
        getChildren().addAll(all);
        scaleImages(1.0);        
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
    
    public static CanvasPanel createCanvasPanel(final List<Path> imageFolder)
    {
        return new CanvasPanel(300, 300, imageFolder);
    }
    
    private void renderCanvas(GraphicsContext gc)
    {
        gc.clearRect(0, 0, getWidth(), getHeight());
        //gc.setFill(Color.GREY);
        //gc.fillRect(0, 0, getWidth(), getHeight());       
        paintImages(gc, children);
    }

    public ObservableList<ImageContainer> getChildren() {
        return children;
    }
}
