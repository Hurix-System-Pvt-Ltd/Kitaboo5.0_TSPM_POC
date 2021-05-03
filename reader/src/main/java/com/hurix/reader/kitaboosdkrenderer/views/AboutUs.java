package com.hurix.reader.kitaboosdkrenderer.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.constants.Constants;
import com.hurix.reader.kitaboosdkrenderer.constants.ShelfUIConstants;
import com.hurix.reader.kitaboosdkrenderer.iconify.Typefaces;
import com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager;
import com.hurix.reader.kitaboosdkrenderer.utils.Utils;

import java.util.Locale;

/**
 * Created by seshasri.seshasri on 7/22/2016.
 */

public class AboutUs extends Activity implements View.OnClickListener {

    private TextView mKitabooLogo,mTxtVersion, mCopyrights, mPowerByTxt, mProdectName, mBack;
    private ImageView mImageLogo;
    Locale mLocale;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.about_us);
        initview();
    }

    @SuppressLint("WrongConstant")
    private void initview() {
        Typeface typefaces = Typefaces.get(this, getResources().getString(R.string.kitaboo_bookshelf_font_file_name));
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.parentlayout);
        mKitabooLogo = (TextView) findViewById(R.id.kitabooLogo);
        mCopyrights = (TextView) findViewById(R.id.copyrights);
        mTxtVersion = (TextView)findViewById(R.id.version);
        mPowerByTxt = (TextView) findViewById(R.id.tvPowerByID);
        mProdectName = (TextView) findViewById(R.id.prodectName);
        mImageLogo = (ImageView) findViewById(R.id.logo_image);
        mBack = (TextView) findViewById(R.id.back);
        mBack.setTypeface(typefaces);
        mBack.setAllCaps(false);
        mBack.setText(ShelfUIConstants.SHELF_SEARCH_BACK_ENABLE_TEXT);
        mBack.setOnClickListener(this);
        if(getResources().getBoolean(R.bool.is_AAO)){
            mBack.setVisibility(View.VISIBLE);
        }else {
            mBack.setVisibility(View.GONE);
        }

        if(!getResources().getBoolean(R.bool.is_ACEP_client)) {
            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{Color.parseColor(GlobalDataManager.getInstance().getTheme().getAboutUs().getGradientColor().getColor()),
                    Color.parseColor(GlobalDataManager.getInstance().getTheme().getAboutUs().getBackground()), Color.parseColor(GlobalDataManager.getInstance().getTheme().getAboutUs().getGradientColor().getColor())});
            gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);
            gradientDrawable.setGradientRadius(200);
            layout.setBackground(gradientDrawable);
        }

        //layout1.setBackgroundColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getAboutUs().getBackground()));
        if (getResources().getBoolean(R.bool.is_it_worldbook)) {
            mCopyrights.setText(getResources().getText(R.string.copy_rights_worldbook));
        } else {
            mCopyrights.setText(getResources().getText(R.string.copy_rights));
        }
        mTxtVersion.setText("Version " +( this.getResources().getString(R.string.app_version_name)));
        Typeface typeFace = Typefaces.get(this, this.getResources()
                .getString(R.string.kitabooreader_font_file_name));
        mKitabooLogo.setTypeface(typeFace);
        setRepeatableBitmapI();
        setThemeColor();
        if(getResources().getBoolean(R.bool.is_nanoq_greenland)) {
            setLocale(Utils.getSharedPreferenceStringValue(
                    AboutUs.this, Constants.SHELF_PREFS_NAME, Constants.DEFAULT_APP_LANGUAGE, ""));
        }

    }

    private void setThemeColor() {

        mKitabooLogo.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getAboutUs().getIconsColor()));
        mCopyrights.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getAboutUs().getTextColor()));
        mTxtVersion.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getAboutUs().getTextColor()));
        mPowerByTxt.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getAboutUs().getTextColor()));
        mProdectName.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getAboutUs().getTextColor()));
        mBack.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().
                getProfile().getProfileEdit().getProfileBorder()));

    }

    private void setLocale(String localeName) {
        mLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = mLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
        Utils.insertSharedPrefernceStringValues(this,
                Constants.SHELF_PREFS_NAME, Constants.DEFAULT_APP_LANGUAGE, localeName);
    }


    private void setRepeatableBitmapI() {
//        BitmapDrawable drawable = com.hurix.reader.kitaboosdkrenderer.sdkUtils.Utils.getRepeatableBitmap(AboutUs.this,R.raw.icon_two,10);
//        findViewById(R.id.svg_holder).setBackgroundDrawable(drawable);
    }

    @Override
    public void onClick(View v) {

        if( v == mBack){
            finish();
        }
    }
}

