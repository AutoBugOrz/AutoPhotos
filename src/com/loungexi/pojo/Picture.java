package com.loungexi.pojo;

import javafx.scene.image.Image;

/**
 * @author LoungeXi
 */
public class Picture {
    private final Image image;
    private final String imageName;

    public Picture(Image image, String imageName) {
        this.image = image;
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public Image getImage() {
        return image;
    }
}
