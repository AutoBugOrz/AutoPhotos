package com.loungexi.pojo;


import com.loungexi.controller.ImageShowController;
import com.loungexi.ui.ImageShowBar;
import com.loungexi.ui.ImageShowFrame;
import com.loungexi.utils.HomePage;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LargeImageShowItem {
    public static final ImageView IMAGE_VIEW = new ImageView();
    // public Integer imageHeight;
    // public Integer imageWidth;

    public LargeImageShowItem(Picture picture, Label imageLabel) {
        imageLabel.setStyle("");
        ImageShowBar.ANCHOR_PANE.getChildren().clear();
        // getImageWidthAndHeight(picture);
        showImage(picture, imageLabel);
        ImageShowFrame.STAGE.setTitle(picture.getImageName());
        ImageShowBar.ANCHOR_PANE.getChildren().addAll(imageLabel);
    }

    /**
     * @author: LoungeXi
     * @param: [picture, imageLabel]
     * @return: 展示图片
     **/
    private void showImage(Picture picture, Label imageLabel) {
        IMAGE_VIEW.setImage(picture.getImage());
        IMAGE_VIEW.setSmooth(true);
        IMAGE_VIEW.setPreserveRatio(true);
        IMAGE_VIEW.setFitWidth(HomePage.WIDTH*0.6351626);
        IMAGE_VIEW.setFitHeight(HomePage.HEIGHT*0.6675);
        imageLabel.setGraphic(IMAGE_VIEW);
        imageLabel.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(imageLabel, 0.0);
        AnchorPane.setTopAnchor(imageLabel, 0.0);
        AnchorPane.setBottomAnchor(imageLabel, 0.0);
        AnchorPane.setRightAnchor(imageLabel, 0.0);
    }

    // private void getImageWidthAndHeight(Picture picture) {
    //     String imagePath = picture.getImage().getUrl();
    //     System.out.println(imagePath);
    //     File file = new File(imagePath.split(":")[1] + ":" + imagePath.split(":")[2]);
    //     try {
    //         BufferedImage image = ImageIO.read(new FileInputStream(file));
    //         imageHeight = image.getHeight();
    //         imageWidth = image.getWidth();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}
