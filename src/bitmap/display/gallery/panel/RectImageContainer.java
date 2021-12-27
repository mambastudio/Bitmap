/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery.panel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import bitmap.RectBound;
import bitmap.display.gallery.panel.NodeImageSelection.SelectionType;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 *
 * @author user
 */
public class RectImageContainer implements RectBound<RectImageRow, NodeImage> {
    
    List<RectImageRow> rows = new ArrayList<>();
    List<NodeImage> children = new ArrayList<>(); 
    NodeImageSelection selection;
    double padding = 3;
    
    double offsetY = 0;
    
    private double lastContainerHeight = 0;    
    private double fullSize = 0;
    
    Pane pane;
    ScrollPane spane;
    
    public RectImageContainer(ScrollPane spane, Pane pane)
    {
        this.pane = pane;
        this.spane = spane;
        
        this.pane.widthProperty().addListener((obs, ov, nv)->{      
            Platform.runLater(()-> draw());  
            
        });
        
        this.pane.heightProperty().addListener((obs, ov, nv)->{             
            Platform.runLater(()-> draw());             
        });
        
        spane.hvalueProperty().addListener((h) -> updateNodes());
        spane.vvalueProperty().addListener((v) -> updateNodes());
        
        selection = new NodeImageSelection(children, pane);    
        
        spane.setOnDragOver(e->{
            if (e.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    int i = 0;
                    for(File file : e.getDragboard().getFiles())
                    {
                        if(file.getName().toLowerCase().endsWith(".jpg"))
                            i++;
                        else if(file.getName().toLowerCase().endsWith(".jpeg"))
                            i++;
                        else if(file.getName().toLowerCase().endsWith(".png"))
                            i++;                  
                    }
                    
                    if(i > 0)
                        e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                e.consume();
        });
        
        spane.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                
                List<File> listFiles = new ArrayList<>();
                for(File file : e.getDragboard().getFiles())
                {
                    if(file.getName().toLowerCase().endsWith(".jpg"))
                        listFiles.add(file);
                    else if(file.getName().toLowerCase().endsWith(".jpeg"))
                        listFiles.add(file);
                    else if(file.getName().toLowerCase().endsWith(".png"))
                        listFiles.add(file);                   
                }
                File[] files = new File[listFiles.size()];
                listFiles.toArray(files);
                addAll(files);
                success = true;
            }
            /* let the source know whether the string was successfully 
             * transferred and used */
            e.setDropCompleted(success);

            e.consume();            
        });
        
    }
    
    public void setSelectionType(SelectionType selectionType)
    {
        selection.setSelectionType(selectionType);
    }
    
    public void setContextMenuSelection(ContextMenu contextMenu)
    {
        selection.setContextMenu(contextMenu);
    }
    
    //only display images visible to the viewport
    private void updateNodes() {              
        double hval = spane.getHvalue();
        double vval = spane.getVvalue();
        double displayW = spane.getViewportBounds().getWidth();
        double displayH = spane.getViewportBounds().getHeight();
                
        //set true size of pane if it had images and scrollpane to resize properly
        pane.setPrefSize(getMaxX(), getMaxY());
                
        //starting point of viewport display after scroll
        double startX = Math.floor((pane.getPrefWidth() - displayW) * hval);
        double startY = Math.floor((pane.getPrefHeight() - displayH) * vval);
               
        pane.getChildren().clear();
       
        for(NodeImage image : children)
        {
            if(image.getBoundsInLocal().intersects(startX, startY, displayW, displayH))
                if(!pane.getChildren().contains(image))
                    pane.getChildren().add(image);
        }       
    }
    
    public List<NodeImage> getSelection()
    {
        return selection.getNodeImageSelection();
    }
    public void clear()
    {
        rows.clear();
        children.clear();
    }
    
    public final void addAll(File... files)
    {
       
        for(File file : files)
        {            
            NodeImage nImage = new NodeImage(file.toPath().toString());
            if(!children.contains(nImage))
            {
                nImage.imageProperty().addListener((obs, ov, nv)->draw());
                children.add(nImage); 
            }
        }        
    } 
    
    public void deleteSelection()
    {
        selection.deleteSelection();
    }
    
    public void clearSelection()
    {
        selection.clearSelection();
    }
            
    public void draw()
    {
        rows.clear();        
        offsetY = 0;
        
        for(NodeImage image : children)
        {                        
            if(rows.isEmpty())            
                rows.add(new RectImageRow(offsetY, getMaxX(), padding));
            
            boolean isLimit = getLastRow().add(new RectImage(image));
            if(isLimit) //limit is when there is no space left to add in row
            {                
                //normalize row to fit width of frame
                getLastRow().normalizeWidth();
                //when you add more it fails definitely (this is always called if you add more)                
                if(getLastRow().isLastAttemptFail)
                {
                    offsetY += getLastRow().getMaxY();    //increment offsetY
                    rows.add(new RectImageRow(offsetY, getMaxX(), padding)); //add new row
                    getLastRow().add(new RectImage(image)); //add image in new row
                    
                    
                }
            }
        }
        
        //normalize the last row
        if(!rows.isEmpty())
            getLastRow().normalizeWidth();
        
        //reconfigure to accomodate offset
        if(getLastRow() != null)        
            lastContainerHeight = getLastRow().getMaxY();
        else
            lastContainerHeight = 0;
        
        fullSize = offsetY + lastContainerHeight;
        
        
        
        updateNodes();
    }

    @Override
    public double getMinX() {
        return 0;
    }

    @Override
    public double getMinY() {
        return 0;
    }

    @Override
    public void setMinX(double minX) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void setMinY(double minY) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public double getMaxX() {
        return pane.getWidth();
    }

    @Override
    public double getMaxY() {
        return fullSize;
    }
    
    private RectImageRow getLastRow()
    {
        if(rows.isEmpty()) return null;
        return rows.get(rows.size()-1);
    }

    @Override
    public RectImageRow getDefault() {
        if(rows.isEmpty())
            return null;
        else
            return rows.get(0);
              
    }
    
    @Override
    public List<NodeImage> getChildren()
    {
        return children;
    }
    
}
