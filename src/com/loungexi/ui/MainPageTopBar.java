package com.loungexi.ui;

import com.loungexi.pojo.DisplayItem;
import com.loungexi.pojo.Picture;
import com.loungexi.utils.Pictures;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.*;

/**
 * @author Lantech
 * @description: 主界面中间用来展示图片上面的排序栏的HBox
 */
public class MainPageTopBar extends HBox {
    //默认文本
    private static final String SORT_BY_NAME_TEXT = "名称    ";
    //按图片名称排序的btn
    private static final Button SORT_BY_NAME = new Button(SORT_BY_NAME_TEXT);
    //默认文本
    private static final String SORT_BY_LAST_MODIFIED_TEXT = "修改日期     ";
    //按修改日期排序的btn
    private static final Button SORT_BY_LAST_MODIFIED = new Button(SORT_BY_LAST_MODIFIED_TEXT);
    //默认文本
    private static final String SORT_BY_TYPE_TEXT = "类型    ";
    //按图片类型排序的btn
    private static final Button SORT_BY_TYPE = new Button(SORT_BY_TYPE_TEXT);
    //默认文本
    private static final String SORT_BY_SIZE_TEXT = "大小    ";
    //按图片大小排序的btn
    private static final Button SORT_BY_SIZE = new Button(SORT_BY_SIZE_TEXT);
    //btn对应的默认文本
    private static final Map<Button, String> BTN_INIT_TEXTS = new HashMap<>();

    static {
        BTN_INIT_TEXTS.put(SORT_BY_NAME, SORT_BY_NAME_TEXT);
        BTN_INIT_TEXTS.put(SORT_BY_LAST_MODIFIED, SORT_BY_LAST_MODIFIED_TEXT);
        BTN_INIT_TEXTS.put(SORT_BY_TYPE, SORT_BY_TYPE_TEXT);
        BTN_INIT_TEXTS.put(SORT_BY_SIZE, SORT_BY_SIZE_TEXT);
    }

    public static Map<Button, String> getBtnInitTexts() {
        return BTN_INIT_TEXTS;
    }

    //用来记录当前是否降序排序，默认降序
    private static boolean isDesc = true;
    //用来记录当前按什么排序，默认按修改时间
    private static sortFields sortField = sortFields.LAST_MODIFIED;

    //用来存有哪些排序类型
    private enum sortFields {
        //按名字
        NAME,
        //按修改时间
        LAST_MODIFIED,
        //按类型
        TYPE,
        //按大小
        SIZE,
    }

    /**
     * @description: 实例化这个类的同时进行初始化
     * @author Lantech
     */
    public MainPageTopBar() {
        init();
    }

    /**
     * @description: 构造按钮界面以及监听器
     * @param: null
     * @return: void
     * @author p
     */
    private void init() {
        //buttons的基础设置
        setButtons();
        //设置默认排序——按修改时间降序
        setDefaultSort();
        //设置nameBtn的监听
        setNameOnAction();
        //设置modifiedBtn的监听
        setModifiedOnAction();
        //设置typeBtn的监听
        setTypeOnAction();
        //设置sizeBtn的监听
        setSizeOnAction();
    }

    /**
     * @description: 设置所有btn的默认属性等值
     * @param: null
     * @return: void
     * @author Lantech
     */
    private void setButtons() {
        //文字居中显示
        SORT_BY_NAME.setStyle("-fx-alignment: center");
        SORT_BY_LAST_MODIFIED.setStyle("-fx-alignment: center");
        SORT_BY_TYPE.setStyle("-fx-alignment: center");
        SORT_BY_SIZE.setStyle("-fx-alignment: center");
        this.getChildren().addAll(SORT_BY_NAME, SORT_BY_LAST_MODIFIED, SORT_BY_TYPE, SORT_BY_SIZE);
        //将每个Button的最小宽度设置为0，以便自适应宽度
        SORT_BY_NAME.setMinWidth(0);
        SORT_BY_LAST_MODIFIED.setMinWidth(0);
        SORT_BY_TYPE.setMinWidth(0);
        SORT_BY_SIZE.setMinWidth(0);
        //将各按钮的宽度绑定到mainPageTopBar的宽上
        SORT_BY_NAME.prefWidthProperty().bind(this.widthProperty());
        SORT_BY_LAST_MODIFIED.prefWidthProperty().bind(this.widthProperty());
        SORT_BY_TYPE.prefWidthProperty().bind(this.widthProperty());
        SORT_BY_SIZE.prefWidthProperty().bind(this.widthProperty());
        //将的HGrow属性设置为ALWAYS
        HBox.setHgrow(SORT_BY_NAME, Priority.ALWAYS);
        HBox.setHgrow(SORT_BY_LAST_MODIFIED, Priority.ALWAYS);
        HBox.setHgrow(SORT_BY_TYPE, Priority.ALWAYS);
        HBox.setHgrow(SORT_BY_SIZE, Priority.ALWAYS);
    }

