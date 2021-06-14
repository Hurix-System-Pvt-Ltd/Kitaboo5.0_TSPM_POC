package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;

import com.hurix.commons.datamodel.IPage;
import com.hurix.commons.datamodel.UserChapterVO;
import com.hurix.customui.toc.tor.OnTORItemClick;
import com.hurix.customui.views.EmptyMsg;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import java.util.ArrayList;

public class CustomTORView extends FrameLayout implements OnChildClickListener {
    private OnTORItemClick mOnTORItemClick;
    private CustomExpandableList mExpandableListview;
    private CustomTORViewAdapter mResourceAdapter;
    private LayoutInflater mInflater;
    private Context mContext;
    private View mView;
    private ArrayList<UserChapterVO> mChapterlist, mTempChapterList;
    private EmptyMsg mTxtmessage;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private IPage mCurrentPageDetails;

    public CustomTORView(Context context, ArrayList<UserChapterVO> chapterVo, OnTORItemClick onTORItemClick, ReaderThemeSettingVo _themeUserSettingVo, IPage pageDetails) {
        super(context);
        this.mContext = context;
        mChapterlist = chapterVo;
        mOnTORItemClick = onTORItemClick;
        readerThemeSettingVo = _themeUserSettingVo;
        mCurrentPageDetails = pageDetails;
        onCreateView();

    }

    public CustomTORView(Context context, ArrayList<UserChapterVO> chapterVo, AttributeSet attrs,
                         ReaderThemeSettingVo _themeUserSettingVo, IPage pageDetails) {
        super(context, attrs);
        this.mContext = context;
        mChapterlist = chapterVo;
        readerThemeSettingVo = _themeUserSettingVo;
        mCurrentPageDetails = pageDetails;
        onCreateView();

    }

    public View onCreateView() {
        mTempChapterList = new ArrayList<UserChapterVO>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.custom_enterprise_resource_fragment, this);
        mExpandableListview = (CustomExpandableList) mView.findViewById(R.id.expendableListView);
        mTxtmessage = (EmptyMsg) mView.findViewById(R.id.empty);
        mExpandableListview.setOnChildClickListener(this);
        if (readerThemeSettingVo != null) {
            mView.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            mExpandableListview.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
        } else {
            mView.setBackgroundColor(Color.WHITE);
            mTxtmessage.setBackgroundColor(Color.WHITE);
            mTxtmessage.setBackgroundColor(Color.TRANSPARENT);
        }
        if (mChapterlist != null) {
            update();
        }
        return mView;
    }

    private int getCurrentExpandViewPosition() {
        int position = 0;
        for (int i = 0; i < mTempChapterList.size(); i++) {
            if (mTempChapterList.get(i).getChapterName().equalsIgnoreCase(mCurrentPageDetails.getChapterName())) {
                position = i + 1;
                break;
            }
        }
        return position;
    }

    public void update() {
        for (UserChapterVO vo : mChapterlist) {
            if (vo.getResourcelist().size() > 0) {
                mTempChapterList.add(vo);
            }
        }

        if (!mTempChapterList.isEmpty()) {
            if (mResourceAdapter == null) {
                mResourceAdapter = new CustomTORViewAdapter(mContext);
                mResourceAdapter.setData(mTempChapterList, readerThemeSettingVo);
                mExpandableListview.setAdapter(mResourceAdapter);
                int position = getCurrentExpandViewPosition();
                if (position != 0) {
                    mExpandableListview.expandGroup(position - 1, true);
                }
                mResourceAdapter.setFromAutoExpand(true, mCurrentPageDetails.getChapterName());

            } else {
                mResourceAdapter.setData(mTempChapterList, readerThemeSettingVo);
                mResourceAdapter.notifyDataSetChanged();
            }
        } else {
            if (readerThemeSettingVo != null) {
                mTxtmessage.setData(mContext.getResources().getString(R.string.alert_tor_no_resources_found), Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
            } else {
                mTxtmessage.setData(mContext.getResources().getString(R.string.alert_tor_no_resources_found), mContext.getResources().getColor(R.color.reader_font_color));
            }
            mExpandableListview.setEmptyView(mTxtmessage);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        mOnTORItemClick.onTorItemClick(mTempChapterList.get(groupPosition).getResourcelist().get(childPosition));
        return false;
    }

    private boolean getPageRefreshNeeded(int currentPageID, int selectedPageID) {
        boolean blReturn = false;
        int previousPageID = currentPageID - 1;
        int nextPageID = currentPageID + 1;
        if (selectedPageID == currentPageID || selectedPageID == previousPageID || selectedPageID == nextPageID) {
            blReturn = true;
        }
        return blReturn;
    }
}