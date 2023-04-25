package com.loungexi.ui;

import com.loungexi.pojo.DisplayItem;
import com.loungexi.pojo.Point;
import com.loungexi.pojo.SelectedItem;
import com.loungexi.utils.VBoxData;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * @author LoungeXi
 */
public class PictureDisplayBar {
    // TODO 静态变量整体修改
    private final MainPageTopBar MAIN_PAGE_TOP_BAR = new MainPageTopBar();

    public MainPageTopBar getMainPageTopBar() {
        return MAIN_PAGE_TOP_BAR;
    }

    private static final VBox PICTURE_DISPLAY_VBOX = new VBox();

    public static VBox getPictureDisplayVbox() {
        return PICTURE_DISPLAY_VBOX;
    }

    public static final FlowPane DISPLAY_FLOW_PANE = new FlowPane();
    public static BorderPane DISPLAY_BORDER;
    public static final ScrollPane scrollPane = new ScrollPane();
    private static final Rectangle rectangle = new Rectangle();
    private static Point mouseStart;
    private static Point mouseDragged;
    /**
     * 当前目录下的被选中图片Item
     */
    private static final SelectedItem selectedItem = new SelectedItem();
    public static AnchorPane pane = new AnchorPane();
    /**
     * 判断点击事件是否发生在图片区域
     */
    private static boolean clickBlank = false;
    /**
     * 判断是否为框选完成后的第一次释放点击
     */
    private static boolean releaseDrag = false;
    /**
     * 框选前，滚动条的初始位置
     */
    private static double startValue;
    private double bottomHeight;


    public PictureDisplayBar(BorderPane borderPane) {
        if (DISPLAY_BORDER == null) {
            DISPLAY_BORDER = borderPane;
        }
        initBackground();
    }

    public void initBackground() {
        initRectangle();
        setMainPageTopBar();
        selectedItem.clear();
        setFlowPane();
        setScrollPane();
        DISPLAY_BORDER.setCenter(PICTURE_DISPLAY_VBOX);

    }

    /**
     * @description: 设置MainPageTopBar的各种绑定
     * @param: null
     * @return: void
     * @author Lantech
     */
    private void setMainPageTopBar() {
        PICTURE_DISPLAY_VBOX.getChildren().addAll(MAIN_PAGE_TOP_BAR, scrollPane);
        //将vbox的高度绑定到borderpane上
        PICTURE_DISPLAY_VBOX.prefHeightProperty().bind(DISPLAY_BORDER.heightProperty());
        //将scrollPane的高度绑定到vbox上
        scrollPane.prefHeightProperty().bind(PICTURE_DISPLAY_VBOX.heightProperty());
        //将mainPageTopBar的宽度绑定到vbox的宽上
        MAIN_PAGE_TOP_BAR.prefWidthProperty().bind(PICTURE_DISPLAY_VBOX.widthProperty());
        PICTURE_DISPLAY_VBOX.setVisible(false);
    }

    private void setScrollPane() {
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(pane);
        //在scrollPane上创建右键功能菜单对象
        new FunctionMenu();

        /**
         *
         * @description scrollPane的鼠标点击事件
         * @author Paul
         * @date 23:44 2023/4/20
         **/
        scrollPane.setOnMouseClicked(mouseEvent -> {
            //对发生在scrollPane上右键事件是否触发右键菜单功能进行逻辑判断
            if (mouseEvent.getButton() == MouseButton.SECONDARY && (mouseEvent.getClickCount() == 1 || mouseEvent.getClickCount() == 2)) {
                if (FunctionMenu.getContextMenu().isShowing()) {
                    FunctionMenu.getContextMenu().hide();
                    selectedItem.showSelected();
                } else {
                    if (selectedItem.getItems().size() > 0) {
                        selectedItem.showSelected();
                        FunctionMenu.getContextMenu().show(scrollPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                    }
                }
            }
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 1) {
                if (FunctionMenu.getContextMenu().isShowing()) {
                    FunctionMenu.getContextMenu().hide();
                }
            }
        });
    }

