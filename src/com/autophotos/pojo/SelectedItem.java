package com.autophotos.pojo;

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
     * @description 将图片缩略图置为未被选中状态
     * @author Paul
     * @date 23:48 2023/4/20
     * @param item  选中的图片缩略图
     **/
    public void unselected(DisplayItem item) {
        item.setStyle("-fx-background-color: #ffffff");
        item.setSelected(false);
        items.remove(item);
    }

    /**
     *
     * @description 选中图片，并将其加入到items
     * @author Paul
     * @date 23:50 2023/4/20
     * @param item  选中的图片缩略图
     **/
    public void select(DisplayItem item) {
        item.setStyle("-fx-background-color: rgb(202, 202, 202);" + "-fx-border-insets: 1;" + "-fx-border-color: rgb(163, 163, 163)");
        item.setSelected(true);
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    /**
     *
     * @description 清除选中的图片，此方法在刷新/加载FlowPane时调用
     * @author Paul
     * @date 23:50 2023/4/20
     **/
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
     *
     * @description 移除items中处于未被选中状态的图片缩略图，效果为图片由选中变为未选中
     * @author Paul
     * @date 23:50 2023/4/20
     **/
    public void removeUnselected() {
        for (int i = 0; i < items.size(); i++) {
            if(!items.get(i).isSelected()){
                items.get(i).setStyle("-fx-background-color: #ffffff");
                items.remove(i);
                i--;
            }
        }
    }

    /**
     *
     * @description 得到存放所有被选中图片缩略图数列的方法
     * @author Paul
     * @date 23:51 2023/4/20
     * @return java.util.ArrayList<com.loungexi.pojo.DisplayItem>
     **/
    public ArrayList<DisplayItem> getItems() {
        return items;
    }

    /**
     *
     * @description 将选中的图片存放到File数列中传出，交给剪切板
     * @author Paul
     * @date 23:54 2023/4/20
     * @return java.util.List<java.io.File> 选中的图片的File数列
     **/
    public List<File> copy(){
        List<File> files = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            File file = new File(items.get(i).getPicture().getImage().getUrl().substring(5));
            items.get(i).setSelected(false);
            files.add(file);
        }
        return files;
    }

    /**
     *
     * @description 将在选中数列中的所有图片置为选中状态（表现为图片被选中）
     * @author Paul
     * @date 23:53 2023/4/20
     **/
    public void showSelected(){
        for (DisplayItem item : items) {
            item.setStyle("-fx-background-color: rgb(202, 202, 202);" + "-fx-border-insets: 1;" + "-fx-border-color: rgb(163, 163, 163)");
        }
    }
}
