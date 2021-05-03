package com.hurix.reader.kitaboosdkrenderer.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hurix.bookreader.views.link.LinkEditFIBView;
import com.hurix.commons.Constants.Constants;
import com.hurix.commons.KitabooSDKModel;
import com.hurix.commons.datamodel.LinkVO;
import com.hurix.commons.datamodel.UserPageVO;
import com.hurix.commons.listener.OnDialogOkActionListner;
import com.hurix.commons.notifier.GlobalDataManager;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.commons.utils.DialogUtils;
import com.hurix.customui.datamodel.PentoolVO;
import com.hurix.customui.interfaces.AssesmentControlListener;
import com.hurix.customui.interfaces.IClass;
import com.hurix.customui.interfaces.IUser;
import com.hurix.customui.thumbnails.ThumbnailVO;
import com.hurix.downloadbook.controller.UserController;
import com.hurix.epubreader.Utility.Utils;
import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.service.Interface.IServiceResponse;
import com.hurix.service.Interface.IServiceResponseListener;
import com.hurix.service.adapter.KitabooServiceAdapter;
import com.hurix.service.exception.ServiceException;
import com.hurix.service.networkcall.SERVICETYPES;
import com.hurix.service.response.FetchStudentDataServiceResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressLint("ValidFragment")
public class StudentListFragment extends Fragment implements IServiceResponseListener, StudentListAdapter.ClickListener, AdapterView.OnItemClickListener {

    private int count = 0;
    ArrayList<IUser> mStudentList;
    private IClass classObj;
    Context mContext;
    private RecyclerView rvStudentListView;
    StudentListAdapter studentListAdapter;
    private AssesmentControlListener mAssesmentControlListener;
    private static ArrayList<UserPageVO> mAnnotationpages;
    public static int mCurrentPageNo = 0;
    private LinearLayout rvMainContainer;
    public static IUser selectedStudentInfo;
    private IStudentReviewActionsListner mStudentReviewActionsListner;
    private boolean mAllowFetchStudentUGCDataService = false;

    public StudentListFragment(IClass classObj, ArrayList<IUser> studentList) {
        this.classObj = classObj;
        mStudentList = studentList;
    }

    public void allowCallingFetchStudentDataService(boolean allowFetchStudentUGCDataService) {
        mAllowFetchStudentUGCDataService = allowFetchStudentUGCDataService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_listview, container, false);
        rvStudentListView = (RecyclerView) view.findViewById(R.id.rv_student_list);
        rvMainContainer = (LinearLayout) view.findViewById(R.id.rv_main_container);

        studentListAdapter = new StudentListAdapter(getContext(), classObj, mStudentList, mAssesmentControlListener, this);
        rvStudentListView.setAdapter(studentListAdapter);

