package com.loungexi.ui;

import com.loungexi.controller.DisplayItemController;
import com.loungexi.pojo.DisplayItem;
import com.loungexi.pojo.SelectedItem;
import com.loungexi.utils.HomePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 右键功能实现类
 * @author Paul
 */
public class FunctionMenu {
    private static ContextMenu contextMenu = new ContextMenu();
    private Stage stage;
    private String name;
    private String startId;
    private String digit;

    public FunctionMenu() {
        MenuItem delete = new MenuItem("删除");
        MenuItem copy = new MenuItem("复制");
        MenuItem rename = new MenuItem("重命名");
        MenuItem paste = new MenuItem("粘贴");

        SelectedItem selectedItem = PictureDisplayBar.getSelectedItem();

        delete.setOnAction(actionEvent -> deleteImg());

        copy.setOnAction(actionEvent -> copyImg(selectedItem));

        rename.setOnAction(actionEvent -> renameImg(selectedItem));

        paste.setOnAction(actionEvent -> pasteImg());

        contextMenu.getItems().addAll(copy, paste, rename, delete);
        contextMenu.setStyle("-fx-background-color:rgb(255, 255, 255, .85)");
    }

    /**
     * 图片复制
     */
    private void pasteImg() {
        String newDirPath = DisplayItemController.getNowTreeItem().getValue().getPath() + File.separator;
        Clipboard clipboard = Clipboard.getSystemClipboard();
        List<File> files = clipboard.getFiles();
        for (File file : files) {
            File newFile = new File(newDirPath + file.getName());
            try (FileInputStream is = new FileInputStream(file);
                 FileOutputStream os = new FileOutputStream(newFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    os.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new DisplayItemController();
        new BottomInfoBar();
    }

    /**
     * 图片重命名
     * @param selectedItem 选中的重命名图片集
     */
    private void renameImg(SelectedItem selectedItem) {
        ArrayList<DisplayItem> items = selectedItem.getItems();

        //单图片的重命名
        if (items.size() == 1) {
            //得到图片文件的路径
            String path = items.get(0).getPicture().getImage().getUrl().substring(5);
            items.get(0).setSelected(false);
            File file = new File(path);
            TextField textField = new TextField();
            stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setTitle("重命名");

            Button confirm = new Button("确认");
            Button cancel = new Button("取消");

            VBox vBox = new VBox();
            HBox hBox = new HBox();
            vBox.getChildren().addAll(textField, hBox);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(10));

            hBox.getChildren().addAll(confirm, cancel);
            hBox.setAlignment(Pos.CENTER);
            hBox.setMargin(confirm, new Insets(10, 0, 0, 10));
            hBox.setMargin(cancel, new Insets(10, 10, 0, 10));

            Scene scene = new Scene(vBox);
            stage.setScene(scene);
            stage.setWidth(HomePage.WIDTH * 0.25);
            stage.setHeight(HomePage.HEIGHT * 0.15);
            stage.show();

            String suffix = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1);

            textField.setOnAction(actionEvent ->
            {
                String newName = file.getParentFile().getAbsolutePath() + '/' + textField.getText() + '.' + suffix;
                reNameOne(file, newName, selectedItem);
            });

            confirm.setOnAction(actionEvent -> {
                String newName = file.getParentFile().getAbsolutePath() + '/' + textField.getText() + '.' + suffix;
                reNameOne(file, newName, selectedItem);
            });

            cancel.setOnAction(actionEvent -> {
                selectedItem.unselected(items.get(0));
                stage.close();
            });

        } else if (items.size() > 1) {
            //多图片的重命名
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

                    //存储当前FlowPane中所展示的所有图片文件的文件数组
                    File[] files;
                    //存储所有的新重命名的名字的数列
                    ArrayList<String> newNames = new ArrayList<>();

                    String path = items.get(0).getPicture().getImage().getUrl().substring(5);
                    File file = new File(path);
                    File parentFile = file.getParentFile();
                    //得到当前FlowPane中所展示的所有图片文件
                    files = parentFile.listFiles();

                    //先得到所有的新重命名的名字
                    for (int i = 0; i < items.size(); i++) {
                        DisplayItem item = items.get(i);
                        path = item.getPicture().getImage().getUrl().substring(5);
                        String suffix = path.substring(path.lastIndexOf(".") + 1);
                        String newName = String.format(name + "%0" + digit + "d" + "." + suffix, startIdInt++);

                        newNames.add(newName);
                    }
                    //判断在被选中图片外是否存在有与重命名名字相同名字的图片文件
                    if (isExistSameNameFile(files, items, newNames)) {
                        stage.setAlwaysOnTop(false);
                        //存在重名图片文件，提示报错信息
                        if (AlertView.showAlert(AlertView.AlertType.RenameError)) {
                            stage.close();
                            renameImg(selectedItem);
                        }
                    } else {
                        //被选中图片中不重名的图片文件数列
                        ArrayList<File> fileList = new ArrayList<>();

                        /**
                         * 在多选的图片中可能会存在与重命名名字相同的图片
                         * 通过这个for循环，将与重命名名字相同的被选择重命名文件剔除，同时去除同名的newName
                         * 不重名的被选择图片文件加入到fileList中
                         * 表现为不发生重名冲突的文件被重命名，发生重名冲突的文件不重名，可视为文件重命名后的名字与原名字相同
                         */
                        for (int i = 0; i < items.size(); i++) {
                            DisplayItem item = items.get(i);

                            path = item.getPicture().getImage().getUrl().substring(5);
                            file = new File(path);

                            fileList.add(file);

                            for (int j = 0; j < newNames.size(); j++) {
                                if (file.getName().equals(newNames.get(j))) {
                                    fileList.remove(file);
                                    newNames.remove(newNames.get(j));
                                }
                            }
                            item.setSelected(false);
                        }
                        /**
                         * 将不发生重名冲突的被选择图片文件进行重命名
                         */
                        for (int i = 0; i < fileList.size(); i++) {
                            file = fileList.get(i);
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

            HBox hBox = new HBox();
            hBox.getChildren().addAll(confirm, cancel);
            hBox.setMargin(confirm, new Insets(0, 10, 0, 15));

            gridPane.add(nameLabel, 0, 0);
            gridPane.add(nameField, 1, 0);
            gridPane.add(startIdLabel, 0, 1);
            gridPane.add(startIdField, 1, 1);
            gridPane.add(digitLabel, 0, 2);
            gridPane.add(digitField, 1, 2);
            gridPane.add(hBox, 1, 3);

            gridPane.setPadding(new Insets(10));
            GridPane.setMargin(hBox, new Insets(5));
            GridPane.setMargin(nameField, new Insets(5));
            GridPane.setMargin(startIdField, new Insets(5));
            GridPane.setMargin(digitField, new Insets(5));

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
     * 单张图片的重命名方法
     */
    private void reNameOne(File file, String name, SelectedItem selectedItem) {
        File newNameFile = new File(name);
        if (!file.renameTo(newNameFile)) {
            stage.setAlwaysOnTop(false);
            if (AlertView.showAlert(AlertView.AlertType.RenameError)) {
                stage.close();
                renameImg(selectedItem);
            }
        } else {
            stage.close();
            refresh();
        }
    }

    /**
     * 判断除被选中的图片外，是否存在相同名字文件
     *
     * @param files    当前目录下的所有文件
     * @param items    选中的图片集
     * @param newNames 预重命名的名字数列
     * @return 若在被选中图片外还存在与预命名的名字相同的文件，返回true,否则返回false
     */
    private boolean isExistSameNameFile(File[] files, ArrayList<DisplayItem> items, ArrayList<String> newNames) {
        boolean exists = false;
        for (int i = 0; i < newNames.size(); i++) {
            String newName = newNames.get(i);
            l:
            for (File file : files) {
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

    /**
     * 复制图片
     * @param selectedItem 被选中的图片缩略图集
     */
    private void copyImg(SelectedItem selectedItem) {
        //得到系统剪切板
        Clipboard clipboard = Clipboard.getSystemClipboard();
        List<File> files = selectedItem.copy();
        ClipboardContent content = new ClipboardContent();
        content.putFiles(files);
        clipboard.setContent(content);
    }

    /**
     * 删除图片
     */
    private void deleteImg() {
        SelectedItem selectedItem = PictureDisplayBar.getSelectedItem();
        ArrayList<DisplayItem> items = selectedItem.getItems();
        for (int i = 0; i < items.size(); i++) {
            String path = items.get(i).getPicture().getImage().getUrl().substring(5);
            File file = new File(path);
            file.delete();
            items.get(i).setSelected(false);
        }
        refresh();
    }

    /**
     * 刷新FlowPane，BottomInfoBar
     * 展示各操作完成后的FlowPane，BottomInfoBar
     */
    private void refresh() {
        PictureDisplayBar.getSelectedItem().clear();
        PictureDisplayBar.DISPLAY_FLOW_PANE.getChildren().clear();
        PictureDetailBar.DETAIL_FLOW_PANE.getChildren().clear();
        new DisplayItemController();
        new BottomInfoBar();
    }


    public static ContextMenu getContextMenu() {
        return contextMenu;
    }
}
