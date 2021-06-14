package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.hurix.customui.bookmark.ClearableEditText;
import com.hurix.customui.bookmark.TextWatcherAdapter;
import com.hurix.customui.iconify.IconDrawable;

public class CustomClearableEditText extends EditText implements View.OnTouchListener,
        View.OnFocusChangeListener, TextWatcherAdapter.TextWatcherListener {

    public interface Listener {
        void didClearText();
    }

    public void setListener(ClearableEditText.Listener listener) {
        this.listener = listener;
    }

    private Drawable xD;
    private ClearableEditText.Listener listener;

    public CustomClearableEditText(Context context) {
        super(context);
        init();
    }

    public CustomClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    private OnTouchListener l;
    private OnFocusChangeListener f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                    .getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (listener != null) {
                        listener.didClearText();
                    }
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(!TextUtils.isEmpty(getText().toString().trim()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        if (isFocused()) {
            setClearIconVisible(!TextUtils.isEmpty(text.trim()));
        }
    }

    private void init() {
        xD = getCompoundDrawables()[2];
        if (xD == null) {

            IconDrawable closeDrawable = new IconDrawable(getContext(),
                    CustomPlayerUIConstants.CLOSE_BOOKMARK, com.hurix.commons.utils.Utils.getFontFilePath());
            closeDrawable.setAlpha(70);
            closeDrawable.sizeDp(25);

            xD = closeDrawable;
        }
        xD.setBounds(0, 0, xD.getIntrinsicWidth()-10, xD.getIntrinsicHeight()-10);
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcherAdapter(this, this));
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable x = visible ? xD : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        return false;
    }
}