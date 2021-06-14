package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hurix.commons.KitabooSDKModel;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.interfaces.IEpubSettingPanelListner;

import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.firebase.FirebaseAnalyticsEvents;
import com.hurix.kitaboocloud.firebase.FirebaseConstants;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.epubreader.PrefActivity;
import com.hurix.epubreader.Utility.Utils;


public class ReaderSettingTab extends FrameLayout implements View.OnClickListener {
    private boolean isMobile;
    private ReaderThemeSettingVo mReaderThemeSettingVo;
    private Context mContext = null;
    private int mResId = 0;
    private IEpubSettingPanelListner epubSettingPanelListner = null;
    private TextView tv_night_mode, tv_day_mode, tv_sepia_mode,tv_noto_mode, tv_sans_mode, tv_georgia_mode;
    private SwitchCompat switch_scroll_mode;
    private TextView tv_scroll_view_txt;
    private TextView tv_mode_txt,font_mode_txt, mResetButton;
    private LinearLayout ll_mode_panel, ll_reader_main_panel, mScrollLayout,ff_mode_panel;
    private Button switch_btn_left, switch_btn_right;
    private ToggleButton toggle;
    private boolean isVerticalMode;

    public ReaderSettingTab(@NonNull Context context) {
        super(context);
    }

    public ReaderSettingTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReaderSettingTab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReaderSettingTab(Context context, int resID) {
        super(context);
        mContext = context;
        mResId = resID;
    }


    public ReaderSettingTab(Context context, IEpubSettingPanelListner listner, ReaderThemeSettingVo readerThemeSettingVo, boolean _isMobile) {
        super(context);
        isMobile = _isMobile;
        epubSettingPanelListner = listner;
        mReaderThemeSettingVo = readerThemeSettingVo;
        mContext = context;
        initializeContext(context, R.layout.reader_settings_panel, readerThemeSettingVo);

    }

    public void initializeContext(Context context, int resID, ReaderThemeSettingVo readerThemeSettingVo) {
        View view = LayoutInflater.from(context).inflate(resID, this, true);
        initView(view, readerThemeSettingVo);
    }

    private void initView(View view, ReaderThemeSettingVo readerThemeSettingVo) {

        ll_reader_main_panel = (LinearLayout) findViewById(R.id.ll_reader_main_panel);
        LayoutParams settingMainPanelParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            settingMainPanelParam.setMargins((int) mContext.getResources().getDimension(R.dimen.reader_setting_left_right_margin), 120, (int) mContext.getResources().getDimension(R.dimen.reader_setting_left_right_margin), 0);
        } else {
            settingMainPanelParam.setMargins((int) mContext.getResources().getDimension(R.dimen.reader_setting_left_right_margin), 0, (int) mContext.getResources().getDimension(R.dimen.reader_setting_left_right_margin), 0);
        }
        ll_reader_main_panel.setLayoutParams(settingMainPanelParam);
        mScrollLayout = (LinearLayout) findViewById(R.id.scroll_layout);
        // mScrollLayout.setVisibility(GONE);