        rvStudentListView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //SDKManager.getInstance().setNewTeacherReviewModeOn(true);
        GlobalDataManager.getInstance().setisReviewMode(true);
        super.onViewCreated(view, savedInstanceState);
    }

    public void setlistner(AssesmentControlListener assesmentControlListener, IStudentReviewActionsListner studentReviewActionsListner) {
        mAssesmentControlListener = assesmentControlListener;
        mStudentReviewActionsListner = studentReviewActionsListner;
    }

    private StudentListFragment getCuttrentFragment() {
        StudentListFragment refreshCurrentFragment =
                ((StudentListFragment) com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager.getInstance().getTeacherReviewFragmentInstance(classObj.getID()));
        return refreshCurrentFragment;
    }

    public void refreshAdapter() {
        if (studentListAdapter != null) {
            studentListAdapter.notifyDataSetChanged();
        }
    }

    public void setTabDetails(int count) {
        this.count = count;
    }

    public void callRequestForUserDataService() {
        if (mAssesmentControlListener != null && mStudentList.size() > count) {
            mAssesmentControlListener.sendRequestForUserData(mStudentList.get(count).getUserID(), this);
        }
    }

    @Override
    public void requestCompleted(IServiceResponse iServiceResponse) {
        if (iServiceResponse.getResponseRequestType().equals(SERVICETYPES.FETCHSTUDENTANNOTATIONS)) {
            FetchStudentDataServiceResponse responseObj = ((FetchStudentDataServiceResponse) iServiceResponse);
            if (mStudentList.size() > count) {
                com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager.getInstance().setUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(count).getUserID(), responseObj.getUserpageList());
                studentListAdapter.notifyDataSetChanged();
                if (mAllowFetchStudentUGCDataService) {
                    count++;
                    callRequestForUserDataService();
                }
            }

            /*makeAnnotationsPage(responseObj.getUserpageList());
            //mProgressDialog.dismiss();
            GlobalDataManager.getInstance().setCurrMode(GlobalDataManager.PlayerState.PEN_ENABLE);
            //GlobalDataManager.getInstance().setisReviewMode(true);
            GlobalDataManager.getInstance().setPenColor(com.hurix.commons.Constants.Constants.PENTOOL_ASSESSMENTS_COLOR_RED);*/
        }
    }

    private void showSessionExpiredAlert() {
        com.hurix.commons.utils.DialogUtils.showOKAlert(new View(mContext), 1, mContext, mContext.getResources()
                        .getString(R.string.alert_title), getResources()
                        .getString(R.string.alert_session_expired),
                new com.hurix.commons.listener.OnDialogOkActionListner() {
                    @Override
                    public void onOKClick(View v) {
                        // commented for removal of database dependency from reader
                      /* DBController.getInstance(mContext).getManager().logoutUserByID(UserController
                                .getInstance(mContext).getUserVO().getUserID());*/
                        Utils.startLauncherActivity(mContext);
                        com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager.getInstance().setWebViewClosedAfterTokenReceived(false);
                    }
                });
    }

    @Override
    public void requestErrorOccured(ServiceException exeption) {
        ServiceException exceptionObj = exeption;
        Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
        if (exeption != null) {
            //mProgressDialog.dismiss();

            if (exeption.getResponseRequestType().equals(SERVICETYPES.FETCHSTUDENTANNOTATIONS)) {


                ArrayList<UserPageVO> listUserPageVo = new ArrayList<>();
                UserPageVO dummyUserPage = new UserPageVO();
                dummyUserPage.setPageID(-1);
                listUserPageVo.add(dummyUserPage);
                com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager.getInstance().setUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(count).getUserID(), listUserPageVo);
                //studentListAdapter.showRefreshButton(count);
                studentListAdapter.notifyDataSetChanged();
                if (mAllowFetchStudentUGCDataService) {
                    count++;
                    callRequestForUserDataService();
                    //studentListAdapter.setServiceCalled(true);
                }


                if (!exceptionObj.getInvalidFields().isEmpty()) {
                    if (UserController.getInstance(mContext).getUserSettings().getIsAutoLogOffEnabled() && entry.getValue() == 103) {
                        showSessionExpiredAlert();
                    } else {
                        if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                            KitabooServiceAdapter kitabooServiceAdapter = new KitabooServiceAdapter(mContext, mContext.getString(R.string.clientid));

                            kitabooServiceAdapter
                                    .refreshUserToken(UserController.getInstance(mContext).getUserVO().getToken(), exeption.getResponseRequestType().toString(), new com.hurix.service.Interface.IServiceResponseListener() {
                                        @Override
                                        public void requestCompleted(IServiceResponse response) {

                                            com.hurix.service.response.RefreshUserTokenResponse responseObj = (com.hurix.service.response.RefreshUserTokenResponse) response;
                                            KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());

                                           /* String token = responseObj.getUserVO().getToken();
                                            long userId = responseObj.getUserVO().getUserID();

                                            UserVO userVO = new UserVO();

                                            userVO.setUserID(userId);
                                            userVO.setToken(token);*/
                                            // commented for removal of database dependency from reader
                                            //DBController.getInstance(mContext).getManager().updateUserToken(userVO);

                                        }

                                        @Override
                                        public void requestErrorOccured(ServiceException exeption) {

                                            // showSessionExpiredAlert();
                                        }


//
                                    });
                        }
                    }
                } else {
                    if (mContext != null) {
                        DialogUtils.showOKAlert(new View(mContext), 0, mContext, "Error", exeption.getMessage().toString(), new OnDialogOkActionListner() {
                            @Override
                            public void onOKClick(View view) {

                            }
                        });
                    }
                }
            }


        } else {
            ArrayList<UserPageVO> listUserPageVo = new ArrayList<>();
            UserPageVO dummyUserPage = new UserPageVO();
            dummyUserPage.setPageID(-1);
            listUserPageVo.add(dummyUserPage);
            com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager.getInstance().setUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(count).getUserID(), listUserPageVo);
            //studentListAdapter.showRefreshButton(count);
            studentListAdapter.notifyDataSetChanged();
            if (mAllowFetchStudentUGCDataService) {
                count++;
                callRequestForUserDataService();
            }
            //studentListAdapter.setServiceCalled(true);

        }
    }

    private void makeAnnotationsPage(ArrayList<UserPageVO> _mAnnotationpages) {
        mAnnotationpages = _mAnnotationpages;


        GlobalDataManager.getInstance().setAssessmentData(mAnnotationpages);
        if (mAnnotationpages != null && mAnnotationpages.size() > 0) {
            //setNextAndPreviousDisable();
            int count = 0;
            for (int i = 0; i < mAnnotationpages.size(); i++) {
                if ((((mAnnotationpages.get(i).getLinkCollection().size() > 0) || (mAnnotationpages.get(i).getPenColl().size() > 0)))) {
                    count = count + 1;
                }
                if ((i == mAnnotationpages.size() - 1) && count == 0) {
                    //GlobalDataManager.getInstance().broadcastRefresh();
                    DialogUtils.displayToastAtCustomizePosition(getContext(), getResources().getString(com.hurix.epubreader.R.string.validation_empty_data),
                            Toast.LENGTH_SHORT, Gravity.CENTER, 0, 200);
                } else if ((i == mAnnotationpages.size() - 1)) {
                    if (mAssesmentControlListener != null && mAnnotationpages != null && mAnnotationpages.size() > 0 && mAnnotationpages.size() > mCurrentPageNo) {
                        HashMap<String, String> userFolioId = new HashMap<>();
                        for (UserPageVO userPageVO : mAnnotationpages) {
                            userFolioId.put(userPageVO.getFolioID(), userPageVO.getFolioID());
                        }
                        GlobalDataManager.getInstance().setUserFolioId(userFolioId);
                        GlobalDataManager.getInstance().setCurrMode(GlobalDataManager.PlayerState.PEN_ENABLE);
                        GlobalDataManager.getInstance().setisReviewMode(true);
                        GlobalDataManager.getInstance().setPenColor(com.hurix.commons.Constants.Constants.PENTOOL_ASSESSMENTS_COLOR_RED);

                        mAssesmentControlListener.loadReviewdataToPage(mAnnotationpages.get(mCurrentPageNo));
                        GlobalDataManager.getInstance().getLocalBookData().setCurrentTeacherAssesment(mCurrentPageNo);
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        //mAssesmentControlListener.sendRequestForUserData(mStudentList.get(position).getUserID(),this);

        if (mStudentList.size() > position && position >= 0) {
            ArrayList<UserPageVO> ugcDataArrayList = com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager.getInstance().
                    getUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(position).getUserID());
            if (ugcDataArrayList != null && ugcDataArrayList.size() > 0 && mStudentList.size() > position) {
                selectedStudentInfo = mStudentList.get(position);
                //For Ascending order page sorting
                HashMap<String, UserPageVO> userHashmap = new HashMap();
                ArrayList<UserPageVO> ugcSortedDataArrayList = new ArrayList<>();
                for (com.hurix.commons.datamodel.UserPageVO userPageVO : ugcDataArrayList) {
                    userHashmap.put(userPageVO.getFolioID(), userPageVO);
                }
                SDKManager.getInstance().setUserDataMap(userHashmap);
                for (ThumbnailVO pageList : SDKManager.getInstance().getmThumbnailColl()) {
                    if (userHashmap.get(pageList.getFolioID()) != null) {
                        ugcSortedDataArrayList.add(userHashmap.get(pageList.getFolioID()));
                    }
                }
                ugcDataArrayList = ugcSortedDataArrayList;

                if (ugcDataArrayList.get(0).getPageID() != -1) {
                    makeAnnotationsPage(ugcDataArrayList);
                    /*GlobalDataManager.getInstance().setCurrMode(GlobalDataManager.PlayerState.PEN_ENABLE);
                    GlobalDataManager.getInstance().setisReviewMode(true);
                    GlobalDataManager.getInstance().setPenColor(com.hurix.commons.Constants.Constants.PENTOOL_ASSESSMENTS_COLOR_RED);*/
                }
            }
        }
    }

    @Override
    public void onRefreshClick(final int position) {
        if (mAssesmentControlListener != null && mStudentList.size() > position) {
            mAssesmentControlListener.sendRequestForUserData(mStudentList.get(position).getUserID(), new IServiceResponseListener() {
                @Override
                public void requestCompleted(IServiceResponse iServiceResponse) {
                    FetchStudentDataServiceResponse responseObj = ((FetchStudentDataServiceResponse) iServiceResponse);
                    com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager.getInstance().
                            setUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(position).getUserID(), responseObj.getUserpageList());
                    studentListAdapter.notifyDataSetChanged();
                }

                @Override
                public void requestErrorOccured(ServiceException e) {
                    ArrayList<UserPageVO> listUserPageVo = new ArrayList<>();
                    UserPageVO dummyUserPage = new UserPageVO();
                    dummyUserPage.setPageID(-1);
                    listUserPageVo.add(dummyUserPage);
                    com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager.getInstance().setUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(position).getUserID(), listUserPageVo);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void clickOnRed() {
        //GlobalDataManager.getInstance().setPenDeleteMode(false);
        //GlobalDataManager.getInstance().setPenColor(com.hurix.commons.Constants.Constants.PENTOOL_ASSESSMENTS_COLOR_RED);
    }

    public void clickOnGreen() {
        //GlobalDataManager.getInstance().setPenDeleteMode(false);
        //GlobalDataManager.getInstance().setPenColor(com.hurix.commons.Constants.Constants.PENTOOL_ASSESSMENTS_COLOR_GREEN);
    }

    public void TeacherReviewPreviouse() {
        GlobalDataManager.getInstance().setPenDeleteMode(false);
        GlobalDataManager.getInstance().setNextCountRequired(false);
        SDKManager.getInstance().setPrevClicked(true);
        if (mAnnotationpages != null && !mAnnotationpages.isEmpty()) {
            if (mAnnotationpages != null && mAnnotationpages.size() != 0) {
                if (mCurrentPageNo > 0) {
                    mCurrentPageNo = mCurrentPageNo - 1;
                    if (mAssesmentControlListener != null) {
                        GlobalDataManager.getInstance().getLocalBookData().setCurrentTeacherAssesment(mCurrentPageNo);
                        mAssesmentControlListener.loadReviewdataToPage(mAnnotationpages.get(mCurrentPageNo));
                    }
                    GlobalDataManager.getInstance().broadcastRefresh();

                    if (mCurrentPageNo == 0 && mStudentReviewActionsListner != null)
                        mStudentReviewActionsListner.setNextAndPreviousDisable(true, false);
                }

            }
        }


    }

    public void TeacherReviewNext() {
        GlobalDataManager.getInstance().setPenDeleteMode(false);
        GlobalDataManager.getInstance().setNextCountRequired(true);

        if (mAnnotationpages != null && !mAnnotationpages.isEmpty()) {
            if (mCurrentPageNo < mAnnotationpages.size() - 1) {
                mCurrentPageNo = mCurrentPageNo + 1;
                if (mAssesmentControlListener != null) {
                    GlobalDataManager.getInstance().getLocalBookData().setCurrentTeacherAssesment(mCurrentPageNo);
                    mAssesmentControlListener.loadReviewdataToPage(mAnnotationpages.get(mCurrentPageNo));
                }
                GlobalDataManager.getInstance().broadcastRefresh();

                if (mCurrentPageNo == mAnnotationpages.size() - 1 && mStudentReviewActionsListner != null)
                    mStudentReviewActionsListner.setNextAndPreviousDisable(false, true);
            }

        }
    }

    public void TeacherReviewEraser() {
        GlobalDataManager.getInstance().setPenDeleteMode(true);
    }

    public void TeacherReviewDone(boolean isClearAllData) {
        if (LinkEditFIBView.focusedEditText != null) {
            LinkEditFIBView.focusedEditText.clearFocus();
            LinkEditFIBView.focusedEditText.setText("");
        }
        GlobalDataManager.getInstance().setPenDeleteMode(false);
        sendTeacherData(isClearAllData);
        GlobalDataManager.getInstance().setCurrMode(GlobalDataManager.PlayerState.NAVIGATION);
        GlobalDataManager.getInstance().setPenColor(com.hurix.commons.Constants.Constants.PENTOOL_COLOR_DEFAULT);
        GlobalDataManager.getInstance().refreshHighLightOnPage();

        if (mAssesmentControlListener != null)
            mAssesmentControlListener.closeTeacherAssesmentbar(mAnnotationpages);
    }

    private void sendTeacherData(boolean isClearAllData) {

        if ((((getPenData() != null && getPenData().size() > 0)) || (getLinkData() != null && getLinkData().size() > 0)) && mAssesmentControlListener != null && selectedStudentInfo != null) {
            mAssesmentControlListener.sendTeacherDataForSyncing(selectedStudentInfo.getUserID(), getPenData(), getLinkData(), this);
        } else {
            if (mAssesmentControlListener != null)
                mAssesmentControlListener.showNoDataSubmittedPopup();
        }

    }

    protected void clearLinkData(boolean isClearAllData, String folioId) {

        if (mAssesmentControlListener != null && selectedStudentInfo != null) {
            getLinkData(isClearAllData, folioId);
        } else {
            if (mAssesmentControlListener != null)
                mAssesmentControlListener.showNoDataSubmittedPopup();
        }

    }

    protected void clearPenData(String folioId) {

        if (mAssesmentControlListener != null && selectedStudentInfo != null) {
            //getPenData(folioId);
            SDKManager.getInstance().setAllPenMarkerVO(folioId, getPenData(folioId));
        } else {
            if (mAssesmentControlListener != null)
                mAssesmentControlListener.showNoDataSubmittedPopup();
        }

    }

    protected void changeCount() {
        if (GlobalDataManager.getInstance().isNextCountRequired()) {
            if (mCurrentPageNo < mAnnotationpages.size() - 1) {
                mCurrentPageNo = mCurrentPageNo + 1;
                if (mCurrentPageNo == mAnnotationpages.size() - 1 && mStudentReviewActionsListner != null)
                    mStudentReviewActionsListner.setNextAndPreviousDisable(false, true);
            }
        } else {
            if (mCurrentPageNo < mAnnotationpages.size() - 1) {
                mCurrentPageNo = mCurrentPageNo - 1;
                if (mCurrentPageNo == 0 && mStudentReviewActionsListner != null)
                    mStudentReviewActionsListner.setNextAndPreviousDisable(true, false);
            }
        }
        GlobalDataManager.getInstance().getLocalBookData().setCurrentTeacherAssesment(mCurrentPageNo);
    }

    public ArrayList<LinkVO> getLinkData() {
        ArrayList<LinkVO> listoflinkdata = new ArrayList<LinkVO>();
        for (int i = 0; i < GlobalDataManager.getInstance().getAssessmentPenMarks().size(); i++) {
            ArrayList<LinkVO> linkColl = GlobalDataManager.getInstance().getAssessmentPenMarks()
                    .get(i).getLinkCollection();

            for (int k = 0; k < linkColl.size(); k++) {
               /* if (linkColl.get(k).getType() == LinkVO.LinkType.ACTIVITY_INJECTION)
                    if (!linkColl.get(k).getSyncStatus()) {
                        listoflinkdata.add(linkColl.get(k));
                    }*/
                if (linkColl.get(k).isSubmitReviewedData()) {
                    linkColl.get(k).setSubmitted(false);
                    linkColl.get(k).setSyncStatus(false);
                    listoflinkdata.add(linkColl.get(k));
                }
            }
        }
        /**
         * Remove duplicate elements
         */
        HashSet<LinkVO> set = new HashSet<>(listoflinkdata);
        listoflinkdata.clear();
        listoflinkdata.addAll(set);
        return listoflinkdata;
    }

    public ArrayList<LinkVO> getLinkData(boolean isClearAllData, String folioId) {
        ArrayList<LinkVO> listoflinkdata = new ArrayList<LinkVO>();
        for (int i = 0; i < GlobalDataManager.getInstance().getAssessmentPenMarks().size(); i++) {
            ArrayList<LinkVO> linkColl = GlobalDataManager.getInstance().getAssessmentPenMarks()
                    .get(i).getLinkCollection();
            if (folioId.equals(GlobalDataManager.getInstance().getAssessmentPenMarks().get(i).getFolioID())) {
                for (int k = 0; k < linkColl.size(); k++) {
                    if (linkColl.get(k).getType() == LinkVO.LinkType.ACTIVITY_INJECTION)

                       /* if (linkColl.get(k).getActivityInjectionType().equalsIgnoreCase("dropdown")) {
                            if(isClearAllData){
                                linkColl.get(k).setUserAnswer("select");
                            }
                            if (linkColl.get(k).getUserAnswer().equalsIgnoreCase("select")) {
                                linkColl.get(k).setMode(com.hurix.commons.Constants.Constants.UGC_ITEM_MODE_DELETED);
                            }
                        }else{
                            if(isClearAllData){
                                linkColl.get(k).setUserAnswer("");
                            }
                        }*/
                        linkColl.get(k).setSyncStatus(false);
                    linkColl.get(k).setUserAnswer("");
                    linkColl.get(k).setMode(com.hurix.commons.Constants.Constants.UGC_ITEM_MODE_DELETED);
                    listoflinkdata.add(linkColl.get(k));
                }
            }

        }
        return listoflinkdata;
    }

    public ArrayList<PentoolVO> getPenData(String folioId) {
        ArrayList<PentoolVO> listofpenmarks = new ArrayList<PentoolVO>();
        for (int i = 0; i < GlobalDataManager.getInstance().getAssessmentPenMarks().size(); i++) {
            ArrayList<PentoolVO> pentoolColl = GlobalDataManager.getInstance().getAssessmentPenMarks()
                    .get(i).getPenColl();
            if (folioId.equals(GlobalDataManager.getInstance().getAssessmentPenMarks().get(i).getFolioID())) {
                for (int k = 0; k < pentoolColl.size(); k++) {

                    if (pentoolColl.get(k).getPointarray().size() > 0) {

                        if (pentoolColl.get(k).getUGCID() > 0) {
                            pentoolColl.get(k).setMode(Constants.UGC_ITEM_MODE_DELETED);
                            pentoolColl.get(k).setSyncStatus(false);
                            pentoolColl.get(k).setIsSubmitted(false);
                            pentoolColl.get(k).getpentoolData();
                            pentoolColl.get(k).setDateTime(Utils.getDateTime());
                        } else {
                            listofpenmarks.remove(pentoolColl.get(k));
                        }
                        listofpenmarks.add(pentoolColl.get(k));
                    }

                }
            }
        }
        return listofpenmarks;
    }

    public ArrayList<PentoolVO> getPenData() {
        ArrayList<PentoolVO> listofpenmarks = new ArrayList<PentoolVO>();
        for (int i = 0; i < GlobalDataManager.getInstance().getAssessmentPenMarks().size(); i++) {
            ArrayList<PentoolVO> pentoolColl = GlobalDataManager.getInstance().getAssessmentPenMarks()
                    .get(i).getPenColl();

            for (int k = 0; k < pentoolColl.size(); k++) {
                if (pentoolColl.get(k).isSubmitted()) {
                    if (pentoolColl.get(k).getPointarray().size() > 0) {
                        listofpenmarks.add(pentoolColl.get(k));
                    }
                }
            }
        }
        return listofpenmarks;
    }

    public interface IStudentReviewActionsListner {
        void setNextAndPreviousDisable(boolean isPreviewDisable, boolean isNextDisable);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (SDKManager.getInstance().isNewTeacherReviewModeOn() || GlobalDataManager.getInstance().isReviewMode()) {
            SDKManager.getInstance().setNewTeacherReviewModeOn(true);
            GlobalDataManager.getInstance().setisReviewMode(true);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        // SDKManager.getInstance().setNewTeacherReviewModeOn(false);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
