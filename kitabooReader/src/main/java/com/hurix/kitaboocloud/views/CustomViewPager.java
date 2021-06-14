package com.hurix.kitaboocloud.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.hurix.kitaboocloud.R;

public class CustomViewPager extends ViewPager {
    private boolean enabled;

    public CustomViewPager(Context context) {
        super(context);
        if(getResources().getBoolean(R.bool.is_Infobase_Client)){
            this.setPagingEnabled(false);
        }else
            this.setPagingEnabled(true);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(getResources().getBoolean(R.bool.is_Infobase_Client)){
            this.setPagingEnabled(false);
        }else
            this.setPagingEnabled(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }




/*

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
       return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
       return true;
    }

    //down one is added for smooth scrolling
*/


    /*public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 *//*1 secs*//*);
        }
    }
    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}
