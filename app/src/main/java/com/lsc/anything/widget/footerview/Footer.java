package com.lsc.anything.widget.footerview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.lsc.anything.widget.footerview.Footer.Type.TYPE_LOADING;
import static com.lsc.anything.widget.footerview.Footer.Type.TYPE_NOT_SHOW;
import static com.lsc.anything.widget.footerview.Footer.Type.TYPE_NO_MORE;

/**
 * Created by lsc on 2017/9/14 0014.
 *
 * @author lsc
 */

public class Footer {

    @IntDef({TYPE_LOADING, TYPE_NO_MORE, TYPE_NOT_SHOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        int TYPE_LOADING = 0;
        int TYPE_NO_MORE = 1;
        int TYPE_NOT_SHOW = 3;
    }

    public Footer(@Type int type) {
        this.type = type;
    }

    @Type
    private int type;

    public
    @Type
    int getType() {
        return type;
    }

    public void setType(@Type int type) {
        this.type = type;
    }
}
