package com.lsc.anything.entity.gank;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by lsc on 2017/9/12 0012.
 *
 * @author lsc
 */

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

    private String _id;
    private String desc;
    private String publishedAt;
    private String type;
    private String url;
    private String who;
    private boolean isLike;

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
        dest.writeByte((byte) (isLike ? 1 : 0));
    }

    public static final Creator<GankItem> CREATOR = new Creator<GankItem>() {
        @Override
        public GankItem createFromParcel(Parcel source) {
            GankItem item = new GankItem();
            String id = source.readString();
            String desc = source.readString();
            String url = source.readString();
            boolean islike = source.readByte() != 0;
            item.set_id(id);
            item.setDesc(desc);
            item.setUrl(url);
            item.setLike(islike);
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