    /**
     * @author: LoungeXi
     * @return: 初始化 FlowPane
     **/
    private void setFlowPane() {
        DISPLAY_FLOW_PANE.setPadding(new Insets(20.0));
        DISPLAY_FLOW_PANE.setHgap(23);
        DISPLAY_FLOW_PANE.setVgap(20);

        AnchorPane.setLeftAnchor(DISPLAY_FLOW_PANE, 0.0);
        AnchorPane.setTopAnchor(DISPLAY_FLOW_PANE, 0.0);
        AnchorPane.setBottomAnchor(DISPLAY_FLOW_PANE, 0.0);
        AnchorPane.setRightAnchor(DISPLAY_FLOW_PANE, 0.0);

        /**
         *
         * @description DISPLAY_FLOW_PANE鼠标按下，得到鼠标按下点的坐标，滚动条的起始值，图片展示区域的最下端的y坐标
         * @author Paul
         * @date 23:37 2023/4/20
         **/
        DISPLAY_FLOW_PANE.setOnMousePressed(mouseEvent -> {
            mouseStart = new Point(mouseEvent.getX(), mouseEvent.getY());
            if (mouseEvent.isPrimaryButtonDown() && DISPLAY_FLOW_PANE.getChildren().size() > 0) {
                startValue = scrollPane.getVvalue();

                ObservableList<Node> children = DISPLAY_FLOW_PANE.getChildren();
                DisplayItem item = (DisplayItem) children.get(children.size() - 1);
                bottomHeight = item.getLayoutY() + VBoxData.vBoxHeight + DISPLAY_FLOW_PANE.getVgap();
            }
        });

        /**
         *
         * @description DISPLAY_FLOW_PANE的鼠标点击事件
         * @author Paul
         * @date 23:41 2023/4/20
         **/
        DISPLAY_FLOW_PANE.setOnMouseClicked(mouseEvent -> {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();

            if (DISPLAY_FLOW_PANE.getChildren().size() > 0) {
                /**
                 * 若左键单击事件发生在非图片区域上，则取消选中FlowPane上的所有被选中图片
                 * releaseDrag判断是否为普通单击事件，还是框选操作后释放触发的单击事件
                 */
                if (mouseEvent.getButton() == MouseButton.PRIMARY && !releaseDrag) {
                    if (!isInner(x, y) && selectedItem.getItems().size() == 1) {
                        selectedItem.getItems().get(0).setSelected(false);
                        selectedItem.removeUnselected();
                        refreshBIBar();
                    }
                    if (!isInner(x, y) && clickBlank && selectedItem.getItems().size() > 1) {
                        selectedItem.clear();
                        refreshBIBar();
                    }
                }
                //普通单击事件发生，releaseDrag置为false
                releaseDrag = false;
            }
        });

        /**
         *
         * @description DISPLAY_FLOW_PANE的鼠标拉拽事件
         * @author Paul
         * @date 23:41 2023/4/20
         **/
        DISPLAY_FLOW_PANE.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                mouseDragged = new Point(mouseEvent.getX(), mouseEvent.getY());

                Point t = new Point(Math.max(mouseDragged.getX(), mouseStart.getX()), Math.max(mouseDragged.getY(), mouseStart.getY()));
                Point mouseStart_t = new Point(Math.min(mouseDragged.getX(), mouseStart.getX()), Math.min(mouseDragged.getY(), mouseStart.getY()));
                mouseDragged = t;

                if (mouseEvent.getX() > scrollPane.getWidth()) {
                    double x = scrollPane.getWidth();
                    double y = mouseDragged.getY();
                    mouseDragged = new Point(x, y);
                }
                if (mouseEvent.getY() > bottomHeight) {
                    double x = mouseDragged.getX();
                    double y = bottomHeight;
                    mouseDragged = new Point(x, y);
                }

                //滚动条位置跟随
                double moveY = mouseStart.getY() - mouseEvent.getY();
                scrollPane.setVvalue(startValue - (moveY / (DISPLAY_FLOW_PANE.getHeight() - scrollPane.getHeight())) / 3);

                //判断矩形是否框选中图片
                for (int i = 0; i < DISPLAY_FLOW_PANE.getChildren().size(); i++) {
                    double left = DISPLAY_FLOW_PANE.getChildren().get(i).getLayoutX();
                    double top = DISPLAY_FLOW_PANE.getChildren().get(i).getLayoutY();
                    double right = left + VBoxData.vBoxWidth;
                    double bottom = top + VBoxData.vBoxHeight;

                    if (!(right < mouseStart_t.getX() || left > mouseDragged.getX() ||
                            top > mouseDragged.getY() || bottom < mouseStart_t.getY())) {
                        selectedItem.select((DisplayItem) DISPLAY_FLOW_PANE.getChildren().get(i));
                    } else {
                        DisplayItem item = (DisplayItem) DISPLAY_FLOW_PANE.getChildren().get(i);
                        if (item.isSelected()) {
                            selectedItem.unselected(item);
                        }
                    }
                }
                showRectangle(mouseStart_t, mouseDragged);
            }
            refreshBIBar();
        });

        /**
         *
         * @description DISPLAY_FLOW_PANE的鼠标释放事件
         * @author Paul
         * @date 23:42 2023/4/20
         **/
        DISPLAY_FLOW_PANE.setOnMouseReleased(mouseEvent ->
        {
            //如果rectangle可见，说明此次释放事件的发生在多选操作完成的时候，将releaseDrag置为true
            if (!releaseDrag && rectangle.isVisible()) {
                releaseDrag = true;
            }
            rectangle.setVisible(false);
        });
    }

    /**
     *
     * @description 判断点击是否发生在图片所在区域
     * @author Paul
     * @date 23:33 2023/4/20
     * @param x 鼠标点击事件发生的x坐标
     * @param y 鼠标点击事件发生的y坐标
     * @return boolean 若点击发生在图片所在区域，返回true,否则返回false
     **/
    private boolean isInner(double x, double y) {
        int size = DISPLAY_FLOW_PANE.getChildren().size();
        double left = DISPLAY_FLOW_PANE.getChildren().get(0).getLayoutX();
        double right = 0;
        double top = DISPLAY_FLOW_PANE.getChildren().get(0).getLayoutY();
        double bottom = 0;
        double endRight = DISPLAY_FLOW_PANE.getChildren().get(size - 1).getLayoutX() + VBoxData.vBoxWidth;
        double endTop = DISPLAY_FLOW_PANE.getChildren().get(size - 1).getLayoutY();
        for (int i = 0; i < DISPLAY_FLOW_PANE.getChildren().size(); i++) {
            DisplayItem item = (DisplayItem) DISPLAY_FLOW_PANE.getChildren().get(i);
            if (right < item.getLayoutX() + VBoxData.vBoxWidth) {
                right = item.getLayoutX() + VBoxData.vBoxWidth;
            }
            if (bottom < item.getLayoutY() + VBoxData.vBoxHeight) {
                bottom = item.getLayoutY() + VBoxData.vBoxHeight;
            }
        }
        if (right < x || left > x || top > y || bottom < y) {
            clickBlank = true;
            return false;
        }
        if ((x > endRight && x < right) && (y > endTop && y < bottom) && !rectangle.isVisible()) {
            clickBlank = true;
            return false;
        }
        return true;
    }

    /**
     *
     * @description 刷新选中图片数
     * @author Paul
     * @date 23:35 2023/4/20
     **/
    public static void refreshBIBar() {
        ObservableList<Node> children = DISPLAY_FLOW_PANE.getChildren();
        int selected = 0;
        for (int i = 0; i < children.size(); i++) {
            DisplayItem item = (DisplayItem) children.get(i);
            if (item.isSelected()) {
                selected++;
            }
        }
        new BottomInfoBar(selected);
    }

    /**
     * @description: 初始化选择框，框选时显示
     * @author Paul
     */
    private void initRectangle() {
        rectangle.setVisible(false);
//        pane.getChildren().addAll(DISPLAY_FLOW_PANE, rectangle, new MainPageTopBar());
        pane.getChildren().addAll(DISPLAY_FLOW_PANE, rectangle);
    }

    /**
     *
     * @description 展示矩形
     * @author Paul
     * @date 23:29 2023/4/20
     * @param start 矩形起始点
     * @param end  矩形拉拽点
     **/
    private void showRectangle(Point start, Point end) {
        rectangle.setX(start.getX());
        rectangle.setY(start.getY());
        rectangle.setWidth(end.getX() - start.getX());
        rectangle.setHeight(end.getY() - start.getY());
        rectangle.setStyle("-fx-border-color: #00F5FF;");
        rectangle.setFill(Paint.valueOf("00F5FF33"));
        rectangle.setVisible(true);

        clickBlank = false;
    }

    /**
     *
     * @description 得到选中的图片文件集合
     * @author Paul
     * @date 23:30 2023/4/20
     * @return com.loungexi.pojo.SelectedItem
     **/
    public static SelectedItem getSelectedItem() {
        return selectedItem;
    }
}
