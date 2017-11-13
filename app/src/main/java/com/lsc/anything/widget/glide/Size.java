package com.lsc.anything.widget.glide;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by lsc on 2017/9/25 0025.
 *
 * @author lsc
 */
@DatabaseTable(tableName = Size.TB_NAME)
public class Size implements Parcelable {
    //database
    public static final String TB_NAME = "imgSize";
    public static final String COL_NAME_IMG_H = "imgHeight";
    public static final String COL_NAME_IMG_W = "imgWidth";
    public static final String COL_NAME_IMG_ID = "_id";
    @DatabaseField(columnName = COL_NAME_IMG_H)
    private int imgHeight;
    @DatabaseField(columnName = COL_NAME_IMG_W)
    private int imgWidth;
    @DatabaseField(id = true, columnName = COL_NAME_IMG_ID)
    private String id;
    private boolean isLanscape;

    public Size(String id, int imgWidth, int imgHeight, String imgPath) {
        this.id = id;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
    }

    protected Size(Parcel in) {
        id = in.readString();
        imgHeight = in.readInt();
        imgWidth = in.readInt();
        isLanscape = in.readByte() != 0;
    }

    public Size() {
    }

    public static final Creator<Size> CREATOR = new Creator<Size>() {
        @Override
        public Size createFromParcel(Parcel in) {
            return new Size(in);
        }

        @Override
        public Size[] newArray(int size) {
            return new Size[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(imgHeight);
        dest.writeInt(imgWidth);
        dest.writeByte((byte) (isLanscape ? 1 : 0));
    }
}
