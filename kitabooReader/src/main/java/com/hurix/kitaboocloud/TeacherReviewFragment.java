package com.hurix.kitaboocloud;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hurix.commons.Constants.Constants;
import com.hurix.commons.KitabooSDKModel;
import com.hurix.commons.datamodel.UserPageVO;
import com.hurix.commons.listener.OnDialogOkActionListner;
import com.hurix.commons.notifier.GlobalDataManager;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.commons.utils.DialogUtils;
import com.hurix.customui.interfaces.AssesmentControlListener;
import com.hurix.customui.interfaces.IClass;
import com.hurix.customui.interfaces.IUser;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;



import com.hurix.kitaboocloud.interfaces.AddOnBottomDialogDismissCall;
import com.hurix.kitaboocloud.sdkreadertheme.InsightReaderThemeController;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.kitaboocloud.views.TeacherReviewGenerateReport;
import com.hurix.kitaboocloud.views.TeacherReviewGenerateReportMobile;
import com.hurix.renderer.utility.Utility;
import com.hurix.service.Interface.IServiceResponse;
import com.hurix.service.Interface.IServiceResponseListener;
import com.hurix.service.exception.ServiceException;
import com.hurix.service.networkcall.SERVICETYPES;
import com.hurix.service.response.FetchStudentDataServiceResponse;
import com.hurix.service.response.FetchbookClassesServieResponse;
/*import com.hurix.common.services.adapter.KitabooServiceAPI;
import com.hurix.common.services.kitaboo.response.RefreshUserTokenResponse;*/

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;


@SuppressLint("ValidFragment")
public class TeacherReviewFragment extends BottomSheetDialogFragment implements AddOnBottomDialogDismissCall, View.OnClickListener, IServiceResponseListener, OnDialogOkActionListner {

    private final StudentListFragment.IStudentReviewActionsListner studentReviewActionsListner;
    TabLayout mParentTabLayout;
    ViewPager studentListViewPager;
    RelativeLayout mMainparent;
    AssesmentControlListener mAssesmentControlListener;
    private ArrayList<IClass> mClassList;
    Context mContext;
    ThemeUserSettingVo mthemeUserSettingVo;
    private Dialog mProgressDialog;
    private AssesmentControlListener listner;
    private ArrayList<UserPageVO> mAnnotationpages;
    private int mCurrentPageNo = 0;
    int count = 0;
    private StudentViewPagerAdapter studentViewPagerAdapter;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private TextView textSelectStudent;
    private TextView textCancel;
    private TextView mSelectStudentText, mGenerateReportText;


