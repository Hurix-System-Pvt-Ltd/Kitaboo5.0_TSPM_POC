package com.hurix.kitaboo.iconify;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hurix.kitaboo.constants.R;

public class IconTextView extends TextView {

    public IconTextView(Context context) {
        super(context);
        init();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (!isInEditMode())
            Iconify.addIcons(getResources().getString(R.string.kitabooreader_font_file_name), this);
        else
          this.setText(this.getText());
    }
    
    public void setIconTypeFace(String ttfFile)
    {
    	Iconify.addIcons(ttfFile, this);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(Iconify.compute(text), type);
    }

}
