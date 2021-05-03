package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.app.Dialog;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by amit.sharma on 1/23/2018.
 */

public interface CustomTocListner {
    void setTocLayout(View view);

    void setDialogPosition(Dialog dialog);

    void initialiseTabHost(TabHost tabHost);

    View returnTabView(String tag, CustomTOCEnterpriseView.TocItemClickListener mListner);

    void onDialogBackpressed();
}
