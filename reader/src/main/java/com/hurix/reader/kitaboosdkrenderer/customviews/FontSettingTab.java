package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.hurix.commons.KitabooSDKModel;
import com.hurix.commons.iconify.Typefaces;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.interfaces.IEpubSettingPanelListner;
import com.hurix.epubreader.Utility.Utils;


import com.hurix.epubreader.PrefActivity;
import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.reader.kitaboosdkrenderer.views.CustomPlayerUIConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FontSettingTab extends FrameLayout implements View.OnClickListener {
    private boolean isMobile;
    private ReaderThemeSettingVo _readerThemeSettingVo;
    private Context mContext = null;
    private int mResId = 0;
    private IEpubSettingPanelListner epubSettingPanelListner = null;
    private SeekBar mSetFontSize;
    private TextView mTextLeftAlign, mTextCenterAlign, mTextRightAlign, mTextJustify, tv_justify_headline;
    private TextView marginLevelOne, marginLevelTwo, marginLevelThree,tv_margin_headline;
    private TextView spaceLevelOne, spaceLevelTwo, spaceLevelThree,tv_space_headline;
    private TextView tv_night_mode, tv_day_mode, tv_sepia_mode,tv_scroll_mode, tv_sans_mode, tv_georgia_mode,mResetButton;
    private Typeface typeface;
    private LinearLayout ll_setting_main_panel, ll_justify_panel;
    private LinearLayout ll_mode_panel, ll_reader_main_panel, mScrollLayout,ff_mode_panel;
    private LinearLayout rl_seekbar_main_panel;
    private GradientDrawable selectedDrawable, unSelectedDrawable;
    private TextView tvResetSetting,tvFontSetting,tvSeekone,tvSeekTwo;
    private SeekBar seek_font_size;
    private TextView tv_mode_headline,tv_font_headline;
    private SwitchCompat toggle;
    private boolean isVerticalMode;
    private Spinner fontSpinner;
    private HashMap<String,String> fontMapItems=new HashMap<>();
    private LinearLayout l1_linespacing_panel;
    private LinearLayout ll_margin_panel;
    private LinearLayout ll_scrollmode_panel;
    private ArrayAdapter<String> adapter;
    private View scrolllineDivider;
    List<String> fontStyleList;

    public FontSettingTab(@NonNull Context context) {
        super(context);
    }

    public FontSettingTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FontSettingTab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FontSettingTab(Context context, int resID) {
        super(context);
        mContext = context;
        mResId = resID;
    }


    public FontSettingTab(Context context, IEpubSettingPanelListner listner, ReaderThemeSettingVo readerThemeSettingVo, boolean _isMobile) {
        super(context);
        epubSettingPanelListner = listner;
        isMobile = _isMobile;
        _readerThemeSettingVo = readerThemeSettingVo;
        mContext = context;
        addFontsItems();
        initializeContext(context, R.layout.font_settings_panel);

    }

    private void addFontsItems() {
        fontMapItems.put("Default","");
        fontMapItems.put("Open Sans","sans");
        fontMapItems.put("Georgia","georgia");
        fontMapItems.put("Noto Serif","noto");
    }

    public void initializeContext(Context context, int resID) {
        View view = LayoutInflater.from(context).inflate(resID, this, true);
        setTypefaceTTFFile();
        initView(view);
        setTypefacesInViews();
        setTextInViews();
    }


    private void setTypefaceTTFFile() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            typeface = Typefaces.get(mContext, fontfile);
        } else {
            typeface = Typefaces.get(mContext, "kitabooread.ttf");
        }
    }


    private int getprogress(String fontsize) {

        if (fontsize.equalsIgnoreCase("textSizeOne")) {
            return 0;
        } else if (fontsize.equalsIgnoreCase("textSizeTwo")) {
            return 1;
        } else if (fontsize.equalsIgnoreCase("textSizeThree")) {
            return 2;
        } else if (fontsize.equalsIgnoreCase("textSizeFour")) {
            return 3;
        } else if (fontsize.equalsIgnoreCase("textSizeFive")) {
            return 4;
        }
        return 0;
    }

    private void initView(View view) {

        ll_setting_main_panel = (LinearLayout) findViewById(R.id.ll_setting_main_panel);
        rl_seekbar_main_panel = (LinearLayout) findViewById(R.id.rl_seekbar_main_panel);
        ll_mode_panel = (LinearLayout) findViewById(R.id.ll_mode_panel);
        ff_mode_panel = (LinearLayout) findViewById(R.id.font_margin_panel);
        ll_justify_panel = (LinearLayout) findViewById(R.id.ll_justify_panel);
        l1_linespacing_panel =  (LinearLayout) findViewById(R.id.ll_spacing_panel);
        ll_margin_panel  =  (LinearLayout) findViewById(R.id.ll_margin_panel);
        ll_scrollmode_panel  =  (LinearLayout) findViewById(R.id.scroll_layout);
        scrolllineDivider = (View) findViewById(R.id.scrolllineDivider);


        LayoutParams settingMainPanelParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ll_setting_main_panel.setLayoutParams(settingMainPanelParam);
        selectedDrawable = new GradientDrawable();
        selectedDrawable.setShape(GradientDrawable.RECTANGLE);
        selectedDrawable.setStroke(2, Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getSelectedIconBorder()));
        selectedDrawable.setCornerRadius(7);

        unSelectedDrawable = new GradientDrawable();
        unSelectedDrawable.setShape(GradientDrawable.RECTANGLE);
        unSelectedDrawable.setStroke(0, Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getBoxBorderColor()));
        unSelectedDrawable.setCornerRadius(7);


        modesInitView(view);
        setThemeToView(_readerThemeSettingVo);
        marginsInitView(view);
        fontsInitView(view);
        sizeSeekbarSettings(view);
        justifyViewSettings(view);
        spacingsViewSettings(view);
        scrollModeSetting(view);
        resetSettingView(view);
        tvResetSetting = (TextView) findViewById(R.id.tv_reset_setting);
        tvFontSetting = (TextView) findViewById(R.id.tv_font_setting);
        tvSeekone = (TextView) findViewById(R.id.tv_start);
        tvSeekTwo = (TextView) findViewById(R.id.tv_end);
        tvFontSetting.setOnClickListener(this);
        tvResetSetting.setOnClickListener(this);
        tvResetSetting.setTextColor(getResources().getColor(R.color.font_reset_button));
        tvFontSetting.setTextColor(getResources().getColor(R.color.kitaboo_main_color));
        tvSeekone.setTextColor(getResources().getColor(R.color.black));
        tvSeekTwo.setTextColor(getResources().getColor(R.color.black));

       /* Call this method to configure UI components*/
            configureUiComponentsOnPanel();

    }

    private void configureUiComponentsOnPanel() {

        if(! getContext().getResources().getBoolean(R.bool.fontstyle_setting_required)){
            ff_mode_panel.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.scrollmode_setting_required)){
            scrolllineDivider.setVisibility(View.GONE);
            ll_scrollmode_panel.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.fontsize_setting_required)){
            rl_seekbar_main_panel.setVisibility(View.GONE);
        }

       /* ReaderMode*/

        if(! getContext().getResources().getBoolean(R.bool.readermode_setting_required)){
            ll_mode_panel.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.daymode_enable)){
            tv_day_mode.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.nightmode_enable)){
            tv_night_mode.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.sepiamode_enable)){
            tv_sepia_mode.setVisibility(View.GONE);
        }

       /* LineSpacing*/

        if(! getContext().getResources().getBoolean(R.bool.linespacing_setting_required)){
            l1_linespacing_panel.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.linespacing_one_enable)){
            spaceLevelOne.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.linespacing_two_enable)){
            spaceLevelTwo.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.linespacing_three_enable)){
            spaceLevelThree.setVisibility(View.GONE);
        }

        /*Margin*/

        if(! getContext().getResources().getBoolean(R.bool.margin_setting_required)){
            ll_margin_panel.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.margin_one_enable)){
            marginLevelOne.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.margin_two_enable)){
            marginLevelTwo.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.margin_three_enable)){
            marginLevelThree.setVisibility(View.GONE);
        }

        /*Alignment*/

        if(! getContext().getResources().getBoolean(R.bool.alignment_setting_required)){
            ll_justify_panel.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.alignLeft_enable)){
            mTextLeftAlign.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.alignCenter_enable)){
            mTextCenterAlign.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.alignRight_enable)){
            mTextRightAlign.setVisibility(View.GONE);
        }

        if(! getContext().getResources().getBoolean(R.bool.alignJustify_enable)){
            mTextJustify.setVisibility(View.GONE);
        }

       /* Fontstyle*/

        if(! getContext().getResources().getBoolean(R.bool.fontstyle_setting_required)){
            ff_mode_panel.setVisibility(View.GONE);;
        }

        if(! getContext().getResources().getBoolean(R.bool.OpenSans_enable)){
            fontStyleList.remove(1);
            adapter.notifyDataSetChanged();
        }

        if(! getContext().getResources().getBoolean(R.bool.Georgia_enable)){
            fontStyleList.remove(2);
            adapter.notifyDataSetChanged();
        }

        if(! getContext().getResources().getBoolean(R.bool.NotoSerif_enable)){
            fontStyleList.remove(2);
            adapter.notifyDataSetChanged();
        }



    }

    private void resetSettingView(View view) {
        mResetButton = (TextView) view.findViewById(R.id.tv_reset_setting);
        mResetButton.setTextColor(Color.RED);
        mResetButton.setOnClickListener(this);
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
        toggle = (SwitchCompat) view.findViewById(R.id.toggBtn);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(!isVerticalMode){
                        isVerticalMode = true;
                        verticalModeOn();
                    }
                } else {
                    verticalModeOff();
                    isVerticalMode = false;
                }
            }

        });
        tv_scroll_mode= (TextView) view.findViewById(R.id.tv_scroll_view_txt);
        tv_scroll_mode.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));

        String key = KitabooSDKModel.getInstance().getUserID() + "_" + SDKManager.getInstance().getGetLocalBookData().getBookID()
                + "_" + PrefActivity.KEY_SCROLL_MODE;
        final String readerMode = Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREF_NAME, key, "VERTICAL");

        if (readerMode.equalsIgnoreCase("HORIZONTAL")) {

            toggle.setChecked(false);
            //toggle.setText("OFF");
            isVerticalMode = false;

        } else {
            isVerticalMode = true;
            toggle.setChecked(true);
            //toggle.setText("ON");
        }
    }

    private void spacingsViewSettings(View view) {
        tv_space_headline = (TextView) view.findViewById(R.id.tv_spacing_headline);
        tv_space_headline.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        spaceLevelOne = (TextView) view.findViewById(R.id.spacing_LevelOne);
        spaceLevelTwo = (TextView) view.findViewById(R.id.spacing_Leveltwo);
        spaceLevelThree = (TextView) view.findViewById(R.id.spacing_Levelthree);
        spaceLevelOne.setOnClickListener(this);
        spaceLevelTwo.setOnClickListener(this);
        spaceLevelThree.setOnClickListener(this);

        String keyTextAlignMode = KitabooSDKModel.getInstance().getUserID() + "_" + SDKManager.getInstance().getGetLocalBookData().getBookID()
                + "_" + PrefActivity.LINE_SPACING;
        String textAlignment = com.hurix.commons.utils.Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREF_NAME, keyTextAlignMode, "heightLevel1");

        if (textAlignment != null && textAlignment.equalsIgnoreCase("1.4")) {
            spaceLevelOne.setBackground(selectedDrawable);
            spaceLevelTwo.setBackground(unSelectedDrawable);
            spaceLevelThree.setBackground(unSelectedDrawable);
        } else if (textAlignment != null && textAlignment.equalsIgnoreCase("1.6")) {
            spaceLevelOne.setBackground(unSelectedDrawable);
            spaceLevelTwo.setBackground(selectedDrawable);
            spaceLevelThree.setBackground(unSelectedDrawable);
        } else if (textAlignment != null && textAlignment.equalsIgnoreCase("1.8")) {
            spaceLevelOne.setBackground(unSelectedDrawable);
            spaceLevelTwo.setBackground(unSelectedDrawable);
            spaceLevelThree.setBackground(selectedDrawable);
        } else {
            spaceLevelOne.setBackground(unSelectedDrawable);
            spaceLevelTwo.setBackground(unSelectedDrawable);
            spaceLevelThree.setBackground(unSelectedDrawable);
        }
    }

    private void fontsInitView(View view) {

        tv_font_headline= (TextView) view.findViewById(R.id.tv_font_headline);
        tv_font_headline.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        tv_font_headline.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        fontSpinner= (Spinner) findViewById(R.id.fontSpinner);
        String keyFontFamily = KitabooSDKModel.getInstance().getUserID() + "_" + SDKManager.getInstance().getGetLocalBookData().getBookID()
                + "_" + PrefActivity.KEY_FONT_FAMILY;
        String pageFontFamily = com.hurix.commons.utils.Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREF_NAME, keyFontFamily, "Default");
        fontStyleList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.font_item)));
        adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item,fontStyleList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSpinner.setAdapter(adapter);
        selectValue(fontSpinner,pageFontFamily);
        fontSpinner.post(new Runnable() {
            @Override
            public void run() {
                fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView,
                                               View selectedItemView, int position, long id) {
                        try {

                            String font =parentView.getItemAtPosition(position).toString();
                            if (epubSettingPanelListner != null)
                                epubSettingPanelListner.setPageFontFamily(fontMapItems.get(font));
                        }
                        catch (Exception e) {

                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });
            }
        });

    }

    private void marginsInitView(View view) {

        marginLevelOne = (TextView) view.findViewById(R.id.margin_left);
        marginLevelTwo = (TextView) view.findViewById(R.id.margin_center);
        marginLevelThree = (TextView) view.findViewById(R.id.margin_right);

    }

    private void modesInitView(View view) {
        tv_mode_headline = (TextView) view.findViewById(R.id.tv_mode_txt);
        tv_mode_headline.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        tv_mode_headline.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        tv_night_mode = (TextView) view.findViewById(R.id.tv_night_mode);
        tv_day_mode = (TextView) view.findViewById(R.id.tv_day_mode);
        tv_sepia_mode = (TextView) view.findViewById(R.id.tv_sepia_mode);

        tv_night_mode.setPadding(0, 5, 0, 5);
        tv_day_mode.setPadding(0, 5, 0, 5);
        tv_sepia_mode.setPadding(0, 5, 0, 5);

        tv_night_mode.setOnClickListener(this);
        tv_day_mode.setOnClickListener(this);
        tv_sepia_mode.setOnClickListener(this);
    }

    private void setTypefacesInViews() {
        mTextLeftAlign.setTypeface(typeface);
        mTextCenterAlign.setTypeface(typeface);
        mTextRightAlign.setTypeface(typeface);
        mTextJustify.setTypeface(typeface);

        mTextLeftAlign.setAllCaps(false);
        mTextCenterAlign.setAllCaps(false);
        mTextRightAlign.setAllCaps(false);
        mTextJustify.setAllCaps(false);

        spaceLevelOne.setTypeface(typeface);
        spaceLevelTwo.setTypeface(typeface);
        spaceLevelThree.setTypeface(typeface);

        spaceLevelOne.setAllCaps(false);
        spaceLevelTwo.setAllCaps(false);
        spaceLevelThree.setAllCaps(false);

        tv_night_mode.setTypeface(typeface);
        tv_day_mode.setTypeface(typeface);
        tv_sepia_mode.setTypeface(typeface);

        tv_night_mode.setAllCaps(false);
        tv_day_mode.setAllCaps(false);
        tv_sepia_mode.setAllCaps(false);

        marginLevelOne.setTypeface(typeface);
        marginLevelTwo.setTypeface(typeface);
        marginLevelThree.setTypeface(typeface);

        marginLevelOne.setAllCaps(false);
        marginLevelTwo.setAllCaps(false);
        marginLevelThree.setAllCaps(false);

    }

    private void setTextInViews() {
        mTextLeftAlign.setText(CustomPlayerUIConstants.LEFT_ALIGN);
        mTextCenterAlign.setText(CustomPlayerUIConstants.CENTRE_ALIGN);
        mTextRightAlign.setText(CustomPlayerUIConstants.RIGHT_ALIGN);
        mTextJustify.setText(CustomPlayerUIConstants.JUSTIFY);
    }

    private void justifyViewSettings(View view) {
        tv_justify_headline = (TextView) view.findViewById(R.id.tv_justify_headline);
        tv_margin_headline = (TextView) view.findViewById(R.id.tv_margin_headline);
        tv_justify_headline.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        tv_margin_headline.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));

        mTextLeftAlign = (TextView) view.findViewById(R.id.text_left);
        mTextCenterAlign = (TextView) view.findViewById(R.id.text_center);
        mTextRightAlign = (TextView) view.findViewById(R.id.text_right);
        mTextJustify = (TextView) view.findViewById(R.id.text_justify);

        String keyTextAlignMode = KitabooSDKModel.getInstance().getUserID() + "_" + SDKManager.getInstance().getGetLocalBookData().getBookID()
                + "_" + PrefActivity.KEY_TEXT_ALIGNMENT_MODE;
        String textAlignment = com.hurix.commons.utils.Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREF_NAME, keyTextAlignMode, "0");

        if (textAlignment != null && textAlignment.equalsIgnoreCase("2")) {
            mTextLeftAlign.setBackground(unSelectedDrawable);
            mTextRightAlign.setBackground(unSelectedDrawable);
            mTextCenterAlign.setBackground(selectedDrawable);
            mTextJustify.setBackground(unSelectedDrawable);
            mTextLeftAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextCenterAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getIconColor()));
            mTextRightAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextJustify.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        } else if (textAlignment != null && textAlignment.equalsIgnoreCase("3")) {
            mTextLeftAlign.setBackground(unSelectedDrawable);
            mTextRightAlign.setBackground(selectedDrawable);
            mTextCenterAlign.setBackground(unSelectedDrawable);
            mTextJustify.setBackground(unSelectedDrawable);
            mTextLeftAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextCenterAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextRightAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getIconColor()));
            mTextJustify.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        } else if (textAlignment != null && textAlignment.equalsIgnoreCase("4")) {
            mTextLeftAlign.setBackground(unSelectedDrawable);
            mTextRightAlign.setBackground(unSelectedDrawable);
            mTextCenterAlign.setBackground(unSelectedDrawable);
            mTextJustify.setBackground(selectedDrawable);

            mTextLeftAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextCenterAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextRightAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextJustify.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getIconColor()));

        } else if (textAlignment != null && textAlignment.equalsIgnoreCase("1")) {
            mTextLeftAlign.setBackground(selectedDrawable);
            mTextRightAlign.setBackground(unSelectedDrawable);
            mTextCenterAlign.setBackground(unSelectedDrawable);
            mTextJustify.setBackground(unSelectedDrawable);
            mTextLeftAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getIconColor()));
            mTextCenterAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextRightAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextJustify.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        }

        String keyPageMargin = KitabooSDKModel.getInstance().getUserID() + "_" + SDKManager.getInstance().getGetLocalBookData().getBookID()
                + "_" + PrefActivity.KEY_PAGE_MARGIN;
        String pageMarginLevel = com.hurix.commons.utils.Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREF_NAME, keyPageMargin, "1");

        if (pageMarginLevel != null && pageMarginLevel.equalsIgnoreCase("15")) {
            marginLevelOne.setBackground(selectedDrawable);
            marginLevelOne.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getIconColor()));
            marginLevelTwo.setBackground(unSelectedDrawable);
            marginLevelTwo.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            marginLevelThree.setBackground(unSelectedDrawable);
            marginLevelThree.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        }else if (pageMarginLevel != null && pageMarginLevel.equalsIgnoreCase("20")) {
            marginLevelOne.setBackground(unSelectedDrawable);
            marginLevelTwo.setBackground(selectedDrawable);
            marginLevelThree.setBackground(unSelectedDrawable);
            marginLevelOne.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            marginLevelTwo.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getIconColor()));
            marginLevelThree.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        }else if (pageMarginLevel != null && pageMarginLevel.equalsIgnoreCase("25")) {
            marginLevelOne.setBackground(unSelectedDrawable);
            marginLevelTwo.setBackground(unSelectedDrawable);
            marginLevelThree.setBackground(selectedDrawable);

            marginLevelOne.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            marginLevelTwo.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            marginLevelThree.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getIconColor()));
        }else {
            marginLevelOne.setBackground(unSelectedDrawable);
            marginLevelTwo.setBackground(unSelectedDrawable);
            marginLevelThree.setBackground(unSelectedDrawable);
            marginLevelOne.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            marginLevelTwo.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            marginLevelThree.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        }

        mTextLeftAlign.setOnClickListener(this);
        mTextCenterAlign.setOnClickListener(this);
        mTextRightAlign.setOnClickListener(this);
        mTextJustify.setOnClickListener(this);

        marginLevelOne.setOnClickListener(this);
        marginLevelTwo.setOnClickListener(this);
        marginLevelThree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == mTextLeftAlign) {

            if (selectedDrawable != null && unSelectedDrawable != null) {
                unSelectAllTextAlignBackground();
                drawActiveTextBackground(mTextLeftAlign);
                epubSettingPanelListner.setPageTextAlignment("1");
            }

        } else if (v == mTextRightAlign) {

            if (selectedDrawable != null && unSelectedDrawable != null) {
                unSelectAllTextAlignBackground();
                drawActiveTextBackground(mTextRightAlign);
                epubSettingPanelListner.setPageTextAlignment("3");
            }
        } else if (v == mTextCenterAlign) {

            if (selectedDrawable != null && unSelectedDrawable != null) {
                unSelectAllTextAlignBackground();
                drawActiveTextBackground(mTextCenterAlign);
                epubSettingPanelListner.setPageTextAlignment("2");
            }
        } else if (v == mTextJustify) {

            if (selectedDrawable != null && unSelectedDrawable != null) {
                unSelectAllTextAlignBackground();
                drawActiveTextBackground(mTextJustify);
                epubSettingPanelListner.setPageTextAlignment("4");
            }
        }
        else if (v == tvResetSetting) {
            String keyFontSize = KitabooSDKModel.getInstance().getUserID() + "_" + SDKManager.getInstance().getGetLocalBookData().getBookID()
                    + "_" + PrefActivity.KEY_FONT_SIZE;
            com.hurix.epubreader.Utility.Utils.insertSharedPrefernceStringValues(mContext, PrefActivity.SETTING_PANEL_PREF_NAME, keyFontSize, "textSizeOne");
            if(epubSettingPanelListner != null)
                epubSettingPanelListner.setPageTextAlignment("0");
            unSelectAllTextAlignBackground();
          //  drawActiveTextBackground(mTextLeftAlign);

            if (epubSettingPanelListner != null) {
                epubSettingPanelListner.onSepiaModeClicked(false);
            }
            if (epubSettingPanelListner != null) {
                epubSettingPanelListner.fontSeekBarSize(getprogress("textSizeOne"));
            }
            if(seek_font_size != null)
                seek_font_size.setProgress(0);
            unSelectAllMarginsBackground();
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageMargin("0");
            unSelectAllSpacingsBackground();
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageLineSpacing("0");
            selectValue(fontSpinner,"Default");
        }
        else if (v == tv_night_mode) {
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.onNightmodePressed(true);
        } else if (v == tv_day_mode) {
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.onSepiaModeClicked(false);
        } else if (v == tv_sepia_mode) {
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.onSepiaModeClicked(true);
        } else if (v == mResetButton) {
            toggle.setChecked(true);
            verticalModeOn();
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.onSepiaModeClicked(false);
        } else if (v == toggle) {
            if (!isVerticalMode) {
               // toggle.setText("ON");
                isVerticalMode = true;
                verticalModeOn();
            } else {
                //toggle.setText("OFF");
                verticalModeOff();
                isVerticalMode = false;
            }
        } else if (v == marginLevelOne) {
            unSelectAllMarginsBackground();
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageMargin("15");
            drawActiveTextBackground(marginLevelOne);
        } else if (v == marginLevelTwo) {
            unSelectAllMarginsBackground();
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageMargin("20");
            drawActiveTextBackground(marginLevelTwo);
        } else if (v == marginLevelThree) {
            unSelectAllMarginsBackground();
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageMargin("25");
            drawActiveTextBackground(marginLevelThree);
        } else if (v == spaceLevelOne) {
            unSelectAllSpacingsBackground();
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageLineSpacing("1.4");
            drawActiveTextBackground(spaceLevelOne);
        } else if (v == spaceLevelTwo) {
            unSelectAllSpacingsBackground();
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageLineSpacing("1.6");
            drawActiveTextBackground(spaceLevelTwo);
        } else if (v == spaceLevelThree) {
            unSelectAllSpacingsBackground();
            if (epubSettingPanelListner != null)
                epubSettingPanelListner.setPageLineSpacing("1.8");
            drawActiveTextBackground(spaceLevelThree);
        }
    }

    private void unSelectAllTextAlignBackground() {
        if (unSelectedDrawable != null) {
            mTextLeftAlign.setBackground(unSelectedDrawable);
            mTextRightAlign.setBackground(unSelectedDrawable);
            mTextCenterAlign.setBackground(unSelectedDrawable);
            mTextJustify.setBackground(unSelectedDrawable);

            mTextLeftAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextCenterAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextRightAlign.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            mTextJustify.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        }
    }

    private void unSelectAllMarginsBackground() {
        if (unSelectedDrawable != null && marginLevelOne!=null && marginLevelTwo!=null && marginLevelThree!=null) {
            marginLevelOne.setBackground(unSelectedDrawable);
            marginLevelTwo.setBackground(unSelectedDrawable);
            marginLevelThree.setBackground(unSelectedDrawable);

            marginLevelOne.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            marginLevelTwo.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            marginLevelThree.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        }
    }

    private void unSelectAllSpacingsBackground() {
        if (unSelectedDrawable != null && spaceLevelOne!=null && spaceLevelTwo!=null && spaceLevelThree!=null) {
            spaceLevelOne.setBackground(unSelectedDrawable);
            spaceLevelTwo.setBackground(unSelectedDrawable);
            spaceLevelThree.setBackground(unSelectedDrawable);

            spaceLevelOne.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            spaceLevelTwo.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
            spaceLevelThree.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getTextColor()));
        }
    }

    private void drawActiveTextBackground(TextView view) {
        if (selectedDrawable != null) {
            view.setBackground(selectedDrawable);
            view.setTextColor(Color.parseColor(_readerThemeSettingVo.getReader().getDayMode().getFontSettings().getFont().getIconColor()));
        }
    }

    private void sizeSeekbarSettings(View view) {
        seek_font_size = (SeekBar) view.findViewById(R.id.seek_font_size);
        seek_font_size.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.seekbar));
        seek_font_size.setThumb(mContext.getResources().getDrawable(R.drawable.seekbar_thumb));
        seek_font_size.setMax(4);
        //seek_font_size.setProgress(getprogress(Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREFS_NAME, PrefActivity.KEY_FONT_SIZE, "mediumFont")));
        String keyFontSize = KitabooSDKModel.getInstance().getUserID() + "_" + SDKManager.getInstance().getGetLocalBookData().getBookID()
                + "_" + PrefActivity.KEY_FONT_SIZE;
        seek_font_size.setProgress(getprogress(com.hurix.commons.utils.Utils.getSharedPreferenceStringValue(mContext, PrefActivity.SETTING_PANEL_PREF_NAME, keyFontSize, "textSizeOne")));

        seek_font_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

                if (epubSettingPanelListner != null){
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            epubSettingPanelListner.fontSeekBarSize(seekBar.getProgress());
                        }
                    }, 300);
                }

                /*   //all font related changes handled by webview font setting*/

            }

        });
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

        GradientDrawable nightMode = (GradientDrawable) tv_night_mode.getBackground();
        nightMode.setColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getNight().getTabBg()));

        GradientDrawable sepiaMode = (GradientDrawable) tv_sepia_mode.getBackground();
        sepiaMode.setColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getSepia().getTabBg()));

        GradientDrawable dayMode = (GradientDrawable) tv_day_mode.getBackground();
        dayMode.setColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getFontSettings().getOther().getMode().getDay().getTabBg()));
    }

    private void verticalModeOn() {
        if (epubSettingPanelListner != null)
            epubSettingPanelListner.setPageScrollMode(true);
    }

    private void verticalModeOff() {
        if (epubSettingPanelListner != null)
            epubSettingPanelListner.setPageScrollMode(false);
    }

    private void selectValue(Spinner spinner, String value) {
        String font="Default";
        if(value.equals("sans"))
            font="Open Sans";
        else if (value.equals("georgia"))
            font="Georgia";
        else if (value.equals("noto"))
            font="Noto Serif";

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(font)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
