package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.hurix.commons.KitabooSDKModel;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.datamodel.HighlightVO;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.interfaces.IDestroyable;
import com.hurix.customui.mydata.OldMyDataFragment;
import com.hurix.customui.mydata.UGCBaseDataFragment;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.kitaboocloud.views.OnDialogSwipeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class CustomMyDataLayout extends LinearLayout implements IDestroyable, View.OnClickListener,
        OnItemClickListener, CustomDataFilter.FilterListener, OnTabChangeListener, AddMyDataFilterListener {

    //Constant data use for filter data
    public static final int ALL_DATA = 100;
    public static final int SHARED_WITH_ME = 200;
    public static final int SHARED_WITH_ME_ACTION_TAKEN_YES = 201;
    public static final int SHARED_WITH_ME_ACTION_TAKEN_NO = 202;
    public static final int NOTES_DATA = 301;
    private final Context mContext;
    private final LayoutInflater mLayoutinfilater;
    //user controls
    private ListView mLstugclistdata;
    private final ListView mLstNewNoteShare;
    private final TextView mTxtmessage;
    private final TextView mTitle;
    private final TextView mShareSettings;
    //tmp view
    private final View mView;
    private View mMydataview;
    private View mSharedwithmeview;
    private View mUnderline;
    boolean isImpSelected = false;
    private final RelativeLayout mMainController;
    private CustomMydataListAdapter mNotenotifyadapter;
    private String UGCPath = "";
    private ArrayList<HighlightVO> alldatalist = new ArrayList<HighlightVO>();
    private ArrayList<HighlightVO> currList = new ArrayList<HighlightVO>();
    private boolean isSortByPage = true;
    //private IJumptopage _listner;
    private final ProgressBar mProgDataLaoding;
    private CustomDataFilter mDataFilter;
    private TextView mImportant, mMydatatitle, mSharedwithmetitle, mMydatacount, mSharedwithmecount;
    private Button mBtnnotes, mBtnhighlight;
    private int filterBy;
    private OldMyDataFragment mUgcHolder;
    private View mViewNoteNotificationDivider;
    private boolean mIsSharedWithMe = false;
    private LinearLayout mAllSharedContainer;
    private final TabHost mTabHost;
    private final Drawable mSelectedDrawable;
    private final Drawable mUnSelectedDrawable;
    private final String TAB_MY_DATA = "Notes";
    private final String TAB_SHARED_DATA = "Highlights";
    private String mFilter_by = CustomDataFilter.NOTES;
    private String lastTab = "";
    private CustomMydataLayoutCallback mListner;
    private boolean mIsMobile = false;
    private int mShareSettingEnable;
    private final ReaderThemeSettingVo readerThemeSettingVo;
    private Typeface typeFace;
    private final TextView mTextFilter;
    private final TextView mTextNotification;
    private final LinearLayout mParentTabLayout;
    private View mViewToAnimate;
    private RelativeLayout mFilterLayout;
    private ArrayList<HighlightVO> mCurrentSelectedList;
    private MobileHighlightFilterDialog filterDialog;
    private ArrayList<HighlightVO> ugcAcceptRejectList;
    private ArrayList<String> mCurrentSelectedCheckBox, mCurrentSelectedNoteCheckbox;
    private ArrayList<HighlightVO> mUpdatedList;
    private final RelativeLayout middlecontainer;
    private boolean isFromOnTabChanged;
    private MobileNoteFilterDialog mMobileNotesfilterDialog;
    private ArrayList<HighlightVO> mNotificationList;
    private ArrayList<String> mNoteSelectedList, mContextualNoteSelectedList;
    private View mRoundIndicator;
    private Toolbar mToolbar;

    public void setUGCHolder(UGCBaseDataFragment fragment) {
        //this.mUgcHolder =  (MyDataFragment)fragment;
    }

    public CustomMyDataLayout(Context context, boolean isMobile, ReaderThemeSettingVo themeUserSettingVo, int enableShareSetting) {
        super(context);
        this.mContext = context;
        this.readerThemeSettingVo = themeUserSettingVo;
        this.mIsMobile = isMobile;
        mShareSettingEnable = enableShareSetting;
        mLayoutinfilater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (isMobile) {
            mView = mLayoutinfilater.inflate(R.layout.custom_mydata_mobile_view, this);
        } else {
            mView = mLayoutinfilater.inflate(R.layout.custom_mydata_tab_view, this);
            mViewToAnimate = mView.findViewById(R.id.animate_view);
        }

       // mView.setBackgroundColor(Color.TRANSPARENT);
        initTabView(isMobile);
        mTitle = (TextView) mView.findViewById(R.id.title);
        mTextNotification = (TextView) mView.findViewById(R.id.share_notification);
        mShareSettings = (TextView) mView.findViewById(R.id.sharingSettings);
        mTextFilter = (TextView) mView.findViewById(R.id.text_filer);
        mParentTabLayout = (LinearLayout) mView.findViewById(R.id.tabHolder);
        middlecontainer = (RelativeLayout) mView.findViewById(R.id.middleContainer);
        mRoundIndicator = mView.findViewById(R.id.alert_round_indicator);

        if (getContext().getResources().getBoolean(R.bool.is_bell_icon_enabled)) {
            mTextNotification.setVisibility(VISIBLE);
            mRoundIndicator.setVisibility(VISIBLE);
        } else {
            mTextNotification.setVisibility(GONE);
            mRoundIndicator.setVisibility(GONE);
        }

        mMainController = (RelativeLayout) findViewById(R.id.ugcMainLayoutID);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getPopupBackground()));

        if (!SDKManager.getInstance().isClassAssociated()) {
            mShareSettings.setEnabled(false);
            mShareSettings.setAlpha(0.5f);
            mShareSettings.setVisibility(GONE);
        }

        if (isMobile) {
            mMainController.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.
                            getReader().getDayMode().getMyData().getPopupBackground()),
                    new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2, Color.parseColor((readerThemeSettingVo.
                            getReader().getDayMode().getMyData().getPopupBorder()))));
        }
        mSelectedDrawable = new ColorBarDrawable(Color.parseColor(readerThemeSettingVo.
                getReader().getDayMode().getMyData().getTabBorder()), 5);
        mUnSelectedDrawable = new ColorBarDrawable(Color.parseColor(readerThemeSettingVo.
                getReader().getDayMode().getMyData().getTabBorder()), 0);
        mTabHost = mView.findViewById(R.id.ugctab);
        //  mTabHost.setBackgroundColor(Color.WHITE);
        // middlecontainer.setBackgroundColor((Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getPopupBackground())));
        middlecontainer.setBackgroundDrawable(com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.
                        getReader().getDayMode().getMyData().getPopupBackground()),
                new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 1, Color.parseColor(readerThemeSettingVo.
                        getReader().getDayMode().getMyData().getPopupBorder())));

        mShareSettings.setOnClickListener(this);
        mTextFilter.setOnClickListener(this);

        mLstNewNoteShare = mView.findViewById(R.id.lstNoteshareAcceptReject);
        mLstNewNoteShare.setOnItemClickListener(this);
        // mLstNewNoteShare.setEmptyView(mView.findViewById(R.id.empty_view));
        mTxtmessage = mView.findViewById(R.id.txtmessage);
        mTxtmessage.setBackgroundColor(Color.TRANSPARENT);
        mTxtmessage.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getTabBorder()));
        mTxtmessage.setVisibility(View.GONE);

        mProgDataLaoding = mView.findViewById(R.id.progLoading);
        if (getContext().getResources().getBoolean(R.bool.is_highlight_button_enabled)) {
            lastTab = TAB_SHARED_DATA;
        } else {
            lastTab = TAB_MY_DATA;
        }
        setUpIconFonts();
        initdata(false);

        mLstNewNoteShare.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        mView.findViewById(R.id.toolbar).setOnTouchListener(new OnDialogSwipeListener(getContext()) {

            public void onSwipeTop() {
                mListner.OnViewExpand();
            }

            public void onSwipeBottom() {

                mListner.OnViewMinimize();
            }


            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });


    }

    public void setListner(CustomMydataLayoutCallback listner) {
        mListner = listner;
    }

    private void initTabView(boolean isMobile) {

        if (isMobile) {
            mMydataview = mLayoutinfilater.inflate(R.layout.custom_new_tabhost_view, null);
            mSharedwithmeview = mLayoutinfilater.inflate(R.layout.custom_new_tabhost_view, null);
        } else {
            mMydataview = mLayoutinfilater.inflate(R.layout.custom_new_tablet_tabhost_view, null);
            mSharedwithmeview = mLayoutinfilater.inflate(R.layout.custom_new_tablet_tabhost_view, null);
        }

        mMydataview.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getPopupBackground()));
        mMydatatitle = (TextView) mMydataview.findViewById(R.id.title);
        mMydatacount = (TextView) mMydataview.findViewById(R.id.count);
        mMydatatitle.setText(getResources().getString(R.string.note_title));
        mMydatacount.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSelectedButton().getTextColor()));
        mMydatacount.setText("0");

        if (isMobile) {
            mMydatatitle.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
        }
        mSharedwithmeview.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getPopupBackground()));
        mSharedwithmetitle = (TextView) mSharedwithmeview.findViewById(R.id.title);
        mSharedwithmecount = (TextView) mSharedwithmeview.findViewById(R.id.count);
        mSharedwithmetitle.setText(getResources().getString(R.string.highlight_title));
        mSharedwithmecount.setText("0");
        mSharedwithmecount.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSelectedButton().getTextColor()));
        if (isMobile) {
            mMydatatitle.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
            mMydataview.setRotation(180);
            mSharedwithmeview.setRotation(180);
            mMydatatitle.setRotation(180);
            mSharedwithmetitle.setRotation(180);
            mMydatacount.setRotation(180);
            mSharedwithmecount.setRotation(180);
        }
    }

    public void showToolbar() {
        mView.findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((LinearLayout.LayoutParams) mView.findViewById(R.id.toolbar).getLayoutParams()).weight = 0.5f;
            ((LinearLayout.LayoutParams) mParentTabLayout.getLayoutParams()).weight = 0.9f;
            ((LinearLayout.LayoutParams) middlecontainer.getLayoutParams()).weight = 8.6f;

        } else {
            ((LinearLayout.LayoutParams) mView.findViewById(R.id.toolbar).getLayoutParams()).weight = 0.5f;
            ((LinearLayout.LayoutParams) mParentTabLayout.getLayoutParams()).weight = 0.9f;
        }
        mParentTabLayout.setBackground(getResources().getDrawable(R.drawable.my_data_tab_exapand_background));
        mParentTabLayout.setBackgroundDrawable(com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getPopupBackground()),
                new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 1, Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getPopupBorder())));
        mTabHost.setBackground(getResources().getDrawable(R.drawable.bottom_expand_drwable));
    }

    public void hideToolbar() {
        mViewToAnimate.setAlpha(0f);
        mViewToAnimate.setVisibility(View.GONE);
        mView.findViewById(R.id.toolbar).setVisibility(View.GONE);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((LinearLayout.LayoutParams) mView.findViewById(R.id.toolbar).getLayoutParams()).weight = 0;
            ((LinearLayout.LayoutParams) mParentTabLayout.getLayoutParams()).weight = 3.0f;
            ((LinearLayout.LayoutParams) middlecontainer.getLayoutParams()).weight = 7.0f;

        } else {
            ((LinearLayout.LayoutParams) mView.findViewById(R.id.toolbar).getLayoutParams()).weight = 0;
            ((LinearLayout.LayoutParams) mParentTabLayout.getLayoutParams()).weight = 1.4f;

        }
        mParentTabLayout.setPadding(1, 0, 0, 0);
        //  mParentTabLayout.setBackground(getResources().getDrawable(R.drawable.tablet_mydata_widget_background));
        //       mParentTabLayout.setBackgroundColor(Color.RED);
        mParentTabLayout.setBackgroundDrawable(com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getPopupBackground()),
                new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 1, Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getPopupBorder())));
        mTabHost.setBackground(getResources().getDrawable(R.drawable.bottom_sheet_background));
    }

    public void enableSetting(int visibility) {
        mShareSettingEnable = visibility;
        if (mIsMobile) {
            mView.findViewById(R.id.toolbar).setVisibility(VISIBLE);
        }
        mView.findViewById(R.id.shareSettinglayout).setVisibility(VISIBLE);
        mView.findViewById(R.id.share_notification).setVisibility(visibility);
    }

    public void setUpTabHost() {
        if (mTabHost.getTabContentView() != null) {
            mTabHost.setOnTabChangedListener(null);
            mTabHost.clearAllTabs();
        }
        mTabHost.setup();
        if (getContext().getResources().getBoolean(R.bool.is_highlight_button_enabled)) {
            mTabHost.addTab(mTabHost.newTabSpec(TAB_SHARED_DATA)
                    .setIndicator(mSharedwithmeview).setContent(new TabContentFactory()));
        }
        mTabHost.addTab(mTabHost.newTabSpec(TAB_MY_DATA).setIndicator(mMydataview).setContent(new TabContentFactory()));
        mTabHost.getTabWidget().setDividerDrawable(null);
        updateTab();
        mTabHost.setOnTabChangedListener(this);
    }

    class TabContentFactory implements TabHost.TabContentFactory {

        @Override
        public View createTabContent(String tag) {
            return new View(mContext);
        }
    }

    public void update() {
        initdata(true);
        disableAllViews();
    }

    public void setUGCpath(String ugcpath) {
        UGCPath = ugcpath;
    }

    private void initdata(boolean isUpdate) {
        mFilter_by = CustomDataFilter.HIGHLIGHTS;
        selectHighlights();
        setUpTabHost();
        selectSortByPage();
        selectAllData();
        deSelectAllTabs();
        isImpSelected = false;
        mTxtmessage.setVisibility(View.GONE);
    }


    public void disableAllViews() {
        //commented for disabling loader while coming back from sharing setting screen
        //mProgDataLaoding.setVisibility(View.VISIBLE);

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setEnabled(false);
        }

    }

    public void enableAllViews() {
        mProgDataLaoding.setVisibility(View.INVISIBLE);

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setEnabled(true);
        }
    }

    private void setUpIconFonts() {
        setReaderTyface();
        mShareSettings.setTypeface(typeFace);
        mShareSettings.setAllCaps(false);
        mShareSettings.setText("B");
        mShareSettings.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getDisabledIcon().getIconColor()));

        mTextFilter.setTypeface(typeFace);
        mTextFilter.setAllCaps(false);
        mTextFilter.setText("Ḟ");
        mTextFilter.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getIconColor()));

        mTextNotification.setTypeface(typeFace);
        mTextNotification.setAllCaps(false);
        mTextNotification.setText("ş");
        mTextNotification.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getIconColor()));
        mTextNotification.setOnClickListener(this);


        mTitle.setText(getResources().getString(R.string.ugc_my_data) + ":" + " " + getResources().getString(R.string.highlight_title));
        mTitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSelectedTextColor()));

        //mShareSettings.setVisibility(mShareSettingEnable);

        if(! SDKManager.getInstance().isClassAssociated()){
            mShareSettings.setVisibility(GONE);
        }else{
            mShareSettings.setVisibility(VISIBLE);
        }


    }

    private void setReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            typeFace = Typefaces.get(mContext, fontfile);
        } else {
            typeFace = Typefaces.get(mContext, "kitabooread.ttf");
        }
    }

    @Override
    public void onNoteFilterApplyButtonClicked(ArrayList<String> notelist, ArrayList<String> contextNoteList) {
        if (mMobileNotesfilterDialog != null) {
            mMobileNotesfilterDialog.dismiss();
        }
        mNoteSelectedList = notelist;
        mContextualNoteSelectedList = contextNoteList;
        ArrayList<HighlightVO> mFilterList = new ArrayList<>();
        ArrayList<HighlightVO> mFilterContextNote = new ArrayList<>();

        for (HighlightVO vo : mCurrentSelectedList) {
            if (vo.getHighlightedText().isEmpty()) {
                for (int j = 0; j < notelist.size(); j++) {
                    if (vo.getCreatedByUserVO().getUserID() != KitabooSDKModel.getInstance().getUserID()) {
                        if (notelist.get(j).equalsIgnoreCase("shared_note")) {
                            mFilterList.add(vo);
                        }
                    } else if (vo.getColor().equalsIgnoreCase(notelist.get(j))) {
                        mFilterList.add(vo);
                    }


                }
            } else if (!vo.getHighlightedText().isEmpty()) {
                for (int k = 0; k < contextNoteList.size(); k++) {
                    if (vo.getCreatedByUserVO().getUserID() != KitabooSDKModel.getInstance().getUserID()) {
                        if (contextNoteList.get(k).equalsIgnoreCase("shared_note")) {
                            mFilterList.add(vo);
                        }
                    } else if (vo.getColor().equalsIgnoreCase(contextNoteList.get(k))) {
                        mFilterList.add(vo);
                    }
                }
            }
        }
        Collections.reverse(mFilterList);
        if (mFilterList.size() != 0) {
            mMydatacount.setVisibility(View.VISIBLE);
            mMydatacount.setText(String.valueOf(mFilterList.size()));
        } else {
            mMydatacount.setVisibility(View.GONE);
        }
        mView.findViewById(R.id.empty_view).setVisibility(View.GONE);
        mView.findViewById(R.id.note_empty_view).setVisibility(View.GONE);
        mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
        mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
        mNotenotifyadapter.setMydataListener(mListner);
        mNotenotifyadapter.setData(mFilterList, 0, readerThemeSettingVo);
        mLstNewNoteShare.setAdapter(mNotenotifyadapter);
        if (mFilterList.size() == 0) {
            // mView.findViewById(R.id.empty_view).setVisibility(View.GONE);
            //mView.findViewById(R.id.note_empty_view).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.filter_empty_view).setVisibility(View.VISIBLE);
            ((TextView) mView.findViewById(R.id.filter_empty_view)).setText(getResources().getString(R.string.no_filter_note_msg));
            // setNoteEmptyView("" , getResources().getString(R.string.no_filter_note_msg) ,"" ,"");
        }

    }

    @Override
    public void onApplyButtonClicked(ArrayList<String> selectedColorList) {
        if (filterDialog != null) {
            filterDialog.dismiss();
        }

        if (mMobileNotesfilterDialog != null) {
            mMobileNotesfilterDialog.dismiss();
        }
        if (selectedColorList != null && selectedColorList.size() > 0 && !selectedColorList.get(0).equalsIgnoreCase("all")) {
            String type = "";


            mUpdatedList = new ArrayList<>();

            for (HighlightVO highlightVO : mCurrentSelectedList) {
                for (int i = 0; i < selectedColorList.size(); i++) {

                    if (selectedColorList.get(i).equalsIgnoreCase("shared_highlight")) {
                        boolean isEditEnabled = highlightVO.getCreatedByUserVO().getUserID() ==
                                KitabooSDKModel.getInstance().getUserID();
                        if (!isEditEnabled && highlightVO.getNoteData().isEmpty()) {
                            mUpdatedList.add(highlightVO);
                        }
                    } else if (highlightVO.getColor().equalsIgnoreCase(selectedColorList.get(i))) {
                        boolean isEditEnabled = highlightVO.getCreatedByUserVO().getUserID() ==
                                KitabooSDKModel.getInstance().getUserID();
                        if (isEditEnabled && highlightVO.getNoteData().isEmpty()) {
                            mUpdatedList.add(highlightVO);
                        }
                    }
                }
            }

            Collections.reverse(mUpdatedList);
            if (mTabHost.getCurrentTab() == 0) {
                if (mUpdatedList.size() != 0) {
                    mSharedwithmecount.setVisibility(View.VISIBLE);
                    mSharedwithmecount.setText(String.valueOf(mUpdatedList.size()));
                } else {
                    mSharedwithmecount.setVisibility(View.GONE);
                }
                mCurrentSelectedCheckBox = selectedColorList;
            } else if (mTabHost.getCurrentTab() == 1) {
                if (mUpdatedList.size() != 0) {
                    mMydatacount.setVisibility(View.VISIBLE);
                    mMydatacount.setText(String.valueOf(mUpdatedList.size()));
                } else {
                    mMydatacount.setVisibility(View.GONE);
                }
                mCurrentSelectedNoteCheckbox = selectedColorList;
            }
            mView.findViewById(R.id.empty_view).setVisibility(View.GONE);
            mView.findViewById(R.id.note_empty_view).setVisibility(View.GONE);
            mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
            mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
            mNotenotifyadapter.setMydataListener(mListner);
            mNotenotifyadapter.setData(mUpdatedList, 0, readerThemeSettingVo);
            mLstNewNoteShare.setAdapter(mNotenotifyadapter);
            if (mUpdatedList.size() == 0) {
                // mView.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                //mView.findViewById(R.id.note_empty_view).setVisibility(View.GONE);
                mView.findViewById(R.id.filter_empty_view).setVisibility(View.VISIBLE);
                ((TextView) mView.findViewById(R.id.filter_empty_view)).setText(getResources().getString(R.string.no_filter_highlight_msg));
                // setEmptyView("" ,getResources().getString(R.string.no_filter_highlight_msg) ,"" ,"");
            }
        } else {
            mView.findViewById(R.id.empty_view).setVisibility(View.GONE);
            mView.findViewById(R.id.note_empty_view).setVisibility(View.GONE);
            mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
            mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
            mUpdatedList = new ArrayList<>();
            for (HighlightVO highlightVO : mCurrentSelectedList) {
                if (highlightVO.getNoteData().isEmpty()) {
                    mUpdatedList.add(highlightVO);
                }
            }

            Collections.reverse(mUpdatedList);
            if (mTabHost.getCurrentTab() == 0) {
                if (mUpdatedList.size() != 0) {
                    mSharedwithmecount.setVisibility(View.VISIBLE);
                    mSharedwithmecount.setText(String.valueOf(mUpdatedList.size()));
                } else {
                    mSharedwithmecount.setVisibility(View.GONE);
                }
                mCurrentSelectedCheckBox = selectedColorList;
            } else if (mTabHost.getCurrentTab() == 1) {
                if (mUpdatedList.size() != 0) {
                    mMydatacount.setVisibility(View.VISIBLE);
                    mMydatacount.setText(String.valueOf(mUpdatedList.size()));
                } else {
                    mMydatacount.setVisibility(View.GONE);
                }
                mCurrentSelectedNoteCheckbox = selectedColorList;
            }

            mNotenotifyadapter.setMydataListener(mListner);
            mNotenotifyadapter.setData(mUpdatedList, 0, readerThemeSettingVo);
            mLstNewNoteShare.setAdapter(mNotenotifyadapter);

        }
    }

    @Override
    public void onClick(View v) {
        if (v == mTextFilter) {
            Activity activity = (Activity) mContext;
            // if (mIsMobile) {
            if (mTabHost.getCurrentTab() == 0) {
                filterDialog = new MobileHighlightFilterDialog(readerThemeSettingVo, mShareSettingEnable);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isMobile", mIsMobile);
                filterDialog.setArguments(bundle);
                filterDialog.setListener(this);
                filterDialog.setCurrentSelectedCheckBox(mCurrentSelectedCheckBox);
                filterDialog.show(activity.getFragmentManager(), "");
            } else {
                mMobileNotesfilterDialog = new MobileNoteFilterDialog(readerThemeSettingVo, mShareSettingEnable);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isMobile", mIsMobile);
                mMobileNotesfilterDialog.setArguments(bundle);
                mMobileNotesfilterDialog.setListener(this);
                mMobileNotesfilterDialog.setCurrentSelectedCheckBox(mNoteSelectedList, mContextualNoteSelectedList);
                mMobileNotesfilterDialog.show(activity.getFragmentManager(), "");
            }

            //}


        } else if (v == mTextNotification) {
            mListner.onMyDataNotificationClicked(mNotificationList);
        } else if (v.getId() == mShareSettings.getId()) {
            mListner.settingsClicked();

        }
    }

    public void deSelectAllTabs() {
        showAllSharedLayout();
    }

    private void selectSortByPage() {
        isSortByPage = true;
    }

    private void selectNotes() {
        mFilter_by = CustomDataFilter.NOTES;
    }

    private void selectHighlights() {
        mFilter_by = CustomDataFilter.HIGHLIGHTS;
    }

    private void selectAllData() {
        mIsSharedWithMe = false;
        filterBy = ALL_DATA;
        mMydatacount.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSelectedButton().getTextColor()));
        mSharedwithmecount.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSelectedButton().getTextColor()));
    }

    public void setData(ArrayList<HighlightVO> list) {
        alldatalist = list;
        if (mDataFilter == null) {
            mDataFilter = new CustomDataFilter();
        }
        mDataFilter.setData(alldatalist, mContext);
        enableAllViews();
        //refreshData(list);
        filterListData(list, ALL_DATA);
        mCurrentSelectedList = list;
        getTheCountOfSharedWithMe(CustomDataFilter.NOTES);
        getTheCountsOfMyData(CustomDataFilter.NOTES);
        if (getContext().getResources().getBoolean(R.bool.is_highlight_button_enabled))
            getTheCountsOfMyData(CustomDataFilter.HIGHLIGHTS);
        if (lastTab.equalsIgnoreCase(TAB_MY_DATA)) {
            mTabHost.setCurrentTab(1);
            onTabChanged(TAB_MY_DATA);
        } else if (lastTab.equalsIgnoreCase(TAB_SHARED_DATA)) {
            mTabHost.setCurrentTab(0);
            onTabChanged(TAB_SHARED_DATA);
        }
    }

    public void refreshData(ArrayList<HighlightVO> list) {
        ugcAcceptRejectList = new ArrayList<>();
        mCurrentSelectedList = list;
        mLstNewNoteShare.setVisibility(View.VISIBLE);
        // ugcAcceptRejectList = filterListData(list, SHARED_WITH_ME_ACTION_TAKEN_NO);
        int length = 0;
        mNotificationList = filterListData(list, SHARED_WITH_ME_ACTION_TAKEN_NO);
        if (getContext().getResources().getBoolean(R.bool.is_bell_icon_enabled))
            if (mNotificationList.size() == 0) {
                mTextNotification.setEnabled(false);
                mTextNotification.setAlpha(0.5f);
                mRoundIndicator.setVisibility(View.GONE);
                mTextNotification.setVisibility(View.GONE);
            } else {
                mTextNotification.setEnabled(true);
                mTextNotification.setAlpha(1f);
                mTextNotification.setVisibility(View.VISIBLE);
                mRoundIndicator.setVisibility(View.VISIBLE);
            }
        list = filterListData(list, SHARED_WITH_ME_ACTION_TAKEN_YES);
        ugcAcceptRejectList.addAll(list);

        if (!ugcAcceptRejectList.isEmpty()) {
            mTextFilter.setAlpha(1f);
            mTextFilter.setEnabled(true);
            mView.findViewById(R.id.empty_view).setVisibility(View.GONE);
            mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
            mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
            mView.findViewById(R.id.note_empty_view).setVisibility(View.GONE);
            if (mNotenotifyadapter == null) {
                mNotenotifyadapter = new CustomMydataListAdapter(mContext, mShareSettingEnable);
                mNotenotifyadapter.setMydataListener(mListner);
                mNotenotifyadapter.setData(ugcAcceptRejectList, length, readerThemeSettingVo);
                mNotenotifyadapter.setShareSettingVisibility(mShareSettingEnable);
                mLstNewNoteShare.setAdapter(mNotenotifyadapter);
            } else {
                mNotenotifyadapter.setMydataListener(mListner);
                mNotenotifyadapter.setData(ugcAcceptRejectList, length, readerThemeSettingVo);
                mNotenotifyadapter.notifyDataSetChanged();
            }
        } else {
            mLstNewNoteShare.setVisibility(View.GONE);
            if (mFilter_by.equalsIgnoreCase(CustomDataFilter.NOTES)) {
                mTextFilter.setAlpha(0.5f);
                mTextFilter.setEnabled(false);
                mView.findViewById(R.id.empty_view).setVisibility(View.GONE);
                mView.findViewById(R.id.note_empty_view).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
                mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
                setNoteEmptyView(CustomPlayerUIConstants.NOTE_ICON_TEXT, getResources().getString(R.string.empty_note_first_line),
                        getResources().getString(R.string.empty_note_second_line),
                        getResources().getString(R.string.empty_note_third_line));
            } else if (mFilter_by.equalsIgnoreCase(CustomDataFilter.HIGHLIGHTS)) {
                mTextFilter.setAlpha(0.5f);
                mTextFilter.setEnabled(false);
                mView.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
                mView.findViewById(R.id.filter_empty_view).setVisibility(View.GONE);
                mView.findViewById(R.id.note_empty_view).setVisibility(View.GONE);
                setEmptyView(CustomPlayerUIConstants.HIGHLIGHT_ICON_TEXT, getResources().getString(R.string.empty_highlight_first_line),
                        getResources().getString(R.string.empty_highlight_second_line), "");
            }
        }

    }

    public void refreshMyDataNoteFragment() {
        if (mNotenotifyadapter != null)
            mNotenotifyadapter.notifyDataSetChanged();
    }

    private void setNoteEmptyView(String iconText, String firstline, String secondline, String thirdline) {
        TextView bookmarkIcon = mView.findViewById(R.id.hint_icon);
        TextView bookmarkHintText = mView.findViewById(R.id.first_hint_text);
        TextView bookmarkhintmiddleText = mView.findViewById(R.id.second_hint_text);
        TextView hintThirdText = mView.findViewById(R.id.third_hint_text);
        bookmarkIcon.setText(iconText);
        bookmarkIcon.setAllCaps(false);
        bookmarkIcon.setTypeface(typeFace);
        bookmarkHintText.setText(firstline);
        bookmarkhintmiddleText.setText(secondline);
        hintThirdText.setText(thirdline);
        if (mIsMobile) {
            bookmarkhintmiddleText.setTextSize(15);
            bookmarkHintText.setTextSize(15);
            hintThirdText.setTextSize(15);
            bookmarkIcon.setTextSize(90);
        } else {
            bookmarkhintmiddleText.setTextSize(21);
            bookmarkHintText.setTextSize(21);
            hintThirdText.setTextSize(21);
            bookmarkIcon.setTextSize(180);
        }

        bookmarkIcon.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getTextColor()));
        bookmarkHintText.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getTextColor()));
        bookmarkhintmiddleText.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getTextColor()));
        hintThirdText.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getTextColor()));
    }

    private void setEmptyView(String iconText, String firstline, String secondline, String thirdline) {
        TextView bookmarkIcon = mView.findViewById(R.id.book_mark_hint_icon);
        TextView bookmarkHintText = mView.findViewById(R.id.book_mark_hint_text);
        TextView bookmarkhintmiddleText = mView.findViewById(R.id.book_mark_hint_middle_text);
        // TextView hintThirdText = (TextView) mView.findViewById(R.id.third_line_note_hint);
        bookmarkIcon.setText(iconText);
        bookmarkIcon.setAllCaps(false);
        bookmarkIcon.setTypeface(typeFace);
        bookmarkHintText.setText(firstline);
        bookmarkhintmiddleText.setText(secondline);
        //hintThirdText.setText(thirdline);
        if (mIsMobile) {
            bookmarkhintmiddleText.setTextSize(15);
            bookmarkHintText.setTextSize(15);
            //hintThirdText.setTextSize(15);
            bookmarkIcon.setTextSize(90);
        } else {
            bookmarkhintmiddleText.setTextSize(21);
            bookmarkHintText.setTextSize(21);
            //  hintThirdText.setTextSize(21);
            bookmarkIcon.setTextSize(200);
        }

        bookmarkIcon.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getTextColor()));
        bookmarkHintText.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getTextColor()));
        bookmarkhintmiddleText.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getTextColor()));
    }


    @Override
    public void onItemClick(AdapterView<?> view, View arg1, int arg2, long arg3) {
        mListner.onItemClickListener(mNotenotifyadapter.getData().get(arg2));
        // this.mUgcHolder.onItemClickListener(mNotenotifyadapter.getData().get(arg2));
    }

    public String getDisplayFormatOfDate(String input) {
        String dispFormatDate = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a dd MMM yyyy");
        try {
            Date temp = inputFormat.parse(input);
            dispFormatDate = outputFormat.format(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dispFormatDate;
    }

    private void filterData(String filter) {
        try {
            mTxtmessage.setVisibility(View.GONE);

            mDataFilter.setlistner(this);
            mDataFilter.setImpSelected(isImpSelected);
            mDataFilter.setFilterBy(filterBy);
            mDataFilter.filter(filter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFilteringCompleted(ArrayList<HighlightVO> results) {
        currList = results;
        refreshData(results);
        if (isFromOnTabChanged) {
            if (lastTab.equalsIgnoreCase(TAB_SHARED_DATA)) {
                if (mCurrentSelectedCheckBox != null) {
                    onApplyButtonClicked(mCurrentSelectedCheckBox);
                }
            } else if (lastTab.equalsIgnoreCase(TAB_MY_DATA)) {
                if (mNoteSelectedList != null && mContextualNoteSelectedList != null) {
                    onNoteFilterApplyButtonClicked(mNoteSelectedList, mContextualNoteSelectedList);
                }
            }
        } else {
            isFromOnTabChanged = false;
        }

    }

    private ArrayList<HighlightVO> filterListData(ArrayList<HighlightVO> userHighlightVos, int filterType) {
        ArrayList<HighlightVO> sortedList = new ArrayList<HighlightVO>();
        for (int i = 0; i < userHighlightVos.size(); i++) {
            HighlightVO vo = userHighlightVos.get(i);
            if (vo != null) {
                if (filterType == SHARED_WITH_ME_ACTION_TAKEN_YES) {
                    if (vo.getActionTakenStatus() != null && vo.getActionTakenStatus().equalsIgnoreCase("y")) {
                        sortedList.add(vo);
                    }
                } else if (filterType == SHARED_WITH_ME_ACTION_TAKEN_NO) {
                    if (vo.getActionTakenStatus() != null && vo.getActionTakenStatus().equalsIgnoreCase("n")) {
                        sortedList.add(vo);
                    }
                } else if ((filterType == NOTES_DATA)) {
                    filterData(mFilter_by);
                }

            }
        }
        Collections.sort(sortedList);
        return sortedList;
    }

    public void enableButtons() {
    }

    public void disableButtons() {
    }

    private void showAllSharedLayout() {
        // mAllSharedContainer.setVisibility(View.VISIBLE);
    }

    public void destroy() {

        /*if (mUgcdata != null && (mUgcdata.getStatus() == Status.PENDING || mUgcdata.getStatus() == Status.RUNNING)) {
            mUgcdata.settaskCompleteListner(null);
            mUgcdata.cancel(true);
            mUgcdata = null;
        }*/
        mNotenotifyadapter = null;

    }


    public void dismissDialog() {
        if (mMobileNotesfilterDialog != null) {
            mMobileNotesfilterDialog.dismiss();
        }
        if (filterDialog != null) {
            filterDialog.dismiss();
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundDrawable(mUnSelectedDrawable); // unselected
        }
        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundDrawable(mSelectedDrawable); // selected

        updateTabText();
        isFromOnTabChanged = true;
        if (tabId.equalsIgnoreCase(TAB_MY_DATA)) {
            lastTab = TAB_MY_DATA;
            selectNotes();
            filterData(mFilter_by);
            getTheCountsOfMyData(mFilter_by);
            mTitle.setText(getResources().getString(R.string.ugc_my_data) + ":" + " " + getResources().getString(R.string.note_title));

        } else if (tabId.equals(TAB_SHARED_DATA) &&
                getContext().getResources().getBoolean(R.bool.is_highlight_button_enabled)) {
            mTitle.setText(getResources().getString(R.string.ugc_my_data) + ":" + " " + getResources().getString(R.string.highlight_title));
            lastTab = TAB_SHARED_DATA;
            selectHighlights();
            filterData(mFilter_by);
            getTheCountsOfMyData(mFilter_by);
        }
        filterData(mFilter_by);
    }

    public class ColorBarDrawable extends Drawable {

        private final int themeColors;
        private final int areaHeight;

        public ColorBarDrawable(int themeColors, int areaHeight) {
            this.themeColors = themeColors;
            this.areaHeight = areaHeight;
        }

        @Override
        public void draw(Canvas canvas) {

            // get drawable dimensions
            Rect bounds = getBounds();

            int width = bounds.right - bounds.left;
            int height = bounds.bottom - bounds.top;

            // draw background gradient
            Paint backgroundPaint = new Paint();
            backgroundPaint.setColor(themeColors);
            canvas.drawRect(0, 0, width, height, backgroundPaint);
            backgroundPaint.setColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getPopupBackground()));
            canvas.drawRect(0, 0, width, height - areaHeight, backgroundPaint);
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter cf) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }
    }

    private void updateTab() {
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            //mTabHost.getTabWidget().getChildAt(i).setBackgroundDrawable(mUnSelectedDrawable); // selected
            mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = (int) mContext.getResources()
                    .getDimension(R.dimen.tab_hight);

            TextView tv = mTabHost.getTabWidget().getChildAt(i)
                    .findViewById(R.id.title); // Unselected Tabs
            tv.setTypeface(null, Typeface.NORMAL);
            tv.setGravity(Gravity.CENTER);
            tv.setAllCaps(false);
            tv.setTextSize(16);
            tv.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getIconColor()));

            mTabHost.getTabWidget().getChildAt(i).getBackground().setColorFilter(getResources().getColor(R.color.transparent), PorterDuff.Mode.SRC_ATOP);

        }
        mTabHost.setCurrentTab(0);

        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundDrawable(mSelectedDrawable); // selected
        updateTabText();

    }

    private void updateTabText() {
        mSharedwithmecount.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.
                        getReader().getDayMode().getMyData().getDeSelectedButton().getBackground()),
                new float[]{8, 8, 8, 8, 8, 8, 8, 8}, 2, Color.parseColor(readerThemeSettingVo.
                        getReader().getDayMode().getMyData().getDeSelectedButton().getBackground())));
        mMydatacount.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.
                        getReader().getDayMode().getMyData().getDeSelectedButton().getBackground()),
                new float[]{8, 8, 8, 8, 8, 8, 8, 8}, 2, Color.parseColor(readerThemeSettingVo.
                        getReader().getDayMode().getMyData().getDeSelectedButton().getBackground())));
        mSharedwithmetitle.setTypeface(null, Typeface.NORMAL);
        mMydatatitle.setTypeface(null, Typeface.NORMAL);
        if (mTabHost.getCurrentTab() == 0) {
            mSharedwithmetitle.setTypeface(null, Typeface.BOLD);
            mSharedwithmetitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSelectedTextColor()));

            mSharedwithmecount.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.
                            getReader().getDayMode().getMyData().getSelectedButton().getBackground()),
                    new float[]{8, 8, 8, 8, 8, 8, 8, 8}, 2, Color.parseColor(readerThemeSettingVo.
                            getReader().getDayMode().getMyData().getSelectedButton().getBackground())));
        } else {
            mMydatacount.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.
                            getReader().getDayMode().getMyData().getSelectedButton().getBackground()),
                    new float[]{8, 8, 8, 8, 8, 8, 8, 8}, 2, Color.parseColor(readerThemeSettingVo.
                            getReader().getDayMode().getMyData().getSelectedButton().getBackground())));
            mMydatatitle.setTypeface(null, Typeface.BOLD);
            mMydatatitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSelectedTextColor()));


        }
    }

    public void getTheCountsOfMyData(String type) {
        int mydata = 0;
        if (type.equalsIgnoreCase(CustomDataFilter.NOTES)) {
            ArrayList<HighlightVO> temp = new ArrayList<HighlightVO>();
            for (int i = 0; i < alldatalist.size(); i++) {
                HighlightVO vo = alldatalist.get(i);
                if (!TextUtils.isEmpty(vo.getNoteData()) &&
                        vo.getActionTakenStatus().equalsIgnoreCase("y")) {
                    temp.add(vo);
                }
            }
            if (temp.size() != 0) {
                mMydatacount.setVisibility(View.VISIBLE);
                mMydatacount.setText(String.valueOf(temp.size()));
            } else {
                mMydatacount.setVisibility(View.GONE);
            }
        } else if (type.equalsIgnoreCase(CustomDataFilter.HIGHLIGHTS)) {
            ArrayList<HighlightVO> temp = new ArrayList<HighlightVO>();
            for (int i = 0; i < alldatalist.size(); i++) {
                HighlightVO vo = alldatalist.get(i);

                if (vo != null && vo.getNoteData() != null && vo.getNoteData().equals("") &&
                        vo.getActionTakenStatus().equalsIgnoreCase("y")) {
                    temp.add(vo);
                }
            }
            if (temp.size() != 0) {
                mSharedwithmecount.setVisibility(View.VISIBLE);
                mSharedwithmecount.setText(String.valueOf(temp.size()));
            } else {
                mSharedwithmecount.setVisibility(View.GONE);
            }

        }
    }

    public void getTheCountOfSharedWithMe(String type) {
        int sharedWithMe = 0;
        if (type.equalsIgnoreCase(CustomDataFilter.NOTES)) {
            ArrayList<HighlightVO> temp = new ArrayList<HighlightVO>();
            for (int i = 0; i < alldatalist.size(); i++) {
                HighlightVO vo = alldatalist.get(i);

                if (!TextUtils.isEmpty(vo.getNoteData()) && vo.getActionTakenStatus().equalsIgnoreCase("y")) {
                    boolean isEditEnabled = vo.getCreatedByUserVO().getUserID() == KitabooSDKModel.getInstance().getUserID();

                    if (!isEditEnabled) {
                        sharedWithMe++;
                    }
                }
            }
        } else if (type.equalsIgnoreCase(CustomDataFilter.HIGHLIGHTS)) {
            ArrayList<HighlightVO> temp = new ArrayList<HighlightVO>();
            for (int i = 0; i < alldatalist.size(); i++) {
                HighlightVO vo = alldatalist.get(i);

                if (vo != null && vo.getNoteData() != null && vo.getNoteData().equals("") && vo
                        .getActionTakenStatus().equalsIgnoreCase("y")) {
                    boolean isEditEnabled = vo.getCreatedByUserVO().getUserID() == KitabooSDKModel.getInstance().getUserID();
                    if (!isEditEnabled) {
                        sharedWithMe++;
                    }
                }
            }
        }
        if (sharedWithMe != 0) {
            mSharedwithmecount.setVisibility(View.VISIBLE);
            mSharedwithmecount.setText(String.valueOf(sharedWithMe));
        } else {
            mSharedwithmecount.setVisibility(View.GONE);
        }
    }

    public interface CustomMydataLayoutCallback {
        void settingsClicked();

        void onItemClickListener(HighlightVO vo);

        void onSharebtnClick(HighlightVO vo);

        void openUgcItemCommentScreen(HighlightVO vo);

        void onAcceptRejectBtnClicked(boolean accept, HighlightVO vo);

        void onMyDataNotificationClicked(ArrayList<HighlightVO> notificationList);

        void onMyDataCommentBtnClick(HighlightVO vo);

        void OnViewExpand();

        void OnViewMinimize();
    }


}