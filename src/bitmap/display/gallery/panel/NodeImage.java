/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery.panel;

import bitmap.display.gallery.util.ImageMetaData;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

/**
 *
 * @author user
 */
public final class NodeImage extends Group {
    private ImageView imageView = null;
    private double x = 0, y = 0;
    private double fitWidth = 100, fitHeight = 100;
    private Group selection = new Group();
    private final String url;    
    
        
    public NodeImage(String url)
    {
        File file = new File(url);
        ImageMetaData meta = new ImageMetaData(file);
        double[] adjSize = meta.getAdjustedSize(fitWidth, fitWidth);
        Image im = new WritableImage((int)adjSize[0], (int)adjSize[1]);
        imageView = new ImageView(im);   
        imageView.setPreserveRatio(true);
        
        
        init();
        
        this.url = url;
        
        try {                 
            final Image image;
            //load image
            image = new Image(file.toURI().toURL().toExternalForm(), 600, 600, true, true, true);
            image.progressProperty().addListener((obs, ov, nv)->{
                if(nv.intValue() == 1)               
                    Platform.runLater(()->{
                        imageView.setImage(image);
                        fitSize(fitWidth, fitHeight);
                        
                        //tooltip
                        Tooltip.install(this, new Tooltip(file.getName()));
                        
                    });           
            });

        } catch (IOException ex) {
            Logger.getLogger(NodeImage.class.getName()).log(Level.SEVERE, null, ex);
        }   

    }
    
   
    
    public String getPath()
    {
        return url;
    }
    
    public ObjectProperty<Image> imageProperty()
    {
        return imageView.imageProperty();
    }
    
    public ImageView getImageView()
    {
        return imageView;
    }
    
    public void fitSize(double fitWidth, double fitHeight)
    {
        setFitWidth(fitWidth); setFitHeight(fitHeight);
    }
    
    private void init()
    {
        this.getChildren().clear();
        this.getChildren().add(imageView);  
        
        selection = getSelection();  
        fitSize(fitWidth, fitHeight);
        setX(x); setY(y);    
        
    }
    
    public void addSelection()
    {
        if(!this.getChildren().contains(selection))
            this.getChildren().add(selection);
    }
    
    public void removeSelection()
    {
        if(this.getChildren().contains(selection))
            this.getChildren().remove(selection);
    }
    
    private void repositionSelection()
    {        
        selection.setTranslateX(x);
        selection.setTranslateY(y);
        
        
    }
    
    private Group getSelection()
    {
        SVGPath svgPath = new SVGPath();
        String path ="m3.10189,11.0475l6.36411,10.99071l18.68953,-10.23273l-2.9001,-4.69948l-14.09771,7.95879l-3.70568,-6.51863l-4.35015,2.50133z";  
        svgPath.setContent(path);
        svgPath.setFill(Color.WHITE);
        float scale = 0.51f;
        svgPath.setScaleX(scale);
        svgPath.setScaleY(scale);
        svgPath.setTranslateX(-8);
        svgPath.setTranslateY(-7);
        svgPath.setRotate(-15);
        
        Rectangle rectangle = new Rectangle(15, 15, Color.CORAL);
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
        
        Group icon = new Group(rectangle, svgPath);
        
        //translate final position for icon
        icon.setTranslateX(5);
        icon.setTranslateY(5);
        
        Rectangle selectRect = new Rectangle(100, 100, Color.color(1, 1, 1, 0.2));
        this.imageView.boundsInLocalProperty().addListener((obs, ov, nv)->
        {
            selectRect.setWidth(nv.getWidth());
            selectRect.setHeight(nv.getHeight());
        });
        
        return new Group(selectRect, icon);
    }
    
    public double getComponentWidth()
    {
        if(imageView != null)
            return imageView.getBoundsInLocal().getWidth();
        else
            return -1;
    }
    
    public double getComponentHeight()
    {
        if(imageView != null)
            return imageView.getBoundsInLocal().getHeight();
        else
            return -1;
    }
    
    public void setX(double x)
    {
        this.x = x;
        if(imageView != null)
        {
            imageView.setX(x);
            repositionSelection();
            
        }        
    }
    
    public void setY(double y)
    {
        this.y = y;
        if(imageView != null)
        {
            imageView.setY(y);
            repositionSelection();
        }
    }
    
    public void setFitWidth(double w)
    {
        this.fitWidth = w;
        if(imageView != null)
        {
            
            imageView.setFitWidth(w);              
            repositionSelection();
        }
    }
    
    public void setFitHeight(double h)
    {
        this.fitHeight = h;
        if(imageView != null)
        {
            imageView.setFitHeight(h);             
            repositionSelection();
            
        }
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object == null || !(object instanceof NodeImage))
            return false;
        NodeImage nodeObject = (NodeImage)object;
        return url.equals(nodeObject.url);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.url);
        return hash;
    }
}
