package com.hurix.kitaboocloud;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hurix.customui.interfaces.AssesmentControlListener;
import com.hurix.customui.interfaces.IUser;
import com.hurix.customui.interfaces.IClass;
import com.hurix.kitaboocloud.notifier.GlobalDataManager;

import java.util.ArrayList;

class StudentViewPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{


    private final StudentListFragment.IStudentReviewActionsListner mStudentReviewActionsListner;
    private final ViewPager mStudentListViewPager;
    private ArrayList<IClass> mClasslist ;
    int count=0;
    private StudentListFragment studentFragment;
    private AssesmentControlListener mAssesmentControlListener;
    private StudentListFragment studentListFragment;
    private int lastTabPosition;


    public StudentViewPagerAdapter(FragmentManager childFragmentManager, ArrayList<IClass> mClassList, AssesmentControlListener assesmentControlListener,
                                   StudentListFragment.IStudentReviewActionsListner studentReviewActionsListner, ViewPager studentListViewPager) {
        super(childFragmentManager);
        this.mClasslist=mClassList;
        mAssesmentControlListener = assesmentControlListener;
        mStudentReviewActionsListner = studentReviewActionsListner;
        mStudentListViewPager = studentListViewPager;
        mStudentListViewPager.addOnPageChangeListener(this);
        if(mClasslist!=null)
        mStudentListViewPager.setOffscreenPageLimit(mClasslist.size());
        else
            mStudentListViewPager.setOffscreenPageLimit(3);

        GlobalDataManager.getInstance().clearTeacherReviewFragmentHashMap();
        com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().clearUgcListHashMap();
    }



    @Override
    public Fragment getItem(int position) {

        studentListFragment = new StudentListFragment(mClasslist.get(position),getUsers(mClasslist.get(position).getStudentList()));
        studentListFragment.setlistner(mAssesmentControlListener,mStudentReviewActionsListner);

        GlobalDataManager.getInstance().addTeacherReviewFragmentInstance(mClasslist.get(position).getID(), studentListFragment);

        if(mClasslist.size() > position && position==0)
        {
            StudentListFragment studentListFragment = ((StudentListFragment) com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().getTeacherReviewFragmentInstance(mClasslist.get(0).getID()));

            if(studentListFragment!=null){
                studentListFragment.refreshAdapter();
                studentListFragment.setTabDetails(0);
                studentListFragment.allowCallingFetchStudentDataService(true);
                studentListFragment.callRequestForUserDataService();
            }

        }
        return studentListFragment;
    }

    private ArrayList<IUser> getUsers(ArrayList<IUser> listofalluserd) {
        ArrayList<IUser> listofusertmp = new ArrayList<IUser>();
        ArrayList<IUser> listofuser = listofalluserd;
        for (int i = 0; i < listofuser.size(); i++) {
            if (!listofuser.get(i).getRoleName().equalsIgnoreCase(com.hurix.commons.Constants.Constants.TEACHER)) {
                listofusertmp.add(listofuser.get(i));
            }
        }
        return listofusertmp;
    }

    @Override
    public int getCount() {
        count=mClasslist.size();
        return count;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
           /* if(position == 0)
            {
                StudentListFragment studentListFragment = ((StudentListFragment) com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().getTeacherReviewFragmentInstance(mClasslist.get(position).getID()));
                studentListFragment.allowCallingFetchStudentDataService(true);
                studentListFragment.setTabDetails(0);
                studentListFragment.callRequestForUserDataService();
            }*/
    }

    @Override
    public void onPageSelected(int position)
    {
        if(mClasslist.size() > lastTabPosition)
        {
            //com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().clearUgcListHashMap();
            StudentListFragment lastFragment = ((StudentListFragment) com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().getTeacherReviewFragmentInstance(mClasslist.get(lastTabPosition).getID()));
            if(lastFragment!=null)
            lastFragment.allowCallingFetchStudentDataService(false);
        }

        if(mClasslist.size() > position)
        {
            StudentListFragment studentListFragment = ((StudentListFragment) com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().getTeacherReviewFragmentInstance(mClasslist.get(position).getID()));

            if(studentListFragment!=null){
                studentListFragment.refreshAdapter();
                studentListFragment.setTabDetails(0);
                studentListFragment.allowCallingFetchStudentDataService(true);
                studentListFragment.callRequestForUserDataService();
            }

        }

        lastTabPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void clickOnRed()
    {
        if(studentListFragment != null)
            studentListFragment.clickOnRed();
    }

    public void clickOnGreen()
    {
        if(studentListFragment != null)
            studentListFragment.clickOnGreen();
    }

    public void TeacherReviewPreviouse() {
        if(studentListFragment != null)
            studentListFragment.TeacherReviewPreviouse();
    }

    public void TeacherReviewNext() {
        if(studentListFragment != null)
            studentListFragment.TeacherReviewNext();
    }

    public void TeacherReviewEraser() {
        if(studentListFragment != null)
            studentListFragment.TeacherReviewEraser();
    }

    public void TeacherReviewDone(boolean isClearAllData) {
        if(studentListFragment != null)
            studentListFragment.TeacherReviewDone(isClearAllData);
    }

    public void TeacherReviewClearFibData(boolean isClearAllData,String folioId) {
        if(studentListFragment != null)
            studentListFragment.clearLinkData(isClearAllData,folioId);
    }

    public void TeacherReviewClearPenData(String folioId) {
        if(studentListFragment != null)
            studentListFragment.clearPenData(folioId);
    }

    public void changeCount() {
        if(studentListFragment != null)
            studentListFragment.changeCount();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
