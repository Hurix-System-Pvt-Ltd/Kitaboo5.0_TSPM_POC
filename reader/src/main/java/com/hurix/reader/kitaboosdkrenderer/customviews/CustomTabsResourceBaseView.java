package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.hurix.commons.datamodel.IBook;
import com.hurix.commons.datamodel.UserChapterVO;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.customui.toc.tor.OnTORItemClick;
import com.hurix.epubreader.Utility.Utils;
import com.hurix.reader.kitaboosdkrenderer.R;


import java.util.ArrayList;


public abstract class CustomTabsResourceBaseView extends FrameLayout {

    public abstract void initView(View view, ArrayList<UserChapterVO> torData, IBook iBook, OnTORItemClick onTORItemClick, ThemeUserSettingVo themeUserSettingVo, Typeface typeface);

    public CustomTabsResourceBaseView(Context context, int resID, ArrayList<UserChapterVO> mTorData, IBook iBook, OnTORItemClick onTORItemClick, ThemeUserSettingVo themeUserSettingVo,Typeface typeface) {
        super(context);

        if ((Utils.isDeviceTypeMobile(context))) {
            resID= R.layout.custom_standard_mobile_new;
        }
        initializeContext(context, resID, mTorData, iBook, onTORItemClick, themeUserSettingVo,typeface);

    }

    private void initializeContext(Context context, int resID, ArrayList<UserChapterVO> mTorData, IBook iBook, OnTORItemClick onTORItemClick, ThemeUserSettingVo themeUserSettingVo, Typeface typeface) {
        View view = LayoutInflater.from(context).inflate(resID, this, true);
        initView(view, mTorData, iBook, onTORItemClick, themeUserSettingVo , typeface);
    }

    public abstract int getTotalDepth();
    public abstract void OnClick(AdapterView<?> parent, View view, int position, long id);



}
