package com.autophotos.run;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;

public class Test4Me extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //加载fxml
        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("file:/controller/EditPage.fxml"));
        loader.setLocation(new URL("file:src/com/loungexi/run/test.fxml"));
        Parent root = loader.load();
        //窗口大小根据屏幕自适应
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = visualBounds.getWidth();
        double screenHeight = visualBounds.getHeight();
        Scene scene = new Scene(root, screenWidth * 0.6, screenHeight * 0.6);
        // TODO: 2023/4/24
//        ImageView imageArea = (ImageView) root.lookup("#imageArea");
//        imageArea.setImage(new Image("file:1.jpg"));
        System.out.println("scene");
        System.out.println(scene.getHeight());
        System.out.println(scene.getWidth());
        //窗口居中
        primaryStage.setX((screenWidth - scene.getWidth()) / 2);
        primaryStage.setY((screenHeight - scene.getHeight()) / 2);
        primaryStage.setScene(scene);
        primaryStage.setTitle("nice");
        primaryStage.show();
    }
}