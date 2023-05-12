package com.autophotos.utils;

import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class DragScrollPane extends ScrollPane {
    private double startX;
    private double startY;
    private double startHValue;
    private double startVValue;
    private Region content;

    public DragScrollPane() {
        this(null);
    }

    public DragScrollPane(Region content) {
        setDragContent(content);
    }

    /**
     * @author: LoungeXi
     * @param: [content]
     * @return: 设置scrollPane的基本信息
     **/
    public void setDragContent(Region content) {
        this.content = content;
        setContent(content);
        addDragEvent();
    }

    /**
     * @author: LoungeXi
     * @return: 添加scrollPane的点击事件 实现拖拽效果
     **/
    private void addDragEvent() {
        if (content != null) {
            content.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                // 得到鼠标点击的位置距离窗口最左方的值
                startX = event.getSceneX();
                // 得到鼠标点击的位置距离窗口最上方的值
                startY = event.getSceneY();
                // 得到当前水平滚轮在整条水平滚轮中的位置 最小是0 最大是1
                startHValue = getHvalue();
                // 得到当前垂直滚轮在整条垂直滚轮中的位置 最小是0 最大是1
                startVValue = getVvalue();
                content.setCursor(Cursor.CLOSED_HAND);
            });
            content.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
                if (event.isPrimaryButtonDown()) {
                    // 移动的水平距离是鼠标点击的起始位置距离窗口最上方的值减去鼠标放开的终末位置距离窗口最上方的值
                    double moveX = startX - event.getSceneX();
                    // 移动的垂直距离是鼠标点击的起始位置距离窗口最左方的值减去鼠标放开的终末位置距离窗口最左方的值
                    double moveY = startY - event.getSceneY();
                    // 设置当前水平滚轮在整条水平滚轮中的位置 content.getWidth()为ImageShowBar.ANCHOR_PANE的宽度 其宽度会被子节点撑开 所以宽度一直会改变 而getWidth()为scroll的宽度 不变
                    setHvalue(startHValue + moveX / (content.getWidth() - getWidth()));
                    // 设置当前垂直滚轮在整条垂直滚轮中的位置 content.getHeight()为ImageShowBar.ANCHOR_PANE的高度 其高度会被子节点撑开 所以高度一直会改变 而getHeight()为scroll的高度 不变
                    setVvalue(startVValue + moveY / (content.getHeight() - getHeight()));
                }
            });
            content.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
                content.setCursor(Cursor.OPEN_HAND);
            });
        }
    }
}
