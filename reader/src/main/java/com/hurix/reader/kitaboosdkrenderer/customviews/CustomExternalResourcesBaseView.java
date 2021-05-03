package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.hurix.commons.datamodel.IBook;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.customui.toc.standard.OnTocItemClick;
import com.hurix.customui.toc.tor.OnTORItemClick;


public abstract class CustomExternalResourcesBaseView extends FrameLayout implements OnTocItemClick, AdapterView.OnItemClickListener
{

    public CustomExternalResourcesBaseView(Context context, int resID, IBook _bookVo, ThemeUserSettingVo userSettingVO, OnTORItemClick mOnTORItemClick) {
        super(context);
        initializeContext(context, resID, _bookVo, userSettingVO, mOnTORItemClick);
    }

    private void initializeContext(Context context, int resID, IBook _bookVo, ThemeUserSettingVo userSettingVO, OnTORItemClick mOnTORItemClick) {
        View view = LayoutInflater.from(context).inflate(resID, this, true);
        initView(view, _bookVo, userSettingVO, mOnTORItemClick);
    }

    public abstract void initView(View view, IBook _bookVo, ThemeUserSettingVo userSettingVO, OnTORItemClick mOnTORItemClick);
    public abstract int getTotalDepth();
    public abstract void OnClick(AdapterView<?> parent, View view, int position, long id);

}