    /*@SuppressLint("ValidFragment")
    public TeacherReviewFragment(Context context, ThemeUserSettingVo themeUserSettingVo) {
        super(context);
        this.mthemeUserSettingVo = themeUserSettingVo;



    }*/
    @SuppressLint("ValidFragment")
    public TeacherReviewFragment(Context context, ThemeUserSettingVo themeUserSettingVo, AssesmentControlListener assesmentControlListener, StudentListFragment.IStudentReviewActionsListner studentReviewActionsListner) {
        this.mContext = context;
        this.mthemeUserSettingVo = themeUserSettingVo;
        this.mAssesmentControlListener = assesmentControlListener;
        this.studentReviewActionsListner = studentReviewActionsListner;
        readerThemeSettingVo = InsightReaderThemeController.getInstance(context.getApplicationContext()).getReaderThemeUserSettingVo();
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
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || Utility.isDeviceTypeMobile(getContext())) {
            root = inflater.inflate(R.layout.teacher_review_swipeup_new_dialog_mobile, container);
        } else {
            root = inflater.inflate(R.layout.new_teacher_review_swipeup_dialog, container);
        }        /*mParentTabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        viewPager=(ViewPager) root.findViewById(R.id.viewPager);*/
        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SDKManager.getInstance().setNewTeacherReviewModeOn(true);
        mMainparent = (RelativeLayout) view.findViewById(R.id.Mainparent);
        mParentTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        studentListViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        studentListViewPager.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getPopupBackground()));
        mMainparent.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getPopupBackground()));
        textSelectStudent = (TextView) view.findViewById(R.id.txtselectStudent);
        textSelectStudent.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getTitleColor()));
        textCancel = (TextView) view.findViewById(R.id.txtCancel);
        if (textCancel != null) {
            textCancel.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getHintTextColor()));
        }

        mSelectStudentText = (TextView) view.findViewById(R.id.select_student_txt);
        if (mSelectStudentText != null)
            mSelectStudentText.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getTitleColor()));

        mGenerateReportText = (TextView) view.findViewById(R.id.generate_report_txt);
        if(!getResources().getBoolean(R.bool.show_generateReport)){
            mGenerateReportText.setVisibility(View.GONE);
        }else{
            mGenerateReportText.setVisibility(View.VISIBLE);
        }
        if (mGenerateReportText != null) {
            mGenerateReportText.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getTitleColor()));
            mGenerateReportText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (!Utility.isDeviceTypeMobile(mContext)) {
                        intent = new Intent(mContext, TeacherReviewGenerateReport.class);
                    } else {
                        intent = new Intent(mContext, TeacherReviewGenerateReportMobile.class);
                    }
                    startActivity(intent);
                }
            });
        }
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
        for (int i = 0; i < count; i++) {
            mParentTabLayout.addTab(mParentTabLayout.newTab().setText(mClassList.get(i).getName()));
            mParentTabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#000000")));
        }

        mParentTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //mParentTabLayout.setupWithViewPager(studentListViewPager);

        mParentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               // mParentTabLayout.getTabAt(tab.getPosition()).setTabTextColors(ColorStateList.valueOf(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getTitleColor())));
                studentListViewPager.setCurrentItem(tab.getPosition());
                if (SDKManager.getInstance().getSelectedClassStudentList() != null)
                    SDKManager.getInstance().getSelectedClassStudentList().clear();
                SDKManager.getInstance().setSelectedClassStudentList(getUsers(mClassList.get(tab.getPosition()).getStudentList()));
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

        if (SDKManager.getInstance().getSelectedClassStudentList() != null)
            SDKManager.getInstance().getSelectedClassStudentList().clear();
        if(!mClassList.isEmpty())
        SDKManager.getInstance().setSelectedClassStudentList(getUsers(mClassList.get(0).getStudentList()));
        studentViewPagerAdapter = new StudentViewPagerAdapter(getChildFragmentManager(), mClassList, mAssesmentControlListener, studentReviewActionsListner, studentListViewPager);
        studentListViewPager.setAdapter(studentViewPagerAdapter);
    }

    private ArrayList<IUser> getUsers(ArrayList<IUser> listofalluserd) {
        ArrayList<IUser> listofusertmp = new ArrayList<IUser>();
        for (int i = 0; i < listofalluserd.size(); i++) {
            if (!listofalluserd.get(i).getRoleName().equalsIgnoreCase(com.hurix.commons.Constants.Constants.TEACHER)) {
                listofusertmp.add(listofalluserd.get(i));
            }
        }
        return listofusertmp;
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
                GlobalDataManager.getInstance().getLocalBookData().setClassList(mClassList);
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
        if (listner != null)
            mAnnotationpages = _mAnnotationpages;
        mCurrentPageNo = 0;
        if (mAnnotationpages.isEmpty()) {
            // setNextAndPreviousDisable();
            GlobalDataManager.getInstance().setAssessmentData(mAnnotationpages);
            GlobalDataManager.getInstance().broadcastRefresh();
            DialogUtils.displayToastAtCustomizePosition(getContext(), getResources().getString(R.string.validation_empty_data),
                    Toast.LENGTH_SHORT, Gravity.CENTER, 0, 200);
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
            if (PlayerController.getInstance(mContext).isAutoLogOffEnabled() && entry.getValue() == 103) {
                showSessionExpiredAlert();
            } else if (entry.getValue() == 103) {
                showSessionExpiredAlertDialog();
            } else {
                /*if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                    KitabooServiceAPI.getObject().getServiceAdapter()
                            .refreshUserToken(UserController.getInstance(mContext).getUserVO().getToken(), exeption.getResponseRequestType().toString(), new com.hurix.common.services.listener.IServiceResponseListener() {
                                @Override
                                public void requestCompleted(com.hurix.common.services.interfaces.IServiceResponse response) throws JSONException {
                                    RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) response;
                                    KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                    DBController.getInstance(mContext).getManager().updateUserToken(responseObj.getUserVO());
                                    open();
                                }

                                @Override
                                public void requestErrorOccured(com.hurix.common.services.exception.ServiceException exeption) {
                                    // showSessionExpiredAlert();
                                }
                            });
                }*/
            }
        }
    }

    public void showSessionExpiredAlert() {
        int value = 103;
        if (mContext != null) {
            com.hurix.kitaboo.constants.dialog.DialogUtils.showOKAlert(new View(getActivity()), 1, getActivity(), getResources()
                            .getString(R.string.alert_error),
                    com.hurix.kitaboo.constants.utils.Utils.getMessageForError(mContext, value),
                    new com.hurix.kitaboo.constants.listener.OnDialogOkActionListner() {
                        @Override
                        public void onOKClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("Action","LOGOUT_BY_USERID");
                            intent.setAction("DBCONTROLLER_RECIVER");
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                            com.hurix.kitaboo.constants.utils.Utils.startLauncherActivity(getActivity());
                            com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setWebViewClosedAfterTokenReceived(false);
                        }
                    });
        }

    }


    private void showSessionExpiredAlertDialog() {
        int value = 103;
        if (mContext != null) {
            com.hurix.commons.utils.DialogUtils.showOKAlert(new View(mContext), 1, mContext, mContext.getResources()
                            .getString(R.string.alert_title), com.hurix.kitaboo.constants.utils.Utils.getMessageForError(mContext, value),
                    new com.hurix.commons.listener.OnDialogOkActionListner() {
                        @Override
                        public void onOKClick(View v) {
                        }
                    });
        }
    }

    @Override
    public void onOKClick(View view) {

    }

    public void clickOnRed() {
        if (studentViewPagerAdapter != null) {
            studentViewPagerAdapter.clickOnRed();
        }
    }

    public void clickOnGreen() {
        if (studentViewPagerAdapter != null) {
            studentViewPagerAdapter.clickOnGreen();
        }
    }

    public void TeacherReviewPreviouse() {
        if (studentViewPagerAdapter != null) {
            studentViewPagerAdapter.TeacherReviewPreviouse();
        }
    }

    public void openTeacherReview() {
        StudentListFragment.mCurrentPageNo = 0;
    }

    public void TeacherReviewNext() {
        if (studentViewPagerAdapter != null) {
            studentViewPagerAdapter.TeacherReviewNext();
        }
    }

    public void TeacherReviewEraser() {
        if (studentViewPagerAdapter != null) {
            studentViewPagerAdapter.TeacherReviewEraser();
        }
    }

    public void TeacherReviewDone(boolean isClearAllData) {
        if (studentViewPagerAdapter != null) {
            studentViewPagerAdapter.TeacherReviewDone(isClearAllData);
        }
    }

    public void clearFIBdata(boolean isClearAllData, String folioId) {
        if (studentViewPagerAdapter != null) {
            studentViewPagerAdapter.TeacherReviewClearFibData(isClearAllData, folioId);
        }
    }

    public void clearPendata(String folioId) {
        if (studentViewPagerAdapter != null) {
            studentViewPagerAdapter.TeacherReviewClearPenData(folioId);
        }
    }

    public void changeCount() {
        if (studentViewPagerAdapter != null) {
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
