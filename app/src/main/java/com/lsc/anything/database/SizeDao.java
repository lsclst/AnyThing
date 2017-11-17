package com.lsc.anything.database;

import com.lsc.anything.App;
import com.lsc.anything.widget.glide.Size;

import java.sql.SQLException;

/**
 * Created by lsc on 2017/11/9 0009.
 *
 * @author lsc
 */

public class SizeDao {
    public Size getSizeById(String id) {
        try {
            return DataBaseHelper.getInstance(App.APPContext).getSizeDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveSize(Size size) {
        try {
            DataBaseHelper.getInstance(App.APPContext).getSizeDao().createIfNotExists(size);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
