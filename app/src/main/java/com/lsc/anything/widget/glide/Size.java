package com.lsc.anything.widget.glide;

/**
 * Created by lsc on 2017/9/25 0025.
 *
 * @author lsc
 */

public class Size {
    private int imgHeight;
    private int imgWidth;
    private boolean isLanscape;

    public Size(int imgWidth, int imgHeight) {
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public boolean isLanscape() {
        return imgWidth * 0.9 >= imgHeight;
    }

    @Override
    public String toString() {
        return "Size{" +
                "imgHeight=" + imgHeight +
                ", imgWidth=" + imgWidth +
                '}';
    }
}
