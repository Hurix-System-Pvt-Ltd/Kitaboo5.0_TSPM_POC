package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hurix.reader.kitaboosdkrenderer.R;


public class CustomCompoundView extends FrameLayout {
    private TextView mColorPopup;
    private TextView mColorIndicator1, mColorIndicator2, mColorIndicator3, mColorIndicator4;
    private View view;
    private int mLeftMargin;
    private LinearLayout mParentLayout;

    public CustomCompoundView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CustomCompoundView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomCompoundView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CustomCompoundView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_compund_view, null);
        mParentLayout = (LinearLayout) view.findViewById(R.id.color_popup_layout);
        mColorIndicator1 = (TextView) view.findViewById(R.id.color_selected_indicator1);
        mColorIndicator2 = (TextView) view.findViewById(R.id.color_selected_indicator2);
        mColorIndicator3 = (TextView) view.findViewById(R.id.color_selected_indicator3);
        mColorIndicator4 = (TextView) view.findViewById(R.id.color_selected_indicator4);
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


        } else if (size <= 12) {
            mColorIndicator2.post(new Runnable() {
                @Override
                public void run() {
                    mColorIndicator2.setVisibility(View.VISIBLE);
                }
            });

            return mColorIndicator2;

        } else if (size <= 18) {
            mColorIndicator3.post(new Runnable() {
                @Override
                public void run() {
                    mColorIndicator3.setVisibility(View.VISIBLE);
                }
            });

            return mColorIndicator3;

        } else if (size <= 24) {
            mColorIndicator4.post(new Runnable() {
                @Override
                public void run() {
                    mColorIndicator4.setVisibility(View.VISIBLE);
                }
            });

            return mColorIndicator4;

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

        mColorIndicator2.post(new Runnable() {
            @Override
            public void run() {
                mColorIndicator2.setVisibility(View.GONE);
            }
        });

        mColorIndicator3.post(new Runnable() {
            @Override
            public void run() {
                mColorIndicator3.setVisibility(View.GONE);
            }
        });

        mColorIndicator4.post(new Runnable() {
            @Override
            public void run() {
                mColorIndicator4.setVisibility(View.GONE);
            }
        });
    }

    public View getParnet() {
        return view;
    }

    public void setLeftMargin(int leftMargin) {
        this.mLeftMargin = leftMargin;
    }

    public int getLeftMargin() {
        return this.mLeftMargin;
    }
}
