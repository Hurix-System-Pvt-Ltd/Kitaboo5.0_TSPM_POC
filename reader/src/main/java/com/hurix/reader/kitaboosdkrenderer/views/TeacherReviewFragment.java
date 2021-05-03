package com.hurix.reader.kitaboosdkrenderer.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.hurix.bookreader.kitabooserviceapi.ServiceResponse;
import com.hurix.commons.Constants.Constants;
import com.hurix.commons.KitabooSDKModel;
import com.hurix.commons.datamodel.UserPageVO;
import com.hurix.commons.listener.OnDialogOkActionListner;

import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.commons.utils.DialogUtils;
import com.hurix.customui.interfaces.AssesmentControlListener;
import com.hurix.customui.interfaces.IClass;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;

import com.hurix.downloadbook.controller.UserController;
import com.hurix.reader.kitaboosdkrenderer.R;


import com.hurix.reader.kitaboosdkrenderer.analytics.AddOnBottomDialogDismissCall;

import com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.ReaderThemeController;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.reader.kitaboosdkrenderer.utils.Utils;
import com.hurix.renderer.utility.Utility;
import com.hurix.service.Interface.IServiceResponse;
import com.hurix.service.Interface.IServiceResponseListener;
import com.hurix.service.adapter.KitabooServiceAdapter;
import com.hurix.service.exception.ServiceException;
import com.hurix.service.networkcall.SERVICETYPES;
import com.hurix.service.response.FetchStudentDataServiceResponse;
import com.hurix.service.response.FetchbookClassesServieResponse;
import com.hurix.service.response.RefreshUserTokenResponse;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


@SuppressLint("ValidFragment")
public class TeacherReviewFragment extends BottomSheetDialogFragment implements AddOnBottomDialogDismissCall,View.OnClickListener,IServiceResponseListener, OnDialogOkActionListner {

    private final StudentListFragment.IStudentReviewActionsListner studentReviewActionsListner;
    TabLayout mParentTabLayout;
    ViewPager studentListViewPager;
    AssesmentControlListener mAssesmentControlListener;
    private ArrayList <IClass> mClassList;
    Context mContext;
    ThemeUserSettingVo mthemeUserSettingVo;
    private Dialog mProgressDialog;
    private AssesmentControlListener listner;
    private ArrayList<UserPageVO> mAnnotationpages;
    private int mCurrentPageNo = 0;
    int count=0;
    private StudentViewPagerAdapter studentViewPagerAdapter;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private TextView textSelectStudent;
    private TextView textCancel;




    /*@SuppressLint("ValidFragment")
    public TeacherReviewFragment(Context context, ThemeUserSettingVo themeUserSettingVo) {
        super(context);
        this.mthemeUserSettingVo = themeUserSettingVo;



    }*/
    @SuppressLint("ValidFragment")
    public TeacherReviewFragment(Context context, ThemeUserSettingVo themeUserSettingVo, AssesmentControlListener assesmentControlListener, StudentListFragment.IStudentReviewActionsListner studentReviewActionsListner){
        this.mContext=context;
        this.mthemeUserSettingVo=themeUserSettingVo;
        this.mAssesmentControlListener = assesmentControlListener;
        this.studentReviewActionsListner = studentReviewActionsListner;
        readerThemeSettingVo = ReaderThemeController.getInstance(context.getApplicationContext()).getReaderThemeUserSettingVo();
    }

    public void setlistner(AssesmentControlListener assesmentControlListener) {
        this.listner = assesmentControlListener;
        open();
        //this.initUI();
       // this.setUiProperties();
    }

   /* private void initUI() {
        this.mContext = this.getContext();
    }*/

