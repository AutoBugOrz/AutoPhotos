package com.loungexi.controller;

import com.loungexi.pojo.SelectedItem;
import com.loungexi.ui.BottomInfoBar;
import com.loungexi.ui.PictureDetailBar;
import com.loungexi.ui.PictureDisplayBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

import java.io.File;

/**
 * @author LoungeXi
 */
public class TreeViewController {

    public TreeViewController(BorderPane borderPane, TreeView<File> treeView) {
        treeItemEventsListener(borderPane, treeView);
    }

    /**
     * @author: LoungeXi
     * @param: [borderPane]
     * @return: 添加目录树每一项的鼠标点击监听事件，实现实时生成目录树，避免栈内存溢出，borderPane 用于其他组件初始化
     **/
    private void treeItemEventsListener(BorderPane borderPane, TreeView<File> treeView) {
        treeView.getSelectionModel().selectedItemProperty().addListener((observableValue, fileTreeItem, newValue) -> {
            if (newValue != null) {
                addItems(newValue, 0);
                //在这里底部信息栏优化应该将静态面板在BaseView中new出来然后将数据分装成一个对象实时更新到静态面板上，这样的做法才是正确且快速的.
                BottomInfoBar.setNowClickFile(newValue);
                new BottomInfoBar();

                DisplayItemController.setNowTreeItem(newValue);
                PictureDisplayBar.getSelectedItem().clear();
                PictureDisplayBar.DISPLAY_FLOW_PANE.getChildren().clear();

                new DisplayItemController();
                //点击目录树的其他节点的时候将右边信息展示栏的节点清空
                PictureDetailBar.DETAIL_FLOW_PANE.getChildren().clear();
            }
        });
    }

    /**
     * @author: LoungeXi
     * @param: [newValue, flag]
     * @return: 方法传入两个参数，newValue代表的是当前点击的叶子节点，用来实时添加目录结构，flag用来判断，flag==0时首先清除
     * 当前叶子节点的所有子节点并且重新生成，避免出现多次添加子节点的状况，随即获得当前节点所有子文件将创建为新的TreeItem重新
     * 调用当前的addItems()方法传入当前新的TreeItem节点作为newValue，flag+1作为新的flag，这样新的方法过程中只会执行最后的加
     * 入新的TreeItem语句，这样递归的addItems()方法会因为flag判断条件不通过只执行两层，避免了栈溢出
     **/
    private void addItems(TreeItem<File> newValue, int flag) {
        File[] fileList = newValue.getValue().listFiles();
        if (fileList != null) {
            if (flag == 0) {
                //点击当前节点会将之前的已经添加的节点全部清除，避免了多次添加节点的情况出现
                newValue.getChildren().remove(0, newValue.getChildren().size());
            }
            if (fileList.length > 0) {
                for (File file : fileList) {
                    if (file.isDirectory() && !file.isHidden()) {
                        TreeItem<File> newItem = new TreeItem<>(file);
                        if (flag < 1) {
                            //传入的flag参数为flag+1，下一次进入addItems()方法会因为flag<1的条件不通过而跳过递归，这样递归层数只有两层
                            addItems(newItem, flag + 1);
                        }
                        //这里if判断是为排除C盘下的Documents and Settings文件，不知道为何此文件明明为隐藏文件却通过了非隐藏文件的判断
                        if (!"Documents and Settings".equals(newItem.getValue().getName())) {
                            newValue.getChildren().add(newItem);
                        }
                    }
                }
            }
        }
    }
}
