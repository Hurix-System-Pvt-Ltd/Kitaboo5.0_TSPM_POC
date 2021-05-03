package com.hurix.reader.kitaboosdkrenderer.helpscreen;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.hurix.bookreader.views.link.CirclePageIndicator;



import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.common.HelpVo;
import com.hurix.reader.kitaboosdkrenderer.constants.Constants;
import com.hurix.reader.kitaboosdkrenderer.utils.Utils;
import com.hurix.renderer.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Amit Raj on 08-08-2019.
 */
public class HelpScreenActivity extends Activity {
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
    HelpScreenAdapter mViewPagerAdapter;
    ViewPager mViewPager;
    private static final String HELPSCREEN_TYPE = "HELPSCREEN_TYPE";
    private static final String READER = "Reader";
    private static final String REVIEW = "Review";
    private String reviewmode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);//int flag, int mask
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        FullScreencall();
        HashMap<Integer, ArrayList<HelpVo>> hashMap = (HashMap<Integer, ArrayList<HelpVo>>)intent.getSerializableExtra("coll");
        if(intent.getStringExtra(HELPSCREEN_TYPE).equalsIgnoreCase(REVIEW) && !Utility.isDeviceTypeMobile(this)){
            setContentView(R.layout.activity_help_screen_review);
        }else {
            setContentView(R.layout.activity_help_screen);
        }

        int currentOrientation = getResources().getConfiguration().orientation;
       /* if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }*/
        mParetnView = (RelativeLayout) findViewById(R.id.parent_container);

        if(intent.getStringExtra(HELPSCREEN_TYPE).equalsIgnoreCase(READER)){
            mViewPagerAdapter = new HelpScreenAdapter(this,hashMap,true,false);
           Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_REVIEW, true);
            reviewmode=intent.getStringExtra(HELPSCREEN_TYPE);
        }
        else if( intent.getStringExtra(HELPSCREEN_TYPE).equalsIgnoreCase(REVIEW)){
            reviewmode=intent.getStringExtra(HELPSCREEN_TYPE);
                    mViewPagerAdapter = new HelpScreenAdapter(this,hashMap,false,true);
        }
        else
            mViewPagerAdapter = new HelpScreenAdapter(this,hashMap,false,false);
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
    public void onBackPressed() {
        Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED, false);
        //com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_READER, false);
        if(reviewmode.equalsIgnoreCase(REVIEW)){
            if(Utils.getSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_REVIEW, false))
            Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_REVIEW, false);
        }else if(reviewmode.equalsIgnoreCase(READER)){
            Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_REVIEW, true);
           Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_READER, false);
        }
        Intent data = new Intent();
        data.putExtra("closeMainLayout", true);
        setResult(Activity.RESULT_OK,data);
        finish();

       // super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        FullScreencall();
        finish();
        super.onConfigurationChanged(newConfig);
    }

    public void FullScreencall() {
        if (Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