    public boolean open() {
        //this.clearAnimation();
        if (this.listner != null) {
            this.listner.sendRequestForStudentList(this);
           // this.mProgressDialog.show();
        }
        return false;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || Utility.isDeviceTypeMobile(getContext())) {
            root = inflater.inflate(R.layout.teacher_review_swipeup_dialog_mobile, container);
        } else {
            root = inflater.inflate(R.layout.teacher_review_swipeup_dialog, container);
        }        /*mParentTabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        viewPager=(ViewPager) root.findViewById(R.id.viewPager);*/
        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SDKManager.getInstance().setNewTeacherReviewModeOn(true);
        mParentTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        studentListViewPager=(ViewPager) view.findViewById(R.id.viewPager);
        studentListViewPager.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getPopupBackground()));
        textSelectStudent = (TextView) view.findViewById(R.id.txtselectStudent);
        textSelectStudent.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getTitleColor()));
        textCancel = (TextView) view.findViewById(R.id.txtCancel);
        textCancel.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getHintTextColor()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // SDKManager.getInstance().setNewTeacherReviewModeOn(false);
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // SDKManager.getInstance().setNewTeacherReviewModeOn(true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void createTab() {
        for (int i=0;i<count;i++) {
            mParentTabLayout.addTab(mParentTabLayout.newTab().setText(mClassList.get(i).getName()));
            mParentTabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getTitleColor())));

        }

        mParentTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //mParentTabLayout.setupWithViewPager(studentListViewPager);

        mParentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                studentListViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        studentListViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mParentTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        studentViewPagerAdapter = new StudentViewPagerAdapter(getChildFragmentManager(),mClassList,mAssesmentControlListener,studentReviewActionsListner,studentListViewPager);
        studentListViewPager.setAdapter(studentViewPagerAdapter);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDismissBottomDialog() {

    }

    @Override
    public void requestCompleted(IServiceResponse response) {

        Activity activity = getActivity();
        if (activity != null) {
            if (response.getResponseRequestType().equals(SERVICETYPES.FETCHBOOKCLASSES)) {
                FetchbookClassesServieResponse responseObj = ((FetchbookClassesServieResponse) response);

                mClassList = responseObj.getClassesList();
                count = mClassList.size();
                createTab();
                com.hurix.commons.notifier.GlobalDataManager.getInstance().getLocalBookData().setClassList(mClassList);
                //update();
                // mProgressDialog.dismiss();
            } else if (response.getResponseRequestType().equals(SERVICETYPES.FETCHSTUDENTANNOTATIONS)) {
                FetchStudentDataServiceResponse responseObj = ((FetchStudentDataServiceResponse) response);
                makeAnnotationsPage(responseObj.getUserpageList());
                //mProgressDialog.dismiss();
                //GlobalDataManager.getInstance().setCurrMode(GlobalDataManager.PlayerState.PEN_ENABLE);
                //GlobalDataManager.getInstance().setisReviewMode(true);
                GlobalDataManager.getInstance().setPenColor(Constants.PENTOOL_ASSESSMENTS_COLOR_RED);
            }
        }

    }
    private void makeAnnotationsPage(ArrayList<UserPageVO> _mAnnotationpages) {
        if(listner!=null)
            mAnnotationpages=_mAnnotationpages;
        mCurrentPageNo = 0;
        if (mAnnotationpages.isEmpty()) {
           // setNextAndPreviousDisable();
            GlobalDataManager.getInstance().setAssessmentData(mAnnotationpages);
            GlobalDataManager.getInstance().broadcastRefresh();
            DialogUtils.displayToastAtCustomizePosition(getContext(), getResources().getString(R.string.validation_empty_data),
                    Toast.LENGTH_SHORT, Gravity.CENTER,0,200);
            //mAssessmentPenLayout.setVisibility(View.INVISIBLE);
        } else {

            GlobalDataManager.getInstance().setAssessmentData(mAnnotationpages);
           /* if (mStudentandclassholder.isShown()) {
                *//*AnimationClass a = new AnimationClass(mContext, mStudentandclassholder, 200,
                        AnimationClass.COLLAPSE);
                mStudentandclassholder.startAnimation(a);*//*
                mStudentandclassholder.setVisibility(INVISIBLE);
            }
            if(listner!=null)
                listner.loadReviewdataToPage(mAnnotationpages.get(mCurrentPageNo));

            mAssessmentPenLayout.setVisibility(View.VISIBLE);
            setEnable();*/
        }
        mProgressDialog.dismiss();

    }

    @Override
    public void requestErrorOccured(ServiceException exeption) {
        ServiceException exceptionObj = exeption;
        if (exceptionObj != null && !exceptionObj.getInvalidFields().isEmpty()) {
            Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
            if (UserController.getInstance(mContext).getUserSettings().getIsAutoLogOffEnabled() && entry.getValue() == 103) {
                showSessionExpiredAlert();
            }else if (entry.getValue() == 103) {
                showSessionExpiredAlertDialog();
            } else {
                if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                    KitabooServiceAdapter kitabooServiceAdapter = new KitabooServiceAdapter(mContext, mContext.getString(R.string.clientid));
                    kitabooServiceAdapter
                            .refreshUserToken(UserController.getInstance(mContext).getUserVO().getToken(), exeption.getResponseRequestType().toString(), new com.hurix.service.Interface.IServiceResponseListener() {
                                @Override
                                public void requestCompleted(IServiceResponse response) {

                                   RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) response;
                                    KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());

                                    /*String token = responseObj.getUserVO().getToken();
                                    long userId = responseObj.getUserVO().getUserID();

                                    UserVO userVO= new UserVO();

                                    userVO.setUserID(userId);
                                    userVO.setToken(token);*/
                                    // commented for removal of database dependency from reader
                                   // com.hurix.database.controller.DBController.getInstance(mContext).getManager().updateUserToken(userVO);

                                    open();
                                }

                                @Override
                                public void requestErrorOccured(ServiceException exeption) {

                                    // showSessionExpiredAlert();
                                }


//
                            });
                }
            }
        }
    }

    public void showSessionExpiredAlert() {
        int value=103;
        if(mContext!=null){
            DialogUtils.showOKAlert(new View(getActivity()), 1, getActivity(), getResources()
                            .getString(R.string.alert_error),
                 Utils.getMessageForError(mContext, value),
                    new OnDialogOkActionListner() {
                        @Override
                        public void onOKClick(View v) {
                            // commented for removal of database dependency from reader
                            /*DBController.getInstance(getActivity()).getManager()
                                    .logoutUserByID(UserController.getInstance(getActivity())
                                            .getUserVO().getUserID());*/
                          Utils.startLauncherActivity(getActivity());
                           GlobalDataManager.getInstance().setWebViewClosedAfterTokenReceived(false);
                        }
                    });
        }

    }


    private void showSessionExpiredAlertDialog() {
        int value=103;
        if(mContext!=null){
            DialogUtils.showOKAlert(new View(mContext), 1, mContext, mContext.getResources()
                            .getString(R.string.alert_title), Utils.getMessageForError(mContext, value),
                    new OnDialogOkActionListner() {
                        @Override
                        public void onOKClick(View v) {
                        }
                    });
        }
    }

    @Override
    public void onOKClick(View view) {

    }

    public void clickOnRed()
    {
        if(studentViewPagerAdapter != null)
        {
            studentViewPagerAdapter.clickOnRed();
        }
    }

    public void clickOnGreen()
    {
        if(studentViewPagerAdapter != null)
        {
            studentViewPagerAdapter.clickOnGreen();
        }
    }

    public void TeacherReviewPreviouse() {
        if(studentViewPagerAdapter != null)
        {
            studentViewPagerAdapter.TeacherReviewPreviouse();
        }
    }

    public void openTeacherReview() {
        StudentListFragment.mCurrentPageNo = 0;
    }

    public void TeacherReviewNext() {
        if(studentViewPagerAdapter != null)
        {
            studentViewPagerAdapter.TeacherReviewNext();
        }
    }

    public void TeacherReviewEraser() {
        if(studentViewPagerAdapter != null)
        {
            studentViewPagerAdapter.TeacherReviewEraser();
        }
    }

    public void TeacherReviewDone(boolean isClearAllData) {
        if(studentViewPagerAdapter != null)
        {
            studentViewPagerAdapter.TeacherReviewDone(isClearAllData);
        }
    }

    public void clearFIBdata(boolean isClearAllData,String folioId) {
        if(studentViewPagerAdapter != null)
        {
            studentViewPagerAdapter.TeacherReviewClearFibData(isClearAllData,folioId);
        }
    }

    public void clearPendata(String folioId) {
        if(studentViewPagerAdapter != null)
        {
            studentViewPagerAdapter.TeacherReviewClearPenData(folioId);
        }
    }

    public void changeCount() {
        if(studentViewPagerAdapter != null)
        {
            studentViewPagerAdapter.changeCount();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        SDKManager.getInstance().setNewTeacherReviewModeOn(false);
        GlobalDataManager.getInstance().setisReviewMode(false);
        GlobalDataManager.getInstance().setCurrMode(GlobalDataManager.PlayerState.NAVIGATION);
        dialog.dismiss();
        super.onCancel(dialog);
    }
}
