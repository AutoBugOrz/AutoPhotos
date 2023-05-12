package com.autophotos.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * 报错弹窗类
 * @author Paul
 */
public class AlertView {
    public enum AlertType {
        /**
         * 数字类型错误
         */
        NumberTypeError,
        /**
         * 重命名错误,相同文件名文件已存在
         */
        RenameError
    }

    /**
     *
     * @description 根据不同的AlertType来展示不同的警告信息
     * @author Paul
     * @date 23:46 2023/4/20
     * @param alertType
     * @return boolean
     **/
    public static boolean showAlert(AlertType alertType){
        Alert alert = null;
        switch (alertType){
            case NumberTypeError:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("数字类型/范围错误");
                alert.setHeaderText("请输入正确的数字");
                alert.setContentText("编号位数和起始编号的取值应大于0且不为空");
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
