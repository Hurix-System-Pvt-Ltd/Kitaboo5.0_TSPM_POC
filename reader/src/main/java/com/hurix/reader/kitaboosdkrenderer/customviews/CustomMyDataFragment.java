package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.hurix.customui.datamodel.HighlightVO;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import java.util.ArrayList;

import static android.view.View.VISIBLE;


public class CustomMyDataFragment extends DialogFragment implements CustomMyDataLayout.CustomMydataLayoutCallback {
    private String UGCPath = "";
    private ViewFlipper _viewFlipper;
    private ImageView _arrowUp;
    private CustomMyDataLayout _ugcData;
    private int arrowHeight, arrowWidth;
    private View _anchor;
    private MydataitemClickListner mListner;
    private Animation _in_from_left;
    private Animation _out_to_right;
    private Animation _in_from_right;
    private Animation _out_to_left;
    private Object broadcastReceiverInstance;
    private HighlightVO _vo;
    private int mHeight;
    private int mWidth;
    private int mGravity;
    private ArrayList<HighlightVO> alldatalist = new ArrayList<HighlightVO>();
    public static View mAnchor;
    public static boolean isMobile;
    private int mEnableShareSetting;
    private ReaderThemeSettingVo readerThemeSettingVo;

    /**
     * Create instance of fragment ,initialize fragment
     *
     * @param tool     : flag
     * @param anchor   : View type ,show fragment in below position to view
     * @param ismobile : Boolean variable to show different view for mobile and tablet
     * @return
     */
    public static CustomMyDataFragment newInstance(String tool, View anchor, boolean ismobile) {
        mAnchor = anchor;
        isMobile = ismobile;
        CustomMyDataFragment fragmentFirst = new CustomMyDataFragment();
        Bundle args = new Bundle();
        args.putString("data", tool.toString());
        args.putBoolean("ismobile", ismobile);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
               if ( _viewFlipper.getChildCount() > 1){
                   openMainScreen();
               }else {
                   dismiss();
               }

            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialogFragment);
    }

    @Override
    public int getTheme() {
        return R.style.CustomDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_mydata_main_screen, container, false);
        buildView(view, mAnchor, isMobile);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        if (getDialog() != null) {

            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isMobile) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().getAttributes().windowAnimations = R.style.SideAnimation;
        }
        Display display = (getActivity()).getWindowManager().getDefaultDisplay();
        if (display != null && getDialog() != null) {
            setLayout();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        synchronized (this) {
            if (broadcastReceiverInstance != null) {
                unregisterReceiver(broadcastReceiverInstance);
            }
        }

    }

    private void unregisterReceiver(Object broadcastReceiverInstance) {

    }

    /**
     * use to set height and width from reader
     *
     * @param height
     * @param width
     */
    public void setParams(int width, int height, int gravity) {
        mHeight = height;
        mWidth = width;
        mGravity = gravity;
    }

    public void setData(ArrayList<HighlightVO> list) {
        alldatalist = list;
    }

    private void setLayout() {
        getDialog().getWindow().setLayout(mWidth, mHeight);
        getDialog().getWindow().setGravity(mGravity);
    }

    public void setSharingSettingVisibility(int visibility) {
        mEnableShareSetting = visibility;
    }

    public void buildView(View view, View anchor, boolean ismobile) {
        view.setBackgroundColor(Color.TRANSPARENT);
        _viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
        _anchor = anchor;
        if (_anchor != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int[] location = new int[2];
            _anchor.getLocationOnScreen(location);
            final int anchorLeft = location[0];
            final int anchorTop = location[1];
            final int anchorWidth = anchor.getMeasuredWidth();
            final int anchorHeight = anchor.getMeasuredHeight();
            setPosition(anchorLeft, anchorTop, anchorWidth, anchorHeight);
        }

        AnimateListener listener = new AnimateListener();
        _in_from_left = AnimationUtils.loadAnimation(getActivity(), R.anim.in_from_left);
        _out_to_right = AnimationUtils.loadAnimation(getActivity(), R.anim.out_to_right);
        _in_from_right = AnimationUtils.loadAnimation(getActivity(), R.anim.in_from_right);
        _out_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.out_to_left);

        _in_from_left.setAnimationListener(listener);
        _out_to_right.setAnimationListener(listener);
        _in_from_right.setAnimationListener(listener);
        _out_to_left.setAnimationListener(listener);


        _ugcData = new CustomMyDataLayout(getActivity(), ismobile, readerThemeSettingVo, mEnableShareSetting);
        _ugcData.enableSetting(mEnableShareSetting);
        _ugcData.setData(alldatalist);
        _ugcData.setListner(this);
        _viewFlipper.addView(_ugcData, 0);


    }

    public void buildViewForSharingSetting(View view) {

        _viewFlipper.addView(view, 1);

        // The Next screen will come in form Right and current Screen will go OUT from Left

        _viewFlipper.setInAnimation(_in_from_right);
        _viewFlipper.setOutAnimation(_out_to_left);
        _viewFlipper.showPrevious();

    }

    public void refreshMyDataNoteFragment() {
        if(_ugcData != null)
            _ugcData.refreshMyDataNoteFragment();
    }

    private void setPosition(int anchorLeft, int anchorTop, int anchorWidth, int anchorHeight) {
        Rect anchorRect = new Rect(anchorLeft, anchorTop, anchorLeft + anchorWidth, anchorTop + anchorHeight);
        int xPos = anchorRect.left + (anchorWidth / 2);
        if (xPos < 0) {
            xPos = 5;
        }
        int yPos = anchorRect.bottom;
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        params.x = 10;
        params.y = yPos;
        params.gravity = Gravity.TOP;
        getDialog().getWindow().setAttributes(params);
    }

    public void upDateData() {
        if (_ugcData != null) {
            _ugcData.update();
            _ugcData.enableSetting(mEnableShareSetting);
            _ugcData.setData(alldatalist);
        }
    }

    public void update(Object obj) {
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

        _vo = vo;

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
    public void onSharebtnClick(HighlightVO vo) {
        mListner.onSharebtnClick(vo);
    }

    @Override
    public void onMyDataCommentBtnClick(HighlightVO vo) {
        mListner.onMyDataCommentBtnClick(vo);
    }

    @Override
    public void OnViewExpand() {

    }

    @Override
    public void OnViewMinimize() {

    }

    @Override
    public void onMyDataNotificationClicked(ArrayList<HighlightVO> notificationList) {
        mListner.onNotificationClicked(notificationList);
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
        this.readerThemeSettingVo = themeColor;
    }

    private class AnimateListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation arg0) {
            _viewFlipper.setInAnimation(null);
            _viewFlipper.setOutAnimation(null);
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

    public interface MydataitemClickListner {
        void onMydataItemClick(HighlightVO vo);

        void onSettingbtnClick();

        void onSharebtnClick(HighlightVO vo);

        void onMyDataCommentBtnClick(HighlightVO vo);

        void onAcceptRejectBtnClicked(boolean accept, HighlightVO vo);

        void onNotificationClicked(ArrayList<HighlightVO> list);
    }

    @Override
    public void onDestroyView() {
        _ugcData.dismissDialog();
        super.onDestroyView();
    }
}
