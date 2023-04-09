package com.loungexi.controller;


import com.loungexi.pojo.DisplayItem;
import com.loungexi.pojo.Picture;
import com.loungexi.ui.PictureDisplayBar;
import com.loungexi.utils.MyBorderPane;
import com.loungexi.utils.VBoxData;
import javafx.scene.control.TreeItem;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;


import java.io.File;

/**
 * @author LoungeXi
 */
public class DisplayItemController {
    //当前目录节点
    private static TreeItem<File> nowTreeItem;

    public DisplayItemController() {
        initDisplayBar();
    }

    public static void setNowTreeItem(TreeItem<File> now) {
        nowTreeItem = now;
    }

    public static TreeItem<File> getNowTreeItem() {
        return nowTreeItem;
    }
/**
 * @author: LoungeXi
 * @return: 初始化展示面板，读取目录中的符合条件的图片展示在面板中
 **/
    private void initDisplayBar() {
        if (nowTreeItem != null) {
            File[] files = nowTreeItem.getValue().listFiles();
            if (files.length != 0) {
                for (File file : files) {
                    if (!file.isDirectory()) {
                        String fileName = file.getName();
                        //获取当前文件的后缀名
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        if ("png".equalsIgnoreCase(suffix) || "jpg".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix)
                                || "gif".equalsIgnoreCase(suffix) || "bmp".equalsIgnoreCase(suffix)) {
                            //创造出每一个要展示的对象 加入面板中
                            new DisplayItem(new Picture(new Image("File:" + file.getAbsolutePath()), fileName));
                        }
                    }
                }
            }
        }
    }

}

