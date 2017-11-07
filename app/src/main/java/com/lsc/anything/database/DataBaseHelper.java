package com.lsc.anything.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lsc.anything.entity.gank.DownLoadEntity;
import com.lsc.anything.entity.gank.GankItem;

import java.sql.SQLException;

/**
 * Created by lsc on 2017/10/10 0010.
 *
 * @author lsc
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "gank.db";
    private static final int DB_VERSION = 1;

    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, DownLoadEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, GankItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

    private static volatile DataBaseHelper instance;

    public static DataBaseHelper getInstance(Context context) {

        synchronized (DataBaseHelper.class) {
            if (instance == null) {
                synchronized (DataBaseHelper.class) {
                    instance = new DataBaseHelper(context);
                }

            }
            return instance;
        }
    }

    public void close() {
        super.close();
        mDownLoadDao = null;
    }

    private static Dao<DownLoadEntity, Long> mDownLoadDao;

    public Dao<DownLoadEntity, Long> getDownLoadDao() throws SQLException {
        if (mDownLoadDao == null) {
            mDownLoadDao = getDao(DownLoadEntity.class);
        }
        return mDownLoadDao;
    }

    private static Dao<GankItem, String> mCollectionsDao;

    public Dao<GankItem, String> getCollectionsDao() throws SQLException {
        if (mCollectionsDao == null) {
            mCollectionsDao = getDao(GankItem.class);
        }
        return mCollectionsDao;
    }
}
