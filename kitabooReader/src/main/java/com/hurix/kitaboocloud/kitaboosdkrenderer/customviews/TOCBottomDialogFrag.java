package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.fragment.app.DialogFragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.hurix.customui.toc.TOCHelper;
import com.hurix.customui.toc.TableOfContentVO;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.kitaboocloud.views.OnDialogSwipeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.graphics.Color.*;

public class TOCBottomDialogFrag extends DialogFragment implements TabHost.OnTabChangeListener{

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
    FrameLayout.LayoutParams params;
    private RelativeLayout mContentLayout;
    LinearLayout mHostParentLayout, mTsbContainer;
    private View mFalseLayout;
    WindowManager.LayoutParams view_params;
    private static WindowManager windowManager;
    private boolean isExpanded = false;
    private View.OnTouchListener mDragListener;
    private View view = null;
    private RelativeLayout mTocParentContainer;

    public TOCBottomDialogFrag() {
    }

    public static TOCBottomDialogFrag newInstance(String tool, View anchor, EBookType mReaderType, boolean ismobile) {
        TOCBottomDialogFrag f = new TOCBottomDialogFrag();
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _tocHelper = new TOCHelper();
        view = inflater.inflate(R.layout.tab_toc_layout, container, false);
        if (tocListner != null) {
            tocListner.setTocLayout(view);
        }
        mTypeface = Typefaces.get(getContext(), "kitabooread.ttf");
        mTocParentContainer = (RelativeLayout) view.findViewById(R.id.toc_parent_layout);
        mTitleLayout = (LinearLayout) view.findViewById(R.id.top_bar);
        mTOCtitle = (TextView) view.findViewById(R.id.toc_title);
        mFalseLayout = (View) view.findViewById(R.id.psudeo_layout);
        mContentLayout = (RelativeLayout) view.findViewById(R.id.tab_parent);
        mHostParentLayout = (LinearLayout) view.findViewById(R.id.tab_widget_parent);
        mTsbContainer = (LinearLayout) view.findViewById(R.id.mainLayout);
        mTOCtitle.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.GONE);
        mSwipeLayout = (RelativeLayout) view.findViewById(R.id.swipe_up);
        mSwipeText = (TextView) view.findViewById(R.id.swipe_hint_text);

