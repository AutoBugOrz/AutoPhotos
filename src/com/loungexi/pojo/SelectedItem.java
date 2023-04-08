package com.loungexi.pojo;

import java.util.ArrayList;

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

    public void select(DisplayItem item){
        item.setStyle("-fx-background-color: rgb(202, 202, 202);" + "-fx-border-insets: 1;" + "-fx-border-color: rgb(163, 163, 163)");
        item.setSelected(true);
        if(!items.contains(item)){
            items.add(item);
        }
    }

    public void clear(){
        items.clear();
    }

    public void delete(){
        for (DisplayItem item : items) {

        }
    }

    public ArrayList<DisplayItem> getItems() {
        return items;
    }

}
