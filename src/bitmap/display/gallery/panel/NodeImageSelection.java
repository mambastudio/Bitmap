/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmap.display.gallery.panel;

import static bitmap.display.gallery.panel.NodeImageSelection.SelectionType.MULTI_SELECTION;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

/**
 *
 * @author user
 * 
 * Rubber band selection 
 * https://stackoverflow.com/questions/28562509/how-to-select-multiple-components-of-canvas-using-ctrl-key-in-javafx
 * 
 * 
 * 
 */
public class NodeImageSelection {
    public enum SelectionType{
        SINGLE_SELECTION,
        MULTI_SELECTION
    };
    
    final DragContext dragContext = new DragContext();
    final SelectionModel selectionModel = new SelectionModel();
    final List<NodeImage> children;
    
    ContextMenu contextMenu = null;
    
    Rectangle rect;
    Pane pane;
    SelectionType selectType = MULTI_SELECTION;
    
    public NodeImageSelection(List<NodeImage> children, Pane pane, ContextMenu contextMenu) {
        this(children, pane);
        this.contextMenu = contextMenu;
    }

    public NodeImageSelection(List<NodeImage> children, Pane pane) {
        this.pane = pane;
        this.children = children;

        rect = new Rectangle( 0,0,0,0);
        rect.setStroke(Color.BLUE);
        rect.setStrokeWidth(1);
        rect.setStrokeLineCap(StrokeLineCap.ROUND);
        rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

        pane.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        pane.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
        
        pane.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            if(selectionModel.contains(event.getX(), event.getY()))
            {
                if(contextMenu != null && !contextMenu.getItems().isEmpty())
                {
                    contextMenu.show(pane, event.getScreenX(), event.getScreenY());
                    event.consume();
                }
            }
        });
        pane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if(contextMenu != null && !contextMenu.getItems().isEmpty())
            {
                contextMenu.hide();
            }
        });

    }
    
    public void setContextMenu(ContextMenu contextMenu)
    {
        this.contextMenu = contextMenu;        
    }
    
    public void setSelectionType(SelectionType selectionType)
    {
        this.selectType = selectionType;
    }

    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {            
            
            if(event.getButton() == MouseButton.SECONDARY)
            {               
                event.consume();
                return;
            }
            dragContext.mouseAnchorX = event.getX();
            dragContext.mouseAnchorY = event.getY();

            rect.setX(dragContext.mouseAnchorX);
            rect.setY(dragContext.mouseAnchorY);
            rect.setWidth(0);
            rect.setHeight(0);

            pane.getChildren().add( rect);

            event.consume();

        }
    };
    
    public void deleteSelection()
    {
        for(NodeImage image : selectionModel.selection)
            children.remove(image);
        selectionModel.clear();
    }
    
    public void clearSelection()
    {
        selectionModel.clear();
    }
    
    public int getSelectedCount()
    {
        return selectionModel.getSelectedCount();
    }

    public boolean isOnlyOneSelected()
    {
        return getSelectedCount() == 1;
    }

    public boolean isManySelected()
    {
        return getSelectedCount() > 1;
    }
    
    public boolean isSelected()
    {
        return getSelectedCount() > 0;
    }
    
    public List<NodeImage> getNodeImageSelection()
    {
        return new ArrayList<>(selectionModel.selection);
    }

    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            if(event.getButton() == MouseButton.SECONDARY)
            {   
                event.consume();
                return;
            }
            
            //store the single selected
            NodeImage nImage = null;
            if(isOnlyOneSelected())
                nImage = selectionModel.getFirst();

            //simple click and release
            if( !event.isShiftDown() && !event.isControlDown()) {
                
                selectionModel.clear();
            }
            

            //select all in the rectangle
            for( Node node: pane.getChildren()) {

                if( node instanceof NodeImage) {
                    NodeImage nodeImage = (NodeImage)node;                    
                    if(nodeImage.getBoundsInParent().intersects( rect.getBoundsInParent())) {                        
                        if( event.isShiftDown()) {

                            selectionModel.add(nodeImage);

                        } else if( event.isControlDown()) {

                            if( selectionModel.contains(nodeImage)) {
                                selectionModel.remove(nodeImage);
                            } else {
                                selectionModel.add(nodeImage);
                            }
                        } else {
                            selectionModel.add(nodeImage);
                        }

                    }
                }

            }

            rect.setX(0);
            rect.setY(0);
            rect.setWidth(0);
            rect.setHeight(0);

            pane.getChildren().remove( rect);
            
            //remove selection if being selected twice alone
            if(isOnlyOneSelected())
                if(selectionModel.contains(nImage))
                    clearSelection();

            event.consume();

        }
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            if(selectType == MULTI_SELECTION)
            {
                if(event.getButton() == MouseButton.SECONDARY)
                {   
                    event.consume();
                    return;
                }

                double offsetX = event.getX() - dragContext.mouseAnchorX;
                double offsetY = event.getY() - dragContext.mouseAnchorY;

                if( offsetX > 0)
                    rect.setWidth( offsetX);
                else {
                    rect.setX(event.getX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                }

                if( offsetY > 0) {
                    rect.setHeight( offsetY);
                } else {
                    rect.setY(event.getY());
                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                }

                event.consume();
            }
        }
    };

    private final class DragContext {
        public double mouseAnchorX;
        public double mouseAnchorY;
    }
    
    private class SelectionModel 
    {
        Set<NodeImage> selection = new HashSet<>();
        
        public boolean contains(double x, double y)
        {
            boolean hit = false;
            for(NodeImage select : selection)
                hit |= select.contains(x, y);
            return hit;
        }
        
        public int getSelectedCount()
        {
            return selection.size();
        }
        
        public void add( NodeImage node) {

            selection.add( node);
            node.addSelection();
        }

        public void remove(NodeImage node) {
            if(node == null) return;
            selection.remove( node);
            node.removeSelection();
            
        }

        public void clear() {
            while( !selection.isEmpty()) {            
                remove( selection.iterator().next());
            }
        }

        public boolean contains(NodeImage node) {
            if(node == null) return false;
            return selection.contains(node);
        }
        
        public NodeImage getFirst()
        {
            if(getSelectedCount() == 1)
            {
                List<NodeImage> list = new ArrayList<>(selection);
                return list.get(0);
            }
            return null;
        }
    }
}

