/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rect;

import static bitmap.display.gallery.GalleryCanvas.ImageType.HDR;
import bitmap.display.gallery.HDRImageLoaderFactory;
import bitmap.display.gallery.panel.NodeImageGalleryPanel;
import bitmap.display.gallery.panel.NodeImageSelection;
import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class RectCutMain4 extends Application {
    static 
    {
        HDRImageLoaderFactory.install();        
    }
    
    DirectoryChooser directoryChooser = new DirectoryChooser();
    NodeImageGalleryPanel panel;
    Scene scene;
    
    @Override
    public void start(Stage primaryStage) {
      
        
        VBox root = new VBox();
        root.setSpacing(3);
        root.setPadding(new Insets(3, 0, 0, 0));
        
        panel = new NodeImageGalleryPanel();
        
        ContextMenu menu = new ContextMenu();
        MenuItem item = new MenuItem("Delete");
        item.setOnAction(e->{
            panel.deleteSelection();
            panel.refresh();
        });
        menu.getItems().setAll(item);
        panel.setContextMenuSelection(menu);
        
        //panel.setSelectionType(NodeImageSelection.SelectionType.SINGLE_SELECTION);
        
        HBox hbox = new HBox();
        Button load = new Button("Load");
        load.setMinWidth(70);
        load.setOnAction(this::loadAction);
        Button remove = new Button("Remove");
        remove.setMinWidth(70);
        remove.setOnAction(this::removeAction);
        hbox.getChildren().addAll(load, remove);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5);
        
        root.getChildren().addAll(hbox, panel);
        VBox.setVgrow(panel, Priority.ALWAYS);
        
        scene = new Scene(root, 600, 550);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void loadAction(ActionEvent e)
    {        
        File selectedDirectory = directoryChooser.showDialog(scene.getWindow());
        if(selectedDirectory != null)
            panel.addImagesFromDirectory(selectedDirectory.toPath());
       
    }
    
    public void removeAction(ActionEvent e)
    {        
        panel.deleteSelection();
        panel.refresh();
       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
