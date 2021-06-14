package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.toc_standards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.hurix.commons.datamodel.IBook;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.epubreader.Utility.Utils;
import com.hurix.kitaboocloud.R;


/**
     * Created by Amit Raj on 26-07-2019.
     */


    public abstract class CustomStandardbaseView extends FrameLayout {

        public abstract void initView(View view, ThemeUserSettingVo mUserSettingVO, IBook bookVo);

        public CustomStandardbaseView(Context context, int resID, ThemeUserSettingVo mUserSettingVO, IBook bookVo) {
            super(context);
            if ((Utils.isDeviceTypeMobile(context))) {
                resID= R.layout.custom_standard_mobile_new;
            }
            initializeContext(context, resID,mUserSettingVO,bookVo);

        }

        private void initializeContext(Context context, int resID,ThemeUserSettingVo mUserSettingVO,IBook bookVo) {
            View view = LayoutInflater.from(context).inflate(resID, this, true);
            initView(view,mUserSettingVO,bookVo);
        }

        public abstract int getTotalDepth();
        public abstract void OnClick(AdapterView<?> parent, View view, int position, long id);



    }

