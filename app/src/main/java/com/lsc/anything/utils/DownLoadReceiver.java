package com.lsc.anything.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lsc.anything.database.DownLoadDao;
import com.lsc.anything.entity.gank.DownLoadEntity;

/**
 * Created by lsc on 2017/10/10 0010.
 *
 * @author lsc
 */

public class DownLoadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                long entityid = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                DownLoadDao dao = new DownLoadDao();
                DownLoadEntity entity = dao.getDownLoadEntityById(context, entityid);
                int type = entity.getOp_type();
                Log.d("lsc", "onReceive: entity = " + entity.toString());
                DownLoadUtil.getInstance().downLoadFinished(context, type, entity.getLocalPath());
                break;

        }
    }
}
