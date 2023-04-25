package com.loungexi.ui;

import com.loungexi.utils.HomePage;
import com.loungexi.utils.MyBorderPane;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * @author LoungeXi
 */
public class BottomInfoBar {
    private final HBox hBox;
    private final Label numberOfPictureLabel = new Label();
    private final Label sizeOfAllThePictureLabel = new Label();
    private final Label selectedPictureLabel = new Label();
    private Long numberOfPicture;
    private Double sizeOfAllThePicture;
    private static TreeItem<File> nowClickFile;
    //选中图片数
    private int selectedPicture;

    public BottomInfoBar() {
        hBox = new HBox();
        setNumberOfPicture(0L);
        setSizeOfAllThePicture(0.000);
        setSelectedPicture(0);
        initSetInfoBar(MyBorderPane.getBorderPane());
        setInformation();
        writeInformation();
    }

    /**
     *
     * @description 刷新BottomInfoBar中的选中图片数
     * @author Paul
     * @date 23:43 2023/4/20
     * @param selected 被选择的图片数
     **/
    public BottomInfoBar(int selected) {
        hBox = (HBox) MyBorderPane.getBorderPane().getBottom();
        ObservableList<Node> children = hBox.getChildren();
        Label label = (Label) children.get(2);
        label.setText("\t选中"+ selected + "张图片");
    }

    /**
     * @author: LoungeXi
     * @param: [borderPane]
     * @return: 初始化底部信息栏
     **/
    private void initSetInfoBar(BorderPane borderPane) {
        hBox.setStyle("-fx-background-color: linear-gradient(to bottom,rgb(255, 255, 255),rgb(209, 209, 209))");
        hBox.setPrefHeight(HomePage.HEIGHT * 0.07);
        hBox.setPrefWidth(HomePage.WIDTH);
        numberOfPictureLabel.setPrefWidth(HomePage.WIDTH / 5);
        sizeOfAllThePictureLabel.setPrefWidth(HomePage.WIDTH / 5);
        selectedPictureLabel.setPrefWidth(HomePage.WIDTH / 5);
        numberOfPictureLabel.setPrefHeight(HomePage.HEIGHT * 0.0356);
        sizeOfAllThePictureLabel.setPrefHeight(HomePage.HEIGHT * 0.035);
        selectedPictureLabel.setPrefHeight(HomePage.HEIGHT * 0.035);
        hBox.getChildren().addAll(numberOfPictureLabel, sizeOfAllThePictureLabel,selectedPictureLabel);
        hBox.setAlignment(Pos.CENTER_LEFT);
        borderPane.setBottom(hBox);
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
        selectedPictureLabel.setText("\t选中"+ getSelectedPicture() + "张图片");
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

    private int getSelectedPicture() {
        return selectedPicture;
    }

    public void setSelectedPicture(int selectedPicture) {
        this.selectedPicture = selectedPicture;
    }

    public static void setNowClickFile(TreeItem<File> clickFile) {
        nowClickFile = clickFile;
    }
}
