package com.lsc.anything.utils;

import android.graphics.BitmapFactory;

import com.lsc.anything.api.ApiHolder;
import com.lsc.anything.database.SizeDao;
import com.lsc.anything.widget.glide.Size;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lsc on 2017/11/9 0009.
 *
 * @author lsc
 */

public class BitmapUtil {
    private static volatile BitmapUtil INSTANCE;

    public static BitmapUtil getINSTANCE() {
        synchronized (BitmapUtil.class) {
            if (INSTANCE == null) {
                synchronized (BitmapUtil.class) {
                    INSTANCE = new BitmapUtil();
                }
            }
        }
        return INSTANCE;
    }

    private SizeDao mSizeDao;

    private BitmapUtil() {
        mSizeDao = new SizeDao();
    }

    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    public Size getBitmapWHFromPath(String path) throws IOException {
        Size size = mSizeDao.getSizeById(path);
        if (size == null) {

            Request request = new Request.Builder().get().url(path).build();
            Response response = ApiHolder.getInstance().getOkHttpClient().newCall(request).execute();
            InputStream inputStream = response.body().byteStream();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
            size = new Size(options.outWidth, options.outHeight, path);
            mSizeDao.saveSize(size);
        }
        return size;
    }
}
