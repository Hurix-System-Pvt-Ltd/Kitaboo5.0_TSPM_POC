package com.hurix.reader.kitaboosdkrenderer.sdkRenderer.sdkUtils;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;

import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.customui.views.EpubSettingPanel;

public class EpubSettingPanelAction {
    private Context mContext;
    public EpubSettingPanelAction(Context context) {
        mContext=context;
    }
    public void setData(View view , ThemeUserSettingVo mthemeUserSettingVo) {
        EpubSettingPanel epubSettingPanel= new EpubSettingPanel(mContext,view);
        epubSettingPanel.setData(mthemeUserSettingVo);
        epubSettingPanel.setPosition(locateView(view));
        epubSettingPanel.showPopup(Gravity.RIGHT);
    }
    public static Rect locateView(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }
}
