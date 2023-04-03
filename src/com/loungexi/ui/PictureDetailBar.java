package com.loungexi.ui;

import com.loungexi.utils.HomePage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

/**
 * @author: LoungeXi
 **/
public class PictureDetailBar {
    public final static FlowPane DETAIL_FLOW_PANE = new FlowPane();

    public PictureDetailBar(BorderPane borderPane) {
        DETAIL_FLOW_PANE.setPrefWidth(HomePage.WIDTH*0.2541);
        initBackground(borderPane);
    }

    /**
     * @author: LoungeXi
     * @param: [borderPane]
     * @return: 初始化右边详细信息展示栏的样式，添加对应CSS
     **/
    private void initBackground(BorderPane borderPane) {
        DETAIL_FLOW_PANE.setStyle("-fx-background-color: #eaeaea;" + "-fx-border-color: #a9a9a9");
        borderPane.setRight(DETAIL_FLOW_PANE);
    }
}
