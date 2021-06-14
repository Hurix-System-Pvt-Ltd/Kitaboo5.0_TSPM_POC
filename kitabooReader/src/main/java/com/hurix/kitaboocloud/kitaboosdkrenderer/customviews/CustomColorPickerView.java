package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hurix.commons.iconify.Typefaces;
import com.hurix.kitaboocloud.R;


public class CustomColorPickerView extends FrameLayout {
    private TextView mColorPopup;
    private TextView mColorIndicator1;
    private View view;
    private int mLeftMargin;
    private LinearLayout mParentLayout;
    private Typefaces typefaces;

    public CustomColorPickerView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CustomColorPickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomColorPickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CustomColorPickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.color_picker, null);
      //  mParentLayout = (LinearLayout) view.findViewById(R.id.color_popup_layout);
        mColorIndicator1 = (TextView) view.findViewById(R.id.color_selected_indicator1);
        addView(view);
    }

    public TextView getColorPopupText() {
        return (TextView) view.findViewById(R.id.btn_color_popup);
    }

    public TextView getColorIndicator(int size) {
        setAllViewInvisible();
        return getIndicator(size);
    }


    private TextView getIndicator(int size) {
        TextView view = new TextView(getContext());
        if (size <= 6) {
            mColorIndicator1.post(new Runnable() {
                @Override
                public void run() {
                    mColorIndicator1.setVisibility(View.VISIBLE);

                }
            });
            return mColorIndicator1;


        }
        return view;
    }

    private void setAllViewInvisible() {
        mColorIndicator1.post(new Runnable() {
            @Override
            public void run() {
                mColorIndicator1.setVisibility(View.GONE);
            }
        });
    }

    public View getmParent() {
        return view;
    }

    public void setLeftMargin(int leftMargin) {
        this.mLeftMargin = leftMargin;
    }

    public int getLeftMargin() {
        return this.mLeftMargin;
    }

    public TextView getmColorIndicator1() {
        return mColorIndicator1;
    }

   /* public LinearLayout getmParentLayout() {
        return mParentLayout;
    }*/
}
