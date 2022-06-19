/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pannable;

import bitmap.display.PannableDisplay;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class TestPannable extends Application {
    
    FileChooser fileChooser = new FileChooser();
    File folder = new File("C:\\Users\\user\\Pictures");
    
    PannableDisplay scrollPane = new PannableDisplay();
    Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JPG", "*.jpg")
           ,new FileChooser.ExtensionFilter("PNG", "*.png")
       );
        
        File file = new File("C:\\Users\\user\\Pictures\\Screenshot 2020-10-16 182155.jpg");
        Image image = openFileImage(file); 
        
        
        scrollPane.setImage(image);
        
        Button fitBtn = new Button("Fit");
        fitBtn.setMinWidth(60);
        fitBtn.setOnAction(scrollPane::toFitView);
        
        Button loadBtn = new Button("Load");
        loadBtn.setMinWidth(60);
        loadBtn.setOnAction(this::openImage);
        
        HBox hbox = new HBox(fitBtn, loadBtn);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5);
        
        VBox box = new VBox(scrollPane, hbox);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);
        box.setPadding(new Insets(0, 0, 5, 0));
        
        
        Scene scene = new Scene(box, 800, 650);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        scrollPane.toFitView();
       
    }
    
    public void openImage(ActionEvent e)
    {
        fileChooser.setInitialDirectory(folder);
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if(selectedFile != null)
        {
            folder = selectedFile.getParentFile();
            Image image = openFileImage(selectedFile);
            scrollPane.setImage(image);
        }
    }
    
    private Image openFileImage(File file)
    {
        Image image = null;
        try {
             image = new Image(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException ex) {
            Logger.getLogger(TestPannable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}