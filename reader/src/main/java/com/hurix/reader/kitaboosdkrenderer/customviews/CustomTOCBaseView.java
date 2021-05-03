package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;

import com.hurix.customui.toc.OnTocItemClick;
import com.hurix.customui.toc.TOCHelper;
import com.hurix.customui.toc.TableOfContentVO;

import java.util.ArrayList;

public abstract class CustomTOCBaseView extends FrameLayout implements OnTocItemClick, OnItemClickListener {

    protected TOCHelper _tocHelper;
    private CustomTOCEnterpriseView.TocItemClickListener mListner;
    private Context mContext;
    private int mResId;
    private ArrayList<TableOfContentVO> mTocColl;

    public CustomTOCBaseView(Context context, int resID) {
        super(context);
        mContext = context;
        mResId = resID;
    }

    public CustomTOCBaseView(Context context, int resID, ArrayList<TableOfContentVO> tocColl) {
        super(context);
        mContext = context;
        mResId = resID;
        mTocColl = tocColl;
    }

    public void initializeContext(Context context, int resID, ArrayList<TableOfContentVO> tocColl) {
        _tocHelper = new TOCHelper();
        View view = LayoutInflater.from(context).inflate(resID, this, true);
        initView(view, tocColl);
    }

    public CustomTOCBaseView(Context context, AttributeSet attrs, int resID) {
        super(context, attrs);
        initializeContext(context, resID, null);
    }

    public void setListener(CustomTOCEnterpriseView.TocItemClickListener listner) {
        mListner = listner;
    }

    public CustomTOCEnterpriseView.TocItemClickListener getListener() {
        return mListner;
    }

    public abstract void initView(View view, ArrayList<TableOfContentVO> tocColl);

    public abstract int getTotalDepth();

    public abstract void OnClick(AdapterView<?> parent, View view, int position, long id);

}
