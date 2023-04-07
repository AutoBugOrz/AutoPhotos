package com.loungexi.pojo;

import java.util.ArrayList;
import java.util.List;

public class SelectedItem {
    private List<DisplayItem> items;

    public SelectedItem() {
        items = new ArrayList<>();
    }

    public void unselected(DisplayItem item) {
        item.setStyle("-fx-background-color: #ffffff");
        item.setSelected(false);
        items.remove(item);
    }

    public void select(DisplayItem item){
        item.setStyle("-fx-background-color: rgb(202, 202, 202);" + "-fx-border-insets: 1;" + "-fx-border-color: rgb(163, 163, 163)");
        item.setSelected(true);
        items.add(item);
    }



}
