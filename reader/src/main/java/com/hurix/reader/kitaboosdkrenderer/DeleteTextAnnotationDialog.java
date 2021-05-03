package com.hurix.reader.kitaboosdkrenderer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DeleteTextAnnotationDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private LinearLayout mCancelButton;
    private TextView mDeleteButton , mChildCancelButton;
    private AddTextAnnotationDeletePopupCallback mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hurix.epubreader.R.layout.delete_annotation_popup);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.2f;

        mCancelButton = (LinearLayout) findViewById(com.hurix.epubreader.R.id.cancel_layout);
        mDeleteButton = (TextView) findViewById(com.hurix.epubreader.R.id.delete_button);
        mChildCancelButton = (TextView) findViewById(com.hurix.epubreader.R.id.cancel_button);

        mDeleteButton.setOnClickListener(this);
        mChildCancelButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }


    public DeleteTextAnnotationDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public DeleteTextAnnotationDialog(@NonNull Context context, int themeResId, Context mContext) {
        super(context, themeResId);
        this.mContext = mContext;
    }

    public DeleteTextAnnotationDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, Context mContext) {
        super(context, cancelable, cancelListener);
        this.mContext = mContext;
    }


    private void calldelete(View v){
        if (mListener != null) {
            mListener.onTextAnnotationDeleteClicked();
        }
    }

    private void callCancel(View v){
        if (mListener != null) {
            mListener.onTextAnnotationCancelClicked();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mCancelButton || v == mChildCancelButton) {
            if (mListener != null) {
                mListener.onTextAnnotationCancelClicked();
            }

        } else if (v == mDeleteButton) {
            if (mListener != null) {
                mListener.onTextAnnotationDeleteClicked();
            }
        }
    }

    public void setDeletePopupListener(AddTextAnnotationDeletePopupCallback listener) {
        this.mListener = listener;
    }

    public interface AddTextAnnotationDeletePopupCallback {
        void onTextAnnotationDeleteClicked();

        void onTextAnnotationCancelClicked();
    }
}
