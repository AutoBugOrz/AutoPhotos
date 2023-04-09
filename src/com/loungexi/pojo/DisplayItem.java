package com.loungexi.pojo;

import com.loungexi.ui.*;
import com.loungexi.utils.HomePage;
import com.loungexi.utils.ItemChanger;
import com.loungexi.utils.MyBorderPane;
import com.loungexi.utils.VBoxData;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author LoungeXi
 */
public class DisplayItem extends VBox{
    private final Label imageLabel = new Label();
    private final Label imageNameLabel = new Label();
    private final Picture picture;
    private boolean isSelected = false;

    public DisplayItem(Picture picture) {
        this.picture = picture;
        setLabel();
        setBox();
        setItemPrimaryClick();
        PictureDisplayBar.DISPLAY_FLOW_PANE.getChildren().add(this);
    }

    /**
     * @author: LoungeXi
     * @return: 初始化放在展示盒子中的标签
     **/
    private void setLabel() {
        imageNameLabel.setText(picture.getImageName());
        imageNameLabel.setFont(new Font("Cambria", 20));
        imageNameLabel.setPrefSize(200, 10);
        imageNameLabel.setAlignment(Pos.BASELINE_CENTER);

        ImageView imageView = new ImageView(picture.getImage());
        imageView.setFitHeight(HomePage.HEIGHT * 0.1355);
        imageView.setFitWidth(HomePage.WIDTH * 0.1048);
        imageView.setPreserveRatio(true);
        imageLabel.setGraphic(imageView);
        imageLabel.setAlignment(Pos.BASELINE_CENTER);
        imageLabel.setPrefSize(200, 160);
    }

    /**
     * @author: LoungeXi
     * @return: 初始化展示信息的盒子
     **/
    private void setBox() {
        this.setCursor(Cursor.HAND);
        this.setPrefHeight(VBoxData.vBoxHeight);
        this.setPrefWidth(VBoxData.vBoxWidth);
        this.setStyle("-fx-background-color: #ffffff");
        this.getChildren().addAll(imageLabel, imageNameLabel);
    }


    /**
     * @author: LoungeXi
     * @return: 设置展示项目的点击事件
     **/
    private void setItemPrimaryClick() {
        //设置点击事件
        this.setOnMouseClicked(mouseEvent -> {
            //为了实现单击提示效果 这里利用了一个转换器来储存当前点击的项目 用于判断并设置提示CSS
            if (ItemChanger.vBoxChanger != null) {
                //设置未被点击的项目的CSS
                ItemChanger.vBoxChanger.setStyle("-fx-background-color: #ffffff");
                //改变转换器中的内容
                ItemChanger.vBoxChanger = this;
                //单击后先清除原先展示栏目上的展示信息 再放上新的展示信息
                PictureDetailBar.DETAIL_FLOW_PANE.getChildren().clear();
            } else {
                ItemChanger.vBoxChanger = this;
            }
            // 鼠标左键 单击事件 项目的详细信息会展示在右边详细信息展示框中
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 1) {
                //设置单击项目的CSS
                this.setStyle("-fx-background-color: rgb(202, 202, 202);" + "-fx-border-insets: 1;" + "-fx-border-color: rgb(163, 163, 163)");
                new DetailItem(picture);
                new BottomInfoBar(1);
                SelectedItem selectedItem = PictureDisplayBar.getSelectedItem();
                selectedItem.select(this);
                ArrayList<DisplayItem> items = selectedItem.getItems();
                if(items.size() > 1){
                    for (DisplayItem item : items) {
                        if(!this.equals(item)){
                            item.setSelected(false);
                        }
                    }
                    selectedItem.removeUnselected();
                }
            }

            // 鼠标左键 双击事件 创建一个舞台展示图片的详细信息
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                new ImageShowFrame(picture);
            }

            // 鼠标右键 展开功能条目
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                PictureDisplayBar.getSelectedItem().select(this);
                PictureDisplayBar.refreshBIBar();
                String url = picture.getImage().getUrl();
                String path = url.substring(5);
                FunctionMenu functionMenu = new FunctionMenu(path);
                imageLabel.setContextMenu(functionMenu.getContextMenu());
            }
        });
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Picture getPicture() {
        return picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayItem item = (DisplayItem) o;
        return isSelected == item.isSelected && Objects.equals(imageLabel, item.imageLabel) && Objects.equals(imageNameLabel, item.imageNameLabel) && Objects.equals(picture, item.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageLabel, imageNameLabel, picture, isSelected);
    }
}
