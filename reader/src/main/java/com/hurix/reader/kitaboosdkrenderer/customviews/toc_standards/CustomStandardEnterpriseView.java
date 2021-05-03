package com.hurix.reader.kitaboosdkrenderer.customviews.toc_standards;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.hurix.commons.datamodel.IBook;
import com.hurix.customui.Standard.TableOfTEKSVo;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;

import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.customviews.toc_standards.CustomStandardbaseView;
import com.hurix.reader.kitaboosdkrenderer.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Amit Raj on 26-07-2019.
 */
public class CustomStandardEnterpriseView extends CustomStandardbaseView implements TabHost.TabContentFactory {

    private Context mContext;
    ViewPager viewPager;
    TabHost.TabSpec spec;
    TabHost tabHost;
    private IBook _bookVo;
    private ThemeUserSettingVo userSettingVO;
    private RelativeLayout mTabwidgetHolder;


    public CustomStandardEnterpriseView(Context context, ThemeUserSettingVo mUserSettingVO, IBook _bookVo) {
        super(context, com.hurix.epubreader.R.layout.standard_enterprise_fragment,mUserSettingVO,_bookVo);


    }

    @Override
    public void initView(View view, ThemeUserSettingVo mUserSettingVO, IBook bookVo) {
        _bookVo=bookVo;
        userSettingVO=mUserSettingVO;
        this.mContext = view.getContext();
        tabHost = (TabHost) view.findViewById(android.R.id.tabhost);// initiate TabHost
        tabHost.setup();

        if((com.hurix.epubreader.Utility.Utils.isDeviceTypeMobile(mContext))) {
            mTabwidgetHolder = (RelativeLayout) view.findViewById(R.id.tabwidget_holder);
            mTabwidgetHolder.setBackgroundDrawable(Utils.
                    getRectAngleDrawable(Color.parseColor("#ffffff"),
                            new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, getResources().getColor(com.hurix.epubreader.R.color.kitaboo_main_color)));
        }

        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        if (bookVo.getTableOfTEKSVo()!=null && bookVo.getTableOfTEKSVo().size() != 0) {
            tabHost.addTab(getTabSpec1(mContext, tabHost));
            tabHost.getTabWidget().getChildAt(0).getBackground().setColorFilter(getResources().getColor(com.hurix.epubreader.R.color.kitaboo_main_color), PorterDuff.Mode.SRC_ATOP);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            tv.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);
        }
        if (bookVo.getTableOfELPSVo()!=null && bookVo.getTableOfELPSVo().size() != 0) {
            tabHost.addTab(getTabSpec2(mContext, tabHost));
            tabHost.getTabWidget().getChildAt(1).getBackground().setColorFilter(getResources().getColor(com.hurix.epubreader.R.color.kitaboo_main_color), PorterDuff.Mode.SRC_ATOP);
            TextView tv1 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title); //Unselected Tabs
            tv1.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            tv1.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);
        }
       /* if (GlobalDataManager.getInstance().getCCCOLL().size() != 0) {
            tabHost.addTab(getTabSpec3(mContext, tabHost));
            tabHost.getTabWidget().getChildAt(2).getBackground().setColorFilter(getResources().getColor(R.color.kitaboo_main_color), PorterDuff.Mode.SRC_ATOP);
            TextView tv2 = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title); //Unselected Tabs
            tv2.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            tv2.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);
        }*/


        //tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).getBackground().setColorFilter(getResources().getColor(R.color.kitaboo_main_color), PorterDuff.Mode.SRC_ATOP);

        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int id = tabHost.getCurrentTab();
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).getBackground().setColorFilter(getResources().getColor(com.hurix.epubreader.R.color.kitaboo_main_color), PorterDuff.Mode.SRC_ATOP);

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
        return tabHost.newTabSpec("TEKS")
                .setIndicator("TEKS")
                .setContent(this);
    }

    private TabHost.TabSpec getTabSpec2(Context con, TabHost tabHost) {
        return tabHost.newTabSpec("ELPS")
                .setIndicator("ELPS")
                .setContent(this);
    }

    private TabHost.TabSpec getTabSpec3(Context con, TabHost tabHost) {


        return tabHost.newTabSpec("CC")
                .setIndicator("CC")
                .setContent(this);
    }

    @Override
    public View createTabContent(String tag) {
        if (tag.equalsIgnoreCase("TEKS")) {
            ArrayList<TableOfTEKSVo> tableOfTEKSVos=new ArrayList<>();
            tableOfTEKSVos.addAll(_bookVo.getTekscoll());
            return new CustomTEKSEnterpriseView(mContext,userSettingVO,tableOfTEKSVos);

        }
        if (tag.equalsIgnoreCase("ELPS")) {

            return new CustomELPSEnterpriseView(mContext,userSettingVO,_bookVo.getTableOfELPSVo());
        }
       /* if (tag.equalsIgnoreCase("CC")) {
            return new CCEnterpriseView(mContext);
        }*/

        return null;
    }


}
