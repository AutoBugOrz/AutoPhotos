package com.loungexi.ui;

import com.loungexi.utils.HomePage;
import com.loungexi.utils.MyBorderPane;
import com.loungexi.utils.VBoxData;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * @author: LoungeXi
 **/
public class BaseView {

    public BaseView(Stage stage) {
        BorderPane borderPane = MyBorderPane.getBorderPane();
        // borderPane.setStyle("-fx-background-color: #00CED1");

        //设置程序宽高
        Scene scene = new Scene(borderPane, HomePage.WIDTH, HomePage.HEIGHT);

        //生成目录树
        new TreeView(borderPane);

        //生成底部信息条
        new BottomInfoBar();

        //生成所有图片的信息粗略展示栏
        new PictureDisplayBar(borderPane);

        //生成单击图片的信息详细展示栏
        new PictureDetailBar(borderPane);

        stage.getIcons().add(new Image("File:image/camera.png"));
        stage.setScene(scene);
        stage.setTitle("Photo Management Program");
        stage.show();

        PictureDisplayBar.pane.setMinHeight(borderPane.getCenter().getScene().getHeight());
//        PictureDisplayBar.DISPLAY_FLOW_PANE.setLayoutY(100);
    }

}
