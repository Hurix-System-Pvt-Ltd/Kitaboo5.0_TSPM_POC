package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.hurix.customui.interfaces.IEpubSettingPanelListner;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.epubreader.PrefActivity;
import com.hurix.epubreader.Utility.Utils;

@SuppressLint("ValidFragment")
public class CustomEpubSettingPanel extends Dialog{
    private Context mContext;
   // private View mainView;
    private int mHeight,mWidth;
    private IEpubSettingPanelListner epubSettingPanelListner;
    private SeekBar mSetFontSize;
    private SwitchCompat mNightSwitchCompat, mPageSwitchCompact;
    private LinearLayout mParentContainer;
    private PopupWindow popupWindow;
    private ThemeUserSettingVo themeUserSettingVo;


    public CustomEpubSettingPanel(@NonNull Context context) {
        super(context);
    }

    public CustomEpubSettingPanel(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomEpubSettingPanel(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @SuppressLint("ValidFragment")
    public CustomEpubSettingPanel(Context mContext, View view) {
        super(mContext);
        this.mContext = mContext;
       // mainView=view;
        epubSettingPanelListner=(IEpubSettingPanelListner)mContext;
    }

    public void setData(ThemeUserSettingVo mthemeUserSettingVo) {
        themeUserSettingVo=mthemeUserSettingVo;
        openSettingPopup();
    }

   /* public void setPosition( Rect location) {
        popupWindow.showAtLocation(mainView, Gravity.TOP|Gravity.LEFT, location.left, location.bottom);
    }*/

    public void openSettingPopup() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(com.hurix.epubreader.R.layout.setting_popup, null);
        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), ""));
        mNightSwitchCompat = (SwitchCompat) popupView.findViewById(com.hurix.epubreader.R.id.nightTogglemode);
        mPageSwitchCompact = (SwitchCompat) popupView.findViewById(com.hurix.epubreader.R.id.pagetogglemode);
        mParentContainer = (LinearLayout) popupView.findViewById(com.hurix.epubreader.R.id.parent);
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background

        int[][] states = new int[][] {
                new int[] {-android.R.attr.state_checked},
                new int[] {android.R.attr.state_checked},
        };

        int[] thumbColors = new int[] {
                Color.WHITE,
                mContext.getResources().getColor(com.hurix.epubreader.R.color.kitaboo_main_color),
        };

        int[] trackColors = new int[] {
                Color.GRAY,
                mContext.getResources().getColor(com.hurix.epubreader.R.color.transparent_kitaboo_main_color),

        };

        DrawableCompat.setTintList(DrawableCompat.wrap(mNightSwitchCompat.getThumbDrawable()), new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(mNightSwitchCompat.getTrackDrawable()), new ColorStateList(states, trackColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(mPageSwitchCompact.getThumbDrawable()), new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(mPageSwitchCompact.getTrackDrawable()), new ColorStateList(states, trackColors));

        if (themeUserSettingVo != null) {
            border.setStroke(1, Color.parseColor(themeUserSettingVo.getmKitabooMainColor()));
        }else {
            border.setStroke(1, mContext.getResources().getColor(com.hurix.epubreader.R.color.kitaboo_main_color));
        }
        //black border with full opacity
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mParentContainer.setBackgroundDrawable(border);
        } else {
            mParentContainer.setBackground(border);
        }

        if (Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREFS_NAME, PrefActivity.KEY_VERT_HORI_TYPE, "H_Scroll").equalsIgnoreCase("H_Scroll")) {
            mPageSwitchCompact.setChecked(false);
        } else {
            mPageSwitchCompact.setChecked(true);
        }
        if (Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREFS_NAME, PrefActivity.KEY_DAY_NIGHT_MODE, "Day").equalsIgnoreCase("Night")) {
            mNightSwitchCompat.setChecked(true);
        } else {
            mNightSwitchCompat.setChecked(false);
        }
        /*popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                collapse (popupView);
            }
        });*/

        mNightSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (epubSettingPanelListner != null)
                        epubSettingPanelListner.onNightmodePressed(true);
                }else {
                    if (epubSettingPanelListner != null)
                        epubSettingPanelListner.onNightmodePressed(false);
                }
            }

        });
        mPageSwitchCompact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        mSetFontSize = (SeekBar) popupView.findViewById(com.hurix.epubreader.R.id.seekfontsize);
        mSetFontSize.setProgressDrawable(mContext.getResources().getDrawable(com.hurix.epubreader.R.drawable.progress_seek));
        mSetFontSize.setThumb(mContext.getResources().getDrawable(com.hurix.epubreader.R.drawable.thumb));
        mSetFontSize.setMax(4);
        mSetFontSize.setProgress(getprogress(Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREFS_NAME, PrefActivity.KEY_FONT_SIZE, "mediumFont")));
        mSetFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (epubSettingPanelListner != null)
                    epubSettingPanelListner.fontSeekBarSize(seekBar.getProgress());
                /*   //all font related changes handled by webview font setting*/

            }

        });
        if (epubSettingPanelListner != null) {
            epubSettingPanelListner.settingPanelViewsCallback(mSetFontSize,mNightSwitchCompat,mPageSwitchCompact);
        }
        //expand(popupView);
        if (epubSettingPanelListner != null) {
            epubSettingPanelListner.onSettingPanelPopup(popupWindow);
        }
    }

   /* public void showPopup(int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(mainView, 0,
                    0, gravity);
        }
    }*/

    private int getprogress(String fontsize) {

        if (fontsize.equalsIgnoreCase("xsmallFont")) {
            return 0;
        }
        else if (fontsize.equalsIgnoreCase("smallFont")) {
            return 1;
        }
        else if (fontsize.equalsIgnoreCase("mediumFont")) {
            return 2;
        } else if (fontsize.equalsIgnoreCase("largeFont")) {
            return 3;
        } else if (fontsize.equalsIgnoreCase("xlargeFont")) {
            return 4;
        } else if (fontsize.equalsIgnoreCase("xxlargeFont")) {
            return 5;
        }
        return 2;
    }

   /* public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration (600);
        v.startAnimation(a);
    }*/

    /*public static void collapse(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    view.setVisibility(View.GONE);
                }else{
                    view.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / view.getContext().getResources().getDisplayMetrics().density));
        a.setDuration (500);
        view.startAnimation(a);
        view.clearAnimation();
    }*/

}
