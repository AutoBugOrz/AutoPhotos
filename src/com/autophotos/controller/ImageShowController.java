package com.autophotos.controller;

import com.autophotos.pojo.AsyncPlayer;
import com.autophotos.pojo.LargeImageShowItem;
import com.autophotos.pojo.Picture;
import com.autophotos.ui.ImageShowBar;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LoungeXi
 */
public class ImageShowController {
    private final List<File> list = new ArrayList<>();
    private Boolean isAutoPlay = false;
    private Integer rotation = 0;
    private Integer fitWidthNumber = 1000;
    private Integer fitHeightNumber = 600;
    private Integer nowImageCount;

    public ImageShowController(Button counterClockWiseButton, Button clockWiseButton, Button enlargeButton, Button shrinkButton, Button previousButton, Button nextButton, Button autoPlayButton, Button editImageButton, Picture picture) {
        initFiles(picture);
        addButtonController(counterClockWiseButton, clockWiseButton, enlargeButton, shrinkButton, previousButton, nextButton, autoPlayButton, editImageButton);
    }

    /**
     * @author: LoungeXi
     * @param: [counterClockWiseButton, clockWiseButton, enlargeButton, shrinkButton, previousButton, nextButton, autoPlayButton]
     * @return: 添加按钮的点击事件，实现各个按钮的功能 幻灯片功能使用时点击其他按钮会终止幻灯片功能
     **/
    private void addButtonController(Button counterClockWiseButton, Button clockWiseButton, Button enlargeButton, Button shrinkButton, Button previousButton, Button nextButton, Button autoPlayButton, Button editImageButton) {
        editImageButton.setOnAction(event -> {
            this.isAutoPlay = false;
            autoPlayButton.setGraphic(new ImageView(new Image("File:image/anto play.png")));
            loadFxml();
        });

        autoPlayButton.setOnAction(event -> {
            autoPlayImage(autoPlayButton);
        });

        nextButton.setOnAction(event -> {
            this.isAutoPlay = false;
            autoPlayButton.setGraphic(new ImageView(new Image("File:image/anto play.png")));
            toNext();
        });

        previousButton.setOnAction(event -> {
            this.isAutoPlay = false;
            autoPlayButton.setGraphic(new ImageView(new Image("File:image/anto play.png")));
            toPrevious();
        });

        enlargeButton.setOnAction(event -> {
            this.isAutoPlay = false;
            autoPlayButton.setGraphic(new ImageView(new Image("File:image/anto play.png")));
            enlargeImage();
        });

        shrinkButton.setOnAction(event -> {
            this.isAutoPlay = false;
            autoPlayButton.setGraphic(new ImageView(new Image("File:image/anto play.png")));
            shrinkImage();
        });

        clockWiseButton.setOnAction(event -> {
            this.isAutoPlay = false;
            autoPlayButton.setGraphic(new ImageView(new Image("File:image/anto play.png")));
            clockWiseRotateImage();
        });

        counterClockWiseButton.setOnAction(event -> {
            this.isAutoPlay = false;
            autoPlayButton.setGraphic(new ImageView(new Image("File:image/anto play.png")));
            counterClockWiseRotateImage();
        });
    }

