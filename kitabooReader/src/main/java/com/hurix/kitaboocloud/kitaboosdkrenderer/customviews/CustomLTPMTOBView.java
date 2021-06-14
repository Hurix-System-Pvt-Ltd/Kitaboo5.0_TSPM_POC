package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.hurix.commons.Constants.EBookType;
import com.hurix.commons.datamodel.IPage;
import com.hurix.commons.datamodel.UserChapterVO;
import com.hurix.customui.datamodel.BookMarkVO;
import com.hurix.customui.toc.tob.OnTOBItemClick;
import com.hurix.customui.views.EmptyMsg;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import java.util.ArrayList;

public class CustomLTPMTOBView extends FrameLayout implements ExpandableListView.OnChildClickListener {

    public CustomLTPMTOBViewAdapter mExpandableListAdapter;
    private Context mContext;
    private ArrayList<UserChapterVO> _allbookMarkVOs;
    private EBookType readerType;
    private CustomTOCEnterpriseView.TocItemClickListener listner;
    private OnTOBItemClick onTOBItemClick;
    private boolean ismobile;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private BottomSheetListview mTobList;
    private ArrayList<BookMarkVO> mListOfCurrentBookmark;
    private ArrayList<UserChapterVO> mChapterlist, mTempChapterList;
    private LayoutInflater mInflater;
    private View mView;
    private CustomExpandableList mExpandableListview;
    private EmptyMsg mTxtmessage;
    private IPage mCurrentPageDetails;

    public CustomLTPMTOBView(Context context, ArrayList<UserChapterVO> allbookMarkVOs, EBookType mReaderType,
                             CustomTOCEnterpriseView.TocItemClickListener mListner, OnTOBItemClick _onTOBItemClick,
                             boolean deviceTypeMobile, ReaderThemeSettingVo _themeUserSettingVo,
                             ArrayList<BookMarkVO> listOfCurrentBookmark, IPage pageDetails) {
        super(context);
        mContext = context;
        _allbookMarkVOs = allbookMarkVOs;
        readerType = mReaderType;
        listner = mListner;
        onTOBItemClick = _onTOBItemClick;
        ismobile = deviceTypeMobile;
        readerThemeSettingVo = _themeUserSettingVo;
        mListOfCurrentBookmark = listOfCurrentBookmark;
        mCurrentPageDetails = pageDetails;
        onCreateView();
    }

    public CustomLTPMTOBView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreateView();
    }

    public CustomLTPMTOBView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onCreateView();
    }

    public View onCreateView() {
        mTempChapterList = new ArrayList<UserChapterVO>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.custom_enterprise_ltpm_bookmark_fragment, this);
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
        if (_allbookMarkVOs != null) {
            update();
        }
        return mView;
    }

    public void update() {
        for (UserChapterVO vo : _allbookMarkVOs) {
            if (vo.getBookMarkList().size() > 0) {
                mTempChapterList.add(vo);
            }
        }

        if (!mTempChapterList.isEmpty()) {
            if (mExpandableListAdapter == null) {
                mExpandableListAdapter = new CustomLTPMTOBViewAdapter(mContext);
                mExpandableListAdapter.setData(mTempChapterList, readerThemeSettingVo);
                mExpandableListview.setAdapter(mExpandableListAdapter);
                int position = getCurrentExpandViewPosition();
                if (position != 0) {
                    mExpandableListview.expandGroup(position - 1, true);
                }
                mExpandableListAdapter.setFromAutoExpand(true, mCurrentPageDetails.getChapterName());

            } else {
                mExpandableListAdapter.setData(mTempChapterList, readerThemeSettingVo);
                mExpandableListAdapter.notifyDataSetChanged();
            }
        } else {
            if (readerThemeSettingVo != null) {
                mTxtmessage.setData(mContext.getResources().getString(R.string.alert_toc_no_bookmarks_found),
                        Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
            } else {
                mTxtmessage.setData(mContext.getResources().getString(R.string.alert_toc_no_bookmarks_found),
                        mContext.getResources().getColor(R.color.reader_font_color));
            }
            mExpandableListview.setEmptyView(mTxtmessage);
        }
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

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        onTOBItemClick.onTobitemClick(mTempChapterList.get(groupPosition).getBookMarkList().get(childPosition));
        return false;
    }
}
