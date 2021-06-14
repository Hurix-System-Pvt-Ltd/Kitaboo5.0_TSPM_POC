package com.hurix.kitaboo.views;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.hurix.kitaboo.constants.PlayerUIConstants;
import com.hurix.kitaboo.constants.R;
import com.hurix.kitaboo.iconify.IconDrawable;

public class KitabooSearchView extends SearchView {
    Context _context;
    private int maxLength = 0;

    public KitabooSearchView(Context context) {
        super(context);
        _context = context;
        setIconifiedByDefault(false);
    }

    public KitabooSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        setIconifiedByDefault(false);
    }

    public void setMaxLength(int length){
        this.maxLength = length;
    }

    public void setSearchViewIcon(int iconColor) {
        IconDrawable search = new IconDrawable(_context, PlayerUIConstants.TB_SEARCH_IC_TEXT,
                getResources().getString(R.string.kitabooreader_font_file_name));
        search.actionBarSize().color(iconColor);
        int searchButtonId = this.getContext().getResources().getIdentifier("android:id/search_mag_icon",
                null, null);
        ImageView searchBtn = (ImageView) this.findViewById(searchButtonId);
        searchBtn.setImageDrawable(search);
    }

    public void setUpSearchPlate(String searchHint, int backgroundColor, int hintTextColor, int searchTextColor) {
        int searchPlateId = this.getContext().getResources().getIdentifier("android:id/search_plate",
                null, null);
        View searchPlate = this.findViewById(searchPlateId);

        if (searchPlate != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                searchPlate.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                searchPlate.setBackgroundColor(Color.parseColor("#f2f2f2"));
            }
            searchPlate.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_ATOP);
            int searchTextId = searchPlate.getContext().getResources()
                    .getIdentifier("android:id/search_src_text", null, null);
            final TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText != null) {
                try {
                    Class<?> clazz = Class.forName("android.widget.SearchView$SearchAutoComplete");
                    SpannableStringBuilder stopHint = new SpannableStringBuilder("");
                    stopHint.append(searchHint);

                    Method setHintMethod = clazz.getMethod("setHint", CharSequence.class);
                    setHintMethod.invoke(searchText, stopHint);
                    Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(searchText, R.drawable.cursor);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                searchText.setTextColor(searchTextColor);
                searchText.setHintTextColor(hintTextColor);
                searchText.setFilters(new InputFilter[]{new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        return null;
                    }
                },new InputFilter.LengthFilter(maxLength)});
            }
        }
    }

    public void setCloseIcon(int iconColor, boolean isVisible) {
        int searchCloseBtnId = this.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchCloseBtn = (ImageView) this.findViewById(searchCloseBtnId);
        if (searchCloseBtn != null) {
            if (isVisible) {
                searchCloseBtn.setImageDrawable(new IconDrawable(_context,
                        PlayerUIConstants.SEARCHVIEW_CLOSE_IC_TEXT, getResources()
                        .getString(R.string.kitabooreader_font_file_name)).color(iconColor));
                searchCloseBtn.setAlpha(1.0f);

            } else {
                searchCloseBtn.setVisibility(GONE);
            }
        }
    }

    public void setSearchBackIcon(int iconColor) {
        int searchCloseBtnId = this.getContext().getResources()
                .getIdentifier("android:id/search_back_btn", null, null);
        ImageView searchCloseBtn = (ImageView) this.findViewById(searchCloseBtnId);
        if (searchCloseBtn != null) {
            searchCloseBtn.setImageDrawable(new IconDrawable(_context,
                    PlayerUIConstants.SEARCHVIEW_BACK_IC_TEXT, getResources()
                    .getString(R.string.kitabooreader_font_file_name)).color(iconColor));
            searchCloseBtn.setAlpha(1.0f);

        }
    }
}
