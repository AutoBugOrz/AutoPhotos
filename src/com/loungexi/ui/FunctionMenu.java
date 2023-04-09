package com.loungexi.ui;

import com.loungexi.controller.DisplayItemController;
import com.loungexi.pojo.DisplayItem;
import com.loungexi.pojo.SelectedItem;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;


/**
 * @author Paul
 */
public class FunctionMenu {
    private final ContextMenu contextMenu = new ContextMenu();
    private Stage stage;
    private String name;
    private String startId;
    private String digit;

    public FunctionMenu(String path) {
        MenuItem delete = new MenuItem("删除");
        MenuItem copy = new MenuItem("复制");
        MenuItem rename = new MenuItem("重命名");

        SelectedItem selectedItem = PictureDisplayBar.getSelectedItem();

        delete.setOnAction(actionEvent -> deleteImg());

        copy.setOnAction(actionEvent -> copyImg());

        rename.setOnAction(actionEvent -> renameImg(selectedItem));

        contextMenu.getItems().addAll(copy, rename, delete);
        contextMenu.setStyle("-fx-background-color:rgb(255, 255, 255, .85)");
    }

    private void renameImg(SelectedItem selectedItem) {
        ArrayList<DisplayItem> items = selectedItem.getItems();

        if (items.size() == 1) {
            String path = items.get(0).getPicture().getImage().getUrl().substring(5);
            resetItemStatus(items.get(0));
            File file = new File(path);
            TextField textField = new TextField();
            stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setTitle("重命名");
            stage.setScene(new Scene(textField, 250, 30));
            stage.show();

            String suffix = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1);

            textField.setOnAction(actionEvent ->
            {
                File newNameFile = new File(file.getParentFile().getAbsolutePath() + '/' + textField.getText() + '.' + suffix);
                if (!file.renameTo(newNameFile)) {
                    stage.setAlwaysOnTop(false);
                    if (AlertView.showAlert(AlertView.AlertType.RenameError)) {
                        stage.close();
                        renameImg(selectedItem);
                    }
                }else{
                    stage.close();
                    refresh();
                }
            });
        } else if (items.size() > 1) {
            GridPane gridPane = new GridPane();
            Label nameLabel = new Label("图片名:");
            TextField nameField = new TextField();
            Label startIdLabel = new Label("起始编号:");
            TextField startIdField = new TextField();
            Label digitLabel = new Label("编号位数:");
            TextField digitField = new TextField();

            Button confirm = new Button("确认");
            confirm.setDefaultButton(true);
            confirm.setOnAction(actionEvent ->
            {
                name = nameField.getText();
                startId = startIdField.getText();
                digit = digitField.getText();

                if (startId != "" && digit != "" && Integer.parseInt(digit) > 0 && Integer.parseInt(startId) > 0) {
                    int startIdInt = Integer.parseInt(startId);

                    File[] files = new File[PictureDisplayBar.DISPLAY_FLOW_PANE.getChildren().size()];
                    ArrayList<String> newNames = new ArrayList<>();

                    for (int i = 0; i < items.size(); i++) {
                        DisplayItem item = items.get(i);

                        String path = item.getPicture().getImage().getUrl().substring(5);
                        File file = new File(path);
                        File parentFile = file.getParentFile();
                        files = parentFile.listFiles();

                        String suffix = path.substring(path.lastIndexOf(".") + 1);
                        String newName = String.format(name + "%0" + digit + "d" + "." + suffix, startIdInt++);

                        newNames.add(newName);
                    }
                    if(isExistSameNameFile(files,items,newNames)){
                        stage.setAlwaysOnTop(false);
                        if (AlertView.showAlert(AlertView.AlertType.RenameError)) {
                            stage.close();
                            renameImg(selectedItem);
                        }
                    }else{
                        ArrayList<File> fileList = new ArrayList<>();

                        for (int i = 0; i < items.size(); i++) {
                            DisplayItem item = items.get(i);

                            String path = item.getPicture().getImage().getUrl().substring(5);
                            File file = new File(path);

                            fileList.add(file);

                            for (int j = 0; j < newNames.size(); j++) {
                                if(file.getName().equals(newNames.get(j))){
                                    fileList.remove(file);
                                    newNames.remove(newNames.get(j));
                                }
                            }
                            item.setSelected(false);
                        }
                        for (int i = 0; i < fileList.size(); i++) {
                            File file = fileList.get(i);
                            File newNameFile = new File(file.getParentFile().getAbsolutePath() + '/' + newNames.get(0));
                            newNames.remove(0);
                            file.renameTo(newNameFile);
                        }
                        stage.close();
                        refresh();
                    }

                } else {
                    if (startId == "" || digit == "" || Integer.parseInt(digit) <= 0 || Integer.parseInt(startId) <= 0) {
                        stage.setAlwaysOnTop(false);
                        if (AlertView.showAlert(AlertView.AlertType.NumberTypeError)) {
                            stage.close();
                            renameImg(selectedItem);
                        }
                    }
                }
            });

            Button cancel = new Button("取消");
            cancel.setCancelButton(true);

            cancel.setOnAction(actionEvent ->
            {
                name = null;
                startId = null;
                digit = null;
                stage.close();
            });

            gridPane.add(nameLabel, 0, 0);
            gridPane.add(nameField, 1, 0);
            gridPane.add(startIdLabel, 0, 1);
            gridPane.add(startIdField, 1, 1);
            gridPane.add(digitLabel, 0, 2);
            gridPane.add(digitField, 1, 2);
            gridPane.add(confirm, 0, 3);
            gridPane.add(cancel, 1, 3);

            stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setTitle("重命名");
            stage.setScene(new Scene(gridPane));
            stage.show();
        }
    }

    /**
     * 判断除被选中的图片外，是否存在相同名字文件
     * @param files 当前目录下的所有文件
     * @param items 选中的图片集
     * @param newNames 预重命名的名字数列
     * @return 若在被选中图片外还存在与预命名的名字相同的文件，返回true,否则返回false
     */
    private boolean isExistSameNameFile(File[] files, ArrayList<DisplayItem> items, ArrayList<String> newNames) {
        boolean exists = false;
        for (int i = 0; i < newNames.size(); i++) {
            String newName = newNames.get(i);
            l:for (File file : files) {
                if (file.getName().equals(newName)) {
                    for (DisplayItem item : items) {
                        String path = item.getPicture().getImage().getUrl().substring(5);
                        File f = new File(path);
                        if (file.equals(f)) {
                            break l;
                        }
                    }
                    exists = true;
                }
            }
        }
        return exists;
    }

    private void resetItemStatus(DisplayItem displayItem) {
        displayItem.setSelected(false);
    }

    private void copyImg() {

    }

    private void deleteImg() {
        SelectedItem selectedItem = PictureDisplayBar.getSelectedItem();
        ArrayList<DisplayItem> items = selectedItem.getItems();
        for (DisplayItem item : items) {
            String path = item.getPicture().getImage().getUrl().substring(5);
            File file = new File(path);
            if (file.delete()) {
                refresh();
            }
        }
    }

    private void refresh() {
        PictureDisplayBar.DISPLAY_FLOW_PANE.getChildren().clear();
        PictureDetailBar.DETAIL_FLOW_PANE.getChildren().clear();
        new PictureDisplayBar(PictureDisplayBar.DISPLAY_BORDER);
        new DisplayItemController();
        new BottomInfoBar();
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }
}
