package com.hurix.kitaboo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by pratibha.chandanavar on 09-06-2015.
 */
public class KEditText extends EditText {
    public KEditText(Context context) {
        super(context);
    }

    public KEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
