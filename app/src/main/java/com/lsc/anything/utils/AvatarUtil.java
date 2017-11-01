package com.lsc.anything.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.lsc.anything.R;

/**
 * Created by lsc on 2017/9/15 0015.
 *
 * @author lsc
 */

public class AvatarUtil {

    private static final int[] AVATOR_IDS = new int[]{
            R.mipmap.bear, R.mipmap.bull, R.mipmap.chicken, R.mipmap.dog,
            R.mipmap.duck, R.mipmap.frog, R.mipmap.hippo, R.mipmap.owl,
            R.mipmap.panda, R.mipmap.seal, R.mipmap.sheep, R.mipmap.tiger
    };

    public static Drawable getAvatar(Context context) {
        int randomPos = (int) (Math.random() * 100 % 12);
        return ContextCompat.getDrawable(context, AVATOR_IDS[randomPos]);
    }
}
