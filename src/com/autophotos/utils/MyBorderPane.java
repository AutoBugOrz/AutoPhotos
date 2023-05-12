package com.autophotos.utils;

import javafx.scene.layout.BorderPane;

/**
 * MyBorderPane中存在一个BorderPane,以供对外调用
 * @author Paul
 */
public class MyBorderPane {
    private static final BorderPane borderPane = new BorderPane();

    public static BorderPane getBorderPane() {
        return borderPane;
    }
}
