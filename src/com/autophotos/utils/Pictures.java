package com.autophotos.utils;

import com.autophotos.pojo.Picture;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Lantech
 * @description: Picture类的工具类，用来获取Picture中image的名称、最后修改时间、类型、大小
 */
public class Pictures {
    private Pictures(Picture picture) {
    }

    /**
     * @description: 用来获取Picture对象中image的名称
     * @param: picture
     * @return: java.lang.String
     * @author Lantech
     */
    public static String nameOf(Picture picture) {
        return picture.getImageName();
    }

    /**
     * @description: 用来获取Picture对象中image的最后修改时间
     * @param: picture
     * @return: java.lang.Long
     * @author Lantech
     */
    public static Long lastModifiedOf(Picture picture) {
        //获得picture中image的文件对象
        Path imagePath1 = Paths.get(picture.getImage().getUrl().replace("file:", ""));
        File file = imagePath1.toFile();
        return file.lastModified();
    }

    /**
     * @description: 用来获取Picture对象中image的类型
     * @param: picture
     * @return: java.lang.String
     * @author Lantech
     */
    public static String typeOf(Picture picture) {
        //获得picture中image的文件对象
        Path imagePath1 = Paths.get(picture.getImage().getUrl().replace("file:", ""));
        File file = imagePath1.toFile();
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    /**
     * @description: 用来获取Picture对象中image的大小
     * @param: picture
     * @return: java.lang.Long
     * @author Lantech
     */
    public static Long sizeOf(Picture picture) {
        //获得picture中image的文件对象
        Path imagePath1 = Paths.get(picture.getImage().getUrl().replace("file:", ""));
        File file = imagePath1.toFile();
        return file.length();
    }
}