    /**
     * @description: 设置默认排序
     * @param: null
     * @return: void
     * @author Lantech
     */
    public void setDefaultSort() {
        //先获得首页展示的各个VBox
        ArrayList<DisplayItem> displayItems = getDisplayItems();
        //按修改时间降序
        displayItems.sort((o1, o2) -> Pictures.lastModifiedOf(o2.getPicture()).compareTo(Pictures.lastModifiedOf(o1.getPicture())));
        //更改btn
        SORT_BY_LAST_MODIFIED.setText("修改日期    ↓");
        //排完序后放回展示区
        setDisplayItems(displayItems);
    }

    /**
     * @description: 设置按大小排序btn的监听
     * @param: null
     * @return: void
     * @author Lantech
     */
    private void setSizeOnAction() {
        //按大小排序btn的监听
        SORT_BY_SIZE.setOnAction(e -> {
            //先获得首页展示的各个VBox
            ArrayList<DisplayItem> displayItems = getDisplayItems();
            if (sortField != sortFields.SIZE) {
                //不是当前btn的话初始化其他btns为默认值
                initOtherBtns(SORT_BY_SIZE);
                //不是当前排序的话，排序默认为降序
                isDesc = true;
                //排序类型改为当前排序类型
                sortField = sortFields.SIZE;
            }
            //在点击按钮时判断是什么排序，并更改btn图标
            if (isDesc) {
                //如果是降序，就降序
                displayItems.sort((o1, o2) -> Pictures.sizeOf(o2.getPicture()).compareTo(Pictures.sizeOf(o1.getPicture())));
                SORT_BY_SIZE.setText("大小    ↓");
            } else {
                //如果是升序，就升序
                displayItems.sort(Comparator.comparing(o -> Pictures.sizeOf(o.getPicture())));
                SORT_BY_SIZE.setText("大小    ↑");
            }
            setDisplayItems(displayItems);
            isDesc = !isDesc;
        });
    }

    /**
     * @description: 设置按名称排序btn的监听
     * @param: null
     * @return: void
     * @author Lantech
     */
    private void setNameOnAction() {
        //按名称排序btn的监听
        SORT_BY_NAME.setOnAction(e -> {
            //先获得首页展示的各个VBox
            ArrayList<DisplayItem> displayItems = getDisplayItems();
            if (sortField != sortFields.NAME) {
                //不是当前btn的话初始化其他btns为默认值
                initOtherBtns(SORT_BY_NAME);
                //不是当前排序的话，排序默认为降序
                isDesc = true;
                //排序类型改为当前排序类型
                sortField = sortFields.NAME;
            }
            //在点击按钮时判断是什么排序，并更改btn图标
            if (isDesc) {
                //如果是降序，就降序
                displayItems.sort((o1, o2) -> Pictures.nameOf(o2.getPicture()).compareTo(Pictures.nameOf(o1.getPicture())));
                SORT_BY_NAME.setText("名称    ↓");
            } else {
                //如果是升序，就升序
                displayItems.sort(Comparator.comparing(o -> Pictures.nameOf(o.getPicture())));
                SORT_BY_NAME.setText("名称    ↑");
            }
            setDisplayItems(displayItems);
            isDesc = !isDesc;
        });
    }

