package com.lsc.anything.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.lsc.anything.R;

/**
 * Created by lsc on 2017/9/18 0018.
 *
 * @author lsc
 */

public class ShareUtil {
    public static void ShareNormalText(Context context, String msg) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, msg);
        Intent share = Intent.createChooser(i, context.getString(R.string.share_title));
        context.startActivity(share);
    }

    static void SharePic(Context context, String title, Uri uri) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_STREAM, uri);
        i.setType("image/*");
        i.addCategory(Intent.CATEGORY_DEFAULT);
        Intent share = Intent.createChooser(i, context.getString(R.string.share_title));
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(share);
    }
}
