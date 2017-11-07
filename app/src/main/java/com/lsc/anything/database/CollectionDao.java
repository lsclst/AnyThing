package com.lsc.anything.database;

import android.content.Context;

import com.lsc.anything.entity.gank.GankItem;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by lsc on 2017/10/12 0012.
 *
 * @author lsc
 */

public class CollectionDao {
    public void save(Context context, GankItem collection) {
        try {
            DataBaseHelper.getInstance(context.getApplicationContext()).getCollectionsDao()
                    .createOrUpdate(collection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public GankItem getCollectionById(Context c, String id) {
        GankItem collection = null;
        try {
            collection = DataBaseHelper.getInstance(c.getApplicationContext()).getCollectionsDao()
                    .queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return collection;
    }

    public List<GankItem> getAllArticle(Context c) {
        try {
            return DataBaseHelper.getInstance(c.getApplicationContext()).getCollectionsDao()
                    .queryForEq(GankItem.COL_DATATYPE, GankItem.TYPE_ARTICLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<GankItem> getAllImage(Context c) {
        try {
            return DataBaseHelper.getInstance(c).getCollectionsDao()
                    .queryForEq(GankItem.COL_DATATYPE, GankItem.TYPE_IMG);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int deleteCollectionById(Context c, String id) {
        int deleteId = -1;
        try {
            deleteId = DataBaseHelper.getInstance(c).getCollectionsDao().deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deleteId;
    }

    public void deleteCollections(Context c,List<GankItem> collections){
        try {
            DataBaseHelper.getInstance(c).getCollectionsDao().delete(collections);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll(Context context) {
        DataBaseHelper.getInstance(context)
                .getWritableDatabase()
                .delete(GankItem.TABLE_NAME, null, null);
    }
}
