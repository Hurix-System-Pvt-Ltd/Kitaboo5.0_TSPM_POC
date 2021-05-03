
package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.hurix.commons.Constants.EBookType;
import com.hurix.customui.datamodel.BookMarkVO;
import com.hurix.customui.toc.tob.OnTOBItemClick;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;

/**
 * Created by amitraj.sharma on 4/13/2018.
 */

public class CustomTOBView extends LinearLayout implements AdapterView.OnItemClickListener {
    private ArrayList<BookMarkVO> _allbookMarkVOs;
    private EBookType readerType;
    private CustomTOCEnterpriseView.TocItemClickListener listner;
    private OnTOBItemClick onTOBItemClick;
    private boolean ismobile;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private BottomSheetListview mTobList;
    private ArrayList<BookMarkVO> mListOfCurrentBookmark;

    public CustomTOBView(Context context) {
        super(context);
        inflateView();
    }

    public CustomTOBView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    public CustomTOBView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflateView();
    }

    public void inflateView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tob_tab_view, null, false);
        mTobList = (BottomSheetListview) view.findViewById(R.id.tob_list);
        addView(view);

    }

    public void setdata(Context context, ArrayList<BookMarkVO> allbookMarkVOs, EBookType mReaderType,
                        CustomTOCEnterpriseView.TocItemClickListener mListner, OnTOBItemClick _onTOBItemClick,
                        boolean _ismobile, ReaderThemeSettingVo _themeUserSettingVo, ArrayList<BookMarkVO> listOfCurrentBookmark) {
        readerType = mReaderType;
        listner = mListner;
        onTOBItemClick = _onTOBItemClick;
        ismobile = _ismobile;
        _allbookMarkVOs = allbookMarkVOs;
        readerThemeSettingVo = _themeUserSettingVo;
        mListOfCurrentBookmark = listOfCurrentBookmark;
        adpaterInitialize(context, allbookMarkVOs);
    }

    private void adpaterInitialize(Context context, ArrayList<BookMarkVO> _allbookMarkVOs) {
        mTobList.setOnItemClickListener(this);
        CustomTOBNewAdpater tobNewAdpater = new CustomTOBNewAdpater(context);
        tobNewAdpater.setData(_allbookMarkVOs, readerType, readerThemeSettingVo, mListOfCurrentBookmark);
        mTobList.setAdapter(tobNewAdpater);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (readerType == EBookType.FIXEDKITABOO) {
            _allbookMarkVOs.get(position).getFolioID();
            onTOBItemClick.onTobitemClick(_allbookMarkVOs.get(position));
        } else {
            onTOBItemClick.onTobitemClick(_allbookMarkVOs.get(position));
        }
    }
}
