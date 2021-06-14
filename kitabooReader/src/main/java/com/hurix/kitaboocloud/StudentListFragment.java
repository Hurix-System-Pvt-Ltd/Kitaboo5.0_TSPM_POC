package com.hurix.kitaboocloud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

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


import com.hurix.epubreader.Utility.Utils;

import com.hurix.kitaboocloud.sdkreadertheme.InsightReaderThemeController;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.service.Interface.IServiceResponse;
import com.hurix.service.Interface.IServiceResponseListener;
import com.hurix.service.exception.ServiceException;
import com.hurix.service.networkcall.SERVICETYPES;
import com.hurix.service.response.FetchStudentDataServiceResponse;
/*import com.hurix.common.services.adapter.KitabooServiceAPI;
import com.hurix.common.services.kitaboo.response.RefreshUserTokenResponse;*/

import org.json.JSONException;

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
    private ReaderThemeSettingVo readerThemeSettingVo;
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
        //SDKManager.getInstance().setSelectedClassStudentList(studentList);
        SDKManager.getInstance().setClassObj(classObj);
        readerThemeSettingVo = InsightReaderThemeController.getInstance(mContext).getReaderThemeUserSettingVo();
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
        rvMainContainer.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getPopupBackground()));
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
                ((StudentListFragment) com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().getTeacherReviewFragmentInstance(classObj.getID()));
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
                com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(count).getUserID(), responseObj.getUserpageList());
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
                        Intent intent = new Intent();
                        intent.putExtra("Action","LOGOUT_BY_USERID");
                        intent.setAction("DBCONTROLLER_RECIVER");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        com.hurix.kitaboo.constants.utils.Utils.startLauncherActivity(mContext);
                        com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setWebViewClosedAfterTokenReceived(false);
                    }
                });
    }

    @Override
    public void requestErrorOccured(ServiceException exeption) {
        ServiceException exceptionObj = exeption;
        if (exeption != null) {
        Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();

            //mProgressDialog.dismiss();

            if (exeption.getResponseRequestType().equals(SERVICETYPES.FETCHSTUDENTANNOTATIONS)) {


                ArrayList<UserPageVO> listUserPageVo = new ArrayList<>();
                UserPageVO dummyUserPage = new UserPageVO();
                dummyUserPage.setPageID(-1);
                listUserPageVo.add(dummyUserPage);
                com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(count).getUserID(), listUserPageVo);
                //studentListAdapter.showRefreshButton(count);
                studentListAdapter.notifyDataSetChanged();
                if (mAllowFetchStudentUGCDataService) {
                    count++;
                    callRequestForUserDataService();
                    //studentListAdapter.setServiceCalled(true);
                }


                if (!exceptionObj.getInvalidFields().isEmpty()) {
                    if (PlayerController.getInstance(mContext).isAutoLogOffEnabled() && entry.getValue() == 103) {
                        showSessionExpiredAlert();
                    } else {
                        if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                          /*  KitabooServiceAPI.getObject().getServiceAdapter()
                                    .refreshUserToken(PlayerController.getInstance(mContext).getToken(), exeption.getResponseRequestType().toString(), new com.hurix.common.services.listener.IServiceResponseListener() {
                                        @Override
                                        public void requestCompleted(com.hurix.common.services.interfaces.IServiceResponse response) throws JSONException {
                                            RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) response;
                                            KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                            DBController.getInstance(mContext).getManager().updateUserToken(responseObj.getUserVO());


                                        }

                                        @Override
                                        public void requestErrorOccured(com.hurix.common.services.exception.ServiceException exeption) {
                                           // showSessionExpiredAlert();
                                        }
                                    });*/
                        }
                    }
                }else {
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
            com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(count).getUserID(), listUserPageVo);
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
        if (mAnnotationpages!=null && mAnnotationpages.size()>0) {
            //setNextAndPreviousDisable();
            int count=0;
            for (int i = 0; i <mAnnotationpages.size() ; i++) {
                if((((mAnnotationpages.get(i).getLinkCollection().size()>0) || (mAnnotationpages.get(i).getPenColl().size()>0) ))){
                    count=count+1;
                }
                if((i== mAnnotationpages.size()-1) && count==0){
                    //GlobalDataManager.getInstance().broadcastRefresh();
                    DialogUtils.displayToastAtCustomizePosition(getContext(), getResources().getString(R.string.validation_empty_data),
                            Toast.LENGTH_SHORT, Gravity.CENTER, 0, 200);
                }else if((i== mAnnotationpages.size()-1)){
                    if (mAssesmentControlListener != null && mAnnotationpages!=null && mAnnotationpages.size() > 0 && mAnnotationpages.size() > mCurrentPageNo) {
                        HashMap<String,String> userFolioId=new HashMap<>();
                        for (UserPageVO userPageVO: mAnnotationpages) {
                            userFolioId.put(userPageVO.getFolioID(),userPageVO.getFolioID());
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
            ArrayList<com.hurix.commons.datamodel.UserPageVO> ugcDataArrayList = com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().
                    getUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(position).getUserID());
            if (ugcDataArrayList != null && ugcDataArrayList.size() > 0 && mStudentList.size() > position) {
                selectedStudentInfo = mStudentList.get(position);
                //For Ascending order page sorting
                HashMap<String,com.hurix.commons.datamodel.UserPageVO> userHashmap= new HashMap();
                ArrayList<com.hurix.commons.datamodel.UserPageVO> ugcSortedDataArrayList=new ArrayList<>();
                for (com.hurix.commons.datamodel.UserPageVO userPageVO:ugcDataArrayList) {
                    userHashmap.put(userPageVO.getFolioID(),userPageVO);
                }
                SDKManager.getInstance().setUserDataMap(userHashmap);
                for (ThumbnailVO pageList:SDKManager.getInstance().getmThumbnailColl()) {
                    if(userHashmap.get(pageList.getFolioID())!=null){
                        ugcSortedDataArrayList.add(userHashmap.get(pageList.getFolioID()));
                    }
                }
                ugcDataArrayList=ugcSortedDataArrayList;
                if (ugcDataArrayList.size() > 0 && !ugcDataArrayList.isEmpty() && ugcDataArrayList.get(0).getPageID() != -1) {
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
                    com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().
                            setUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(position).getUserID(), responseObj.getUserpageList());
                    studentListAdapter.notifyDataSetChanged();
                }

                @Override
                public void requestErrorOccured(ServiceException e) {
                    ArrayList<UserPageVO> listUserPageVo = new ArrayList<>();
                    UserPageVO dummyUserPage = new UserPageVO();
                    dummyUserPage.setPageID(-1);
                    listUserPageVo.add(dummyUserPage);
                    com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setUgcListPerUserIdMap("" + classObj.getID() + mStudentList.get(position).getUserID(), listUserPageVo);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView <?> parent, View view, int position, long id) {

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

        if ((((getPenData()!=null &&  getPenData().size()>0))  || (getLinkData()!=null && getLinkData().size()>0)) && mAssesmentControlListener != null && selectedStudentInfo != null) {
            mAssesmentControlListener.sendTeacherDataForSyncing(selectedStudentInfo.getUserID(), getPenData(), getLinkData(), this);
        } else {
            if (mAssesmentControlListener != null)
                mAssesmentControlListener.showNoDataSubmittedPopup();
        }

    }

    protected void clearLinkData(boolean isClearAllData,String folioId) {

        if (mAssesmentControlListener != null && selectedStudentInfo != null) {
            getLinkData(isClearAllData,folioId);
        } else {
            if (mAssesmentControlListener != null)
                mAssesmentControlListener.showNoDataSubmittedPopup();
        }

    }

    protected void clearPenData(String folioId) {

        if (mAssesmentControlListener != null && selectedStudentInfo != null) {
            //getPenData(folioId);
            SDKManager.getInstance().setAllPenMarkerVO(folioId,  getPenData(folioId));
        } else {
            if (mAssesmentControlListener != null)
                mAssesmentControlListener.showNoDataSubmittedPopup();
        }

    }

    protected void changeCount() {
        if(GlobalDataManager.getInstance().isNextCountRequired()){
            if (mCurrentPageNo < mAnnotationpages.size() - 1) {
                mCurrentPageNo = mCurrentPageNo + 1;
                if (mCurrentPageNo == mAnnotationpages.size() - 1 && mStudentReviewActionsListner != null)
                    mStudentReviewActionsListner.setNextAndPreviousDisable(false, true);
            }
        }else {
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

    public ArrayList<LinkVO> getLinkData(boolean isClearAllData,String folioId) {
        ArrayList<LinkVO> listoflinkdata = new ArrayList<LinkVO>();
        for (int i = 0; i < GlobalDataManager.getInstance().getAssessmentPenMarks().size(); i++) {
            ArrayList<LinkVO> linkColl = GlobalDataManager.getInstance().getAssessmentPenMarks()
                    .get(i).getLinkCollection();
        if(folioId.equals(GlobalDataManager.getInstance().getAssessmentPenMarks().get(i).getFolioID())){
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
            if(folioId.equals(GlobalDataManager.getInstance().getAssessmentPenMarks().get(i).getFolioID())){
                for (int k = 0; k < pentoolColl.size(); k++) {

                        if (pentoolColl.get(k).getPointarray().size() > 0) {

                            if (pentoolColl.get(k).getUGCID() > 0) {
                                pentoolColl.get(k).setMode(Constants.UGC_ITEM_MODE_DELETED);
                                pentoolColl.get(k).setSyncStatus(false);
                                pentoolColl.get(k).setIsSubmitted(false);
                                pentoolColl.get(k).getpentoolData();
                                pentoolColl.get(k).setDateTime(Utils.getDateTime());
                            }else {
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

    public interface IStudentReviewActionsListner
    {
        void setNextAndPreviousDisable(boolean isPreviewDisable, boolean isNextDisable);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(SDKManager.getInstance().isNewTeacherReviewModeOn() || GlobalDataManager.getInstance().isReviewMode()){
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
