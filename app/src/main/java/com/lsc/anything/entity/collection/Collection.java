package com.lsc.anything.entity.collection;

import android.support.annotation.IntDef;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lsc.anything.entity.gank.GankItem;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lsc on 2017/10/12 0012.
 *
 * @author lsc
 */

@DatabaseTable(tableName = Collection.TABLE_NAME)
public class Collection {
    public static final int TYPE_ARTICLE = 0;
    public static final int TYPE_IMG = 1;
    public static final String COL_DATE = "date";
    public static final String COL_URL = "url";
    public static final String COL_LOCALPATH = "localPath";
    public static final String COL_DATATYPE = "datatype";
    public static final String COL_DES = "des";
    public static final String TABLE_NAME = "collection";
    public static final String COL_WHO = "who";
    @DatabaseField(columnName = COL_WHO, defaultValue = "null")
    private String who;
    @DatabaseField(columnName = "id", id = true)
    private String id;

    @DatabaseField(columnName = COL_DATE)
    private String collectionDate;

    @DatabaseField(columnName = COL_URL)
    private String url;

    @DatabaseField(columnName = COL_LOCALPATH)
    private String localPath;

    @Type
    @DatabaseField(columnName = COL_DATATYPE)
    private int type;

    @DatabaseField(columnName = COL_DES)
    private String des;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_ARTICLE, TYPE_IMG})
    public @interface Type {
    }

    @Type
    public int getType() {
        return type;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(@Type int type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Collection) {
            return this.id.equals(((Collection) obj).id);
        }
        return false;
    }

    public GankItem toGankItem() {
        GankItem item = new GankItem();
        item.setDesc(des);
        item.setUrl(url);
        return item;
    }
}
