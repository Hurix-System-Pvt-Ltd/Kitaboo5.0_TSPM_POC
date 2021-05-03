package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hurix.reader.kitaboosdkrenderer.R;


public class HighlightCheckboxView extends LinearLayout {

    public HighlightCheckboxView(Context context) {
        super(context);
        initView();
    }

    public HighlightCheckboxView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HighlightCheckboxView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public HighlightCheckboxView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.highlight_checkbox_view , null);
    }


}