    /**
     * @description: 设置按类型排序btn的监听
     * @param: null
     * @return: void
     * @author Lantech
     */
    private void setTypeOnAction() {
        //按类型排序btn的监听
        SORT_BY_TYPE.setOnAction(e -> {
            //先获得首页展示的各个VBox
            ArrayList<DisplayItem> displayItems = getDisplayItems();
            if (sortField != sortFields.TYPE) {
                //不是当前btn的话初始化其他btns为默认值
                initOtherBtns(SORT_BY_TYPE);
                //不是当前排序的话，排序默认为降序
                isDesc = true;
                //排序类型改为当前排序类型
                sortField = sortFields.TYPE;
            }
            //在点击按钮时判断是什么排序，并更改btn图标
            if (isDesc) {
                //如果是降序，就降序
                displayItems.sort((o1, o2) -> Pictures.typeOf(o2.getPicture()).compareTo(Pictures.typeOf(o1.getPicture())));
                SORT_BY_TYPE.setText("类型    ↓");
            } else {
                //如果是升序，就升序
                displayItems.sort(Comparator.comparing(o -> Pictures.typeOf(o.getPicture())));
                SORT_BY_TYPE.setText("类型    ↑");
            }
            setDisplayItems(displayItems);
            isDesc = !isDesc;
        });
    }


    /**
     * @description: 设置按修改时间排序btn的监听
     * @param: null
     * @return: void
     * @author Lantech
     */
    private void setModifiedOnAction() {
        //按修改时间排序btn的监听
        SORT_BY_LAST_MODIFIED.setOnAction(e -> {
            //先获得首页展示的各个VBox
            ArrayList<DisplayItem> displayItems = getDisplayItems();
            if (sortField != sortFields.LAST_MODIFIED) {
                //不是当前btn的话初始化其他btns为默认值
                initOtherBtns(SORT_BY_LAST_MODIFIED);
                //不是当前排序的话，排序默认为降序
                isDesc = true;
                //排序类型改为当前排序类型
                sortField = sortFields.LAST_MODIFIED;
            }
            //在点击按钮时判断是什么排序，并更改btn图标
            if (isDesc) {
                //如果是降序，就降序
                displayItems.sort((o1, o2) -> Pictures.lastModifiedOf(o2.getPicture()).compareTo(Pictures.lastModifiedOf(o1.getPicture())));
                SORT_BY_LAST_MODIFIED.setText("修改日期    ↓");
            } else {
                //如果是升序，就升序
                displayItems.sort(Comparator.comparing(o -> Pictures.lastModifiedOf(o.getPicture())));
                SORT_BY_LAST_MODIFIED.setText("修改日期    ↑");
            }
            setDisplayItems(displayItems);
            isDesc = !isDesc;
        });
    }

    /**
     * @description: 获得主页面中间展示那块区域中的各个VBox的list
     * @param: null
     * @return: java.util.ArrayList<javafx.scene.Node>
     * @author Lantech
     */
    private ArrayList<DisplayItem> getDisplayItems() {
        ArrayList<DisplayItem> displayItems = new ArrayList<>();
        for (Node child : PictureDisplayBar.DISPLAY_FLOW_PANE.getChildren()) {
            displayItems.add((DisplayItem) child);
        }
        return displayItems;
    }

    /**
     * @description: 排序完后重新给主页中间展示图片信息的VBox的list赋值
     * @param: displayItems
     * @return: void
     * @author Lantech
     */
    private void setDisplayItems(ArrayList<DisplayItem> displayItems) {
        //清空原来里面的
        PictureDisplayBar.DISPLAY_FLOW_PANE.getChildren().clear();
        //再重新加进去
        PictureDisplayBar.DISPLAY_FLOW_PANE.getChildren().addAll(displayItems);
    }

    /**
     * @description: 将除本btn外的btns的text设置为对应的默认text
     * @param: nowBtn
     * @return: void
     * @author Lantech
     */
    private void initOtherBtns(Button nowBtn) {
        for (Node node : this.getChildren()) {
            Button btn = (Button) node;
            if (!btn.equals(nowBtn)) {
                System.out.println(btn.getText());
                btn.setText(BTN_INIT_TEXTS.get(btn));
                System.out.println(btn.getText());
            }
        }
    }

}
