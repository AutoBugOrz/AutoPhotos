package com.loungexi.ui;

import com.loungexi.pojo.DisplayItem;
import com.loungexi.pojo.Point;
import com.loungexi.pojo.SelectedItem;
import com.loungexi.utils.VBoxData;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
    private static SelectedItem selectedItem = new SelectedItem();
    private AnchorPane pane = new AnchorPane();
    // if first time click on the blank
    private boolean clickBlank = false;
    private boolean releaseDrag = false;


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

        AnchorPane.setLeftAnchor(DISPLAY_FLOW_PANE, 0.0);
        AnchorPane.setTopAnchor(DISPLAY_FLOW_PANE, 0.0);
        AnchorPane.setBottomAnchor(DISPLAY_FLOW_PANE, 0.0);
        AnchorPane.setRightAnchor(DISPLAY_FLOW_PANE, 0.0);

        System.out.println(scrollPane.getMinViewportWidth());


        DISPLAY_FLOW_PANE.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                mouseStart = new Point(mouseEvent.getX(), mouseEvent.getY());
            }
        });

        DISPLAY_FLOW_PANE.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !releaseDrag) {
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                if (!isInner(x, y) && clickBlank) {
                    for (int i = 0; i < DISPLAY_FLOW_PANE.getChildren().size(); i++) {
                        DisplayItem item = (DisplayItem) DISPLAY_FLOW_PANE.getChildren().get(i);
                        if (item.isSelected()) {
                            selectedItem.unselected(item);
                        }
                    }
                    refreshBIBar();
                }
            }
            releaseDrag = false;
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

        DISPLAY_FLOW_PANE.setOnMouseReleased(mouseEvent ->
        {
            if(!releaseDrag && rectangle.isVisible()){
                releaseDrag = true;
            }
            rectangle.setVisible(false);
        });
    }

    /**
     * @author: Paul
     * @return: if click point in picture field return true, else return false
     **/
    private boolean isInner(double x, double y) {
        int size = DISPLAY_FLOW_PANE.getChildren().size();
        double left = DISPLAY_FLOW_PANE.getChildren().get(0).getLayoutX();
        double right = 0;
        double top = DISPLAY_FLOW_PANE.getChildren().get(0).getLayoutY();
        double bottom = 0;
        double endRight = DISPLAY_FLOW_PANE.getChildren().get(size - 1).getLayoutX() + VBoxData.vBoxWidth;
        double endTop = DISPLAY_FLOW_PANE.getChildren().get(size - 1).getLayoutY();
        for (int i = 0; i < DISPLAY_FLOW_PANE.getChildren().size(); i++) {
            DisplayItem item = (DisplayItem) DISPLAY_FLOW_PANE.getChildren().get(i);
            if (right < item.getLayoutX() + VBoxData.vBoxWidth) {
                right = item.getLayoutX() + VBoxData.vBoxWidth;
            }
            if (bottom < item.getLayoutY() + VBoxData.vBoxHeight) {
                bottom = item.getLayoutY() + VBoxData.vBoxHeight;
            }
        }
        if (right < x || left > x || top > y || bottom < y) {
            clickBlank = true;
            return false;
        }
        if ((x > endRight && x < right) && (y > endTop && y < bottom) && !rectangle.isVisible()) {
            clickBlank = true;
            return false;
        }
        return true;
    }

    public static void refreshBIBar() {
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

        clickBlank = false;
    }

    public static SelectedItem getSelectedItem() {
        return selectedItem;
    }
}
