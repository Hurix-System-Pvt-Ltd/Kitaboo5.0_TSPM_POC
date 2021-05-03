package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hurix.customui.datamodel.HighlightVO;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.reader.kitaboosdkrenderer.R;


import java.util.ArrayList;

public class UGCDataAcceptRejectView extends LinearLayout implements View.OnClickListener,
        AddUGCAcceptRejectListener {
    private Context mContext;
    private CustomMyDataTabDialogFragment mTabFragmentHolder;
    private CustomMyDataFragment mMobileFragment;
    private boolean mIsMobile;
    private ThemeUserSettingVo mThemeVo;
    private ArrayList<HighlightVO> mUGClist;
    private TextView mAcceptAllText, mDeclineAllText;
    private AddUGCAcceptRejectListener mListener;
    private int mItemPositionToRemove;
    private UGCDataAcceptRejectAdapter acceptRejectAdapter;
    private ListView ugcAcceptRejectList;

    public UGCDataAcceptRejectView(Context context) {
        super(context);
    }

    public UGCDataAcceptRejectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UGCDataAcceptRejectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UGCDataAcceptRejectView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public UGCDataAcceptRejectView(Context context, CustomMyDataTabDialogFragment tabFragment,
                                   ThemeUserSettingVo themeUserSettingVo, boolean isMobile,
                                   ArrayList<HighlightVO> list) {
        super(context);
        mContext = context;
        mTabFragmentHolder = tabFragment;
        mIsMobile = isMobile;
        mUGClist = list;
        mThemeVo = themeUserSettingVo;
        initView();

    }

    public UGCDataAcceptRejectView(Context context, CustomMyDataFragment tabFragment,
                                   ThemeUserSettingVo themeUserSettingVo, boolean isMobile,
                                   ArrayList<HighlightVO> list) {
        super(context);
        mContext = context;
        mMobileFragment = tabFragment;
        mIsMobile = isMobile;
        mUGClist = list;
        mThemeVo = themeUserSettingVo;
        initView();
    }

    private void initView() {
        LayoutInflater mLayoutinfilater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mLayoutinfilater.inflate(R.layout.accept_reject_list_container, this);
        ugcAcceptRejectList = (ListView) mView.findViewById(R.id.list_share);
        ugcAcceptRejectList.setOnTouchListener(new ListView.OnTouchListener() {
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
        //LinearLayout mParent = (LinearLayout) mView.findViewById(R.id.parent_share_list);

        mView.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.WHITE,
                new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2, Color.parseColor(mThemeVo.getmKitabooMainColor())));

        mAcceptAllText = (TextView) mView.findViewById(R.id.text_accept_all);
        mDeclineAllText = (TextView) mView.findViewById(R.id.text_decline_all);
        mAcceptAllText.setOnClickListener(this);
        mDeclineAllText.setOnClickListener(this);


        acceptRejectAdapter = new UGCDataAcceptRejectAdapter();
        acceptRejectAdapter.setData(getContext(), mUGClist, mThemeVo, mIsMobile);
        acceptRejectAdapter.setListener(this);
        ugcAcceptRejectList.setAdapter(acceptRejectAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == mAcceptAllText) {
            //Need to implement
        } else if (v == mDeclineAllText) {
            //Need to implement
        }
    }

    @Override
    public void onUGCAcceptRejectData(boolean value, int position) {
        if (mListener != null && mUGClist != null && mUGClist.size() > 0 && mUGClist.size() > position) {
            mListener.onAcceptRejectViewClicked(value, mUGClist.get(position), mUGClist);
            mItemPositionToRemove = position;
        }
    }

    public void setListener(AddUGCAcceptRejectListener listener) {
        this.mListener = listener;
    }

    public void removeHighlightVo() {
        if(mUGClist.size() > 0  && mUGClist != null && mUGClist.size() > mItemPositionToRemove)
        {
            mUGClist.remove(mItemPositionToRemove);
            acceptRejectAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onAcceptRejectViewClicked(boolean v, HighlightVO vo,  ArrayList<HighlightVO>  mUGClist) {

    }
}
