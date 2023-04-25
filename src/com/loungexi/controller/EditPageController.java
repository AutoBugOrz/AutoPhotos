package com.loungexi.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Lantech
 * @description: 编辑界面的控制器
 */
public class EditPageController implements Initializable {
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
    private ToolBar toolBar;

    @FXML
    private StackPane stackPane;

    private GraphicsContext gc;
    //开始涂鸦时鼠标的起始位置
    private Double startX, startY;
    //实时鼠标位置
    private Double nowX, nowY;
    //
    private double textX, textY;
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
//            AnchorPane.setTopAnchor(textField, nowY - textField.getPrefHeight() / 2);
//            AnchorPane.setLeftAnchor(textField, nowX - textField.getWidth() / 2);
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
                    System.out.println("换行");
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
            System.out.println("777");
        } else if (isTexting) {
            System.out.println("666");
            exitTexting(nowTextField);
        }
    }

    private void deleteTextField(TextField textField) {
        textPane.getChildren().remove(textField);
    }

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
        System.out.println("exit...");
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
        System.out.println("drawBtn");

    }

    /**
     * @description: 保存图片
     * @param: event
     * @return: void
     * @author Lantech
     */
    @FXML
    void save(ActionEvent event) {
        // TODO: 2023/4/24 保存时，还要绘制文本框的内容到canvas上
        System.out.println("--------------------");
        System.out.println("editArea");
        System.out.println(editArea.getHeight());
        System.out.println(editArea.getWidth());
        System.out.println("--------------------");
        System.out.println("imageArea");
        System.out.println(imageArea.getFitHeight());
        System.out.println(imageArea.getFitWidth());
        System.out.println("--------------------");
        System.out.println("anchorPane");
        System.out.println(anchorPane.getHeight());
        System.out.println(anchorPane.getWidth());
        System.out.println("--------------------");
        System.out.println("borderPane");
        System.out.println(borderPane.getHeight());
        System.out.println(borderPane.getWidth());
        System.out.println("--------------------");
        System.out.println("toolBar");
        System.out.println(toolBar.getHeight());
        System.out.println(toolBar.getWidth());
        System.out.println("--------------------");
        System.out.println("stackPane");
        System.out.println(stackPane.getHeight());
        System.out.println(stackPane.getWidth());
        System.out.println("--------------------");
        System.out.println("textPane");
        System.out.println(textPane.getHeight());
        System.out.println(textPane.getWidth());
        System.out.println("--------------------");
        System.out.println("saveBtn");
        // TODO: 2023/4/24

        //获取原始图像
        Image originalImage = imageArea.getImage();
        //获取长宽
        double originalWidth = originalImage.getWidth();
        double originalHeight = originalImage.getHeight();
//        double originalWidth = imageArea.getFitWidth();
//        double originalHeight = imageArea.getFitHeight();
        double editWidth = editArea.getWidth();
        double editHeight = editArea.getHeight();
        System.out.println("imageAreaWidth:" + imageArea.getFitWidth());
        System.out.println("imageAreaHeight" + imageArea.getFitHeight());
        System.out.println("imgWidth:" + originalWidth);
        System.out.println("imgHeight" + originalHeight);
        System.out.println("editWidth:" + editWidth);
        System.out.println("editHeight" + editHeight);

        SnapshotParameters para = new SnapshotParameters();
        para.setFill(Color.TRANSPARENT);

        PixelReader reader = originalImage.getPixelReader();
        //创建可写入的图像
        WritableImage writableImage = new WritableImage((int) originalWidth, (int) originalHeight);
        PixelWriter writer = writableImage.getPixelWriter();
        //将原始图像绘制到可写入的图像中
        writer.setPixels(0, 0, (int) originalWidth, (int) originalHeight, reader, 0, 0);
        //将canvas上的涂鸦绘制到可写入的图像中
        GraphicsContext gc = editArea.getGraphicsContext2D();
//        gc.setFill(Color.RED);
        gc.setFill(Color.TRANSPARENT);


//        Canvas tempCanvas = new Canvas(editWidth, editHeight);
//        tempCanvas.getGraphicsContext2D().setFill(Color.WHITE);
//        tempCanvas.getGraphicsContext2D().fillRect(0, 0, editWidth, editHeight);
//        tempCanvas.getGraphicsContext2D().drawImage(editArea.snapshot(null, null), 0, 0);

        writer = writableImage.getPixelWriter();
        // TODO: 2023/4/24 ?
        gc.stroke();
        gc.strokeRect(0, 0, editWidth, editHeight);

        reader = editArea.snapshot(para, null).getPixelReader();

        writer.setPixels(0, 0, (int) editWidth, (int) editHeight, reader, 0, 0);
        choosePathToSave(writableImage);
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
        // TODO: 2023/4/24 需要把默认文件名改为图片名加上"（1）"
        fileChooser.setInitialFileName("image.png");
        File file = fileChooser.showSaveDialog(stackPane.getScene().getWindow());
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                //将新图片展示成新的图片
//                Image newImage = new Image(file.toURI().toString());
//                imageArea.setImage(newImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @description: 切换到编辑文字模式
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("init...");
        editArea.setStyle("-fx-background-color: transparent");
        gc = editArea.getGraphicsContext2D();
        gc.setLineWidth(lineWidth);
        gc.setStroke(Color.RED);
        // 将背景颜色设置为透明色
        gc.setFill(Color.TRANSPARENT);
        // 填充背景
        //这样，当在Canvas上进行涂鸦时，就会显示透明的背景，从而看起来像是在图像上直接涂鸦。
        gc.fillRect(0, 0, editArea.getWidth(), editArea.getHeight());
        imageHeight = imageArea.getImage().getHeight();
        imageWidth = imageArea.getImage().getWidth();
        //设置编辑区域监听事件
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
