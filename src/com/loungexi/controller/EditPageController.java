package com.loungexi.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * @author Lantech
 * @description: 编辑界面的控制器
 */
public class EditPageController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane textPane;

    @FXML
    private Button drawBtn;

    @FXML
    private Canvas editArea;

    @FXML
    private ImageView imageArea;

    @FXML
    private Button saveBtn;

    @FXML
    private Button textBtn;
    @FXML
    private HBox toolBar;

    @FXML
    private StackPane stackPane;
    //  editArea的画笔对象
    private GraphicsContext gc;
    //开始涂鸦时鼠标的起始位置
    private Double startX, startY;
    //实时鼠标位置
    private Double nowX, nowY;
    //画笔线宽
    private Double lineWidth = 5.0;
    //当前是否在涂鸦
    private boolean isDrawing = true;
    //当前是否在编辑文字
    private boolean isTexting = false;
    //当前是否在添加文本框
    private boolean isAddingTextField = false;
    private TextField nowTextField;
    //图片的原始长宽
    private double imageWidth, imageHeight;

    /**
     * @description: 涂鸦和擦除
     * @param: event
     * @return: void
     * @author Lantech
     */
    @FXML
    void drawAndErase(MouseEvent event) {
        if (isDrawing && event.isPrimaryButtonDown()) {
            //拖动鼠标左键时涂鸦
            gc.strokeLine(startX, startY, event.getX(), event.getY());
            //重置画笔开始坐标
            startX = event.getX();
            startY = event.getY();
        } else if (isDrawing && event.isSecondaryButtonDown()) {
            //拖动鼠标右键时擦除
            nowX = event.getX();
            nowY = event.getY();
            //获得鼠标当前位置和上一次位置之间的矩形区域的长宽
            double width = Math.abs(nowX - startX);
            double height = Math.abs(nowY - startY);
            //获得需要清除的矩形区域的左上角坐标
            double minX = Math.min(nowX, startX);
            double minY = Math.min(nowY, startY);
            //为了保证清除的区域能够刚好覆盖需要清除的内容，需要在指左上角的坐标减去一半的线宽
            //同时右下角的坐标也要加一半，但是因为是width和height，所以要加上一个线宽（一半线宽+一半线宽）
            gc.clearRect(minX - gc.getLineWidth() / 2, minY - gc.getLineWidth() / 2, width + gc.getLineWidth(), height + gc.getLineWidth());
            //重置画笔开始坐标
            startX = nowX;
            startY = nowY;
        }
    }

    /**
     * @description: 获取鼠标位置
     * @param: event
     * @return: void
     * @author Lantech
     */
    @FXML
    void getMouseLocation(MouseEvent event) {
        if (isDrawing && event.isPrimaryButtonDown()) {
            startX = event.getX();
            startY = event.getY();
        }
        System.out.println(startX + " " + startY);
    }

    /**
     * @description: 调整鼠标大小
     * @param: event
     * @return: void
     * @author Lantech
     */
    @FXML
    void adjustMouseSize(ScrollEvent event) {
        if (isDrawing) {
            //获取画笔的线宽，即涂鸦的粗细
            lineWidth = gc.getLineWidth();
            //获取鼠标滚轮的滚动距离，单位是像素。正值表示向上滚动，负值表示向下滚动
            double delta = event.getDeltaY();
            //根据鼠标滚轮的滚动量来增加或减少画笔的线宽。
            lineWidth += delta > 0 ? 1 : -1;
            //限制画笔的线宽最小值为1，避免画笔线宽过小，无法绘制。
            lineWidth = Math.max(1, lineWidth);
            //限制画笔的线宽最大值为50，避免画笔线宽过大，影响涂鸦效果。
            lineWidth = Math.min(50, lineWidth);
            gc.setLineWidth(lineWidth);
        }
    }

    /**
     * @description: 编辑文字
     * @param: event
     * @return: void
     * @author Lantech
     */
    @FXML
    void addTextField(MouseEvent event) {
        if (isAddingTextField) {
            nowX = event.getX();
            nowY = event.getY();
            //显示文本框
            TextField textField = new TextField();
            nowTextField = textField;
            //将文本框添加到画布上
            textPane.getChildren().add(textField);
            //设置文本框宽度
            textField.setPrefWidth(100);
            //设置文本框高度
            textField.setPrefHeight(50);
            textField.setLayoutX(nowX);
            textField.setLayoutY(nowY);
            // TODO: 2023/4/27 得根据imageArea的坐标来，不能根据stackPane的宽高来
            // 获取imageArea的初始位置
//            Point2D initPos = imageArea.localToScene(imageArea.getBoundsInLocal().getCenterX(), imageArea.getBoundsInLocal().getCenterY());
//            textField.setLayoutX(initPos.getX() - textField.getWidth() / 2);
//            textField.setLayoutY(initPos.getY() - textField.getHeight() / 2);

            // 监听imageArea中心点坐标的变化
            imageArea.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
                // 获取imageArea的中心点全局坐标
                Point2D newPos = imageArea.localToScene(newValue.getCenterX(), newValue.getCenterY());

                // 计算textField应该在textPane中的位置
                double newX = newPos.getX() - textField.getWidth() / 2;
                double newY = newPos.getY() - textField.getHeight() / 2;

                // 更新textField的位置
                textField.setLayoutX(newX);
                textField.setLayoutY(newY);
            });


            textField.setStyle("-fx-background-color: transparent;-fx-border-color: red;-fx-font-size: 15;-fx-text-fill: #0eeacb;-fx-font-weight: bold");
            // 设置焦点使用户可以立即编辑文本
            textField.requestFocus();
            /*
             * @description: 给textField的可编辑属性添加监听事件
             * @param: event
             * @return: void
             * @author Lantech
             */
            textField.editableProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if (textField.isEditable()) {
                        //如果可编辑
                        textField.setStyle("-fx-background-color: transparent;-fx-border-color: red;-fx-font-size: 15;-fx-text-fill: #0eeacb;-fx-font-weight: bold");
                    } else {
                        //如果不可编辑
                        if (!"".equals(textField.getText())) {
                            //如果文本框里有文字
                            textField.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-font-size: 15;-fx-text-fill: #0eeacb;-fx-font-weight: bold");
                        } else {
                            //如果文本框里没文字，直接删除
                            deleteTextField(textField);
                        }
                    }
                }
            });
            /*
             * @description: textField的键盘监听事件
             * @param: event
             * @return: void
             * @author Lantech
             */
            textField.setOnKeyPressed(keyEvent -> {
                if ((keyEvent.getCode() == KeyCode.ENTER && !keyEvent.isShiftDown()) || keyEvent.getCode() == KeyCode.ESCAPE) {
                    //退出编辑
                    exitTexting(textField);
                } else if (keyEvent.getCode() == KeyCode.ENTER && keyEvent.isShiftDown()) {
                    //换行
                    textField.appendText("\n");
                }
            });

            /*
             * @description: textField的鼠标点击事件
             * @param: event
             * @return: void
             * @author Lantech
             */
            textField.setOnMouseClicked(event1 -> {
                if (event1.getClickCount() == 1) {
                    //开始编辑
                    startTexting(textField);
                } else if (event1.getClickCount() == 2) {
                    //询问是否删除文本框
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除该文本框吗？", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        //直接删除
                        deleteTextField(textField);
                    }
                }
            });
            exitAddText();
            startTexting(textField);
        } else if (isTexting) {
            exitTexting(nowTextField);
        }
    }

    /**
     * @description: 删除文本框
     * @param: textField
     * @return: void
     * @author Lantech
     */
    private void deleteTextField(TextField textField) {
        textPane.getChildren().remove(textField);
    }

    /**
     * @description: 退出添加文本框
     * @param: null
     * @return: void
     * @author Lantech
     */
    private void exitAddText() {
        isAddingTextField = false;
    }

    /**
     * @description: 退出编辑文字
     * @param: textField
     * @return: void
     * @author Lantech
     */
    private void exitTexting(TextField textField) {
        textPane.requestFocus();
        textField.setEditable(false);
        isTexting = false;
    }

    /**
     * @description: 开始编辑文字
     * @param: textField
     * @return: void
     * @author Lantech
     */
    private void startTexting(TextField textField) {
        textField.setEditable(true);
        isTexting = true;
    }

    /**
     * @description: 切换到涂鸦模式
     * @param: event
     * @return: void
     * @author Lantech
     */
    @FXML
    void draw(ActionEvent event) {
        isDrawing = true;
        isAddingTextField = false;
    }

    /**
     * @description: 保存图片
     * @param: event
     * @return: void
     * @author Lantech
     */
    @FXML
    void save(ActionEvent event) {
        //获取原始图像
        Image originalImage = imageArea.getImage();
        //获取长宽
        double originalWidth = originalImage.getWidth();
        double originalHeight = originalImage.getHeight();
        // TODO: 2023/4/26 textFields保存下来的位置和大小都不对
        Canvas canvas = new Canvas(originalWidth, originalHeight);
        GraphicsContext canvasGc = canvas.getGraphicsContext2D();
        SnapshotParameters parameters = new SnapshotParameters();
        //将原图绘制到canvas上
        canvasGc.drawImage(originalImage, 0, 0);
        //将editArea的涂鸦绘制到canvas上
        saveEditArea(canvasGc, parameters);
        //将textFields的内容绘制到canvas上
        saveTextFields(canvasGc, parameters);
        //将canvas的内容保存为图片
        Image newImage = canvas.snapshot(null, null);
        choosePathToSave(newImage);
    }

    /**
     * @description: 将textFields的内容绘制到canvas
     * @param: canvasGc
     * @param: parameters
     * @return: void
     * @author Lantech
     */
    private void saveTextFields(GraphicsContext canvasGc, SnapshotParameters parameters) {
        // TODO: 2023/4/26 保存的坐标位置有问题
        for (Node node : textPane.getChildren()) {
            TextField textField = (TextField) node;
            double x = textField.getLayoutX();
            double y = textField.getLayoutY();
            double width = textField.getWidth();
            double height = textField.getHeight();
            //获取文本
            String text = textField.getText();
            //获取字体
//            Font font=textField.getFont();
            String style = textField.getStyle();
            System.out.println(style);
            //创建文本对象
            Text textNode = new Text(text);
//            textNode.setFont(font);
            textNode.setStyle(style);
            Image textImage = textNode.snapshot(parameters, null);
            canvasGc.drawImage(textImage, x, y, width, height);
        }
    }

    /**
     * @description: 将editArea的内容绘制到canvas上
     * @param: canvasGc
     * @param: parameters
     * @return: void
     * @author Lantech
     */
    private void saveEditArea(GraphicsContext canvasGc, SnapshotParameters parameters) {
        //设置画笔在绘制时的params是透明背景
        parameters.setFill(Color.TRANSPARENT);
        double originalWidth = imageArea.getImage().getWidth();
        double originalHeight = imageArea.getImage().getHeight();
        double editWidth = editArea.getWidth();
        double editHeight = editArea.getHeight();
        // 创建一个临时Canvas用来将涂鸦进行缩放
        Canvas tempCanvas = new Canvas(editWidth, editHeight);
        GraphicsContext tempGc = tempCanvas.getGraphicsContext2D();
        //得到缩放比例
        double scale = originalWidth / editWidth;
        //将editArea上的涂鸦绘制到临时Canvas上
        tempGc.drawImage(editArea.snapshot(parameters, null), 0, 0);
        // 将临时Canvas上的涂鸦进行缩放
        tempCanvas.setScaleX(scale);
        tempCanvas.setScaleY(scale);
        //得到临时canvas上的涂鸦快照
        Image drawImage = tempCanvas.snapshot(parameters, null);
        // 将涂鸦快照绘制到canvas上
        canvasGc.drawImage(drawImage, 0, 0, originalWidth, originalHeight);
    }

    /**
     * @description: 选择路径保存图片
     * @param: image
     * @return: void
     * @author Lantech
     */
    private void choosePathToSave(Image image) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存图片");
        //添加保存格式
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG Files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter jpegFilter = new FileChooser.ExtensionFilter("JPEG Files (*.jpeg)", "*.jpeg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");
        FileChooser.ExtensionFilter bmpFilter = new FileChooser.ExtensionFilter("BMP Files (*.bmp)", "*.bmp");
        fileChooser.getExtensionFilters().addAll(jpgFilter, jpegFilter, pngFilter, bmpFilter);
        fileChooser.setInitialFileName("image.jpg");
        File file = fileChooser.showSaveDialog(stackPane.getScene().getWindow());
        if (file != null) {
            try {
                //用jpg会保存不了
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                //将保存成功的消息反馈给用户
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("保存成功");
                alert.setHeaderText(null);
                alert.setContentText("文件已保存到：" + file.getAbsolutePath());
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @description: 切换到添加文本框模式
     * @param: event
     * @return: void
     * @author Lantech
     */
    @FXML
    void text(ActionEvent event) {
        isDrawing = false;
        isAddingTextField = true;
        System.out.println("textBtn");
    }

    /**
     * @description: 初始化controller
     * @param: url
     * @param: resourceBundle
     * @return: void
     * @author Lantech
     */
    public void initialize() {
        editArea.setStyle("-fx-background-color: transparent");
        gc = editArea.getGraphicsContext2D();
        gc.setLineWidth(lineWidth);
        gc.setStroke(Color.RED);
        // 将背景颜色设置为透明色
        gc.setFill(Color.TRANSPARENT);
        // 在editArea上画出透明背景
        gc.fillRect(0, 0, editArea.getWidth(), editArea.getHeight());
        // 计算Button的宽度
        double buttonWidth = (borderPane.getWidth()) / 3;
        // 设置Button的宽度
        drawBtn.setPrefWidth(buttonWidth);
        textBtn.setPrefWidth(buttonWidth);
        saveBtn.setPrefWidth(buttonWidth);
        // 设置Button的水平增长策略为ALWAYS
        HBox.setHgrow(drawBtn, Priority.ALWAYS);
        HBox.setHgrow(textBtn, Priority.ALWAYS);
        HBox.setHgrow(saveBtn, Priority.ALWAYS);
        borderPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = (newVal.doubleValue()) / 3;
            drawBtn.setPrefWidth(width);
            textBtn.setPrefWidth(width);
            saveBtn.setPrefWidth(width);
        });
    }

    /**
     * @description: 供外部调用来设置要编辑的图片
     * @param: image
     * @return: void
     * @author Lantech
     */
    public void setImage(Image image) {
        // TODO: 2023/4/26 最大化窗口后再恢复的话图片展示就会有问题
        // TODO: 2023/4/26 最大化之后对话框位置也有问题
        imageArea.setImage(image);
        imageHeight = imageArea.getImage().getHeight();
        imageWidth = imageArea.getImage().getWidth();
        /*
         * @description: 图片随窗口大小变化的监听器
         * @author Lantech
         */
        stackPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            resizeImage();
        });
        stackPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            resizeImage();
        });
        //绑定编辑区域和图片区域的大小
        editArea.widthProperty().bind(imageArea.fitWidthProperty());
        editArea.heightProperty().bind(imageArea.fitHeightProperty());
        //绑定textPane区域和图片区域的大小
        textPane.prefWidthProperty().bind(imageArea.fitWidthProperty());
        textPane.prefHeightProperty().bind(imageArea.fitHeightProperty());
    }

    /**
     * @description: image自适应窗口大小
     * @param: null
     * @return: void
     * @author Lantech
     */
    private void resizeImage() {
        double width = stackPane.getWidth();
        double height = stackPane.getHeight();
        double scale = Math.min(width / imageWidth, height / imageHeight);
        imageArea.setFitWidth(imageWidth * scale);
        imageArea.setFitHeight(imageHeight * scale);
    }
}
