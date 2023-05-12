package com.autophotos.ui;

import com.autophotos.controller.ImageShowController;
import com.autophotos.pojo.Picture;
import com.autophotos.utils.HomePage;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

/**
 * @author LoungeXi
 */
public class ImageControllerBar {
    private final FlowPane flowPane = new FlowPane();
    private final Button enlargeButton = new Button();
    private final Button shrinkButton = new Button();
    private final Button previousButton = new Button();
    private final Button nextButton = new Button();
    private final Button autoPlayButton = new Button();
    private final Button clockWiseButton = new Button();
    private final Button counterClockWiseButton = new Button();
    private final Button editImageButton = new Button();

    public ImageControllerBar(Picture picture) {
        initButtonStyle();
        realTimeSetButtonStyle();
        setFlowPane();
        //初始化页面之后给页面上的按钮添加相应的功能
        new ImageShowController(counterClockWiseButton, clockWiseButton, enlargeButton, shrinkButton, previousButton, nextButton, autoPlayButton, editImageButton, picture);
        ImageShowFrame.BORDER_PANE.setBottom(flowPane);
    }

    /**
     * @author: LoungeXi
     * @return: 设置flowPane的基本信息
     **/
    private void setFlowPane() {
        flowPane.setPrefSize(HomePage.WIDTH * 0.85, HomePage.HEIGHT * 0.9 * 0.13);
        flowPane.setHgap(HomePage.WIDTH * 0.02);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setStyle("-fx-background-color: #ffffff");
        flowPane.getChildren().addAll(editImageButton, counterClockWiseButton, clockWiseButton, previousButton, nextButton, enlargeButton, shrinkButton, autoPlayButton);
    }

    /**
     * @author: LoungeXi
     * @return: 初始化按钮的样式
     **/
    private void initButtonStyle() {

        enlargeButton.setGraphic(new ImageView(new Image("File:image/enlarge.png")));
        enlargeButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10");

        shrinkButton.setGraphic(new ImageView(new Image("File:image/shrink.png")));
        shrinkButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10");

        previousButton.setGraphic(new ImageView(new Image("File:image/previous.png")));
        previousButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10");

        nextButton.setGraphic(new ImageView(new Image("File:image/next.png")));
        nextButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10");

        autoPlayButton.setGraphic(new ImageView(new Image("File:image/anto play.png")));
        autoPlayButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10");

        clockWiseButton.setGraphic(new ImageView(new Image("File:image/clock wise.png")));
        clockWiseButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10");

        counterClockWiseButton.setGraphic(new ImageView(new Image("File:image/counter clock wise.png")));
        counterClockWiseButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10");

        editImageButton.setGraphic(new ImageView(new Image("File:image/edit.png")));
        editImageButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10");
    }

    /**
     * @author: LoungeXi
     * @return: 设置按钮点击的样式变换
     **/
    private void realTimeSetButtonStyle() {
        enlargeButton.setOnMouseEntered(event -> enlargeButton.setCursor(Cursor.HAND));
        enlargeButton.setOnMousePressed(event -> enlargeButton.setStyle("-fx-background-color: #d5d5d5;" + "-fx-border-color: #838383;" + "-fx-border-radius: 10;" + "-fx-background-radius: 10"));
        enlargeButton.setOnMouseReleased(event -> enlargeButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10"));
        shrinkButton.setOnMouseEntered(event -> shrinkButton.setCursor(Cursor.HAND));
        shrinkButton.setOnMousePressed(event -> shrinkButton.setStyle("-fx-background-color: #d5d5d5;" + "-fx-border-color: #838383;" + "-fx-border-radius: 10;" + "-fx-background-radius: 10"));
        shrinkButton.setOnMouseReleased(event -> shrinkButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10"));
        previousButton.setOnMouseEntered(event -> previousButton.setCursor(Cursor.HAND));
        previousButton.setOnMousePressed(event -> previousButton.setStyle("-fx-background-color: #d5d5d5;" + "-fx-border-color: #838383;" + "-fx-border-radius: 10;" + "-fx-background-radius: 10"));
        previousButton.setOnMouseReleased(event -> previousButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10"));
        nextButton.setOnMouseEntered(event -> nextButton.setCursor(Cursor.HAND));
        nextButton.setOnMousePressed(event -> nextButton.setStyle("-fx-background-color: #d5d5d5;" + "-fx-border-color: #838383;" + "-fx-border-radius: 10;" + "-fx-background-radius: 10"));
        nextButton.setOnMouseReleased(event -> nextButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10"));
        autoPlayButton.setOnMouseEntered(event -> autoPlayButton.setCursor(Cursor.HAND));
        autoPlayButton.setOnMousePressed(event -> autoPlayButton.setStyle("-fx-background-color: #d5d5d5;" + "-fx-border-color: #838383;" + "-fx-border-radius: 10;" + "-fx-background-radius: 10"));
        autoPlayButton.setOnMouseReleased(event -> autoPlayButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10"));
        clockWiseButton.setOnMouseEntered(event -> clockWiseButton.setCursor(Cursor.HAND));
        clockWiseButton.setOnMousePressed(event -> clockWiseButton.setStyle("-fx-background-color: #d5d5d5;" + "-fx-border-color: #838383;" + "-fx-border-radius: 10;" + "-fx-background-radius: 10"));
        clockWiseButton.setOnMouseReleased(event -> clockWiseButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10"));
        counterClockWiseButton.setOnMouseEntered(event -> counterClockWiseButton.setCursor(Cursor.HAND));
        counterClockWiseButton.setOnMousePressed(event -> counterClockWiseButton.setStyle("-fx-background-color: #d5d5d5;" + "-fx-border-color: #838383;" + "-fx-border-radius: 10;" + "-fx-background-radius: 10"));
        counterClockWiseButton.setOnMouseReleased(event -> counterClockWiseButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10"));
        editImageButton.setOnMouseEntered(event -> editImageButton.setCursor(Cursor.HAND));
        editImageButton.setOnMousePressed(event -> editImageButton.setStyle("-fx-background-color: #d5d5d5;" + "-fx-border-color: #838383;" + "-fx-border-radius: 10;" + "-fx-background-radius: 10"));
        editImageButton.setOnMouseReleased(event -> editImageButton.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: lightgray;" + "-fx-border-radius: 10"));
    }

}
