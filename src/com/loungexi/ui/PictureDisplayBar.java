package com.loungexi.ui;

import com.loungexi.pojo.DisplayItem;
import com.loungexi.pojo.Point;
import com.loungexi.pojo.SelectedItem;
import com.loungexi.utils.VBoxData;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * @author LoungeXi
 */
public class PictureDisplayBar {
    public static final FlowPane DISPLAY_FLOW_PANE = new FlowPane();
    public static BorderPane DISPLAY_BORDER;
    private final ScrollPane scrollPane = new ScrollPane();
    private Rectangle rectangle;
    private Point mouseStart;
    private Point mouseDragged;
    private SelectedItem selectedItem = new SelectedItem();
    private Pane pane;


    public PictureDisplayBar(BorderPane borderPane) {
        if (DISPLAY_BORDER == null) {
            DISPLAY_BORDER = borderPane;
        }
        initBackground();
    }

    public void initBackground() {
        initRectangle();
        setFlowPane();
        setScrollPane();
        DISPLAY_BORDER.setCenter(scrollPane);
    }

    private void setScrollPane() {
        scrollPane.setContent(pane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
    }

    /**
     * @author: LoungeXi
     * @return: 初始化 FlowPane
     **/
    private void setFlowPane() {
        DISPLAY_FLOW_PANE.setPadding(new Insets(20.0));
        DISPLAY_FLOW_PANE.setHgap(23);
        DISPLAY_FLOW_PANE.setVgap(20);

        DISPLAY_FLOW_PANE.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                mouseStart = new Point(mouseEvent.getX(), mouseEvent.getY());
            }
        });

        DISPLAY_FLOW_PANE.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                mouseDragged = new Point(mouseEvent.getX(), mouseEvent.getY());
                if (mouseDragged.getX() == mouseStart.getX() && mouseDragged.getY() == mouseStart.getY()) {
                    return;
                }
                Point t = new Point(Math.max(mouseDragged.getX(), mouseStart.getX()), Math.max(mouseDragged.getY(), mouseStart.getY()));
                Point mouseStart_t = new Point(Math.min(mouseDragged.getX(), mouseStart.getX()), Math.min(mouseDragged.getY(), mouseStart.getY()));
                mouseDragged = t;

                for (int i = 0; i < DISPLAY_FLOW_PANE.getChildren().size(); i++) {
                    double left = DISPLAY_FLOW_PANE.getChildren().get(i).getLayoutX();
                    double top = DISPLAY_FLOW_PANE.getChildren().get(i).getLayoutY();
                    double right = left + VBoxData.vBoxWidth;
                    double bottom = top + VBoxData.vBoxHeight;

                    if (!(right < mouseStart_t.getX() || left > mouseDragged.getX() ||
                            top > mouseDragged.getY() || bottom < mouseStart_t.getY())) {
                        selectedItem.select((DisplayItem) DISPLAY_FLOW_PANE.getChildren().get(i));
                    } else {
                        DisplayItem item = (DisplayItem) DISPLAY_FLOW_PANE.getChildren().get(i);
                        if (item.isSelected()) {
                            selectedItem.unselected(item);
                        }
                    }
                }
                showRectangle(mouseStart_t, mouseDragged);
            }
            refreshBIBar();
        });

        DISPLAY_FLOW_PANE.setOnMouseReleased(mouseEvent -> rectangle.setVisible(false));
    }

    private void refreshBIBar() {
        ObservableList<Node> children = DISPLAY_FLOW_PANE.getChildren();
        int selected = 0;
        for (int i = 0; i < children.size(); i++) {
            DisplayItem item = (DisplayItem) children.get(i);
            if (item.isSelected()) {
                selected++;
            }
        }
        new BottomInfoBar(selected);
    }

    private void initRectangle() {
        pane = new Pane();
        rectangle = new Rectangle();
        rectangle.setVisible(false);
        pane.getChildren().addAll(DISPLAY_FLOW_PANE, rectangle);
    }

    private void showRectangle(Point start, Point end) {
        rectangle.setVisible(true);
        rectangle.setLayoutX(start.getX());
        rectangle.setLayoutY(start.getY());
        rectangle.setWidth(end.getX() - start.getX());
        rectangle.setHeight(end.getY() - start.getY());
        rectangle.setStyle("-fx-border-color: #00F5FF;");
        rectangle.setFill(Paint.valueOf("00F5FF33"));
    }
}
