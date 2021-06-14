package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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

import com.hurix.commons.Constants.EBookType;
import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.toc.TOCHelper;
import com.hurix.customui.toc.TableOfContentVO;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by amitraj.sharma on 1/28/2018.
 */

public class CustomTocView extends DialogFragment implements TabHost.OnTabChangeListener {
   //protected Drawable _selectedDrawable;
   // protected Drawable _unSelectedDrawable;
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
    private TextView backbutton, mTocTitle;
    private int mHeight;
    private int mWidth;
    private ReaderThemeSettingVo mReaderThemeSettingVo;
    private Typeface mTypeface;
    private Drawable mSelectedDrawable, mUnSelectedDrawable;
    private LinearLayout mTopbarMobile, mTabLayout, mMainLayout;


    public CustomTocView() {
    }

    public static CustomTocView newInstance(String tool, View anchor, EBookType mReaderType, boolean ismobile) {
        CustomTocView f = new CustomTocView();
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

    public void setParams(int param1, int param2) {
        mWidth = param1;
        mHeight = param2;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        _tocHelper = new TOCHelper();


        if (devicetype) {
            view = inflater.inflate(R.layout.customtocviewmobile, container, false);
        } else {
            view = inflater.inflate(R.layout.customtabtocview, container, false);
        }
        if (tocListner != null) {
            tocListner.setTocLayout(view);
        }
        backbutton = (TextView) view.findViewById(R.id.back);
        mTocTitle = (TextView) view.findViewById(R.id.toc_title);
        mTopbarMobile = (LinearLayout) view.findViewById(R.id.top_bar);
        mTabLayout = (LinearLayout) view.findViewById(R.id.tabLayout);
        mMainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        if (mTabLayout != null)
            mTabLayout.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
        if (mTocTitle != null) {
            mTocTitle.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            if(getResources().getBoolean(R.bool.is_Navneet_Client) || getResources().getBoolean(R.bool.main_color)){
                mTocTitle.setTextColor(getResources().getColor(R.color.kitaboo_main_color));
            }else{
                mTocTitle.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
            }
        }
        mMainLayout.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
        mTabLayout.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
        if (backbutton != null) {
            String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
            if (fontfile != null && !fontfile.isEmpty()) {
                mTypeface = Typefaces.get(getContext(), fontfile);
            } else {
                mTypeface = Typefaces.get(getContext(), "kitabooread.ttf");
            }
            backbutton.setTypeface(mTypeface);
            backbutton.setTextSize(24);
            backbutton.setText(PlayerUIConstants.TB_BACK_CIRCLE_IC_TEXT);
            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tocListner != null) {
                        tocListner.onDialogBackpressed();
                    }
                }
            });
        }
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        int[] location = new int[2];
        _anchor.getLocationOnScreen(location);
        final int anchorLeft = location[0];
        final int anchorTop = location[1];
        initialiseTabHost(view);
        final int anchorWidth = _anchor.getMeasuredWidth();
        final int anchorHeight = _anchor.getMeasuredHeight();
        setPosition(anchorLeft, anchorTop, anchorWidth, anchorHeight);
        return view;
    }

    private void initialiseTabHost(View view) {

        _mTabHost = (TabHost) view.findViewById(R.id.toctab);

        if (mReaderThemeSettingVo != null) {
           /* _selectedDrawable = Utils.getRectAngleDrawable(Color.parseColor(themeUserSettingVo.getmTocSelectedTabBackground()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(themeUserSettingVo.getmKitabooMainColor()));
            _unSelectedDrawable = Utils.getRectAngleDrawable(Color.parseColor(themeUserSettingVo.getmTocUnSelectedTabBackground()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(themeUserSettingVo.getmKitabooMainColor()));*/
            mSelectedDrawable = new ColorBarDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getSelectedTextColor()), 10);
            mUnSelectedDrawable = new ColorBarDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getTabTextColor()), 3);
        }
        if (tocListner != null) {
            tocListner.initialiseTabHost(_mTabHost);
        }
        setUpTabHost();
    }

    private void setPosition(int anchorLeft, int anchorTop, int anchorWidth, int anchorHeight) {
        Rect anchorRect = new Rect(anchorLeft, anchorTop, anchorLeft + anchorWidth,
                anchorTop + anchorHeight);
        int yPos = anchorRect.bottom;
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        params.x = 10;
        params.y = yPos;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        getDialog().getWindow().setAttributes(params);
        if (tocListner != null) {
            tocListner.setDialogPosition(getDialog());
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.dismissAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*To remove black background overlay of toc*/
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windoparams = window.getAttributes();
            windoparams.dimAmount = 0.0f;
            window.setAttributes(windoparams);
            /*To remove black overlay of toc*/
            Display display = (getActivity()).getWindowManager().getDefaultDisplay();
            if (display != null) {
                setLayout(mWidth, mHeight);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if (getDialog() != null) {
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        }
    }

    public void setLayout(int width, int height) {
        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

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
            //mTocTitle.setText(tag);
            if (tocListner != null) {
                return tocListner.returnTabView(tag, mListner);
            }
            return new View(CustomTocView.this.getActivity());
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
                            .setContent(new TabContentFactory(mListener)));
                } else {
                    _mTabHost.addTab(_mTabHost.newTabSpec(pair.getKey().toString()).setIndicator(pair.getValue().toString())
                            .setContent(new TabContentFactory(mListener)));
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
            if (devicetype) {
                tv.setRotationX(180);
            }
            _mTabHost.getTabWidget().getChildAt(i).setBackgroundDrawable(mUnSelectedDrawable);
            // _mTabHost.getTabWidget().getChildAt(i).getBackground().setColorFilter(getResources().getColor(R.color.transparent), PorterDuff.Mode.SRC_ATOP);

            if (mReaderThemeSettingVo != null) {
                tv.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getTabTextColor()));
            }
        }
        // selected
        if (_mTabHost != null && _mTabHost.getCurrentTabView() != null) {
            _mTabHost.getTabWidget().getChildAt(_mTabHost.getCurrentTab()).setBackgroundDrawable(mSelectedDrawable);
            // _mTabHost.getTabWidget().getChildAt(_mTabHost.getCurrentTab()).getBackground().setColorFilter(Color.parseColor(themeUserSettingVo.getmKitabooMainColor()), PorterDuff.Mode.SRC_ATOP);
            TextView tv = (TextView) _mTabHost.getCurrentTabView().findViewById(android.R.id.title); // for Selected Tab
            tv.setTypeface(null, Typeface.BOLD);
            tv.setGravity(Gravity.CENTER);
            tv.setAllCaps(false);
            tv.setTextSize(16);
            if (mReaderThemeSettingVo != null) {
                tv.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getSelectedTextColor()));
            }
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equalsIgnoreCase("contents") || tabId.equalsIgnoreCase("resources")) {
            if (getResources().getBoolean(R.bool.is_Oup_client) && tabId.equalsIgnoreCase("resources")) {
                tabId = "My Data: " + tabId;
            } else {
                tabId = "Table of " + tabId;
            }
        }
        mTocTitle.setText(tabId);
        for (int i = 0; i < _mTabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) _mTabHost.getTabWidget().getChildAt(i)
                    .findViewById(android.R.id.title); // Unselected Tabs
            tv.setTypeface(null, Typeface.NORMAL);
            tv.setGravity(Gravity.CENTER);
            tv.setAllCaps(false);
            tv.setTextSize(16);

            _mTabHost.getTabWidget().getChildAt(i).setBackgroundDrawable(mUnSelectedDrawable);
            // _mTabHost.getTabWidget().getChildAt(i).getBackground().setColorFilter(getResources().getColor(R.color.transparent), PorterDuff.Mode.SRC_ATOP);
            if (mReaderThemeSettingVo != null) {
                tv.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getTabTextColor()));
            }
        }

        // selected

        TextView tv = (TextView) _mTabHost.getCurrentTabView().findViewById(android.R.id.title); // for Selected Tab
        tv.setTypeface(null, Typeface.BOLD);
        tv.setGravity(Gravity.CENTER);
        tv.setAllCaps(false);
        tv.setTextSize(16);
        if (mReaderThemeSettingVo != null) {
            _mTabHost.getTabWidget().getChildAt(_mTabHost.getCurrentTab()).setBackgroundDrawable(mSelectedDrawable);
            // _mTabHost.getTabWidget().getChildAt(_mTabHost.getCurrentTab()).getBackground().setColorFilter(Color.parseColor(themeUserSettingVo.getmKitabooMainColor()), PorterDuff.Mode.SRC_ATOP);
            tv.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getSelectedTextColor()));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setThemeColor(ReaderThemeSettingVo _readerThemeSettingVo) {
        mReaderThemeSettingVo = _readerThemeSettingVo;
    }

    public class ColorBarDrawable extends Drawable {

        private int themeColors;
        private int areaHeight;

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
            backgroundPaint.setColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
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
}
