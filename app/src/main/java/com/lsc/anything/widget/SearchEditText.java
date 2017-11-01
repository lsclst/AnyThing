package com.lsc.anything.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lsc.anything.App;
import com.lsc.anything.R;
import com.lsc.anything.utils.KeyBoardUtil;

/**
 * Created by lsc on 2017/10/19 0019.
 *
 * @author lsc
 */

public class SearchEditText extends AppCompatEditText implements TextWatcher {
    public static final String TAG = SearchEditText.class.getSimpleName();
    private Drawable mClearDrawable = null;
    private boolean isClearDrawableVisible;
    private int[] mClearDrawablePos = new int[4];
    private onClearClickListener mOnClearClickListener;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            if (!isClearDrawableVisible)
                showClearDrawable();
        } else {
            if (isClearDrawableVisible) {

                hideClearDrawable();
            }

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface onClearClickListener {
        void onClear();
    }

    public void setOnClearClickListener(onClearClickListener onClearClickListener) {
        mOnClearClickListener = onClearClickListener;
    }

    public SearchEditText(Context context) {
        super(context);
        init(context);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.ic_delete);
        mClearDrawable = DrawableCompat.wrap(drawable);
        final int[] clearDrawableSize = new int[2];
        clearDrawableSize[0] = mClearDrawable.getIntrinsicWidth();
        clearDrawableSize[1] = mClearDrawable.getIntrinsicHeight();
        mClearDrawable.setBounds(0, 0, clearDrawableSize[0], clearDrawableSize[1]);

        post(new Runnable() {
            @Override
            public void run() {
                //left
                mClearDrawablePos[0] = getWidth() - getPaddingRight() - clearDrawableSize[0] - 30;
                //top
                mClearDrawablePos[1] = getPaddingTop();
                //right
                mClearDrawablePos[2] = getWidth() - getPaddingRight();
                //bottom
                mClearDrawablePos[3] = getHeight();
            }
        });


        addTextChangedListener(this);
    }

    private void showClearDrawable() {
        if (mClearDrawable != null) {
            mClearDrawable.clearColorFilter();
            setCompoundDrawables(null, null, mClearDrawable, null);
            isClearDrawableVisible = true;
        }
    }

    private void hideClearDrawable() {
        if (mClearDrawable != null) {
            mClearDrawable.clearColorFilter();
            setCompoundDrawables(null, null, null, null);
            isClearDrawableVisible = false;
        }
    }

    private void updateDrawableInTouch() {
        if (isClearDrawableVisible) {
            Drawable[] drawables = getCompoundDrawables();
            drawables[2].setColorFilter(ContextCompat.getColor(getContext(), R.color.alert_red), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private boolean isInTargetArea(int x, int y) {
        boolean inTarget = false;
        if (x >= mClearDrawablePos[0] && x <= mClearDrawablePos[2]
                && y > mClearDrawablePos[1] && y <= mClearDrawablePos[3]) {
            inTarget = true;
        }
        return inTarget;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInTargetArea(x, y) && isClearDrawableVisible) {
                    updateDrawableInTouch();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInTargetArea(x, y) && isClearDrawableVisible) {
                    updateDrawableInTouch();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isClearDrawableVisible && isInTargetArea(x, y)) {
                    hideClearDrawable();
                    setText("");
                    if (mOnClearClickListener != null) {
                        mOnClearClickListener.onClear();
                    }
                    return true;
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            KeyBoardUtil.showKeyBoard(App.APPContext, this);
        } else {
            KeyBoardUtil.closeKeyBoard(App.APPContext, this);
        }
    }


}
