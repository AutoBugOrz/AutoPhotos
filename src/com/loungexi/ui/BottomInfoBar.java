package com.loungexi.ui;

import com.loungexi.utils.HomePage;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * @author LoungeXi
 */
public class BottomInfoBar {
    private final VBox vBox = new VBox();
    private final Label numberOfPictureLabel = new Label();
    private final Label sizeOfAllThePictureLabel = new Label();
    private Long numberOfPicture;
    private Double sizeOfAllThePicture;
    public static TreeItem<File> nowClickFile;

    public BottomInfoBar(BorderPane borderPane) {
        setNumberOfPicture(0L);
        setSizeOfAllThePicture(0.000);
        initSetInfoBar(borderPane);
        setInformation();
        writeInformation();
    }

    /**
     * @author: LoungeXi
     * @param: [borderPane]
     * @return: 初始化底部信息栏
     **/
    private void initSetInfoBar(BorderPane borderPane) {
        vBox.setStyle("-fx-background-color: linear-gradient(to bottom,rgb(255, 255, 255),rgb(209, 209, 209))");
        vBox.setPrefHeight(HomePage.HEIGHT * 0.07);
        vBox.setPrefWidth(HomePage.WIDTH);
        numberOfPictureLabel.setPrefWidth(HomePage.WIDTH);
        sizeOfAllThePictureLabel.setPrefWidth(HomePage.WIDTH);
        numberOfPictureLabel.setPrefHeight(HomePage.HEIGHT * 0.0356);
        sizeOfAllThePictureLabel.setPrefHeight(HomePage.HEIGHT * 0.035);
        vBox.getChildren().addAll(numberOfPictureLabel, sizeOfAllThePictureLabel);
        borderPane.setBottom(vBox);
    }

    /**
     * @author: LoungeXi
     * @return: 将文件目录中的图片信息写在程序页面上，这里做了展示信息为 KB 还是 MB 的判断 ，如果图片大小 < 1MB，则展示 KB ，否则展示 MB
     **/
    private void writeInformation() {
        numberOfPictureLabel.setText("\t图片数量:\t" + getNumberOfPicture());
        setSizeOfAllThePicture(getSizeOfAllThePicture() / 1024.0);
        //如果大小>1MB 则自身除以1024并且展示为MB
        if (getSizeOfAllThePicture() > 1024.0) {
            setSizeOfAllThePicture(getSizeOfAllThePicture() / 1024.0);
            sizeOfAllThePictureLabel.setText("\t图片大小:\t" + sizeOfAllThePicture.toString().split("\\.")[0] + "." + sizeOfAllThePicture.toString().split("\\.")[1].charAt(0) + "MB");
        } else {
            sizeOfAllThePictureLabel.setText("\t图片大小:\t" + sizeOfAllThePicture.toString().split("\\.")[0] + "." + sizeOfAllThePicture.toString().split("\\.")[1].charAt(0) + "KB");
        }

    }

    /**
     * @author: LoungeXi
     * @return: 获得文件目录的大小和数量信息
     **/
    private void setInformation() {
        if (nowClickFile != null) {
            File[] files = nowClickFile.getValue().listFiles();
            if (files.length != 0) {
                for (File file : files) {
                    if (!file.isDirectory()) {
                        String fileName = file.getName();
                        //获取当前文件的后缀名
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        if ("png".equalsIgnoreCase(suffix) || "jpg".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix)
                                || "gif".equalsIgnoreCase(suffix) || "bmp".equalsIgnoreCase(suffix)) {
                            setSizeOfAllThePicture(getSizeOfAllThePicture() + file.length());
                            setNumberOfPicture(getNumberOfPicture() + 1);
                        }
                    }
                }
            }
        }
    }

    private Long getNumberOfPicture() {
        return numberOfPicture;
    }

    private void setNumberOfPicture(Long numberOfPicture) {
        this.numberOfPicture = numberOfPicture;
    }

    private Double getSizeOfAllThePicture() {
        return sizeOfAllThePicture;
    }

    private void setSizeOfAllThePicture(Double sizeOfAllThePicture) {
        this.sizeOfAllThePicture = sizeOfAllThePicture;
    }
}
