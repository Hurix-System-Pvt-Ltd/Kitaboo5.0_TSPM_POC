package com.hurix.reader.kitaboosdkrenderer.sdkRenderer.dictionary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.hurix.commons.Constants.Constants;
import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.iconify.Typefaces;
import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkRenderer.dictionary.interfaces.GlossaryClosed;
import com.hurix.reader.kitaboosdkrenderer.sdkRenderer.dictionary.interfaces.GlossaryHighlightClosed;


public class DictionaryPopUp implements OnDismissListener, OnClickListener {
    private Context mContext;
    private PopupWindow mResourcesTypePopup;
    private View mView;
    private String html_content;
    private WebView webview;
    float deviceDensity;
    private TextView mBtnClose, mIcon;
    private TextView mDictHeaderTxt;
    int widhth = 250;
    public static final int BASE_DPI = 160;
    public GlossaryClosed mGlossaryClosed;
    public GlossaryHighlightClosed mGlossaryHighlightClosed;

    public DictionaryPopUp(Context mContext) {
        super();
        this.mContext = mContext;
        initView(this.mContext);
    }

    public DictionaryPopUp(Context mContext, String html_content) {
        super();
        this.mContext = mContext;
        this.html_content = html_content;

        initView(this.mContext);
    }

    private void initView(final Context con) {

        deviceDensity = con.getResources().getDisplayMetrics().densityDpi;
        widhth = (int) (widhth * ((deviceDensity * 1f) / (BASE_DPI * 1f)));
        //positiony = (int) (positiony* ((deviceDensity*1f)/(Constants.BASE_DPI*1f)));

        LayoutInflater layoutInflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.dictionarypopup, null);

        //mResourcesTypePopup = new PopupWindow(mView,widhth,(int)(widhth*1.6f));
        //mResourcesTypePopup = new PopupWindow(mView,widhth,(int)(widhth*1.1f));
        mResourcesTypePopup = new PopupWindow(mView, LayoutParams.MATCH_PARENT, Constants.DICTIONARY_POP_UP_HEIGHT);
        mResourcesTypePopup.setContentView(mView);
        mDictHeaderTxt = (TextView) mView.findViewById(R.id.dict_header_txt);
        mDictHeaderTxt.setText(mContext.getResources().getString(R.string.dictHeaderText));
        Typeface typeFace = Typefaces.get(mContext, mContext.getResources()
                .getString(R.string.kitaboo_font_file_name));
        mBtnClose = (TextView) mView.findViewById(R.id.close_dict);
        mBtnClose.setText("4");
        mBtnClose.setTypeface(typeFace);
        mBtnClose.setTextColor(Color.WHITE);
        mBtnClose.setAllCaps(false);
        mIcon = (TextView) mView.findViewById(R.id.double_quote_img);
        mIcon.setText(PlayerUIConstants.HC_GLOSSARY_IC_TEXT);
        mIcon.setTypeface(typeFace);
        mIcon.setTextColor(Color.WHITE);
        mIcon.setAllCaps(false);
        webview = (WebView) mView.findViewById(R.id.dict_contents);

        WebSettings setting = webview.getSettings();

        setting.setJavaScriptEnabled(true);
        mBtnClose.setOnClickListener(this);
        mResourcesTypePopup.setFocusable(true);
        mResourcesTypePopup.setBackgroundDrawable(new BitmapDrawable());
        webview.setBackgroundColor(Color.parseColor("#bababa"));
        mResourcesTypePopup.setOnDismissListener(this);
        webview.loadData(html_content, "text/html; charset=UTF-8", null);
        //webview.loadUrl(html_content);

        webview.setLongClickable(true);
        webview.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        /*mBtnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mResourcesTypePopup.isShowing()) {
                    mResourcesTypePopup.dismiss();
                    con.unregisterReceiver(broadcastReceiver);

					*//*if(_listner!=null)
                    {
						_listner.OnDictonaryClick(true);
					}*//*
                }
            }
        });*/

        IntentFilter filter = new IntentFilter("android.intent.action.CONFIGURATION_CHANGED");
        con.registerReceiver(broadcastReceiver, filter);


        //activity.onConfigurationChanged(ne);
    }

    public void setData(String html_content) {
        //this.html_content = html_content;
        this.html_content = html_content;
    }


    public void showHidePopup() {
        if (mResourcesTypePopup.isShowing()) {
            mResourcesTypePopup.dismiss();
        } else {


            WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();

            Point posTemp = new Point();
            display.getSize(posTemp);

            final Point posPopup = new Point(posTemp.x, posTemp.y);

            //	mResourcesTypePopup.showAsDropDown(btn,(int)(widhth*0.03f),(int)3.0f);
            mResourcesTypePopup.showAtLocation(new View(mContext), Gravity.LEFT | Gravity.TOP, posPopup.x, posPopup.y);

            webview.loadData(html_content, "text/html; charset=UTF-8", null);

            webview.setLongClickable(true);
            webview.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });


        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mResourcesTypePopup.dismiss();

        }
    };

    @Override
    public void onClick(View v) {
        if (v == mBtnClose) {
            if (mResourcesTypePopup != null && mResourcesTypePopup.isShowing())
                mGlossaryHighlightClosed = null;
                mResourcesTypePopup.dismiss();
        }
    }

    public void dismissPopup(){
        if(mResourcesTypePopup != null && mResourcesTypePopup.isShowing()){
            mResourcesTypePopup.dismiss();
        }
    }

    @Override
    public void onDismiss() {
        if (mGlossaryClosed != null) {
            mGlossaryClosed.onGlossaryClosed();
            mGlossaryClosed = null;
        }
        if (mGlossaryHighlightClosed != null) {
            mGlossaryHighlightClosed.onGlossaryHighlightClosed();
        }
    }

    public void setGlossaryHighlightListener(GlossaryHighlightClosed listener){
        mGlossaryHighlightClosed = listener;
    }

    public void setListner(GlossaryClosed listner) {
        mGlossaryClosed = listner;
    }

}
