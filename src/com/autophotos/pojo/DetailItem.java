package com.autophotos.pojo;

import com.autophotos.ui.PictureDetailBar;
import com.autophotos.utils.HomePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LoungeXi
 */
public class DetailItem {
    private Integer imageHeight;
    private Integer imageWidth;
    private String imagePath;
    private Double imageSize;
    private String lastTimeChange;
    private String lastTimeCheck;
    private final Picture picture;
    private final VBox vBox = new VBox();
    private final Label imageLabel = new Label();
    private final Label imagePathLabel = new Label();
    private final Label imageSizeLabel = new Label();
    private final Label imageNameLabel = new Label();
    private final Label imageHeightLabel = new Label();
    private final Label imageWidthLabel = new Label();
    private final Label lastTimeChangeLabel = new Label();
    private final Label lastTimeCheckLabel = new Label();


    public DetailItem(Picture picture) {
        this.picture = picture;
        setInfo();
        setBox();
        setLabel();
        PictureDetailBar.DETAIL_FLOW_PANE.getChildren().add(vBox);
    }

    /**
     * @author: LoungeXi
     * @return: 获取当前单击的对象的信息，初始化对象
     **/
    private void setInfo() {
        this.imagePath = picture.getImage().getUrl();
        File file = new File(imagePath.split(":")[1] + ":" + imagePath.split(":")[2]);
        this.imageSize = file.length() / 1024.0;
        try {
            //这一部分的知识是为了获取获取一些图片的详细信息
            BufferedImage image = ImageIO.read(new FileInputStream(file));
            this.imageHeight = image.getHeight();
            this.imageWidth = image.getWidth();
            Path path = Paths.get(file.getPath());
            BasicFileAttributeView basicView = Files.getFileAttributeView(path, BasicFileAttributeView.class);
            BasicFileAttributes basicFileAttributes = basicView.readAttributes();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.lastTimeCheck = df.format(new Date(basicFileAttributes.lastAccessTime().toMillis()));
            this.lastTimeChange = df.format(new Date(basicFileAttributes.lastModifiedTime().toMillis()));
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }

    /**
     * @author: LoungeXi
     * @return: 对展示的盒子进行排版
     **/
    private void setBox() {
        vBox.setPrefHeight(HomePage.HEIGHT * 0.7);
        vBox.setPrefWidth(HomePage.WIDTH*0.2541);
        vBox.setPadding(new Insets(20));
        //下面两个Label标签是为了美化界面 使用了一个取巧的办法
        Label whiteLabel = new Label();
        Label whiteLabelTwo = new Label();
        whiteLabel.setPrefSize(360, 40);
        whiteLabelTwo.setPrefSize(360, 40);
        vBox.getChildren().addAll(whiteLabel, imageLabel, whiteLabelTwo, imageNameLabel, imagePathLabel, imageSizeLabel, imageHeightLabel, imageWidthLabel, lastTimeCheckLabel, lastTimeChangeLabel);
    }

    /**
     * @author: LoungeXi
     * @return: 将信息展示在右边的详细信息展示框中
     **/
    private void setLabel() {
        ImageView imageView = new ImageView(picture.getImage());
        //避免图片过大而撑开盒子 做了一定的拟合 两种情况 1.图片高大于宽  2.图片宽大于高
        if (imageHeight >= imageWidth) {
            if (imageHeight > 1400) {
                imageView.setFitHeight(HomePage.HEIGHT*0.21454381);
            } else {
                imageView.setFitHeight(HomePage.HEIGHT*0.26);
            }
        } else {
            imageView.setFitWidth(HomePage.WIDTH*0.2223);
        }
        imageView.setPreserveRatio(true);
        imageLabel.setPrefSize(HomePage.WIDTH*0.22865854, HomePage.HEIGHT*0.34638418);
        imageLabel.setAlignment(Pos.BASELINE_CENTER);
        imageLabel.setStyle("-fx-border-color: lightgray");
        imageLabel.setGraphic(imageView);

        imageNameLabel.setText("图片名称:  " + picture.getImageName());
        imageNameLabel.setWrapText(true);
        imageNameLabel.setFont(new Font("Cambria", 15));
        imageNameLabel.setWrapText(true);

        imagePathLabel.setText("位置:  " + imagePath);
        imagePathLabel.setWrapText(true);
        imagePathLabel.setFont(new Font("Cambria", 15));
        imagePathLabel.setWrapText(true);

        imageSizeLabel.setText("图片大小:  " + imageSize.toString().split("\\.")[0] + "." + imageSize.toString().split("\\.")[1].substring(0, 3) + " KB");
        imageSizeLabel.setFont(new Font("Cambria", 15));
        imageSizeLabel.setWrapText(true);

        imageHeightLabel.setText("图片高度:  " + imageHeight + " px");
        imageHeightLabel.setFont(new Font("Cambria", 15));
        imageHeightLabel.setWrapText(true);

        imageWidthLabel.setText("图片宽度:  " + imageWidth + " px");
        imageWidthLabel.setFont(new Font("Cambria", 15));
        imageWidthLabel.setWrapText(true);

        lastTimeCheckLabel.setText("最后访问时间:  " + lastTimeCheck);
        lastTimeCheckLabel.setFont(new Font("Cambria", 15));
        lastTimeCheckLabel.setWrapText(true);

        lastTimeChangeLabel.setText("最后修改时间:  " + lastTimeChange);
        lastTimeChangeLabel.setFont(new Font("Cambria", 15));
        lastTimeChangeLabel.setWrapText(true);

    }
}
