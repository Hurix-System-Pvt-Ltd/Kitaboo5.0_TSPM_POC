package com.hurix.kitaboo.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by vishal.singh on 5/18/2015.
 */
public class EmptyMsg extends TextView {
    String mMessage;
    Context mContext;

    public EmptyMsg(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public EmptyMsg(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public void initView(){
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setGravity(Gravity.CENTER);
        this.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
    }

    public void setData(String msg, int color) {
        mMessage = msg;
        this.setText(mMessage);
        this.setTextColor(color);
    }
}
