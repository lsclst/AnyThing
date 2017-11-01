package com.lsc.anything.database;

import android.content.Context;

import com.lsc.anything.entity.gank.DownLoadEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by lsc on 2017/10/10 0010.
 *
 * @author lsc
 */

public class DownLoadDao {


    public void add(Context context, DownLoadEntity entity) {
        try {
            DataBaseHelper.getInstance(context).getDownLoadDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeById(Context context, long id) {
        try {
            DataBaseHelper.getInstance(context).getDownLoadDao().deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DownLoadEntity getDownLoadEntityById(Context context, long id) {
        DownLoadEntity downLoadEntity = null;
        try {
            downLoadEntity = DataBaseHelper.getInstance(context).getDownLoadDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return downLoadEntity;
    }

    public DownLoadEntity getDownLoadEntityByUri(Context context, String uri) {
        DownLoadEntity entity = null;
        try {
            List<DownLoadEntity> entities = DataBaseHelper.getInstance(context).getDownLoadDao().queryForEq(DownLoadEntity.COL_N_URI, uri);
            if (entities.size() > 0) {
                entity = entities.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    public List<DownLoadEntity> getall(Context c) {
        try {
            return DataBaseHelper.getInstance(c).getDownLoadDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void close(Context context) {
        DataBaseHelper.getInstance(context).close();
    }
}
