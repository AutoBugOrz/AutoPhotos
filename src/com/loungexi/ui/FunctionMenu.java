package com.loungexi.ui;

import com.loungexi.controller.DisplayItemController;
import com.loungexi.pojo.DisplayItem;
import com.loungexi.pojo.Picture;
import com.loungexi.pojo.SelectedItem;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FunctionMenu {
    private final ContextMenu contextMenu = new ContextMenu();

    public FunctionMenu(String path){
        MenuItem delete =new MenuItem("删除");
        MenuItem copy =new MenuItem("复制");
        MenuItem rename =new MenuItem("重命名");

        delete.setOnAction(actionEvent -> deleteImg());

        copy.setOnAction(actionEvent -> copyImg(path));

        rename.setOnAction(actionEvent -> renameImg(path));

        contextMenu.getItems().addAll(copy,rename,delete);
        contextMenu.setStyle("-fx-background-color:rgb(255, 255, 255, .85)");
    }

    private void renameImg(String path) {
        File file = new File(path);
        TextField textField = new TextField();
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.setTitle("重命名");
        stage.setScene(new Scene(textField,250,30));
        stage.show();

        String suffix = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1);

        textField.setOnAction(actionEvent ->
        {
            File newNameFile = new File(file.getParentFile().getAbsolutePath() + '/' + textField.getText() + '.' + suffix );
            if(file.renameTo(newNameFile)){
                refresh();
            }
            stage.close();
        });
    }

    private void copyImg(String path) {

    }

    private void deleteImg() {
//        File file = new File(path);
//
//        if(file.delete()){
//            refresh();
//        }

        SelectedItem selectedItem = PictureDisplayBar.getSelectedItem();
        ArrayList<DisplayItem> items = selectedItem.getItems();
        for (DisplayItem item : items) {

        }

//        refresh();
    }

    private void refresh() {
        PictureDisplayBar.DISPLAY_FLOW_PANE.getChildren().clear();
        PictureDetailBar.DETAIL_FLOW_PANE.getChildren().clear();
        PictureDisplayBar pictureDisplayBar = new PictureDisplayBar(PictureDisplayBar.DISPLAY_BORDER);
        pictureDisplayBar.initBackground();
        new DisplayItemController();
        new BottomInfoBar();
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }
}
