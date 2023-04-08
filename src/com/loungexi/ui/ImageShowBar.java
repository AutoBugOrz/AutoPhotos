package com.loungexi.ui;

import com.loungexi.utils.DragScrollPane;
import com.loungexi.pojo.LargeImageShowItem;
import com.loungexi.pojo.Picture;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

public class ImageShowBar {
    // TODO 静态变量整体修改
    public final static AnchorPane ANCHOR_PANE = new AnchorPane();
    // TODO 静态变量整体修改
    public final static Label Image_LABEL = new Label();
    private final DragScrollPane scrollPane = new DragScrollPane();

    public ImageShowBar(Picture picture) {
        new LargeImageShowItem(picture, Image_LABEL);
        setScrollPane();
    }

    /**
     * @author: LoungeXi
     * @return: 初始化一些scrollPane的设置
     **/
    private void setScrollPane() {
        ImageShowFrame.BORDER_PANE.setCenter(scrollPane);
        scrollPane.setDragContent(ANCHOR_PANE);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setCursor(Cursor.OPEN_HAND);
    }
}
