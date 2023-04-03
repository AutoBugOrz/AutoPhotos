package com.loungexi.run;

import com.loungexi.ui.BaseView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new BaseView(stage);
    }

}
