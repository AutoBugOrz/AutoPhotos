package com.autophotos.ui;

import com.autophotos.controller.TreeViewController;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.File;

/**
 * @author LoungeXi
 */
public class TreeView extends javafx.scene.control.TreeView<File> {

    private final AnchorPane anchorPane = new AnchorPane();
    private final File[] files = File.listRoots();
    private final javafx.scene.control.TreeView<File> treeView = new javafx.scene.control.TreeView<>();
    private final TreeItem<File> rootFile = new TreeItem<>();


    public TreeView(BorderPane borderPane) {
        initSetTreeView(borderPane);
    }

    /**
     * @author: LoungeXi
     * @param: [borderPane]
     * @return: 程序开始后首次初始化目录树
     **/
    private void initSetTreeView(BorderPane borderPane) {
        borderPane.setLeft(anchorPane);
        treeView.setRoot(rootFile);
        treeView.setShowRoot(false);
        //首次进入直接生成两层目录
        firstSetTreeView();
        //美化目录树
        beautifyTreeView();
        //目录树的点击监听 用于实时生成目录树 避免栈内存溢出
        // listenInViewItem(borderPane);
        new TreeViewController(borderPane, treeView);

        anchorPane.getChildren().add(treeView);
        //让目录树充满 anchorPane 布局
        AnchorPane.setLeftAnchor(treeView, 0.0);
        AnchorPane.setTopAnchor(treeView, 0.0);
        AnchorPane.setBottomAnchor(treeView, 0.0);
        AnchorPane.setRightAnchor(treeView, 0.0);
    }

    /**
     * @author: LoungeXi
     * @return: 美化目录树
     **/
    private void beautifyTreeView() {
        treeView.setCellFactory(new Callback<>() {
            @Override
            public TreeCell<File> call(javafx.scene.control.TreeView<File> fileTreeView) {
                return new TreeCell<>() {
                    @Override
                    protected void updateItem(File file, boolean empty) {
                        super.updateItem(file, false);
                        //树结构不为空
                        if (!empty && file != null) {
                            HBox hBox = new HBox();
                            Label label = new Label(setTreeViewValue(file));
                            hBox.getChildren().add(label);
                            this.setGraphic(hBox);
                            //加入一点CSS边框作为美化
                            this.setStyle("-fx-border-color: rgb(244,244,244)");
                        } else {
                            this.setGraphic(null);
                        }
                    }
                };
            }
        });
    }

    /**
     * @author: LoungeXi
     * @param: [file]
     * @return: 返回文件夹的绝对路径的结尾文件命用来初始化目录树，若为根目录则去除反斜杠返回
     **/
    private String setTreeViewValue(File file) {
        for (File rootFile : files) {
            if (file.toString().equals(rootFile.toString())) {
                return file.toString().split("\\\\")[0];
            }
        }
        return file.getName();
    }

    /**
     * @author: LoungeXi
     * @return: 打开程序首次进行目录树的初始化
     **/
    private void firstSetTreeView() {
        //首次进入直接生成两层目录
        for (File file : files) {
            TreeItem<File> item = new TreeItem<>(file);
            rootFile.getChildren().add(item);
            File[] filesTwo = file.listFiles();
            if (filesTwo.length != 0) {
                for (File fileTwo : filesTwo) {
                    if (fileTwo.isDirectory() && !fileTwo.isHidden()) {
                        TreeItem<File> itemTwo = new TreeItem<>(fileTwo);
                        if (!"Documents and Settings".equals(itemTwo.getValue().getName())) {
                            item.getChildren().add(itemTwo);
                        }
                    }
                }
            }
        }
    }

}