        modeSetting(view);
        scrollModeSetting(view);
        setTypefacesInViews();
        setTextInViews();
        setThemeToView(readerThemeSettingVo);
    }

    private void setThemeToView(ReaderThemeSettingVo readerThemeSettingVo) {
        tv_night_mode.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getNight().getTextColor()));
        tv_day_mode.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTextColor()));
        tv_sepia_mode.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getSepia().getTextColor()));

        tv_night_mode.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getNight().getTabBg()));
        tv_day_mode.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTabBg()));
        tv_sepia_mode.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getSepia().getTabBg()));

        tv_night_mode.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_justify_btn));
        tv_day_mode.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_justify_btn));
        tv_sepia_mode.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_justify_btn));
        //mResetButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_justify_btn));
       // toggle.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_justify_btn));

        GradientDrawable nightMode = (GradientDrawable) tv_night_mode.getBackground();
        nightMode.setColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getNight().getTabBg()));

        GradientDrawable sepiaMode = (GradientDrawable) tv_sepia_mode.getBackground();
        sepiaMode.setColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getSepia().getTabBg()));

        GradientDrawable dayMode = (GradientDrawable) tv_day_mode.getBackground();
        dayMode.setColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTabBg()));

        tv_noto_mode.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTextColor()));
        tv_georgia_mode.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTextColor()));
        tv_sans_mode.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTextColor()));

        tv_noto_mode.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTabBg()));
        tv_georgia_mode.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTabBg()));
        tv_sans_mode.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTabBg()));

        tv_noto_mode.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_justify_btn));
        tv_georgia_mode.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_justify_btn));
        tv_sans_mode.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_justify_btn));

        GradientDrawable gradientDrawablenoto = (GradientDrawable) tv_noto_mode.getBackground();
        gradientDrawablenoto.setColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTabBg()));


        GradientDrawable gradientDrawablegeorgia = (GradientDrawable) tv_georgia_mode.getBackground();
        gradientDrawablegeorgia.setColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTabBg()));

        GradientDrawable gradientDrawablesans = (GradientDrawable) tv_sans_mode.getBackground();
        gradientDrawablesans.setColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTabBg()));
    }

    private void setTextInViews() {
    }

    private void setTypefacesInViews() {
    }

    private void scrollModeSetting(View view) {
        String keyDayMode = KitabooSDKModel.getInstance().getUserID() + "_" + SDKManager.getInstance().getGetLocalBookData().getBookID()
                + "_" + PrefActivity.KEY_DAY_NIGHT_MODE;

        String spMode = Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREF_NAME, keyDayMode, "Day");

        if (spMode.equals("nightMode")) {
            /**
             * chnange background color of button for Night Mode as per reqiurement
             */

        } else if (spMode.equals("Sepia")) {
            /**
             * chnange background color of button for Sepia Mode as per reqiurement
             */
        } else {
            /**
             * chnange background color of button for Day Mode as per reqiurement
             */
        }


        /*switch_scroll_mode = (SwitchCompat) view.findViewById(R.id.switch_scroll_mode);

        //switch_scroll_mode.setTrackDrawable(new SwitchTrackTextDrawable(getContext(),"LEFT", "RIGHT"));

        tv_scroll_view_txt = (TextView) view.findViewById(R.id.tv_scroll_view_txt);
        tv_scroll_view_txt.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));

        if (Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREFS_NAME,PrefActivity.KEY_VERT_HORI_TYPE, "H_Scroll").equalsIgnoreCase("H_Scroll")) {
            switch_scroll_mode.setChecked(false);
        } else {
            switch_scroll_mode.setChecked(true);
        }

        switch_scroll_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (epubSettingPanelListner != null)
                        epubSettingPanelListner.setPageScrollMode(true);
                }else {
                    if (epubSettingPanelListner != null)
                        epubSettingPanelListner.setPageScrollMode(false);
                }
            }
        });

        CustomSwitch mySwitch = new CustomSwitch(findViewById(R.id.custom_switch));
        simpleSwitch = (Switch)findViewById(R.id.simpleSwitch);
        switch_btn_left = (Button) findViewById(R.id.switch_btn_left);
        switch_btn_right = (Button) findViewById(R.id.switch_btn_right);
        mySwitch.setTextLeft("ON");
        mySwitch.setTextRight("OFF");
*/

        toggle = (ToggleButton) findViewById(R.id.toggBtn);
        toggle.setOnClickListener(this);

        String key = KitabooSDKModel.getInstance().getUserID() + "_" + SDKManager.getInstance().getGetLocalBookData().getBookID()
                + "_" + PrefActivity.KEY_SCROLL_MODE;
        final String readerMode = Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREF_NAME, key, "VERTICAL");

        if (readerMode.equalsIgnoreCase("HORIZONTAL")) {
            //setScrollModeOn(switch_btn_left, switch_btn_right);
            //simpleSwitch.setChecked(false);
            toggle.setChecked(false);
            toggle.setText("OFF");
            isVerticalMode = false;

        } else {
            //setScrollModeOff(switch_btn_left, switch_btn_right);
            //simpleSwitch.setChecked(true);
            toggle.setChecked(true);
            toggle.setText("ON");
            isVerticalMode = true;
        }


        //SWITCH
/*       simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   verticalModeOn();
               }
               else{
                   verticalModeOff();
               }
           }
       });
*/


        //SCROLL_VIEW
/*        mySwitch.setOnChangeListener(new CustomSwitch.OnSelectedChangeListener() {
            @Override
            public void OnSelectedChange(CustomSwitch sender) {
                if (sender.getCheckedIndex() == 0)  // Scroll Mode On
                {
                    setScrollModeOff(switch_btn_left, switch_btn_right);
                    verticalModeOn();
                    if (switch_btn_left.getText().toString().equalsIgnoreCase("ON")) {
                        setScrollModeOn(switch_btn_left, switch_btn_right);
                        verticalModeOn();
                       } else {
                        setScrollModeOff(switch_btn_left, switch_btn_right);
                        verticalModeOff();
                    }
                } else if (sender.getCheckedIndex() == 1)  // Scroll Mode OFF
                {
                    setScrollModeOn(switch_btn_left, switch_btn_right);
                    verticalModeOff();
                    if (switch_btn_right.getText().toString().equalsIgnoreCase("OFF")){
                        setScrollModeOn(switch_btn_left, switch_btn_right);
                        verticalModeOn();
                    } else{
                        setScrollModeOff(switch_btn_left, switch_btn_right);
                        verticalModeOff();
                    }
                }
            }
        });
*/
    }

    private void verticalModeOn() {
        if (epubSettingPanelListner != null)
            epubSettingPanelListner.setPageScrollMode(true);
    }

    private void verticalModeOff() {
        if (epubSettingPanelListner != null)
            epubSettingPanelListner.setPageScrollMode(false);
    }


