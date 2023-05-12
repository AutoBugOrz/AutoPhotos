package com.autophotos.utils;

import com.autophotos.ui.MainPageTopBar;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * @author Lantech
 * @description: 实现排序功能的类
 */
public class MainPageTopBars {

    private MainPageTopBars() {

    }

    /**
     * @description: 每次切换界面时将MainPageTopBar重新初始化
     * @param: null
     * @return: void
     * @author Lantech
     */
    public static void setDefault(MainPageTopBar mainPageTopBar) {
        for (Node node : mainPageTopBar.getChildren()) {
            Button btn = (Button) node;
            btn.setText(MainPageTopBar.getBtnInitTexts().get(btn));
        }
        mainPageTopBar.setDefaultSort();
    }
}
