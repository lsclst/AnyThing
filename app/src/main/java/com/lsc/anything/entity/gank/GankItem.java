package com.lsc.anything.entity.gank;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by lsc on 2017/9/12 0012.
 *
 * @author lsc
 */
@DatabaseTable(tableName = GankItem.TABLE_NAME)
public class GankItem implements Parcelable {

    /**
     * _id : 59b667cf421aa9118887ac12
     * createdAt : 2017-09-11T18:39:11.631Z
     * desc : 用少量Rxjava代码，为retrofit添加退避重试功能
     * publishedAt : 2017-09-12T08:15:08.26Z
     * source : web
     * type : Android
     * url : http://www.jianshu.com/p/fca90d0da2b5
     * used : true
     * who : 小鄧子
     * images : ["http://img.gank.io/3b0b193d-6abf-4714-9d5a-5508404666f4"]
     */
    public static final int TYPE_ARTICLE = 0;
    public static final int TYPE_IMG = 1;
    public static final String COL_DATE = "date";
    public static final String COL_URL = "url";
    public static final String COL_LOCALPATH = "localPath";
    public static final String COL_DATATYPE = "datatype";
    public static final String COL_DES = "des";
    public static final String TABLE_NAME = "collection";
    public static final String COL_WHO = "who";

    @DatabaseField(columnName = "id", id = true)
    private String _id;
    @DatabaseField(columnName = COL_DES)
    private String desc;
    @DatabaseField(columnName = COL_DATE)
    private String publishedAt;
    @DatabaseField(columnName = COL_DATATYPE)
    private int saveType;
    private String type;
    @DatabaseField(columnName = COL_URL)
    private String url;
    @DatabaseField(columnName = COL_WHO)
    private String who;
    @DatabaseField(columnName = COL_LOCALPATH)
    private String localPath;
    private boolean isLike;

    public int getSaveType() {
        return saveType;
    }

    public void setSaveType(int saveType) {
        this.saveType = saveType;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getWho() {
        return TextUtils.isEmpty(who) ? "null" : who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getFileName() {
        String suffix = getsuffix();
        return _id + suffix;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(desc);
        dest.writeString(url);
        dest.writeString(publishedAt);
        dest.writeByte((byte) (isLike ? 1 : 0));
    }

    public static final Creator<GankItem> CREATOR = new Creator<GankItem>() {
        @Override
        public GankItem createFromParcel(Parcel source) {
            GankItem item = new GankItem();
            String id = source.readString();
            String desc = source.readString();
            String url = source.readString();
            String date = source.readString();
            boolean islike = source.readByte() != 0;
            item.set_id(id);
            item.setDesc(desc);
            item.setUrl(url);
            item.setLike(islike);
            item.setPublishedAt(date);
            return item;
        }

        @Override
        public GankItem[] newArray(int size) {
            return new GankItem[size];
        }
    };

    private String getsuffix() {
        if (!TextUtils.isEmpty(url)) {
            int lastIndex = url.lastIndexOf(".");
            return url.substring(lastIndex);

        }
        return ".jpg";
    }
}
