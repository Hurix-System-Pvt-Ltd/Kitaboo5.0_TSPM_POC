package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import com.hurix.kitaboocloud.R;


public class CustomFontSizeSeekBar extends AppCompatSeekBar {

    private Drawable mTickMark;

    public CustomFontSizeSeekBar(Context context) {
        this(context, null);
    }

    public CustomFontSizeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.seekBarStyle);
    }

    public CustomFontSizeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttributes(attrs, defStyleAttr);
    }

    private void applyAttributes(AttributeSet rawAttrs, int defStyleAttr)
    {
        TypedArray attrs = getContext().obtainStyledAttributes(rawAttrs, R.styleable.CustomSeekBar, defStyleAttr, 0);
        try {
            mTickMark = attrs.getDrawable(R.styleable.CustomSeekBar_tickMarkFixed);
        } finally {
            attrs.recycle();
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTickMarks(canvas);
    }

    @Override
    public int getThumbOffset() {
        return super.getThumbOffset();
    }

    void drawTickMarks(Canvas canvas) {
        if (mTickMark != null) {
            final int count = getMax();
            if (count > 1) {
                final int w = mTickMark.getIntrinsicWidth();
                final int h = mTickMark.getIntrinsicHeight();
                final int halfThumbW = getThumb().getIntrinsicWidth() / 2;
                final int halfW = w >= 0 ? w / 2 : 1;
                final int halfH = h >= 0 ? h / 2 : 1;
                mTickMark.setBounds(-halfW, -halfH, halfW, halfH);
                final float spacing = (getWidth() - getPaddingLeft() - getPaddingRight() + getThumbOffset() * 2 - halfThumbW * 2) / (float) count;
                final int saveCount = canvas.save();
                canvas.translate(getPaddingLeft() - getThumbOffset() + halfThumbW, getHeight() / 2);
                for (int i = 0; i <= count; i++) {
                    if(i!=getProgress())
                        mTickMark.draw(canvas);
                    canvas.translate(spacing, 0);
                }
                canvas.restoreToCount(saveCount);
            }
        }
    }
}