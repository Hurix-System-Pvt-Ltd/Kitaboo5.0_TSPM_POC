package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.hurix.commons.iconify.Typefaces;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.interfaces.IEpubSettingPanelListner;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;

import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;



public class CustomBottomSettingDialogPanel extends DialogFragment implements TabHost.OnTabChangeListener {

    protected TabHost _mTabHost;
    private ThemeUserSettingVo themeUserSettingVo;
    protected Drawable _selectedDrawable;
    protected Drawable _unSelectedDrawable;
    private LinkedHashMap settingPanelVos;
    private IEpubSettingPanelListner settingPanelListner;
    private Typeface mTypeface;
    private int screenHeight,screenWidth;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean isMobile;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private LinearLayout mainLayout;
    View contentView;


    public CustomBottomSettingDialogPanel() {
        // Required empty public constructor
    }

    public void setConfiguration(boolean _isMobile) {
        isMobile = _isMobile;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialogFragment);
    }

    public void setviewlistner(IEpubSettingPanelListner listener) {
        settingPanelListner = listener;
    }

    public void setThemeColor(ThemeUserSettingVo _themeUserSettingVo, ReaderThemeSettingVo _readerThemeSettingVo) {
        themeUserSettingVo = _themeUserSettingVo;
        readerThemeSettingVo = _readerThemeSettingVo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }


    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if (getDialog() != null) {
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.custom_bottom_setting_panel, container, false);

        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            mTypeface = Typefaces.get(getContext(), fontfile);
        } else {
            mTypeface = Typefaces.get(getContext(), "kitabooread.ttf");
        }
        contentView.setBackgroundColor(getResources().getColor(R.color.transparent));
        contentView.measure(0, 0);
        contentView.setBackgroundColor(getResources().getColor(R.color.transparent));
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        return  contentView;
    }





    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windoparams = window.getAttributes();
            windoparams.dimAmount = 0.0f;
            window.setAttributes(windoparams);
            Display display = (getActivity()).getWindowManager().getDefaultDisplay();
            if (display != null) {
                setLayout();
            }
        }

    }

    private void setLayout() {

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        View parent = (View) contentView.getParent();
        parent.setFitsSystemWindows(true);
        contentView.setBackgroundColor(getResources().getColor(R.color.transparent));
        contentView.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth= displaymetrics.widthPixels;

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)  // Landscape
        {
            if(isMobile)  //Mobile
            {
                /*params.setMargins((int)getContext().getResources().getDimension(R.dimen.mob_bottom_setting_panel_margin),0,(int)getContext().getResources().getDimension(R.dimen.mob_bottom_setting_panel_margin),0);
                params.height = ((int)(screenHeight - 250));
                parent.setLayoutParams(params);*/

                getDialog().getWindow().setLayout(screenWidth-((int)getContext().getResources().getDimension(R.dimen.mob_bottom_setting_panel_margin)*2),((int)(screenHeight - 250)));
            }
            else
            {
               // params.setMargins((int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin),0,(int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin),0);
               // params.height = (int)(screenHeight /getDensityLand(getContext()));
                //parent.setLayoutParams(params);
                getDialog().getWindow().setLayout(screenWidth-((int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin)*2),(int)(screenHeight /getDensityLand(getContext())));
            }
        }
        else
        {
            if(!isMobile)
            {
                /*params.setMargins((int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin_new),0,(int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin_new),0);
                params.height = ((int)(screenHeight / getDensityName(getContext())));
                parent.setLayoutParams(params);*/

                getDialog().getWindow().setLayout(screenWidth-((int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin_new)*2),(int)(screenHeight /getDensityName(getContext())));
            }else {
               /* params.setMargins(0,0,0,0);
                params.height = ((int)(screenHeight / getDensityName(getContext())));
                parent.setLayoutParams(params);*/
                getDialog().getWindow().setLayout(screenWidth,(int)(screenHeight /getDensityName(getContext())));

            }
        }
        parent.setBackgroundColor(getResources().getColor(R.color.transparent));
        parent.setLayoutParams(params);
        initialiseTabHost(contentView);


    }


    private void initialiseTabHost(View view) {

        _mTabHost = (TabHost) view.findViewById(R.id.setting_tab);

        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        _mTabHost.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor("#FFFFFF"),
                new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2, Color.parseColor(themeUserSettingVo.getmKitabooMainColor())));

        if (themeUserSettingVo != null) {
            //panel background
            _mTabHost.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor("#FFFFFF"),
                    new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2, Color.parseColor(themeUserSettingVo.getmKitabooMainColor())));
            //selectedDrawable
            _selectedDrawable = Utils.getRectAngleDrawable(Color.parseColor(themeUserSettingVo.getmTocSelectedTabBackground()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(readerThemeSettingVo.
                            getReader().getDayMode().getFontSettings().getFont().getSelectedIconBorder()));
            //unSelectedDrawable
            _unSelectedDrawable = Utils.getRectAngleDrawable(Color.parseColor(themeUserSettingVo.getmTocUnSelectedTabBackground()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(themeUserSettingVo.getmKitabooMainColor()));
        }

        setUpTabHost();
    }

    public void setUpTabHost() {
        int count = 0;
        if (_mTabHost.getTabContentView() != null) {
            _mTabHost.clearAllTabs();
        }
        _mTabHost.setup();
        settingPanelVos = new LinkedHashMap();
        settingPanelVos.put(getResources().getString(R.string.font_setting), getResources().getString(R.string.font_setting));

        if (settingPanelVos != null) {
            Iterator it = settingPanelVos.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                if (count == 0) {
                    count = count + 1;
                    _mTabHost.addTab(_mTabHost.newTabSpec(pair.getKey().toString()).setIndicator(pair.getValue().toString())
                            .setContent(new CustomBottomSettingDialogPanel.TabContentFactory(settingPanelListner)));
                } else {
                    _mTabHost.addTab(_mTabHost.newTabSpec(pair.getKey().toString()).setIndicator(pair.getValue().toString())
                            .setContent(new CustomBottomSettingDialogPanel.TabContentFactory(settingPanelListner)));
                }
            }
        }
        updateTab();
        _mTabHost.getTabWidget().setDividerDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        _mTabHost.setOnTabChangedListener(this);
    }

    class TabContentFactory implements TabHost.TabContentFactory {

        private IEpubSettingPanelListner mListner;

        public TabContentFactory(IEpubSettingPanelListner listner) {
            mListner = listner;
        }

        public TabContentFactory() {
        }

        @Override
        public View createTabContent(String tag) {
            if (settingPanelListner != null) {
                return settingPanelListner.returnSettingPanelView(tag, mListner);
            }
            return new View(CustomBottomSettingDialogPanel.this.getActivity());
        }
    }

    public void updateTab() {
        for (int i = 0; i < _mTabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) _mTabHost.getTabWidget().getChildAt(i)
                    .findViewById(android.R.id.title); // Unselected Tabs
            tv.setTypeface(null, Typeface.NORMAL);
            tv.setGravity(Gravity.CENTER);
            tv.setAllCaps(false);
            tv.setTextSize(16);
            if (readerThemeSettingVo != null) {
                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getTabTextColor()));
            }

            _mTabHost.getTabWidget().getChildAt(i).getBackground().setColorFilter(getResources().getColor(R.color.transparent), PorterDuff.Mode.SRC_ATOP);
        }

        // selected
        if (_mTabHost != null && _mTabHost.getCurrentTabView() != null) {

            if (themeUserSettingVo != null) {
                _mTabHost.getTabWidget().getChildAt(_mTabHost.getCurrentTab()).getBackground().setColorFilter(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getSelectedIconBorder()), PorterDuff.Mode.SRC_ATOP);
                TextView tv = (TextView) _mTabHost.getCurrentTabView().findViewById(android.R.id.title); // for Selected Tab
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setGravity(Gravity.CENTER);
                tv.setAllCaps(false);
                tv.setTextSize(16);
                tv.setTypeface(null, Typeface.BOLD);
                if (readerThemeSettingVo != null) {
                    tv.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getSelectedTextColor()));
                }
            }
        }
    }


    @Override
    public void onTabChanged(String tabId) {

        for (int i = 0; i < _mTabHost.getTabWidget().getChildCount(); i++) {
            // unselected
            TextView tv = (TextView) _mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); // Unselected Tabs
            tv.setTypeface(null, Typeface.NORMAL);
            tv.setGravity(Gravity.CENTER);
            tv.setAllCaps(false);
            tv.setTextSize(16);
            // unselected
            if (readerThemeSettingVo != null) {
                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getTabTextColor()));
            }
            _mTabHost.getTabWidget().getChildAt(i).getBackground().setColorFilter(getResources().getColor(R.color.transparent), PorterDuff.Mode.SRC_ATOP);
        }

        // selected

        TextView tv = (TextView) _mTabHost.getCurrentTabView().findViewById(android.R.id.title); // for Selected Tab
        tv.setTypeface(null, Typeface.BOLD);
        tv.setGravity(Gravity.CENTER);
        tv.setAllCaps(false);
        tv.setTextSize(16);
        if (themeUserSettingVo != null) {
            _mTabHost.getTabWidget().getChildAt(_mTabHost.getCurrentTab()).getBackground().setColorFilter(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getSelectedIconBorder()), PorterDuff.Mode.SRC_ATOP);
            if (readerThemeSettingVo != null) {
                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getTabTextColor()));
            }
        }

    }

    private float getDensityName(Context context) {
        float value = 2.3f;
        String str = "";
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "small-ldpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "small-mdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "small-hdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "small-xhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "small-xxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "small-xxxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "small-tvdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                default:
                    str = "small-unknown";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "normal-ldpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "normal-mdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "normal-hdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "normal-xhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "One Plus 7T";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.95f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.45f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "normal-xxxhdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "normal-tvdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_560:
                    str = "Pixel XL mobile";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 1.85f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.35f;
                    }
                    break;
                default:
                    str = "Samsung mobile";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        if(!isMobile)
                            value = 2.8f;
                        else
                            value = 2.45f;
                    }else {
                        if(!isMobile)
                            value = 2.3f;
                        else
                            value = 1.65f;
                    }

                    break;
            }
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "large-ldpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.8f;
                    }else {
                        value = 2.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "large-mdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.8f;
                    }else {
                        value = 2.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "large-hdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.8f;
                    }else {
                        value = 2.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "large-xhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.8f;
                    }else {
                        value = 2.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "large-xxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.8f;
                    }else {
                        value = 2.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "large-xxxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.8f;
                    }else {
                        value = 2.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "large-tvdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.8f;
                    }else {
                        value = 2.25f;
                    }
                    break;
                default:
                    str = "large-unknown Galaxy Tab S4";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.8f;
                    }else {
                        value = 2.25f;
                    }
                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "xlarge-ldpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.7f;
                    }else {
                        value = 2.15f;
                    }
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "xlarge-mdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.7f;
                    }else {
                        value = 2.15f;
                    }
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "xlarge-hdpi Samsung Tab A and Samsung Tab A 10.5";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 3.40f;
                    }else {
                        value = 2.55f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "xlarge-xhdpi Galaxy Tab S3";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.9f;
                    }else {
                        value = 2.2f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "xlarge-xxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.7f;
                    }else {
                        value = 2.2f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "xlarge-xxxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.7f;
                    }else {
                        value = 2.2f;
                    }
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "xlarge-tvdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.7f;
                    }else {
                        value = 2.2f;
                    }
                    break;
                default:
                    str = "xlarge-unknown";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.7f;
                    }else {
                        value = 2.15f;
                    }
                    break;
            }
        }
        return value;
    }

    private float getDensityLand(Context context) {
        float value = 20;
        String str = "";
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "small-ldpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.45f;
                    }else {
                        value = 1.15f;
                    }
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "small-mdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.45f;
                    }else {
                        value = 1.15f;
                    }
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "small-hdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.45f;
                    }else {
                        value = 1.15f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "small-xhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.45f;
                    }else {
                        value = 1.15f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "small-xxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.45f;
                    }else {
                        value = 1.15f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "small-xxxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.45f;
                    }else {
                        value = 1.15f;
                    }
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "small-tvdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.45f;
                    }else {
                        value = 1.15f;
                    }
                    break;
                default:
                    str = "small-unknown";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.45f;
                    }else {
                        value = 1.15f;
                    }
                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "normal-ldpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.55f;
                    }else {
                        value = 1.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "normal-mdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.55f;
                    }else {
                        value = 1.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "normal-hdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.55f;
                    }else {
                        value = 1.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "normal-xhdpi";
                    value = 1.25f;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "normal-xxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.55f;
                    }else {
                        value = 1.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "normal-xxxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.55f;
                    }else {
                        value = 1.25f;
                    }
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "normal-tvdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.55f;
                    }else {
                        value = 1.25f;
                    }
                    break;
                default:
                    str = "normal-unknown";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.55f;
                    }else {
                        value = 1.25f;
                    }
                    break;
            }
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "large-ldpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.65f;
                    }else {
                        value = 1.35f;
                    }
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "large-mdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.65f;
                    }else {
                        value = 1.35f;
                    }
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "large-hdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.65f;
                    }else {
                        value = 1.35f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "large-xhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.65f;
                    }else {
                        value = 1.35f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "large-xxhdpi";
                    value = 1.35f;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "large-xxxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.65f;
                    }else {
                        value = 1.35f;
                    }
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "large-tvdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.65f;
                    }else {
                        value = 1.35f;
                    }
                    break;
                default:
                    str = "large-unknown Galaxy Tab S4";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 1.65f;
                    }else {
                        value = 1.35f;
                    }
                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "xlarge-ldpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.0f;
                    }else {
                        value = 1.67f;
                    }
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "xlarge-mdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable))  {
                        value = 2.0f;
                    }else {
                        value = 1.67f;
                    }
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "xlarge-hdpi Samsung Tab A and Samsung Tab A 10.5";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.0f;
                    }else {
                        value = 1.67f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "xlarge-xhdpi Galaxy Tab S3";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.2f;
                    }else {
                        value = 1.69f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "xlarge-xxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.0f;
                    }else {
                        value = 1.67f;
                    }
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "xlarge-xxxhdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.0f;
                    }else {
                        value = 1.67f;
                    }
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "xlarge-tvdpi";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.0f;
                    }else {
                        value = 1.67f;
                    }
                    break;
                default:
                    str = "xlarge-unknown";
                    if (!getResources().getBoolean(R.bool.font_setting_panel_all_feature_enable)) {
                        value = 2.0f;
                    }else {
                        value = 1.67f;
                    }
                    break;
            }
        }
        return value;
    }

}
