package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hurix.commons.datamodel.IBook;
import com.hurix.commons.datamodel.UserChapterVO;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.customui.toc.tor.OnTORItemClick;
import com.hurix.customui.toc.tor.TORViewAdapter;
import com.hurix.customui.views.EmptyMsg;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;

public class CustomTOR extends FrameLayout implements OnChildClickListener, ExpandableListView.OnGroupClickListener {
    private IBook mbookVo;
    private OnTORItemClick mOnTORItemClick;
    private ExpandableListView mExpandableListview;
    private CustomTORAdapter mResourceAdapter;
    private LayoutInflater mInflater;
    private Context mContext;
    private View mView;
    private ArrayList<UserChapterVO> mChapterlist, mTempChapterList;
    private EmptyMsg mTxtmessage;
    private ThemeUserSettingVo themeUserSettingVo;
    private boolean isJumpToBookInNative = false;
    private Typeface _typeface;
    private ReaderThemeSettingVo mReaderSettingVo;

    public CustomTOR(Context context, ArrayList<UserChapterVO> chapterVo, OnTORItemClick onTORItemClick, ThemeUserSettingVo _themeUserSettingVo, Typeface typeface,
                     ReaderThemeSettingVo readerSettingVo) {
        super(context);
        this.mContext = context;
        mChapterlist = chapterVo;
        mOnTORItemClick = onTORItemClick;
        themeUserSettingVo=_themeUserSettingVo;
        _typeface = typeface;
        mReaderSettingVo = readerSettingVo;
        onCreateView();
    }

    public CustomTOR(Context context, ArrayList<UserChapterVO> chapterVo, AttributeSet attrs,ThemeUserSettingVo _themeUserSettingVo,Typeface typeface) {
        super(context, attrs);
        this.mContext = context;
        mChapterlist = chapterVo;
        themeUserSettingVo=_themeUserSettingVo;
        _typeface = typeface;
        onCreateView();
    }

    public CustomTOR(Context context, ArrayList<UserChapterVO> chapterVo, OnTORItemClick onTORItemClick,
                   ThemeUserSettingVo _themeUserSettingVo, IBook _bookVo, boolean isJumpToBookInNative, Typeface typeface) {
        super(context);
        this.mContext = context;
        mChapterlist = chapterVo;
        mbookVo = _bookVo;
        mOnTORItemClick = onTORItemClick;
        themeUserSettingVo=_themeUserSettingVo;
        this.isJumpToBookInNative = isJumpToBookInNative;
        _typeface = typeface;
        onCreateView();
    }

    public View onCreateView() {
        mTempChapterList = new ArrayList<UserChapterVO>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.enterprise_resource_fragment, this);
        // mView.setBackgroundColor(Color.WHITE);
        mExpandableListview = (ExpandableListView) mView.findViewById(R.id.expendableListView);
        if(isJumpToBookInNative)
        {
            TextView tvResourceTitle = (TextView) mView.findViewById(R.id.tv_resource_title);
            tvResourceTitle.setVisibility(GONE);
            if(mbookVo != null && mbookVo.getChapterName() != null && !mbookVo.getChapterName().isEmpty())
                tvResourceTitle.setText(mbookVo.getChapterName() + "-TOC");
            mExpandableListview.setOnGroupClickListener(this);
        }

        mTxtmessage = (EmptyMsg) mView.findViewById(R.id.empty);
        mExpandableListview.setOnChildClickListener(this);

        if (mReaderSettingVo != null) {
            mView.setBackgroundColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            mExpandableListview.setBackgroundColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            //mTxtmessage.setBackgroundColor(Color.parseColor(themeUserSettingVo.getmToclistBackgroundColor()));
        }else {
            mView.setBackgroundColor(Color.WHITE);
            mTxtmessage.setBackgroundColor(Color.WHITE);
            mTxtmessage.setBackgroundColor(Color.TRANSPARENT);
        }
        if (mChapterlist != null) {
            update();
        }
        return mView;
    }

    public void update() {
        /*for (UserChapterVO vo : mChapterlist) {
            if (vo.getResourcelist().size() > 0) {
                mTempChapterList.add(vo);
            }
        }*/
        mTempChapterList = SDKManager.getInstance().getInternalResources();

        if (!mTempChapterList.isEmpty()) {
            if (mResourceAdapter == null) {
                mResourceAdapter = new CustomTORAdapter(mContext,mReaderSettingVo);
                mResourceAdapter.setData(mTempChapterList,themeUserSettingVo, _typeface);
                if(mbookVo != null)
                    mResourceAdapter.setBookVo(mbookVo,isJumpToBookInNative);
                mExpandableListview.setAdapter(mResourceAdapter);
            } else {
                mResourceAdapter.setData(mTempChapterList,themeUserSettingVo, _typeface);
                mResourceAdapter.notifyDataSetChanged();
            }
        } else {
            //mTxtmessage.setData(getContext().getResources().getString(R.string.alert_tor_no_resources_found),Color.parseColor(UserController.getInstance(getContext()).getUserSettings().getReaderFont()));
            if (themeUserSettingVo != null) {
                mTxtmessage.setData(mContext.getResources().getString(R.string.alert_tor_no_resources_found),Color.parseColor(themeUserSettingVo.getReaderFont()));
                //mTxtmessage.setBackgroundColor(Color.TRANSPARENT);
            }else {
                mTxtmessage.setData(mContext.getResources().getString(R.string.alert_tor_no_resources_found),mContext.getResources().getColor(R.color.reader_font_color));
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
        int previousPageID =currentPageID - 1 ;
        int nextPageID =currentPageID + 1 ;
        if(selectedPageID == currentPageID || selectedPageID == previousPageID || selectedPageID ==nextPageID) {
            blReturn = true;
        }
        return blReturn;
    }
    private void setThemeColor(ThemeUserSettingVo _themeUserSettingVo){
        //themeUserSettingVo=_themeUserSettingVo;
        onCreateView();
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if(v.findViewById(R.id.expandableIcon) != null && v.findViewById(R.id.expandableIcon).getVisibility() == VISIBLE)
        {
            //return  mExpandableListview.isGroupExpanded(groupPosition) ? mExpandableListview.collapseGroup(groupPosition) : mExpandableListview.expandGroup(groupPosition);
            return  mExpandableListview.expandGroup(groupPosition);
        }
        else
        {
            if(!(mChapterlist.get(groupPosition).getChapterName().equalsIgnoreCase(mbookVo.getChapterName())))
            {
                mOnTORItemClick.onTorItemClick(mTempChapterList.get(groupPosition).getResourceObj());
            }
            return false;
        }
    }
}