        if (mReaderThemeSettingVo != null) {

            mTitleLayout.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));

            if(getResources().getBoolean(R.bool.is_Navneet_Client)){
                mTOCtitle.setTextColor(getResources().getColor(R.color.kitaboo_main_color));
            }else{
                mTOCtitle.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
            }

            mTsbContainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
        }
        //mSwipeLayout.setOnTouchListener(mDragListener);
        mTocParentContainer.setOnTouchListener(new OnDialogSwipeListener(getContext()) {

            public void onSwipeTop(){

                String s ="";
               // Log.d("Drag Event","Swiped Top");
                maximizeTocDialog();

            }

            public void onSwipeBottom() {
                String s = "";
                //Log.d("Drag Event", "Swiped Bottom");
                minimizeTocDialog(isExpanded);

            }


            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        mSwipeText.setTypeface(mTypeface);
        mSwipeText.setAllCaps(false);
        mSwipeText.setText("6");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        int[] location = new int[2];
        _anchor.getLocationOnScreen(location);
        final int anchorLeft = location[0];
        final int anchorTop = location[1];
       // initialiseTabHost(view);
        final int anchorWidth = _anchor.getMeasuredWidth();
        final int anchorHeight = _anchor.getMeasuredHeight();
        setPosition(anchorLeft, anchorTop, anchorWidth, anchorHeight);
        return view;
    }

    private void initialiseTabHost(View view) {

        _mTabHost = (TabHost) view.findViewById(R.id.toctab);

        if (mReaderThemeSettingVo != null) {
            //panel background

            //selectedDrawable
            _selectedDrawable = new ColorBarDrawable(parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getSelectedTextColor()),10);
            //unSelectedDrawable
            _unSelectedDrawable = new ColorBarDrawable(parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getTabTextColor()),3);
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
        view_params = new WindowManager.LayoutParams(mSwipeText.getWidth(),
                mSwipeText.getHeight(),
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O? +   WindowManager.LayoutParams.TYPE_SYSTEM_ERROR : +WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if (getDialog() != null) {
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        }
    }

    public void setLayout(int width, int height) {

        //view.setBackgroundColor(getResources().getColor(R.color.transparent));
        isExpanded = false;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        parent = (View) view.getParent();
        parent.setFitsSystemWindows(true);
      //  bottomSheetBehavior = BottomSheetBehavior.from(parent);
        params = (FrameLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        view.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,screenHeight/2);
    //    bottomSheetBehavior.setPeekHeight(screenHeight / 2);
        params.height = screenHeight/2;
      //  params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.leftMargin = 40;
        params.rightMargin = 40;
        parent.setLayoutParams(params);
        parent.setBackgroundColor(getResources().getColor(R.color.transparent));
        initialiseTabHost(view);

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
            return new View(TOCBottomDialogFrag.this.getActivity());
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
                            .setContent(new TOCBottomDialogFrag.TabContentFactory(mListener)));
                } else {
                    _mTabHost.addTab(_mTabHost.newTabSpec(pair.getKey().toString()).setIndicator(pair.getValue().toString())
                            .setContent(new TOCBottomDialogFrag.TabContentFactory(mListener)));
                }
            }
        }
        updateTab();
        _mTabHost.getTabWidget().setDividerDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        _mTabHost.setOnTabChangedListener(this);
        _mTabHost.post(new Runnable() {
            @Override
            public void run() {
                if(_mTabHost != null)
                {
                    TextView tv = (TextView) _mTabHost.getCurrentTabView().findViewById(android.R.id.title); // for Selected Tab
                    tv.setTypeface(null, Typeface.BOLD);
                }
            }
        });
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
                tv.setTextColor(parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getTableofcontents().getTabTextColor()));
            }

            _mTabHost.getTabWidget().getChildAt(i).getBackground().setColorFilter(getResources().getColor
                    (R.color.transparent), PorterDuff.Mode.SRC_ATOP);
        }

        // selected
        if (_mTabHost != null && _mTabHost.getCurrentTabView() != null) {

            if (mReaderThemeSettingVo != null) {

                _mTabHost.getTabWidget().getChildAt(_mTabHost.getCurrentTab()).getBackground().setColorFilter(parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getTableofcontents().getSelectedTabBorder()), PorterDuff.Mode.SRC_ATOP);
                TextView tv = (TextView) _mTabHost.getCurrentTabView().findViewById(android.R.id.title); // for Selected Tab
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setGravity(Gravity.CENTER);
                tv.setAllCaps(false);
                tv.setTextSize(16);
                if (mReaderThemeSettingVo != null) {
                    tv.setTextColor(parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getTableofcontents().getSelectedTextColor()));
                }
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
                tv.setTextColor(parseColor(mReaderThemeSettingVo.
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
            _mTabHost.getTabWidget().getChildAt(_mTabHost.getCurrentTab()).getBackground().setColorFilter(parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getTableofcontents().getSelectedTabBorder()), PorterDuff.Mode.SRC_ATOP);
            tv.setTextColor(parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getTableofcontents().getSelectedTextColor()));
        }

    }

    public void maximizeTocDialog(){

        isExpanded = true;
        mTOCtitle.setVisibility(View.VISIBLE);
        mTitleLayout.setVisibility(View.VISIBLE);
        mSwipeLayout.setVisibility(View.GONE);
        mSwipeText.setVisibility(View.GONE);

        getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,screenHeight);
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
        anim.setDuration(200);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // mFalseLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //mFalseLayout.setVisibility(View.VISIBLE);
                Activity activity = getActivity();
                if(activity != null && isAdded() && mReaderThemeSettingVo != null){
                    mContentLayout.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getTabBg()));
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

    public void minimizeTocDialog(boolean dialogAction){

        if (dialogAction) {
            isExpanded = false;
            mTOCtitle.setVisibility(View.GONE);
            mTitleLayout.setVisibility(View.GONE);
            mSwipeLayout.setVisibility(View.VISIBLE);
            mSwipeText.setVisibility(View.VISIBLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
            parent = (View) view.getParent();
            parent.setFitsSystemWindows(true);
            params = (FrameLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
            view.measure(0, 0);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            screenHeight = displaymetrics.heightPixels;
            getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,screenHeight/2);
            params.height = screenHeight/2;
            params.leftMargin = 40;
            params.rightMargin = 40;
            parent.setLayoutParams(params);
            parent.setBackgroundColor(getResources().getColor(R.color.transparent));


            mFalseLayout.setAlpha(0f);
            // mContentLayout.setBackground(getResources().getDrawable(R.drawable.bottom_sheet_background));

            if (mReaderThemeSettingVo != null) {

                mContentLayout.setBackgroundDrawable(com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()),
                        new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2, Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBorder())));
            }

            RelativeLayout.LayoutParams titleParam = (RelativeLayout.LayoutParams) mTitleLayout.getLayoutParams();
            titleParam.setMargins(10, 10, 10, 0);
            mTitleLayout.setLayoutParams(titleParam);

            RelativeLayout.LayoutParams hostParam = (RelativeLayout.LayoutParams) mHostParentLayout.getLayoutParams();
            hostParam.setMargins(10, 10, 10, 0);
            mHostParentLayout.setLayoutParams(hostParam);
        }
        else{
            dismiss();

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setThemeColor(ReaderThemeSettingVo _themeUserSettingVo) {
        mReaderThemeSettingVo = _themeUserSettingVo;
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
            if (mReaderThemeSettingVo != null) {
                backgroundPaint.setColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            }
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
