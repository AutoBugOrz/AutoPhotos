package com.autophotos.ui;

import com.autophotos.pojo.Picture;
import com.autophotos.utils.HomePage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author LoungeXi
 */
public class ImageShowFrame {
    public static BorderPane BORDER_PANE;
    public static Stage STAGE;

    public ImageShowFrame(Picture picture) {
        setBaseInfo();
        new ImageControllerBar(picture);
        new ImageShowBar(picture);
    }

    private void setBaseInfo() {
        BORDER_PANE = new BorderPane();
        STAGE = new Stage();
        STAGE.getIcons().add(new Image("File:image/image.png"));
        Scene scene = new Scene(BORDER_PANE, HomePage.WIDTH * 0.85, HomePage.HEIGHT * 0.9);
        STAGE.initModality(Modality.APPLICATION_MODAL);
        STAGE.setScene(scene);
        STAGE.show();
    }
}
