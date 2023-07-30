/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author user
 */
public class CanvasPanelController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    StackPane pane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Path rootFolder = FileSystems.getDefault().getPath("C:\\Users\\user\\Pictures\\Sheila");
        final List<Path> subfolders = getSubfolders(rootFolder).parallelStream().filter(file -> file.toString().endsWith("jpg")).sequential().collect(Collectors.toList());
        
        CanvasPanel canvas = CanvasPanel.createCanvasPanel(subfolders);
        pane.getChildren().add(canvas);
        canvas.widthProperty().bind(pane.widthProperty().subtract(10));
        canvas.heightProperty().bind(pane.heightProperty().subtract(10));
    }    
    
    private List<Path> getSubfolders(Path root) {
        final List<Path> roots = new ArrayList<>();
        try (DirectoryStream<Path> folders = Files.newDirectoryStream(root)) {
            // level one check, can have entries like floppy, so check level two
            for (final Path pathElement : folders) {
                roots.add(pathElement);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return roots;
    }
    
}
