package com.loungexi.pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * SelectedItem类中存放当前目录下FlowPane上被选中的图片DisplayItem
 * @author Paul
 */
public class SelectedItem {
    //被选中的图片缩略图数列
    private ArrayList<DisplayItem> items;

    public SelectedItem() {
        items = new ArrayList<>();
    }

    /**
     *
     * @param item 选中的图片缩略图
     */
    public void unselected(DisplayItem item) {
        item.setStyle("-fx-background-color: #ffffff");
        item.setSelected(false);
        items.remove(item);
    }

    /**
     * 选中图片，并将其加入到items
     * @param item 选中的图片缩略图
     */
    public void select(DisplayItem item) {
        item.setStyle("-fx-background-color: rgb(202, 202, 202);" + "-fx-border-insets: 1;" + "-fx-border-color: rgb(163, 163, 163)");
        item.setSelected(true);
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    /**
     *  清除选中的图片，此方法在刷新/加载FlowPane时调用
     */
    public void clear() {
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).isSelected()){
                items.get(i).setStyle("-fx-background-color: #ffffff");
                items.get(i).setSelected(false);
            }
        }
        items.clear();
    }

    /**
     * 移除items中处于未被选中状态的图片缩略图
     * 效果为图片由选中变为未选中
     */
    public void removeUnselected() {
        for (int i = 0; i < items.size(); i++) {
            if(!items.get(i).isSelected()){
                items.get(i).setStyle("-fx-background-color: #ffffff");
                items.remove(i);
                i--;
            }
        }
    }

    public ArrayList<DisplayItem> getItems() {
        return items;
    }

    /**
     * 将选中的图片存放到File数列中传出，交给剪切板
     * @return 选中的图片的File数列
     */
    public List<File> copy(){
        List<File> files = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            File file = new File(items.get(i).getPicture().getImage().getUrl().substring(5));
            items.get(i).setSelected(false);
            files.add(file);
        }
        return files;
    }

    public void showSelected(){
        for (DisplayItem item : items) {
            item.setStyle("-fx-background-color: rgb(202, 202, 202);" + "-fx-border-insets: 1;" + "-fx-border-color: rgb(163, 163, 163)");
        }
    }
}
