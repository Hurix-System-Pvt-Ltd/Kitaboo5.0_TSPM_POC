package com.hurix.kitaboocloud.kitaboosdkrenderer.sdkUtils;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;

import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.AddEubPanelDismissListener;


public class EpubSettingPanelAction  implements AddEubPanelDismissListener {
    private Context mContext;
    private CustomEpubSettingPanel epubSettingPanel;
    private AddEpubSettingPanelListener mListener;

    public EpubSettingPanelAction(Context context) {
        mContext = context;
    }

    public void setData(View view, ThemeUserSettingVo mthemeUserSettingVo) {
        epubSettingPanel = new CustomEpubSettingPanel(mContext, view);
        epubSettingPanel.setData(mthemeUserSettingVo);
        epubSettingPanel.setPosition(locateView(view));
        epubSettingPanel.showPopup(Gravity.RIGHT);
        epubSettingPanel.setListener(this);
    }

    @Override
    public void onEpubPanelPopupDismiss() {
        mListener.onSettingPanelDismiss();
    }

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0] - (v.getWidth() * 2);
        location.top = loc_int[1];
        // location.right = 200;
        location.bottom = location.top - (v.getWidth() * 4);
        return location;
    }

    public void dismissSettingPanel() {
        if (epubSettingPanel != null) {
            epubSettingPanel.dismiss();
        }

    }

    public void setListener(AddEpubSettingPanelListener listener) {
        this.mListener = listener;
    }

    public interface AddEpubSettingPanelListener{
        void onSettingPanelDismiss();
    }
}
