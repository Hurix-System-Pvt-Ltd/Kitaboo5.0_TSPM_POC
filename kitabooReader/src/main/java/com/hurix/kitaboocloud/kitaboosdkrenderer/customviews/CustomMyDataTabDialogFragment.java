package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hurix.customui.datamodel.HighlightVO;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.kitaboocloud.views.OnDialogSwipeListener;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static android.graphics.Color.TRANSPARENT;
import static android.view.View.VISIBLE;


public class CustomMyDataTabDialogFragment extends DialogFragment implements CustomMyDataLayout.CustomMydataLayoutCallback {
    private String UGCPath = "";
    private ViewFlipper _viewFlipper;
    private CustomMyDataLayout _ugcData;
    public static View _anchor;
    private MydataitemClickListner mListner;
    private Animation _in_from_left;
    private Animation _out_to_right;
    private Animation _in_from_right;
    private Animation _out_to_left;

    int screenHeight;
    private ArrayList<HighlightVO> alldatalist = new ArrayList<HighlightVO>();
    public static View mAnchor;
    public static boolean isMobile;
    private int mEnableShareSetting = VISIBLE;
    private ReaderThemeSettingVo mReaderThemeSettingVo;
    View parent;
    FrameLayout.LayoutParams params;
    WindowManager.LayoutParams view_params;
    private Typeface typeFace;
    TextView mSwipeText;
    LinearLayout mSwipeLayout;
    View contentView;
    private boolean isExpanded = false;



    /**
     * Create instance of fragment ,initialize fragment
     *
     * @param tool     : flag
     * @param anchor   : View type ,show fragment in below position to view
     * @param ismobile : Boolean variable to show different view for mobile and tablet
     * @return
     */
    public static CustomMyDataTabDialogFragment newInstance(String tool, View anchor, boolean ismobile) {
        mAnchor = anchor;
        isMobile = ismobile;
        CustomMyDataTabDialogFragment fragmentFirst = new CustomMyDataTabDialogFragment();
        Bundle args = new Bundle();
        args.putString("data", tool.toString());
        args.putBoolean("ismobile", ismobile);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {

                if ((keyCode ==  KeyEvent.KEYCODE_BACK)  && event.getAction() == KeyEvent.ACTION_UP)
                {
                    if ( _viewFlipper.getChildCount() > 1)
                        openMainScreen();
                    else
                        dismiss();

                    return true; // pretend we've processed it
                }
                else
                    return false; // pass on to be processed as normal
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.custom_mydata_main_screen, container, false);
        _viewFlipper = (ViewFlipper) contentView.findViewById(R.id.viewFlipper);
        mSwipeText = (TextView) contentView.findViewById(R.id.swipe_hint_text);
        mSwipeLayout = (LinearLayout) contentView.findViewById(R.id.swipe_layout);
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            typeFace = Typefaces.get(getContext(), fontfile);
        } else {
            typeFace = Typefaces.get(getContext(), "kitabooread.ttf");
        }
        mSwipeLayout.setVisibility(View.VISIBLE);
        mSwipeText.setTypeface(typeFace);
        mSwipeText.setAllCaps(false);
        mSwipeText.setText("6");
        mSwipeLayout.setOnTouchListener(new OnDialogSwipeListener(getContext()) {

            public void onSwipeTop(){

                String s ="";
                maximizeTocDialog();

            }

            public void onSwipeBottom() {
                String s = "";
                minimizeTocDialog(isExpanded);

            }


            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        contentView.setBackgroundColor(getResources().getColor(R.color.transparent));
        contentView.measure(0, 0);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        int[] location = new int[2];
        mAnchor.getLocationOnScreen(location);
        final int anchorLeft = location[0];
        final int anchorTop = location[1];
        final int anchorWidth = mAnchor.getMeasuredWidth();
        final int anchorHeight = mAnchor.getMeasuredHeight();
        setPosition(anchorLeft, anchorTop, anchorWidth, anchorHeight);
        return  contentView;
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
                setLayout();
            }
        }
        view_params = new WindowManager.LayoutParams(mSwipeText.getWidth(),
                mSwipeText.getHeight(),
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O? +   WindowManager.LayoutParams.TYPE_SYSTEM_ERROR : +WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }


    private void updateUI() {
        AnimateListener listener = new AnimateListener();
        _in_from_left = AnimationUtils.loadAnimation(getActivity(), R.anim.in_from_left);
        _out_to_right = AnimationUtils.loadAnimation(getActivity(), R.anim.out_to_right);
        _in_from_right = AnimationUtils.loadAnimation(getActivity(), R.anim.in_from_right);
        _out_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.out_to_left);

        _in_from_left.setAnimationListener(listener);
        _out_to_right.setAnimationListener(listener);
        _in_from_right.setAnimationListener(listener);
        _out_to_left.setAnimationListener(listener);


