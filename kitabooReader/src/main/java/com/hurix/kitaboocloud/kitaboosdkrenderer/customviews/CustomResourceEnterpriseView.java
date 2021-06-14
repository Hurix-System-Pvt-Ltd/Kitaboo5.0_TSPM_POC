package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.hurix.commons.datamodel.IBook;
import com.hurix.commons.datamodel.UserChapterVO;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.customui.toc.tor.OnTORItemClick;
import com.hurix.customui.toc.tor.TORView;
import com.hurix.epubreader.Utility.Utils;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import java.util.ArrayList;


public class CustomResourceEnterpriseView extends CustomTabsResourceBaseView implements TabHost.TabContentFactory {


    private Context mContext;
    private TabHost.TabSpec spec;
    private TabHost tabHost;
    private IBook _bookVo;
    private ThemeUserSettingVo userSettingVO;
    private ArrayList<UserChapterVO> mTorData;
    private OnTORItemClick mOnTORItemClick;
    private RelativeLayout mTabwidgetHolder;
    private Typeface _typeface;
    private ReaderThemeSettingVo mReaderSettingVo;

    public CustomResourceEnterpriseView(Context context, ArrayList<UserChapterVO> torData, IBook iBook, OnTORItemClick onTORItemClick, ThemeUserSettingVo themeUserSettingVo, Typeface typeface,
                                        ReaderThemeSettingVo readerSettingVo) {
        super(context, R.layout.custom_standard_enterprise_fragment, torData, iBook, onTORItemClick, themeUserSettingVo,typeface,readerSettingVo);
        _bookVo = iBook;
        mTorData = torData;
        mOnTORItemClick = onTORItemClick;
        userSettingVO = themeUserSettingVo;
        _typeface = typeface;
        mReaderSettingVo = readerSettingVo;
    }

    @Override
    public void initView(View view, ArrayList<UserChapterVO> torData, IBook iBook, OnTORItemClick onTORItemClick, ThemeUserSettingVo themeUserSettingVo,Typeface typeface,
                         ReaderThemeSettingVo readerSettingVo) {
        this.mContext = view.getContext();
        _bookVo = iBook;
        mTorData = torData;
        mOnTORItemClick = onTORItemClick;
        userSettingVO = themeUserSettingVo;
        _typeface =typeface;
        tabHost = (TabHost) view.findViewById(android.R.id.tabhost); // initiate TabHost
        tabHost.setup();
        mReaderSettingVo = readerSettingVo;

        /*if((Utils.isDeviceTypeMobile(mContext))) {
            mTabwidgetHolder = (RelativeLayout) view.findViewById(R.id.tabwidget_holder);
            mTabwidgetHolder.setBackgroundDrawable(com.hurix.kitaboo.constants.utils.Utils.
                    getRectAngleDrawable(Color.parseColor("#ffffff"),
                            new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, getResources().getColor(R.color.kitaboo_main_color)));
        }*/

        TabHost.TabSpec spec;


        if (SDKManager.getInstance().getInternalResources() != null && SDKManager.getInstance().getInternalResources().size() > 0) {
                tabHost.addTab(getTabSpec1(mContext, tabHost));
                tabHost.getTabWidget().getChildAt(0).getBackground().setColorFilter(getResources().getColor(R.color.kitaboo_main_color), PorterDuff.Mode.SRC_ATOP);
                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //Unselected Tabs
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);
                tv.setAllCaps(false);
        }


        if (_bookVo.getExternalResourcesVocoll() != null && _bookVo.getExternalResourcesVocoll().size() != 0) {

            tabHost.addTab(getTabSpec2(mContext, tabHost));
            if (mTorData.size() > 0 && mTorData.get(0).getResourcelist().size() != 0) {
                tabHost.getTabWidget().getChildAt(1).getBackground().setColorFilter(getResources().getColor(R.color.kitaboo_main_color), PorterDuff.Mode.SRC_ATOP);
                TextView tv1 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
                tv1.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv1.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);
                tv1.setAllCaps(false);
            } else {
                tabHost.getTabWidget().getChildAt(0).getBackground().setColorFilter(getResources().getColor(R.color.kitaboo_main_color), PorterDuff.Mode.SRC_ATOP);
                TextView tv1 = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
                tv1.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv1.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);
                tv1.setAllCaps(false);
            }


        }

        tabHost.getTabWidget().setDividerDrawable(null);
        if(mReaderSettingVo != null)
        {
            tabHost.setBackgroundColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
        }

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab()).setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getTabBg()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2,Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getTabBorder())));
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            if(mReaderSettingVo != null)
                tv.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getTabTextColor()));
            else
                tv.setTextColor(Color.BLACK);
        }
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int id = tabHost.getCurrentTab();
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).getBackground().setColorFilter(getResources().getColor(R.color.kitaboo_main_color), PorterDuff.Mode.SRC_ATOP);

                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    tv.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);
                }

                TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);
            }
            /*      tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab()).setBackgroundDrawable(Utils.getRectAngleDrawable(Color.WHITE,
                        new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2, Color.parseColor("#ffffff")));*/

              /*  for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff")); // unselected
                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab()).setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(UserController
                                    .getInstance(getContext()).getUserSettings().get_reader_default_panel_color()),
                            new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor("#3385ff")));
                    tv.setTextColor(Color.parseColor("#0066ff"));
                }

               // tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#0000FF")); // selected
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).getBackground().setColorFilter(getResources().getColor(R.color.kitaboo_main_color), PorterDuff.Mode.SRC_ATOP);
                TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab()).setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(UserController
                                .getInstance(getContext()).getUserSettings().get_reader_default_panel_color()),
                        new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor("#ffffff")));
                tv.setTextColor(Color.parseColor("#000000"));*/

        });

    }

    @Override
    public int getTotalDepth() {
        return 0;
    }

    @Override
    public void OnClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private TabHost.TabSpec getTabSpec1(Context con, TabHost tabHost) {
        return tabHost.newTabSpec("INTERNAL RESOURCE")
                .setIndicator(getResources().getString(R.string.internal_resources))
                .setContent(this);
    }

    private TabHost.TabSpec getTabSpec2(Context con, TabHost tabHost) {
        return tabHost.newTabSpec("EXTERNAL RESOURCE")
                .setIndicator("External Resources")
                .setContent(this);
    }


    @Override
    public View createTabContent(String tag) {
        if (tag.equalsIgnoreCase("INTERNAL RESOURCE")) {
            //return new TEKSEnterpriseView(mContext);
            //return new TORView(mContext);
            if(mReaderSettingVo != null)
                return new CustomTOR(mContext,mTorData, mOnTORItemClick, userSettingVO,_typeface,mReaderSettingVo);
            else
                return new TORView(mContext,mTorData, mOnTORItemClick, userSettingVO,_typeface);
            //return LayoutInflater.from(mContext).inflate(R.layout.resource_item, null);
        }
        if (tag.equalsIgnoreCase("EXTERNAL RESOURCE")) {
            //return LayoutInflater.from(mContext).inflate(R.layout.resource_item, null);
            //return new ELPSEnterpriseView(mContext);
            return new CustomExternalResourcesEnterpriseView(mContext, _bookVo, userSettingVO, mOnTORItemClick);
        }
//        if (tag.equalsIgnoreCase("CC")){
//            return new CCEnterpriseView(mContext);
//        }

        return null;

    }
}
