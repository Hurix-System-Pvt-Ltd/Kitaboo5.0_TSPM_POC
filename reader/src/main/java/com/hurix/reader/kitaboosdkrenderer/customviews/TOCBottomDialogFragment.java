package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.hurix.commons.Constants.EBookType;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.toc.TOCHelper;
import com.hurix.customui.toc.TableOfContentVO;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TOCBottomDialogFragment extends BottomSheetDialogFragment implements TabHost.OnTabChangeListener {
    protected Drawable _selectedDrawable;
    protected Drawable _unSelectedDrawable;
    private CustomTocListner tocListner;
    protected TabHost _mTabHost;
    public static View _anchor;
    private View _underlineView;
    private static ArrayList<TableOfContentVO> mTocdata = new ArrayList<TableOfContentVO>();
    private HashMap contentVOs;
    private CustomTOCEnterpriseView.TocItemClickListener mListener;
    protected TOCHelper _tocHelper;
    private String readertype;
    private boolean devicetype;
    private TextView backbutton, mTOCtitle;
    private int mHeight;
    private int mWidth;
    private ReaderThemeSettingVo mReaderThemeSettingVo;
    private Typeface mTypeface;
    private BottomSheetBehavior bottomSheetBehavior;
    int screenHeight;
    private LinearLayout mTitleLayout;
    private TextView mSwipeText;
    private RelativeLayout mSwipeLayout;
    View parent;
    CoordinatorLayout.LayoutParams params;
    private RelativeLayout mContentLayout;
    LinearLayout mHostParentLayout, mTsbContainer;
    private View mFalseLayout;
    private boolean mSlideCalled, isExpanded;

    public TOCBottomDialogFragment() {
    }

    public static TOCBottomDialogFragment newInstance(String tool, View anchor, EBookType mReaderType, boolean ismobile) {
        TOCBottomDialogFragment f = new TOCBottomDialogFragment();
        _anchor = anchor;
        Bundle args = new Bundle();
        args.putString("data", tool.toString());
        args.putString("readertype", mReaderType.toString());
        args.putBoolean("ismobile", ismobile);
        f.setArguments(args);
        return f;
    }


    public void setitemclickListener(CustomTOCEnterpriseView.TocItemClickListener listener) {
        mListener = listener;
    }

    public void setviewlistner(CustomTocListner listener) {
        tocListner = listener;
    }

    public void setData(HashMap contentVO) {
        contentVOs = contentVO;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        devicetype = getArguments().getBoolean("ismobile");
        readertype = getArguments().getString("readertype");
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            mSlideCalled = false;
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
                isExpanded = false;
            }

            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                isExpanded = false;
            }

            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                if (!isExpanded) {
                    isExpanded = true;
                    mTOCtitle.setVisibility(View.VISIBLE);
                    mTitleLayout.setVisibility(View.VISIBLE);
                    mSwipeLayout.setVisibility(View.GONE);
                    mSwipeText.setVisibility(View.GONE);


                    params.height = screenHeight;
                    params.leftMargin = 0;
                    params.rightMargin = 0;
                    parent.setLayoutParams(params);

                    RelativeLayout.LayoutParams titleParam = (RelativeLayout.LayoutParams) mTitleLayout.getLayoutParams();
                    titleParam.setMargins(0, 0, 0, 0);
                    mTitleLayout.setLayoutParams(titleParam);

                    RelativeLayout.LayoutParams hostParam = (RelativeLayout.LayoutParams) mHostParentLayout.getLayoutParams();
                    hostParam.setMargins(0, 0, 0, 0);
                    mHostParentLayout.setLayoutParams(hostParam);

                    ObjectAnimator anim = ObjectAnimator.ofFloat(mFalseLayout, "alpha", 0f, 1f);
                    anim.setInterpolator(new LinearInterpolator());
                    anim.setDuration(1000);
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            // mFalseLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //mFalseLayout.setVisibility(View.VISIBLE);
                            Activity activity = getActivity();
                            if(activity != null && isAdded()){
                                mContentLayout.setBackground(getResources().getDrawable(R.drawable.bottom_expand_drwable));
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    anim.start();
                }


            } else {
                if (newState != BottomSheetBehavior.STATE_DRAGGING) {
                    mTOCtitle.setVisibility(View.GONE);
                    mTitleLayout.setVisibility(View.GONE);
                    mSwipeLayout.setVisibility(View.VISIBLE);
                    mSwipeText.setVisibility(View.VISIBLE);
                    params.height = screenHeight;
                    params.leftMargin = 40;
                    params.rightMargin = 40;
                    parent.setLayoutParams(params);


                    //mFalseLayout.setVisibility(View.GONE);

                    mFalseLayout.setAlpha(0f);
                   // mContentLayout.setBackground(getResources().getDrawable(R.drawable.bottom_sheet_background));

                    mContentLayout.setBackgroundDrawable(com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()),
                            new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2, Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBorder())));

                    RelativeLayout.LayoutParams titleParam = (RelativeLayout.LayoutParams) mTitleLayout.getLayoutParams();
                    titleParam.setMargins(10, 10, 10, 0);
                    mTitleLayout.setLayoutParams(titleParam);

                    RelativeLayout.LayoutParams hostParam = (RelativeLayout.LayoutParams) mHostParentLayout.getLayoutParams();
                    hostParam.setMargins(10, 10, 10, 0);
                    mHostParentLayout.setLayoutParams(hostParam);
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            mSlideCalled = true;
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            mTypeface = Typefaces.get(getContext(), fontfile);
        } else {
            mTypeface = Typefaces.get(getContext(), "kitabooread.ttf");
        }
        View contentView = null;
        _tocHelper = new TOCHelper();
        contentView = View.inflate(getContext(), R.layout.tab_toc_layout, null);
        if (tocListner != null) {
            tocListner.setTocLayout(contentView);
        }
        mTitleLayout = (LinearLayout) contentView.findViewById(R.id.top_bar);
        mTitleLayout.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
        mTOCtitle = (TextView) contentView.findViewById(R.id.toc_title);
        mTOCtitle.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        mFalseLayout = (View) contentView.findViewById(R.id.psudeo_layout);
        mContentLayout = (RelativeLayout) contentView.findViewById(R.id.tab_parent);
        mHostParentLayout = (LinearLayout) contentView.findViewById(R.id.tab_widget_parent);
        mTsbContainer = (LinearLayout) contentView.findViewById(R.id.mainLayout);
        mTsbContainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
        mTOCtitle.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.GONE);
        mSwipeLayout = (RelativeLayout) contentView.findViewById(R.id.swipe_up);
        mSwipeText = (TextView) contentView.findViewById(R.id.swipe_hint_text);

        mSwipeText.setTypeface(mTypeface);
        mSwipeText.setAllCaps(false);
        mSwipeText.setText("6");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        contentView.setBackgroundColor(getResources().getColor(R.color.transparent));
        dialog.setContentView(contentView);

        FrameLayout bottomSheet = (FrameLayout) dialog.getWindow().findViewById(R.id.design_bottom_sheet);
        params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        parent = (View) contentView.getParent();
        parent.setFitsSystemWindows(true);
        bottomSheetBehavior = BottomSheetBehavior.from(parent);
        contentView.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        bottomSheetBehavior.setPeekHeight(screenHeight / 2);

        if (bottomSheetBehavior instanceof BottomSheetBehavior) {
            bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        params.height = screenHeight;
        params.leftMargin = 40;
        params.rightMargin = 40;
        parent.setLayoutParams(params);
        parent.setBackgroundColor(getResources().getColor(R.color.transparent));
        initialiseTabHost(contentView);
    }

    private void initialiseTabHost(View view) {

        _mTabHost = (TabHost) view.findViewById(R.id.toctab);

        if (mReaderThemeSettingVo != null) {
            //panel background
            _mTabHost.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getTableofcontents().getTabBg()),
                    new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 5, Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getTableofcontents().getTabBorder())));
            //selectedDrawable
            _selectedDrawable = Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getTableofcontents().getSelectedTabBorder()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getTableofcontents().getSelectedTextColor()));
            //unSelectedDrawable
            _unSelectedDrawable = Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getTableofcontents().getTabBg()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getTableofcontents().getSelectedTextColor()));

            mContentLayout.setBackgroundDrawable(com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getTableofcontents().getPopupBackground()),
                    new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2, Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getTableofcontents().getPopupBorder())));
        }

        if (tocListner != null) {
            tocListner.initialiseTabHost(_mTabHost);
        }
        setUpTabHost();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.dismissAllowingStateLoss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    class TabContentFactory implements TabHost.TabContentFactory {

        private ArrayList<TableOfContentVO> mTocdata;
        private CustomTOCEnterpriseView.TocItemClickListener mListner;

        public TabContentFactory(CustomTOCEnterpriseView.TocItemClickListener listner) {
            mListner = listner;
        }

        public TabContentFactory() {
        }

        @Override
        public View createTabContent(String tag) {
            if (tocListner != null) {
                return tocListner.returnTabView(tag, mListner);
            }
            return new View(TOCBottomDialogFragment.this.getActivity());
        }
    }

    public void setUpTabHost() {
        int count = 0;
        if (_mTabHost.getTabContentView() != null) {
            _mTabHost.clearAllTabs();
        }
        _mTabHost.setup();
        if (contentVOs != null) {
            Iterator it = contentVOs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                if (count == 0) {
                    count = count + 1;
                    _mTabHost.addTab(_mTabHost.newTabSpec(pair.getKey().toString()).setIndicator(pair.getValue().toString())
                            .setContent(new TOCBottomDialogFragment.TabContentFactory(mListener)));
                } else {
                    _mTabHost.addTab(_mTabHost.newTabSpec(pair.getKey().toString()).setIndicator(pair.getValue().toString())
                            .setContent(new TOCBottomDialogFragment.TabContentFactory(mListener)));
                }
            }
        }
        updateTab();
        _mTabHost.getTabWidget().setDividerDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        _mTabHost.setOnTabChangedListener(this);
    }

    public void updateTab() {
        for (int i = 0; i < _mTabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) _mTabHost.getTabWidget().getChildAt(i)
                    .findViewById(android.R.id.title); // Unselected Tabs
            tv.setTypeface(null, Typeface.NORMAL);
            tv.setGravity(Gravity.CENTER);
            tv.setAllCaps(false);
            tv.setTextSize(16);
            if (mReaderThemeSettingVo != null) {
                tv.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getTableofcontents().getTabTextColor()));
            }

            _mTabHost.getTabWidget().getChildAt(i).getBackground().setColorFilter(getResources().getColor
                    (R.color.transparent), PorterDuff.Mode.SRC_ATOP);
        }

        // selected
        if (_mTabHost != null && _mTabHost.getCurrentTabView() != null) {

            _mTabHost.getTabWidget().getChildAt(_mTabHost.getCurrentTab()).getBackground().setColorFilter(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getTableofcontents().getSelectedTabBorder()), PorterDuff.Mode.SRC_ATOP);
            TextView tv = (TextView) _mTabHost.getCurrentTabView().findViewById(android.R.id.title); // for Selected Tab
            tv.setTypeface(null, Typeface.NORMAL);
            tv.setGravity(Gravity.CENTER);
            tv.setAllCaps(false);
            tv.setTextSize(16);
            if (mReaderThemeSettingVo != null) {
                tv.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getTableofcontents().getSelectedTextColor()));
            }
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equalsIgnoreCase("contents") || tabId.equalsIgnoreCase("resources")) {
            if(getResources().getBoolean(R.bool.is_Oup_client) && tabId.equalsIgnoreCase("resources"))
            {
                tabId = "My Data: " + tabId;
            }
            else
            {
                tabId = "Table of " + tabId;
            }
        }
        mTOCtitle.setText(tabId);
        for (int i = 0; i < _mTabHost.getTabWidget().getChildCount(); i++) {
            // unselected
            TextView tv = (TextView) _mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); // Unselected Tabs
            tv.setTypeface(null, Typeface.NORMAL);
            tv.setGravity(Gravity.CENTER);
            tv.setAllCaps(false);
            tv.setTextSize(16);
            // unselected
            if (mReaderThemeSettingVo != null) {
                tv.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getTableofcontents().getTabTextColor()));
            }
            _mTabHost.getTabWidget().getChildAt(i).getBackground().setColorFilter(getResources().getColor(R.color.transparent), PorterDuff.Mode.SRC_ATOP);
        }

        // selected

        TextView tv = (TextView) _mTabHost.getCurrentTabView().findViewById(android.R.id.title); // for Selected Tab
        tv.setTypeface(null, Typeface.BOLD);
        tv.setGravity(Gravity.CENTER);
        tv.setAllCaps(false);
        tv.setTextSize(16);
        if (mReaderThemeSettingVo != null) {
            _mTabHost.getTabWidget().getChildAt(_mTabHost.getCurrentTab()).getBackground().setColorFilter(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getTableofcontents().getSelectedTabBorder()), PorterDuff.Mode.SRC_ATOP);
            tv.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getTableofcontents().getSelectedTextColor()));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setThemeColor(ReaderThemeSettingVo _themeUserSettingVo) {
        mReaderThemeSettingVo = _themeUserSettingVo;
    }


}
