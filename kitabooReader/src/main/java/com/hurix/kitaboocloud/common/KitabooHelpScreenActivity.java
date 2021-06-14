package com.hurix.kitaboocloud.common;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.hurix.kitaboocloud.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by harish.edara on 4/1/2016.
 */
public class KitabooHelpScreenActivity extends Activity {
    private ArrayList<HelpVo> mViewColl;
    private ArrayList<HelpVo> mNewViewColl;
    private Typeface typeFace;
    private RelativeLayout mParetnView;
    //private ImageView imgView;
    private Bitmap bitmap;
    private Canvas canvas;
    private int lineHeight = 250;
    int i = 0;
    CirclePageIndicator mIndicator;
    HelpViewPagerAdapter mViewPagerAdapter;
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        com.hurix.epubreader.Utility.Utils.setSecureWindowFlags(KitabooHelpScreenActivity.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_help_screen);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        mParetnView = (RelativeLayout) findViewById(R.id.parent_container);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        HashMap<Integer, ArrayList<HelpVo>> hashMap = (HashMap<Integer, ArrayList<HelpVo>>)intent.getSerializableExtra("coll");
        mViewPagerAdapter = new HelpViewPagerAdapter(this,hashMap);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mViewPagerAdapter);
        if(hashMap.size() > 1) {
            mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
            mIndicator.setVisibility(View.VISIBLE);
            mIndicator.setRadius(8.0f);
            mIndicator.setViewPager(mViewPager);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