    /**
     * @description: 加载FXML文件
     * @param: null
     * @return: void
     * @author Lantech
     */
    private void loadFxml() {
        File file = list.get(nowImageCount);
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        //不支持对gif格式进行修改
        if (!"gif".equals(fileType)) {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            //fxml路径
            String fxmlUrlString = "file:src/com/loungexi/ui/EditPage.fxml";
            try {
                //加载fxml
                loader.setLocation(new URL(fxmlUrlString));
                root = loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //设置图片
            Image image = new Image(file.getPath());
            EditPageController controller = loader.getController();
            controller.setImage(image);
            //窗口大小根据屏幕自适应
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = visualBounds.getWidth();
            double screenHeight = visualBounds.getHeight();
            Scene scene = new Scene(root, screenWidth * 0.8, screenHeight * 0.8);
            primaryStage.setResizable(false);
            //窗口居中
            primaryStage.setX((screenWidth - scene.getWidth()) / 2);
            primaryStage.setY((screenHeight - scene.getHeight()) / 2);
            primaryStage.setScene(scene);
            primaryStage.setTitle("编辑图片");
            //禁止对其他窗口进行操作
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("操作提示");
            alert.setHeaderText("操作提示");
            alert.setContentText("不支持对gif格式进行编辑");
            alert.showAndWait();
        }
    }

    /**
     * @author: LoungeXi
     * @param: [autoPlayButton]
     * @return: 完成自动播放图片功能，即幻灯片功能，主要逻辑是点击播放按钮后创建一个新的线程来异步实现图片的切换，编写一个类
     * AsyncPlayer(IntPredicate intPredicate)完成功能new AsyncPlayer(IntPredicate intPredicate)重写intPredicate中
     * 的test方法来实时检测和返回isAutoPlay的值，条件不通过则停止播放改变按钮样式。
     **/
    private void autoPlayImage(Button autoPlayButton) {
        //转换isAutoPlay的值
        this.isAutoPlay = !isAutoPlay;
        if (isAutoPlay) {
            autoPlayButton.setGraphic(new ImageView(new Image("File:image/stop play.png")));
        } else {
            autoPlayButton.setGraphic(new ImageView(new Image("File:image/anto play.png")));
        }
        new AsyncPlayer(intPredicate -> {
            try {
                // 优化:若isAutoPlay的值发生改变 则不执行语句直接返回isAutoPlay的值
                if (isAutoPlay) {
                    // 若已经使最后一张 转换转换isAutoPlay的值 让AsyncPlayer类中的keepGo()方法判断不通过 提前结束play()方法和keepGo()方法互相调用
                    if (nowImageCount + 1 == list.size()) {
                        this.isAutoPlay = false;
                        // 改变按钮样式
                        autoPlayButton.setGraphic(new ImageView(new Image("File:image/anto play.png")));
                        // 等待一秒后弹出播放完毕的提示
                        Thread.sleep(1000);
                        // 弹出提示框
                        popUpAlert();
                    } else {
                        // 播放下一张图片
                        toNext();
                        // 时间间隔为两秒
                        Thread.sleep(2000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return isAutoPlay;
        });
    }

    /**
     * @author: LoungeXi
     * @return: 完成顺时针旋转图片功能
     **/
    private void clockWiseRotateImage() {
        rotation += 90;
        ImageShowBar.Image_LABEL.setStyle("-fx-rotate: " + rotation + ";");
    }

    /**
     * @author: LoungeXi
     * @return: 完成逆时针旋转图片功能
     **/
    private void counterClockWiseRotateImage() {
        rotation -= 90;
        ImageShowBar.Image_LABEL.setStyle("-fx-rotate: " + rotation + ";");
    }


    /**
     * @author: LoungeXi
     * @return: 完成查看下一张图片功能
     **/
    private void toNext() {
        // 索引不能越界
        if (nowImageCount + 1 < list.size()) {
            // 转换图片初始化图片显示大小
            initImageShowSize();
            //索引自增 展示新图片
            this.nowImageCount++;
            File file = list.get(nowImageCount);
            //记住JavaFX中图片路径的前面有 "File:"
            Picture picture = new Picture(new Image("File:" + file.getAbsolutePath()), file.getName());
            // 直接new一个对象放进窗口
            new LargeImageShowItem(picture, ImageShowBar.Image_LABEL);
        } else {
            //弹出提示框
            popUpAlert();
        }
    }


    /**
     * @author: LoungeXi
     * @return: 完成查看上一张图片功能
     **/
    private void toPrevious() {
        // 索引不能越界
        if (nowImageCount - 1 >= 0) {
            // 转换图片初始化图片显示大小
            initImageShowSize();
            //索引自减 展示新图片
            this.nowImageCount--;
            File file = list.get(nowImageCount);
            //记住JavaFX中图片路径的前面有 "File:"
            Picture picture = new Picture(new Image("File:" + file.getAbsolutePath()), file.getName());
            // 直接new一个对象放进窗口
            new LargeImageShowItem(picture, ImageShowBar.Image_LABEL);
        } else {
            //弹出提示框
            popUpAlert();
        }
    }

    /**
     * @author: LoungeXi
     * @return: 完成图片放大功能
     **/
    private void enlargeImage() {
        if (this.fitWidthNumber < 4400) {
            this.fitWidthNumber += 100;
        }
        if (this.fitHeightNumber < 4000) {
            this.fitHeightNumber += 100;
        }
        LargeImageShowItem.IMAGE_VIEW.setFitWidth(this.fitWidthNumber);
        LargeImageShowItem.IMAGE_VIEW.setFitHeight(this.fitHeightNumber);
    }

    /**
     * @author: LoungeXi
     * @return: 完成图片缩小功能
     **/
    private void shrinkImage() {
        if (this.fitWidthNumber > 500) {
            this.fitWidthNumber -= 100;
        }
        if (this.fitHeightNumber > 100) {
            this.fitHeightNumber -= 100;
        }
        LargeImageShowItem.IMAGE_VIEW.setFitWidth(this.fitWidthNumber);
        LargeImageShowItem.IMAGE_VIEW.setFitHeight(this.fitHeightNumber);
    }

    /**
     * @author: LoungeXi
     * @return: 弹出提示框 在文件图片浏览完毕的时候弹出
     **/
    private void popUpAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("当前文件夹图片已浏览完毕!\n请点击确定继续操作!");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add((new Image("File:image/note.png")));
        alert.showAndWait();
    }

    /**
     * @author: LoungeXi
     * @param: [picture]
     * @return: 点击当前图片后获得图片所在的目录的路径，将目录中的图片存放人一个 List集合中，为了程序的
     * "显示上一张图片" 和 "显示下一张图片" 功能做先前准备，主要思路是根据图片的索引来实现图片显示的转换
     **/
    private void initFiles(Picture picture) {
        //这里为了得到当前图片所在目录的绝对路径下了很大功夫 记住JavaFX中图片路径的前面有 "File:" 方法不只这一种 希望大家能耐心尝试得到最终结果
        int pictureNameLength = picture.getImageName().length();
        String fileAbsolutePath = picture.getImage().getUrl();
        String firstSetFilesPath = (fileAbsolutePath.substring(0, fileAbsolutePath.length() - pictureNameLength));
        String finalFilesPath = firstSetFilesPath.split(":")[1] + ":" + firstSetFilesPath.split(":")[2];

        File[] allFile = new File(finalFilesPath).listFiles();
        if (allFile.length != 0) {
            for (File file : allFile) {
                if (!file.isDirectory()) {
                    String fileName = file.getName();
                    //获取当前文件的后缀名
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if ("png".equalsIgnoreCase(suffix) || "jpg".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix)
                            || "gif".equalsIgnoreCase(suffix) || "bmp".equalsIgnoreCase(suffix)) {
                        //将图片文件放入list集合中
                        list.add(file);
                    }
                }
            }
        }
        //得到当前展示图片的索引
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equalsIgnoreCase(picture.getImageName())) {
                this.nowImageCount = i;
            }
        }
    }

    /**
     * @author: LoungeXi
     * @return: 初始化图片的显示大小
     **/
    private void initImageShowSize() {
        this.fitWidthNumber = 1000;
        this.fitHeightNumber = 600;
        LargeImageShowItem.IMAGE_VIEW.setFitWidth(this.fitWidthNumber);
        LargeImageShowItem.IMAGE_VIEW.setFitHeight(this.fitHeightNumber);
    }
}


