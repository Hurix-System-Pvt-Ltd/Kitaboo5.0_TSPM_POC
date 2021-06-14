package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.toc_standards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.hurix.customui.Standard.TableOfTEKSVo;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.customui.toc.TOCEnterpriseView;
import com.hurix.customui.toc.standard.OnTocItemClick;
import com.hurix.customui.toc.standard.TEKSHelper;
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomTOCEnterpriseView;

import java.util.ArrayList;


/**
 * Created by Amit Raj on 26-07-2019.
 */
public abstract class CustomTEKSEBaseView extends FrameLayout implements OnTocItemClick, AdapterView.OnItemClickListener
{

    protected TEKSHelper _teksHelper;
    public CustomTEKSEBaseView(Context context, int resID, ThemeUserSettingVo userSettingVO, ArrayList<TableOfTEKSVo> _rootColl) {
        super(context);
        initializeContext(context, resID,userSettingVO,_rootColl);
    }

    private void initializeContext(Context context, int resID, ThemeUserSettingVo userSettingVO, ArrayList<TableOfTEKSVo> rootColl) {
        _teksHelper = new TEKSHelper();
        SetListner((CustomTOCEnterpriseView.TocItemClickListener) context);
        View view = LayoutInflater.from(context).inflate(resID, this, true);
        initView(view,userSettingVO,rootColl);
    }

    public CustomTEKSEBaseView(Context context, AttributeSet attrs, int resID, ThemeUserSettingVo userSettingVO, ArrayList<TableOfTEKSVo> _rootColl) {
        super(context, attrs);
        initializeContext(context, resID,userSettingVO,_rootColl);
    }

    public abstract void initView(View view, ThemeUserSettingVo userSettingVO, ArrayList<TableOfTEKSVo> _rootColl);
    public abstract int getTotalDepth();
    public abstract void OnClick(AdapterView<?> parent, View view, int position,long id);
    public abstract void SetListner(CustomTOCEnterpriseView.TocItemClickListener listener);
}
