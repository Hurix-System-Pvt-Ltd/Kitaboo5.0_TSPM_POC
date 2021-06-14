package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

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
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.hurix.commons.iconify.Typefaces;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.interfaces.IEpubSettingPanelListner;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomBottomSettingPanel extends BottomSheetDialogFragment implements TabHost.OnTabChangeListener {

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

    public CustomBottomSettingPanel() {
        // Required empty public constructor
    }

    public void setConfiguration(boolean _isMobile) {
        isMobile = _isMobile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {


        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            mTypeface = Typefaces.get(getContext(), fontfile);
        } else {
            mTypeface = Typefaces.get(getContext(), "kitabooread.ttf");
        }
        View contentView = null;

        contentView = View.inflate(getContext(), R.layout.custom_bottom_setting_panel, null);

        // Below dialog properties are commented as it is impacting underlying view
       // getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //contentView.setBackgroundColor(getResources().getColor(R.color.transparent));
        dialog.setContentView(contentView);

        //FrameLayout bottomSheet = (FrameLayout) dialog.getWindow().findViewById(R.id.design_bottom_sheet);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        //CoordinatorLayout.Behavior behavior = params.getBehavior();



        View parent = (View) contentView.getParent();
        parent.setFitsSystemWindows(true);
        bottomSheetBehavior = BottomSheetBehavior.from(parent);
        contentView.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth= displaymetrics.widthPixels;
       // bottomSheetBehavior.setPeekHeight((int)(screenHeight / 2.2));
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);
        if (bottomSheetBehavior instanceof BottomSheetBehavior) {
            bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)  // Landscape
        {
            if(isMobile)  //Mobile
            {
                bottomSheetBehavior.setPeekHeight((int)(screenHeight));
                params.setMargins((int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin),0,(int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin),0);
            }
            else
            {
                bottomSheetBehavior.setPeekHeight((int)(screenHeight /getDensityLand(getContext())));
                params.setMargins((int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin),0,(int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin),0);
            }
        }
        else
        {
            if(!isMobile)
            {
                bottomSheetBehavior.setPeekHeight((int)(screenHeight / getDensityName(getContext())));
                params.setMargins((int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin_new),0,(int)getContext().getResources().getDimension(R.dimen.main_bottom_setting_panel_margin_new),0);
            }else {
                bottomSheetBehavior.setPeekHeight((int)(screenHeight / 1.6));
            }
        }

        params.height = screenHeight;
        parent.setLayoutParams(params);
        //parent.setBackgroundColor(getResources().getColor(R.color.transparent));
        initialiseTabHost(contentView);
    }

    private void initialiseTabHost(View view) {

        _mTabHost = (TabHost) view.findViewById(R.id.setting_tab);

        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        //mainLayout.setBackground(getResources().getDrawable(R.drawable.shadow));
       /* mainLayout.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.
                        getReader().getDayMode().getFontSettings().getPopupBackground()),
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2,Color.parseColor(readerThemeSettingVo.
                        getReader().getDayMode().getFontSettings().getPopupBorder())));*/

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
       // settingPanelVos.put(getResources().getString(R.string.reader_setting), getResources().getString(R.string.reader_setting));

        if (settingPanelVos != null) {
            Iterator it = settingPanelVos.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                if (count == 0) {
                    count = count + 1;
                    _mTabHost.addTab(_mTabHost.newTabSpec(pair.getKey().toString()).setIndicator(pair.getValue().toString())
                            .setContent(new CustomBottomSettingPanel.TabContentFactory(settingPanelListner)));
                } else {
                    _mTabHost.addTab(_mTabHost.newTabSpec(pair.getKey().toString()).setIndicator(pair.getValue().toString())
                            .setContent(new CustomBottomSettingPanel.TabContentFactory(settingPanelListner)));
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
            return new View(CustomBottomSettingPanel.this.getActivity());
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
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "small-mdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "small-hdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "small-xhdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "small-xxhdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "small-xxxhdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "small-tvdpi";
                    value = 2.3f;
                    break;
                default:
                    str = "small-unknown";
                    value = 2.3f;
                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "normal-ldpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "normal-mdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "normal-hdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "normal-xhdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "normal-xxhdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "normal-xxxhdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "normal-tvdpi";
                    value = 2.3f;
                    break;
                default:
                    str = "normal-unknown";
                    value = 2.3f;
                    break;
            }
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "large-ldpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "large-mdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "large-hdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "large-xhdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "large-xxhdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "large-xxxhdpi";
                    value = 2.3f;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "large-tvdpi";
                    value = 2.3f;
                    break;
                default:
                    str = "large-unknown Galaxy Tab S4";
                    value = 2.3f;
                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "xlarge-ldpi";
                    value = 2.55f;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "xlarge-mdpi";
                    value = 2.55f;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "xlarge-hdpi Samsung Tab A and Samsung Tab A 10.5";
                    value = 2.55f;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "xlarge-xhdpi Galaxy Tab S3";
                    value = 2.2f;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "xlarge-xxhdpi";
                    value = 2.2f;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "xlarge-xxxhdpi";
                    value = 2.2f;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "xlarge-tvdpi";
                    value = 2.2f;
                    break;
                default:
                    str = "xlarge-unknown";
                    value = 2.2f;
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
                    value = 1.15f;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "small-mdpi";
                    value = 1.15f;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "small-hdpi";
                    value = 1.15f;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "small-xhdpi";
                    value = 1.15f;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "small-xxhdpi";
                    value = 1.15f;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "small-xxxhdpi";
                    value = 1.15f;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "small-tvdpi";
                    value = 1.15f;
                    break;
                default:
                    str = "small-unknown";
                    value = 1.15f;
                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "normal-ldpi";
                    value = 1.25f;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "normal-mdpi";
                    value = 1.25f;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "normal-hdpi";
                    value = 1.25f;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "normal-xhdpi";
                    value = 1.25f;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "normal-xxhdpi";
                    value = 1.25f;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "normal-xxxhdpi";
                    value = 1.25f;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "normal-tvdpi";
                    value = 1.25f;
                    break;
                default:
                    str = "normal-unknown";
                    value = 1.25f;
                    break;
            }
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "large-ldpi";
                    value = 1.35f;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "large-mdpi";
                    value = 1.35f;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "large-hdpi";
                    value = 1.35f;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "large-xhdpi";
                    value = 1.35f;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "large-xxhdpi";
                    value = 1.35f;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "large-xxxhdpi";
                    value = 1.35f;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "large-tvdpi";
                    value = 1.35f;
                    break;
                default:
                    str = "large-unknown Galaxy Tab S4";
                    value = 1.35f;
                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "xlarge-ldpi";
                    value = 1.6f;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "xlarge-mdpi";
                    value = 1.6f;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "xlarge-hdpi Samsung Tab A and Samsung Tab A 10.5";
                    value = 1.6f;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "xlarge-xhdpi Galaxy Tab S3";
                    value = 1.6f;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "xlarge-xxhdpi";
                    value = 1.6f;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "xlarge-xxxhdpi";
                    value = 1.6f;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "xlarge-tvdpi";
                    value = 1.6f;
                    break;
                default:
                    str = "xlarge-unknown";
                    value = 1.6f;
                    break;
            }
        }
        return value;
    }
}
