package com.lsc.anything.utils;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lsc.anything.R;
import com.lsc.anything.database.DownLoadDao;
import com.lsc.anything.entity.gank.DownLoadEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lsc on 2017/10/9 0009.
 *
 * @author lsc
 */

public class DownLoadUtil {
    private static final String TAG = DownLoadUtil.class.getSimpleName();
    private static final String PIC_FOLDER = "gank";
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + Environment.DIRECTORY_PICTURES;
    public static final String IMAGE_FOLDER = BASE_PATH + File.separator + PIC_FOLDER + File.separator;

    public static final int PIC_OP_TYPE_DL = 0;
    public static final int PIC_OP_TYPE_SWP = 1;
    public static final int PIC_OP_TYPE_S = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PIC_OP_TYPE_DL, PIC_OP_TYPE_S, PIC_OP_TYPE_SWP})
    public @interface PIC_OP_TYPE {
    }

    private DownLoadUtil() {

    }

    private static volatile DownLoadUtil instance;

    public static DownLoadUtil getInstance() {
        synchronized (DownLoadUtil.class) {
            if (instance == null) {
                synchronized (DownLoadUtil.class) {
                    instance = new DownLoadUtil();
                }
            }
        }
        return instance;
    }

    private void downLoad(Context context, String uri, String fileName, @PIC_OP_TYPE int type) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            ToastUtil.showErrorMsg("外部存储未挂载");
            return;
        }
        DownLoadDao dao = new DownLoadDao();
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
        request.setDescription(context.getString(R.string.downloading));
        request.setAllowedOverRoaming(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.allowScanningByMediaScanner();
        request.setMimeType("image/*");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES + File.separator + PIC_FOLDER, fileName);
        if (type == PIC_OP_TYPE_DL) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        } else {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
        long id = downloadManager.enqueue(request);

        DownLoadEntity entity = new DownLoadEntity(id, type);
        entity.setUri(uri);
        entity.setOp_type(type);
        entity.setLocalPath(BASE_PATH + File.separator + PIC_FOLDER + File.separator + fileName);
        Log.d(TAG, "downLoad: " + entity.toString());
        dao.add(context, entity);


    }

    public void setWallPaper(Context context, String uri, String fileName) {
        if (!isFileExist(fileName)) {
            downLoad(context, uri, fileName, PIC_OP_TYPE_SWP);
        } else {
            ToastUtil.showMsg(context.getString(R.string.pic_is_existed));
            downLoadFinished(context, PIC_OP_TYPE_SWP, BASE_PATH + File.separator + PIC_FOLDER + File.separator + fileName);
        }
    }

    public void downloadPic(Context context, String uri, String fileName) {
        if (!isFileExist(fileName)) {
            downLoad(context, uri, fileName, PIC_OP_TYPE_DL);
        } else {
            downLoadFinished(context, PIC_OP_TYPE_DL, BASE_PATH + File.separator + PIC_FOLDER + File.separator + fileName);
        }
    }

    public void sharePic(Context c, String uri, String fileName) {
        if (!isFileExist(fileName)) {
            downLoad(c, uri, fileName, PIC_OP_TYPE_S);
        } else {
            downLoadFinished(c, PIC_OP_TYPE_S, BASE_PATH + File.separator + PIC_FOLDER + File.separator + fileName);
        }
    }

    public void downLoadFinished(final Context c, @PIC_OP_TYPE int type, String localPath) {

        if (type == PIC_OP_TYPE_DL) {
            ToastUtil.showSuccessMsg(c.getString(R.string.download_finished));
        } else if (type == PIC_OP_TYPE_S) {
            ShareUtil.SharePic(c, c.getString(R.string.flower), Uri.fromFile(new File(localPath)));
        } else if (type == PIC_OP_TYPE_SWP) {
            Glide.with(c).fromFile().asBitmap().load(new File(localPath)).override(DensityUtil.getScreenWidth(c), DensityUtil.getScreenHeight(c))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            try {
                                WallpaperManager.getInstance(c).setBitmap(resource);
                                ToastUtil.showSuccessMsg("set wallpaper success");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }
    }

    private boolean isFileExist(String fileName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File fileFolder = new File(BASE_PATH + File.separator + PIC_FOLDER);
            if (!fileFolder.exists()) {
                fileFolder.mkdirs();
            }
            File file = new File(BASE_PATH + File.separator + PIC_FOLDER + File.separator + fileName);
            return file.exists();
        }
        return false;
    }

    public Uri filePathToUri(Context context, @NonNull String filePath) {

        Uri uri = null;
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        try {
            String url = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    filePath, fileName, null);
            if (!TextUtils.isEmpty(url)) {
                uri = Uri.parse(url);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return uri;
    }
}