        _ugcData = new CustomMyDataLayout(getActivity(), false, mReaderThemeSettingVo, mEnableShareSetting);
        _ugcData.enableSetting(mEnableShareSetting);
        _ugcData.setData(alldatalist);
        _ugcData.setListner(this);
        _ugcData.hideToolbar();
        _viewFlipper.addView(_ugcData, 0);
    }

    public void setLayout() {

        contentView.setBackgroundColor(getResources().getColor(R.color.transparent));
        isExpanded = false;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        parent = (View) contentView.getParent();
        parent.setFitsSystemWindows(true);
        params = (FrameLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        contentView.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        getDialog().getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT,((screenHeight/2)-0));
        params.height = screenHeight/2;
        params.leftMargin = 40;
        params.rightMargin = 40;
        parent.setLayoutParams(params);
        parent.setBackgroundColor(getResources().getColor(R.color.transparent));
        updateUI();

    }



    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if (getDialog() != null) {
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * use to set height and width from reader
     *
     * @param //height
     * @param //width
     */

    public void setData(ArrayList<HighlightVO> list) {
        alldatalist = list;
    }

    public void setSharingSettingVisibility(int visibility) {
        mEnableShareSetting = visibility;
    }

    public void buildViewForSharingSetting(View view) {
        _viewFlipper.addView(view, 1);


        _viewFlipper.setInAnimation(_in_from_right);
        _viewFlipper.setOutAnimation(_out_to_left);
        _viewFlipper.showPrevious();
    }

    public void upDateData() {
        if (_ugcData != null) {
            _ugcData.update();
            _ugcData.enableSetting(mEnableShareSetting);
            _ugcData.setData(alldatalist);
        }
    }

    public void update() {
        if (_viewFlipper.getChildCount() > 1) {
            _viewFlipper.removeViewAt(1);
        }
        _ugcData.update();
        _viewFlipper.setDisplayedChild(0);
    }

    public void setUGCpath(String ugcpath) {
        UGCPath = ugcpath;
        _ugcData.setUGCpath(ugcpath);
    }

    public void refreshMyDataNoteFragment() {
        if(_ugcData != null)
            _ugcData.refreshMyDataNoteFragment();
    }

    public String getUGCpath() {
        return UGCPath;
    }


    /**
     * set listner from outer class
     *
     * @param listener : give callback on user action
     */
    public void setListener(MydataitemClickListner listener) {
        mListner = listener;
    }

    @Override
    public void onItemClickListener(HighlightVO vo) {
        mListner.onMydataItemClick(vo);
    }

    @Override
    public void openUgcItemCommentScreen(HighlightVO vo) {
        if (_viewFlipper.getChildCount() > 1) {
            _viewFlipper.removeViewAt(1);
        }
        _viewFlipper.setInAnimation(_in_from_right);
        _viewFlipper.setOutAnimation(_out_to_left);
        // Show The Previous Screen
        _viewFlipper.showPrevious();

    }

    @Override
    public void onAcceptRejectBtnClicked(boolean accept, HighlightVO vo) {
        mListner.onAcceptRejectBtnClicked(accept, vo);
    }

    @Override
    public void onMyDataNotificationClicked(ArrayList<HighlightVO> notificationList) {
        mListner.onNotificationClicked(notificationList);
    }

    @Override
    public void onSharebtnClick(HighlightVO vo) {
        mListner.onSharebtnClick(vo);
    }

    @Override
    public void onMyDataCommentBtnClick(HighlightVO vo) {
        mListner.onMyDataCommentBtnClick(vo);
    }

    @Override
    public void OnViewExpand() {
        maximizeTocDialog();
    }

    @Override
    public void OnViewMinimize() {
        minimizeTocDialog(isExpanded);
    }

    public void openMainScreen() {
        upDateData();
        _viewFlipper.setInAnimation(_in_from_left);
        _viewFlipper.setOutAnimation(_out_to_right);
        _viewFlipper.setDisplayedChild(0);
        if (_viewFlipper.getChildCount() > 1) {
            _viewFlipper.removeViewAt(1);
        }
    }

    public void setThemeColor(ReaderThemeSettingVo themeColor) {
        this.mReaderThemeSettingVo = themeColor;
    }

    private class AnimateListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation arg0) {
            _ugcData.enableButtons();
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {

        }

        @Override
        public void onAnimationStart(Animation arg0) {
            _ugcData.disableButtons();
        }

    }

    @Override
    public void settingsClicked() {
        mListner.onSettingbtnClick();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.dismissAllowingStateLoss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void maximizeTocDialog(){

        isExpanded = true;

        getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,screenHeight);
        params.height = screenHeight;
        params.leftMargin = 0;
        params.rightMargin = 0;
        _ugcData.showToolbar();
        mSwipeLayout.setVisibility(View.GONE);
        parent.setLayoutParams(params);

        ObjectAnimator anim = ObjectAnimator.ofFloat(contentView, "alpha", 0f, 1f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(200);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Activity activity = getActivity();
                if(activity != null && isAdded() && mReaderThemeSettingVo != null){
                    contentView.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getTableofcontents().getTabBg()));
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

    private void minimizeTocDialog(boolean dialogAction){

        if (dialogAction) {
            isExpanded = false;
            parent = (View) contentView.getParent();
            parent.setFitsSystemWindows(true);
            params = (FrameLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
            contentView.measure(0, 0);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            screenHeight = displaymetrics.heightPixels;
            getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,screenHeight/2);
            params.height = screenHeight/2;
            params.leftMargin = 40;
            params.rightMargin = 40;
            _ugcData.hideToolbar();
            mSwipeLayout.setVisibility(View.VISIBLE);
            parent.setLayoutParams(params);
            parent.setBackgroundColor(getResources().getColor(R.color.transparent));
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
            contentView.setBackgroundColor(getResources().getColor(R.color.transparent));

        }
        else{
            dismiss();

        }


    }

    public interface MydataitemClickListner {
        void onMydataItemClick(HighlightVO vo);

        void onSettingbtnClick();

        void onSharebtnClick(HighlightVO vo);

        void onAcceptRejectBtnClicked(boolean accept, HighlightVO vo);

        void onNotificationClicked(ArrayList<HighlightVO> notificationList);

        void onMyDataCommentBtnClick(HighlightVO vo);
    }

    @Override
    public void onDestroyView() {
        _ugcData.dismissDialog();
        super.onDestroyView();
    }
}
