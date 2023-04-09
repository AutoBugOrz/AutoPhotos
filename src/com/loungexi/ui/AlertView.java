package com.loungexi.ui;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * @author Paul
 */
public class AlertView {
    public static enum AlertType {
        /**
         * 数字类型错误
         */
        NumberTypeError,
        /**
         * 重命名错误,相同文件名文件已存在
         */
        RenameError,
        NameTypeError
    }

    public static boolean showAlert(AlertType alertType){
        Alert alert = null;
        switch (alertType){
            case NumberTypeError:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("数字类型/范围错误");
                alert.setHeaderText("请输入正确的数字");
                alert.setContentText("编号位数和起始编号的取值应大于0且不为空");
                break;
            case NameTypeError:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("名称类型错误");
                alert.setHeaderText("请输入正确的名称");
                alert.setContentText("请输入正确的名称");
                break;
            case RenameError:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("重命名错误");
                alert.setHeaderText("请输入正确的名称");
                alert.setContentText("相同文件名文件已存在");
                break;
            default:
        }
        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.get() == ButtonType.OK){
            return true;
        }
        return false;
    }
}