/*    private void setScrollModeOff(Button switch_btn_left, Button switch_btn_right) {

        switch_btn_left.setText("ON");
        switch_btn_right.setText("OFF");
        switch_btn_left.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getScrollView().getTextColor()));
        switch_btn_right.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getScrollView().getSelectedTabBg()));
        //switch_btn_left.setBackground(mContext.getResources().getDrawable(R.drawable.switch_custom_selector));
        //switch_btn_left.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getFontSettings().getSelectedTabBorder()));
        switch_btn_right.setBackground(mContext.getResources().getDrawable(R.drawable.switch_custom_track));
        switch_btn_left.setBackground(com.hurix.kitaboo.constants.utils.Utils.
                getRectAngleDrawable(Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getLoginScreen().getButtonColor()),
                        new float[]{20, 20, 20, 20, 20, 20, 20, 20}, 3, Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getLoginScreen().getButtonColor())));
    }

    private void setScrollModeOn(Button switch_btn_left, Button switch_btn_right) {
        switch_btn_left.setText("ON");
        switch_btn_right.setText("OFF");
        switch_btn_left.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getScrollView().getSelectedTabBg()));
        switch_btn_right.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getScrollView().getTextColor()));
        switch_btn_left.setBackground(mContext.getResources().getDrawable(R.drawable.switch_custom_track));
        // switch_btn_right.setBackground(mContext.getResources().getDrawable(R.drawable.switch_custom_selector));
        // switch_btn_right.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getFontSettings().getSelectedTabBorder()));
        switch_btn_right.setBackground(com.hurix.kitaboo.constants.utils.Utils.
                getRectAngleDrawable(Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getLoginScreen().getButtonColor()),
                        new float[]{20, 20, 20, 20, 20, 20, 20, 20}, 3, Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getLoginScreen().getButtonColor())));
    }
*/

    private void modeSetting(View view) {

        ll_mode_panel = (LinearLayout) findViewById(R.id.ll_mode_panel);
        ff_mode_panel = (LinearLayout) findViewById(R.id.ff_mode_panel);

        if (isMobile){
            ll_mode_panel.setPadding(0, 9, 0, 9);
            ff_mode_panel.setPadding(0, 9, 0, 9);
        }
        else
            ll_mode_panel.setPadding(0, 0, 0, 0);

        tv_mode_txt = (TextView) view.findViewById(R.id.tv_mode_txt);
        font_mode_txt= (TextView) view.findViewById(R.id.font_mode_txt);
        tv_night_mode = (TextView) view.findViewById(R.id.tv_night_mode);
        tv_day_mode = (TextView) view.findViewById(R.id.tv_day_mode);
        tv_sepia_mode = (TextView) view.findViewById(R.id.tv_sepia_mode);


        tv_noto_mode = (TextView) view.findViewById(R.id.font_Serif_mode);
        tv_sans_mode = (TextView) view.findViewById(R.id.font_sans_mode);
        tv_georgia_mode = (TextView) view.findViewById(R.id.font_Georgia_mode);

        tv_night_mode.setPadding(0, 5, 0, 15);
        tv_day_mode.setPadding(0, 5, 0, 15);
        tv_sepia_mode.setPadding(0, 5, 0, 15);

        tv_noto_mode.setPadding(0, 5, 0, 15);
        tv_sans_mode.setPadding(0, 5, 0, 15);
        tv_georgia_mode.setPadding(0, 5, 0, 15);

        tv_mode_txt.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        tv_mode_txt.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));

        tv_night_mode.setOnClickListener(this);
        tv_day_mode.setOnClickListener(this);
        tv_sepia_mode.setOnClickListener(this);
        setlistner(tv_noto_mode);
        setlistner(tv_sans_mode);
        setlistner(tv_georgia_mode);
        mResetButton = (TextView) findViewById(R.id.tv_reset_setting);
        mResetButton.setTextColor(Color.RED);
        mResetButton.setOnClickListener(this);
    }

    private void setlistner(View view) {
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_night_mode) {
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.onNightmodePressed(true);
        } else if (v == tv_day_mode) {
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.onSepiaModeClicked(false);
        } else if (v == tv_sepia_mode) {
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.onSepiaModeClicked(true);
        } else if (v == mResetButton) {
            //   setScrollModeOff(switch_btn_left, switch_btn_right);
            // simpleSwitch.setChecked(true);
            Bundle bundle= new Bundle();
            bundle.putString(FirebaseConstants.RESET_SELECTION,"NA");
            FirebaseAnalyticsEvents.INSTANCE.sendFirebaseEvents(FirebaseConstants.RESET_SELECTION_CLICK,bundle);

            toggle.setChecked(true);
            verticalModeOn();
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.onSepiaModeClicked(false);
        } else if (v == toggle) {
            if (!isVerticalMode) {
                toggle.setText("ON");
                isVerticalMode = true;
                verticalModeOn();
            } else {
                toggle.setText("OFF");
                verticalModeOff();
                isVerticalMode = false;
            }
        } else if (v == tv_noto_mode) {
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageFontFamily("noto");
        }else if (v == tv_sans_mode) {
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageFontFamily("sans");
        }else if (v == tv_georgia_mode) {
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageFontFamily("georgia");
        }
    }


}
