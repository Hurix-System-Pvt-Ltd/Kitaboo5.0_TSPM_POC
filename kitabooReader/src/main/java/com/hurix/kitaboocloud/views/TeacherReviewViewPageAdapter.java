package com.hurix.kitaboocloud.views;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hurix.kitaboocloud.StudentListFragment;

import com.hurix.kitaboocloud.notifier.GlobalDataManager;

import java.util.ArrayList;
import java.util.List;

public class TeacherReviewViewPageAdapter extends FragmentPagerAdapter {

    //integer to count number of tabs
    int tabCount;
    Context mContext;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    //Constructor to the class
    public TeacherReviewViewPageAdapter(FragmentManager childFragmentManager, int tabCount, Context mContext) {
        super(childFragmentManager);
        //Initializing tab count
        this.tabCount = tabCount;
        this.mContext = mContext;
    }


    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        return mFragmentList.get(position);
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}