package com.loungexi.pojo;

import com.loungexi.utils.ItemChanger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul
 */
public class SelectedItem {
    private ArrayList<DisplayItem> items;

    public SelectedItem() {
        items = new ArrayList<>();
    }

    public void unselected(DisplayItem item) {
        item.setStyle("-fx-background-color: #ffffff");
        item.setSelected(false);
        items.remove(item);
    }

    public void select(DisplayItem item) {
        item.setStyle("-fx-background-color: rgb(202, 202, 202);" + "-fx-border-insets: 1;" + "-fx-border-color: rgb(163, 163, 163)");
        item.setSelected(true);
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    public void clear() {
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).isSelected()){
                items.get(i).setSelected(false);
            }
        }
        items.clear();
    }

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

    public List<File> copy(){
        List<File> files = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            File file = new File(items.get(i).getPicture().getImage().getUrl().substring(5));
            items.get(i).setSelected(false);
            files.add(file);
        }
        return files;
    }
}
