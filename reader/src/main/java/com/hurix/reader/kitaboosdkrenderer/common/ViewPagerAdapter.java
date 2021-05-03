package com.hurix.reader.kitaboosdkrenderer.common;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapter extends PagerAdapter {

    Context _context;
    private String[] _content;


    public ViewPagerAdapter(Context context, String[] content) {
        this._context = context;
        this._content = content;
    }

    @Override
    public int getCount() {
        return _content.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    @Override
    public void destroyItem(View collection, int position, Object object) {
        ViewGroup parent = (ViewGroup) collection;
        parent.removeView((View) object);
    }

    public View instantiateItem(View collection, int position) {
        ImageView view = null;
        try {

            WindowManager wm = (WindowManager) collection.getContext().getSystemService(Context.WINDOW_SERVICE);

            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);

            view = new ImageView(_context);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            view.setScaleType(ScaleType.FIT_CENTER);
            int resID = _context.getResources().getIdentifier(_content[position], "drawable", _context.getPackageName());
            //mContext.getResources().getDrawable(resID);
            if (resID != 0) {
                view.setBackgroundDrawable(_context.getResources().getDrawable(resID));
                ((ViewPager) collection).addView(view, 0);
            }


        } catch (IndexOutOfBoundsException e) {

        }
        return view;
    }

}
