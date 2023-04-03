package com.loungexi.ui;

import javafx.geometry.Insets;

import javafx.scene.control.ScrollPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;


/**
 * @author LoungeXi
 */
public class PictureDisplayBar {
    public static final FlowPane DISPLAY_FLOW_PANE = new FlowPane();
    private final ScrollPane scrollPane = new ScrollPane();


    public PictureDisplayBar(BorderPane borderPane) {
        initBackground(borderPane);
    }

    private void initBackground(BorderPane borderPane) {
        setFlowPane();
        setScrollPane();
        borderPane.setCenter(scrollPane);
    }

    private void setScrollPane() {
        scrollPane.setContent(DISPLAY_FLOW_PANE);
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
    }
}
