package com.hurix.kitaboocloud.kitaboosdkrenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hurix.bookreader.views.link.ScormVO;
import com.hurix.commons.Constants.Constants;
import com.hurix.commons.KitabooSDKModel;
import com.hurix.commons.datamodel.KitabooFixedBook;
import com.hurix.commons.datamodel.LinkVO;
import com.hurix.commons.datamodel.UserPageVO;
import com.hurix.commons.notifier.GlobalDataManager;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.datamodel.HighlightVO;
import com.hurix.customui.datamodel.PentoolVO;
import com.hurix.customui.datamodel.UserClassVO;
import com.hurix.customui.datamodel.UserVO;
import com.hurix.customui.interfaces.IClass;
import com.hurix.customui.interfaces.IUser;


import com.hurix.epubreader.database.DatabaseManager;
import com.hurix.kitaboo.constants.utils.Utils;
import com.hurix.kitaboocloud.PlayerController;
import com.hurix.kitaboocloud.R;

import com.hurix.service.Interface.IServiceResponse;
import com.hurix.service.Interface.IServiceResponseListener;
import com.hurix.service.adapter.KitabooServiceAdapter;
import com.hurix.service.datamodel.SaveUGCResponseItem;
import com.hurix.service.datamodel.UGCFetchResponseObject;
import com.hurix.service.datamodel.UGCSaveObject;
import com.hurix.service.exception.ServiceException;
import com.hurix.service.networkcall.SERVICETYPES;
import com.hurix.service.response.FetchCollaborationMappingDataHandler;
import com.hurix.service.response.FetchCollaborationResponse;
import com.hurix.service.response.FetchCollaborationShareDataResponse;
import com.hurix.service.response.FetchTeacherAnnotationsResponse;
import com.hurix.service.response.PageTrackingServiceResponse;
import com.hurix.service.response.RefreshUserTokenResponse;
import com.hurix.service.response.SaveCollaborationDataResponse;
import com.hurix.service.response.SubmitAnnotationResponse;
import com.hurix.service.response.UGCSyncResponse;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Created by lopamudra.mohanty on 3/20/2018.
 */

//Class handle the service interaction and db insertion
public class ServiceHandler extends KitabooServiceAdapter {

    private final String BOOK_ClASS_LIST_TAG = "bookClassList";
    private final String CLASS_INFO_TAG = "classInfo";
    private final String CLASS_ID_TAG = "id";
    private final String CLASS_NAME_TAG = "name";
    private final String LEARNERS_TAG = "learners";
    private final String USER_TAG = "user";
    private final String USER_ID_TAG = "id";
    private final String USER_NAME_TAG = "userName";
    private final String USER_FIRSTNAME_TAG = "firstName";
    private final String USER_LAST_NAME_TAG = "lastName";
    private final String USER_SHARED_LIST_TAG = "shareList";
    private final String USER_RECEIVED_LIST_TAG = "recieveList";
    private final String INSTRUCTOR_TAG = "instructors";
    private final String SHARING_SETTING = "sharingSetting";
    private final String NOTE_STUDENT_STUDENT = "NOTES-Learner-Learner Sharing";
    private final String NOTE_STUDENT_TEACHER = "NOTES-Learner-Instructor Sharing";
    private final String NOTE_TEACHER_STUDENT = "NOTES-Instructor-Learner Sharing";
    private final String HIGHLIGHT_STUDENT_STUDENT = "HIGHLIGHT-Learner-Learner Sharing";
    private final String HIGHLIGHT_STUDENT_TEACHER = "HIGHLIGHT-Learner-Instructor Sharing";
    private final String HIGHLIGHT_TEACHER_STUDENT = "HIGHLIGHT-Instructor-Learner Sharing";
    private final String PAGE_UGC = "pageUgc";
    Context mContext;
    private ArrayList<IClass> _classesList;
    String mPagetrackdata = "";
    private ServiceCompletedListener mServiceCompletedListener;
    private final int chunkSize = 3 ;

    public ServiceHandler(Context context, String clientID) {
        super(context, clientID);
        mContext = context;
    }

    public ServiceHandler(Context context, String clientID, String serviceUrl) {
        super(context, clientID, serviceUrl);
            mContext = context;
    }

    /**
     * Call fetch UGC service request and on success save in DB insertion
     *  @param bookid
     * @param userid
     * @param bookVersion
     * @param pagenatedugcarray
     */
    public void sendSaveUGCRequest(final long bookid, final long userid, final String bookVersion, final JSONArray pagenatedugcarray, int position) {
        String timestamp = getLastSyncedDateTime(bookid, userid);
        timestamp = Utils.escapeString(timestamp);


        super.backgroundSaveUGC(bookid,
                bookVersion, userid, KitabooSDKModel.getInstance().getUserToken(), timestamp, false, pagenatedugcarray, new IServiceResponseListener() {
                    @Override
                    public void requestCompleted(IServiceResponse response) {
                        Log.e("SaveUGCSynRequest : ", "sucess");
                        UGCSyncResponse responseObj = (UGCSyncResponse) response;
                        if (responseObj.getListOfUGCItems() != null) {
                            updateSaveUGCDataToDB(userid, responseObj.getListOfUGCItems());
                        }

                        if(position == 0) {
                            if (SDKManager.getInstance().isSubmittedClicked()) {
                                submitStudentAnnotation(bookid, userid, bookVersion);
                            }
                            sendFetchCollaborationDataRequest(bookid, userid, bookVersion);
                        }
                    }

                    @Override
                    public void requestErrorOccured(ServiceException exeption) {
                        ServiceException exceptionObj = exeption;
                        if (exceptionObj != null && !exceptionObj.getInvalidFields().isEmpty()) {
                            Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
                            if (PlayerController.getInstance(mContext).isAutoLogOffEnabled() && entry.getValue() == 103) {
                                showSessionExpiredAlert();
                            } else if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                                refreshUserToken(PlayerController.getInstance(mContext).getToken(), exeption.getResponseRequestType().toString(), new IServiceResponseListener() {
                                    @Override
                                    public void requestCompleted(IServiceResponse iServiceResponse) {
                                        RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) iServiceResponse;
                                        KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                        String token = responseObj.getUserVO().getToken();
                                        long userId = responseObj.getUserVO().getUserID();
                                        Intent intent = new Intent();
                                        intent.putExtra("Action","UPDATE_USER_TOKEN");
                                        intent.putExtra("UserID",userId);
                                        intent.putExtra("UserToken",token);
                                        intent.setAction("DBCONTROLLER_RECIVER");
                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


                                       /* ArrayList<JSONArray> ugcDataPagenated = DatabaseManager.getInstance(mContext).getUGCDataPagenated(userid, bookid, chunkSize);
                                        for(int i=0 ; i<= (ugcDataPagenated.size()-1); i++){
                                            sendSaveUGCRequest(bookid, userid, bookVersion,ugcDataPagenated.get(i) ,i);
                                        }*/

                                        int ugcDataLength = DatabaseManager.getInstance(mContext).getUGCData(userid, bookid).length();

                                        ArrayList<JSONArray>paginatedList= sendBackGroundServiceDataInMBs(userid,bookid);

                                        if (ugcDataLength > 0) {

                                            for(int i=0 ; i<= (paginatedList.size()-1); i++){
                                                sendSaveUGCRequest(bookid, userid, bookVersion,paginatedList.get(i) ,i);
                                            }

                                        }
                                    }

                                    @Override
                                    public void requestErrorOccured(ServiceException e) {
                                        //showSessionExpiredAlert();
                                    }
                                });
                            } else if (entry.getValue() == 103) {
                                showErrorAlertDialog(entry.getValue());
                            }
                        }
                    }
                });
    }

    public ArrayList<JSONArray> sendBackGroundServiceDataInMBs(final long userId, final long bookId){

        JSONArray jsonArray=  DatabaseManager.getInstance(mContext).getUGCData(userId,bookId);

        ArrayList<JSONArray>paginatedList= new ArrayList<>();
        JSONArray pgArray= new JSONArray();
        // 10485760
        int weightMaxCount=mContext.getResources().getInteger(R.integer.ugc_chunk_size);
        int ugcChunkLimit=mContext.getResources().getInteger(R.integer.ugc_chunk_limit);
        int singleUGCCount=0;
        int paginatedCount=0;
        boolean alreadyLessThanTen=false;

        if(jsonArray.toString().getBytes().length<weightMaxCount && jsonArray.length()<=ugcChunkLimit ){

            alreadyLessThanTen=true;
        }


        for(int i=0;i<jsonArray.length();i++){

            try {

                JSONObject singleObj= jsonArray.getJSONObject(i);
                singleUGCCount=singleObj.toString().getBytes().length;


                paginatedCount=pgArray.toString().getBytes().length;

                paginatedCount=paginatedCount+singleUGCCount;

                if(singleUGCCount<weightMaxCount){

                    if(weightMaxCount>=paginatedCount && pgArray.length()<ugcChunkLimit ){

                        pgArray.put(singleObj);

                        if (alreadyLessThanTen && i==jsonArray.length()-1){

                            paginatedList.add(pgArray);
                        }
                        else if(i==jsonArray.length()-1){

                            paginatedList.add(pgArray);
                        }


                    }
                    else{

                        if(pgArray.length()<=ugcChunkLimit){

                            paginatedList.add(pgArray);
                        }

                        paginatedCount=0;
                        pgArray= new JSONArray();
                        pgArray.put(singleObj);

                        if(i==jsonArray.length()-1){

                            paginatedList.add(pgArray);
                        }

                    }

                }

                else{

                    pgArray.put(singleObj);

                    paginatedList.add(pgArray);

                    pgArray=new JSONArray();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        Log.e("paginatedList",""+paginatedList.size());

        return paginatedList;


    }


    public void sendFetchUGCRequest(final long bookid, final String accountType, final long userid, final String bookVersion, final IServiceResponseListener _iServiceResponse) {

        String timestamp = getLastSyncedDateTime(bookid, userid);
        timestamp = Utils.escapeString(timestamp);
        super.backgroundFetchUGC(userid, bookid,
                KitabooSDKModel.getInstance().getUserToken(), timestamp, bookVersion, new IServiceResponseListener() {
                    @Override
                    public void requestCompleted(IServiceResponse response) {

                        UGCSyncResponse responseObj = (UGCSyncResponse) response;

                        Log.d("UGCData", "requestCompleted: "+response);
                        if (responseObj.getListOfData() != null) {
                            sendFetchCollaborationDataRequest(bookid, userid, bookVersion);
                            updateFetchUGCDataToDB(userid, responseObj.getListOfData(), bookid);
                        }
                        JSONArray array = DatabaseManager.getInstance(mContext).getUGCData(userid, bookid);

                        ArrayList<JSONArray>paginatedList=  sendBackGroundServiceDataInMBs(userid,bookid);

                        if (array.length() > 0) {
                            ArrayList<JSONArray> ugcDataPagenated = DatabaseManager.getInstance(mContext).getUGCDataPagenated(userid, bookid, chunkSize);
                            for(int i=0 ; i<= (ugcDataPagenated.size()-1); i++){
                                sendSaveUGCRequest(bookid, userid, bookVersion,ugcDataPagenated.get(i), i);
                            }
                            for(int i=0 ; i<= (paginatedList.size()-1); i++){
                                sendSaveUGCRequest(bookid, userid, bookVersion,paginatedList.get(i) ,i);
                            }




                        } else {
                            //sendFetchCollaborationDataRequest(bookid, userid, bookVersion);
                        }

                        //sendFetchCollaborationDataRequest(bookid, userid, bookVersion);
                        if (accountType.equalsIgnoreCase(Constants.STUDENT) && _iServiceResponse != null) {
                            getTeacherAnnotation(GlobalDataManager.getInstance().getLocalBookData(), bookid, accountType, userid, bookVersion, _iServiceResponse);
                        } else {
                            sendFetchBookClassRequest(userid, bookid, _iServiceResponse);
                        }
                    }

                    @Override
                    public void requestErrorOccured(ServiceException exeption) {
                        ServiceException exceptionObj = exeption;
                        if (exceptionObj != null && !exceptionObj.getInvalidFields().isEmpty()) {
                            Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
                            if (PlayerController.getInstance(mContext).isAutoLogOffEnabled() && entry.getValue() == 103) {
                                showSessionExpiredAlert();
                            } else if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                                refreshUserToken(PlayerController.getInstance(mContext).getToken(), exeption.getResponseRequestType().toString(), new IServiceResponseListener() {
                                    @Override
                                    public void requestCompleted(IServiceResponse iServiceResponse) {
                                        RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) iServiceResponse;
                                        KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                        String token = responseObj.getUserVO().getToken();
                                        long userId = responseObj.getUserVO().getUserID();
                                        Intent intent = new Intent();
                                        intent.putExtra("Action","UPDATE_USER_TOKEN");
                                        intent.putExtra("UserID",userId);
                                        intent.putExtra("UserToken",token);
                                        intent.setAction("DBCONTROLLER_RECIVER");
                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                        sendFetchUGCRequest(bookid, accountType, userid, bookVersion, _iServiceResponse);
                                    }

                                    @Override
                                    public void requestErrorOccured(ServiceException e) {
                                        // showSessionExpiredAlert();
                                    }
                                });
                            } else if (entry.getValue() == 103) {
                                showErrorAlertDialog(500);
                            }

                        }
                    }
                });
    }

    /**
     * Call Fetch Bookclass service request and on success save in DB insertion
     *
     * @param userId
     * @param bookid
     * @param iServiceResponseListener
     */
    public void sendFetchBookClassRequest(long userId, long bookid, IServiceResponseListener iServiceResponseListener) {
        String timestamp = getLastSyncedDateTime(bookid, userId);
        timestamp = Utils.escapeString(timestamp);


        super.getClassesOfBook(userId, bookid,
                KitabooSDKModel.getInstance().getUserToken(), iServiceResponseListener);

    }


    public void sendRequestsubmitTeacherAnnotation(KitabooFixedBook kitabooFixedBook, long learnerID, long bookid, ArrayList<PentoolVO> pentools, ArrayList<LinkVO> fibVos, String bookVersion, IServiceResponseListener iServiceResponseListener) {
        String ugcjsonstring = getugcListJSON(pentools, fibVos, bookVersion);
        super.submitTeacherAnnotation(mContext, kitabooFixedBook, ugcjsonstring, learnerID, bookid,
                KitabooSDKModel.getInstance().getUserToken(), iServiceResponseListener);
    }

    public void getStudentAnnotation(long bookid, String accountType, long userid, String bookVersion, IServiceResponseListener iServiceResponseListener) {
        String timestamp = getLastSyncedDateTime(bookid, userid);
        timestamp = Utils.escapeString(timestamp);
        super.getStudentAnnotation(KitabooSDKModel.getInstance().getUserToken(), bookid, userid,
                bookVersion, iServiceResponseListener);
    }

    public void getTeacherAnnotation(final KitabooFixedBook book, final long bookid, final String accountType, final long userid, final String bookVersion, final IServiceResponseListener _iServiceResponse) {
        String timestamp = getLastSyncedDateTime(bookid, userid);
        timestamp = Utils.escapeString(timestamp);

        super.getTeacherAnnotation(bookid, userid, bookVersion,
                KitabooSDKModel.getInstance().getUserToken(), new IServiceResponseListener() {
                    @Override
                    public void requestCompleted(IServiceResponse response) {
                        FetchTeacherAnnotationsResponse responseObj = (FetchTeacherAnnotationsResponse) response;
                        if (responseObj.isSuccess()) {
                            saveDataIntoDB(bookid, userid, responseObj.getListOfAnnotataion());
                        }
                        sendFetchBookClassRequest(userid, bookid, _iServiceResponse);
                    }

                    @Override
                    public void requestErrorOccured(ServiceException exeption) {
                        ServiceException exceptionObj = exeption;
                        if (exceptionObj != null && !exceptionObj.getInvalidFields().isEmpty()) {
                            Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
                            if (PlayerController.getInstance(mContext).isAutoLogOffEnabled() && entry.getValue() == 103) {
                                showSessionExpiredAlert();
                            } else if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                                refreshUserToken(PlayerController.getInstance(mContext).getToken(), exeption.getResponseRequestType().toString(), new IServiceResponseListener() {
                                    @Override
                                    public void requestCompleted(IServiceResponse iServiceResponse) {
                                        RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) iServiceResponse;
                                        KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                        String token = responseObj.getUserVO().getToken();
                                        long userId = responseObj.getUserVO().getUserID();
                                        Intent intent = new Intent();
                                        intent.putExtra("Action","UPDATE_USER_TOKEN");
                                        intent.putExtra("UserID",userId);
                                        intent.putExtra("UserToken",token);
                                        intent.setAction("DBCONTROLLER_RECIVER");
                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                        getTeacherAnnotation(book, bookid, accountType, userid, bookVersion, _iServiceResponse);
                                    }

                                    @Override
                                    public void requestErrorOccured(ServiceException e) {
                                        // showSessionExpiredAlert();
                                    }
                                });
                            } else if (entry.getValue() == 103) {
                                showErrorAlertDialog(entry.getValue());
                            }
                        }
                       /* if(!sessionexpire(exeption)) {
                            sendFetchBookClassRequest(userid,bookid,_iServiceResponse);
                        }*/
                    }
                });
    }

    public void submitStudentAnnotation(final long bookid, final long userid, final String bookVersion) {
        final String timestamp = getLastSyncedDateTime(bookid, userid);
        final String _timestamp = Utils.escapeString(timestamp);

        super.submitStudentData(
                KitabooSDKModel.getInstance().getUserToken(), bookid, new IServiceResponseListener() {
                    @Override
                    public void requestCompleted(IServiceResponse response) {
                        SubmitAnnotationResponse responseObj = (SubmitAnnotationResponse) response;
                        if (responseObj.isSuccess()) {
                            SDKManager.getInstance().setSubmittedClicked(false);
                        }
                    }

                    @Override
                    public void requestErrorOccured(ServiceException exeption) {
                        ServiceException exceptionObj = exeption;
                        if (exceptionObj != null && !exceptionObj.getInvalidFields().isEmpty()) {
                            Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
                            if (PlayerController.getInstance(mContext).isAutoLogOffEnabled() && entry.getValue() == 103) {
                                showSessionExpiredAlert();
                            } else if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                                refreshUserToken(PlayerController.getInstance(mContext).getToken(), exeption.getResponseRequestType().toString(), new IServiceResponseListener() {
                                    @Override
                                    public void requestCompleted(IServiceResponse iServiceResponse) {
                                        RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) iServiceResponse;
                                        KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                        String token = responseObj.getUserVO().getToken();
                                        long userId = responseObj.getUserVO().getUserID();
                                        Intent intent = new Intent();
                                        intent.putExtra("Action","UPDATE_USER_TOKEN");
                                        intent.putExtra("UserID",userId);
                                        intent.putExtra("UserToken",token);
                                        intent.setAction("DBCONTROLLER_RECIVER");
                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                        submitStudentAnnotation(bookid, userid, bookVersion);
                                    }

                                    @Override
                                    public void requestErrorOccured(ServiceException e) {
                                        //showSessionExpiredAlert();
                                    }
                                });
                            } else if (entry.getValue() == 103) {
                                showErrorAlertDialog(entry.getValue());
                            }
                        }
                    }
                });
    }

    private String getLastSyncedDateTime(long bookid, long userid) {
        String lastSyncedOn = Constants.DEFAULT_TIME_STAMP;
        String returnValue = DatabaseManager.getInstance(mContext).getLastSyncedDateTime(
                userid, bookid);
        if (returnValue != null && returnValue.length() > 1) {
            lastSyncedOn = returnValue;
        }
        return lastSyncedOn;
    }

    private void updateFetchUGCDataToDB(long userID, ArrayList<UGCFetchResponseObject> arrayOfUGCIDs, long bookid) {
        UserVO userVo = new UserVO();
        userVo.setUserID(userID);
        DatabaseManager.getInstance(mContext).updateUGCDataFromServer(userVo, arrayOfUGCIDs, bookid);
        if (mServiceCompletedListener != null)
            mServiceCompletedListener.fetchUGCRequestCompleted(arrayOfUGCIDs);
    }

    private boolean updateSaveUGCDataToDB(long userID, Hashtable<Long, UGCSaveObject> arrayOfUGCIDs) {
        boolean isSuccessful = true;

        Set<Long> keys = arrayOfUGCIDs.keySet();
        for (Long key : keys) {
            long bookID = (key != null) ? key : -1;

            if (bookID > 0) {
                UGCSaveObject ugcResponseObj = arrayOfUGCIDs.get(key);

                for (SaveUGCResponseItem item : ugcResponseObj.getUGCResponseItems()) {
                    String type = item.get_ugcType();
                    String localID = item.getlocalID();
                    String ugcID = item.getUgcID();
                    String lastSyncedOn = item.getlastSyncedOn();

                    long id = DatabaseManager.getInstance(mContext).updateServerIDForUGCData(item.get_ugcType(), localID, ugcID, lastSyncedOn);

                    if (id < 0) {
                        isSuccessful = false;
                        break;
                    }
                }

                if (isSuccessful) {
                    DatabaseManager.getInstance(mContext).deleteMarkedUGCData(userID, bookID);
                }
            } else {
                isSuccessful = false;
            }
        }

        return isSuccessful;
    }

    private String getugcListJSON(ArrayList<PentoolVO> pentools, ArrayList<LinkVO> fibVos, String bookVersion) {
        JSONObject ugcheader = new JSONObject();
        JSONArray ugcList = new JSONArray();
        try {
            //add pen tools
            if (pentools != null && !pentools.isEmpty()) {
                for (int i = 0; i < pentools.size(); i++) {
                    JSONObject obj = new JSONObject();
                    obj.put("localId", pentools.get(i).getLocalID());
                    obj.put("ugcData", "");
                    obj.put("id", pentools.get(i).getUGCID());
                    obj.put("type", "3");
                    obj.put("createdOn", pentools.get(i).getDateTime());
                    obj.put("metadata", Utils.escapeString(makePentoolData(pentools.get(i))));
                    obj.put("pageId", 0);
                    obj.put("folioID", pentools.get(i).getFolioID());
                    if (pentools.get(i).getMode().equalsIgnoreCase(Constants.UGC_ITEM_MODE_NEW)) {
                        obj.put("status", "NEW");
                    } else if (pentools.get(i).getMode().equalsIgnoreCase(Constants.UGC_ITEM_MODE_DELETED)) {
                        obj.put("status", "DELETE");
                    }
                    obj.put("important", "N");
                    ugcList.put(obj);
                }
            }
            //FIB adds
            if (fibVos != null && !fibVos.isEmpty()) {
                for (int i = 0; i < fibVos.size(); i++) {
                    JSONObject obj = new JSONObject();
                    obj.put("localId", fibVos.get(i).getLocalID());
                    obj.put("ugcData", fibVos.get(i).getUserAnswer());
                    obj.put("id", fibVos.get(i).getUGCID());
                    obj.put("type", "5");
                    obj.put("createdOn", fibVos.get(i).getDateTime());
                    obj.put("metadata", Utils.escapeString(getFIBMetaData(fibVos.get(i))));
                    obj.put("pageId", 0);
                    obj.put("folioID", fibVos.get(i).getFolioID());
                    if (fibVos.get(i).getMode().equalsIgnoreCase(Constants.UGC_ITEM_MODE_NEW)) {
                        obj.put("status", "NEW");
                    } else if (fibVos.get(i).getMode().equalsIgnoreCase(Constants.UGC_ITEM_MODE_DELETED)) {
                        obj.put("status", "DELETE");
                    } else {
                        obj.put("status", "UPDATE");
                    }
                    obj.put("important", "N");
                    ugcList.put(obj);
                }
            }
            ugcheader.put("ugcList", ugcList);
            ugcheader.put(Constants.UGC_PARAM_BOOK_VERSION, bookVersion);
        } catch (Exception e) {

        }
        return ugcheader.toString();
    }

    private String makePentoolData(PentoolVO vo) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("LineColor", vo.getColor());
            obj.put("LineWidth", vo.getThickness());
            obj.put("LineStyle", "0");
            ArrayList<Point> allpoints = vo.getPointarray();
            JSONArray pointarray = new JSONArray();
            for (int i = 0; i < allpoints.size(); i++) {
                JSONObject obja = new JSONObject();
                obja.put("x", allpoints.get(i).x);
                obja.put("y", allpoints.get(i).y);
                pointarray.put(obja);
            }

            obj.put("PathPoints", pointarray);
            return obj.toString();

        } catch (Exception e) {
            return "";
        }

    }

    private String getFIBMetaData(LinkVO vo) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Constants.UGC_METADATA_PARAM_LINK_ID,
                    vo.getLinkID());
            obj.put(Constants.UGC_METADATA_PARAM_REVIEW_MODE, true);
            if (vo.ismIsMathView()) {
                obj.put("type", "EE");
            }
            return obj.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public ArrayList<IClass> getClassesVoList(JSONObject responseData, long userId) {

        ArrayList<IClass> arrClassVo = new ArrayList<IClass>();
        //UserVO user = (UserVO) DBController.getInstance(_context).getManager().getLogInUserVO();
        try {
            JSONArray bookClassList = responseData.getJSONArray(BOOK_ClASS_LIST_TAG);

            for (int i = 0; i < bookClassList.length(); i++) {
                JSONArray classjson = bookClassList.getJSONObject(i).getJSONArray(CLASS_INFO_TAG);

                for (int j = 0; j < classjson.length(); j++) {
                    JSONObject obj = classjson.getJSONObject(j);
                    UserClassVO classObj = new UserClassVO();

                    //fill classes

                    classObj.setID(obj.getString(CLASS_ID_TAG));
                    classObj.setName(obj.getString(CLASS_NAME_TAG));

                    // fill setting
                    if (obj.has(SHARING_SETTING)) {
                        JSONObject sharingSetting = obj.getJSONObject(SHARING_SETTING);
                        if (sharingSetting.has(NOTE_STUDENT_STUDENT)) {
                            classObj.setNoteStudentStudentSharing(sharingSetting
                                    .getString(NOTE_STUDENT_STUDENT).equalsIgnoreCase("true"));
                        }
                        if (sharingSetting.has(NOTE_STUDENT_TEACHER)) {
                            classObj.setNoteStudentTeacherSharing(sharingSetting
                                    .getString(NOTE_STUDENT_TEACHER).equalsIgnoreCase("true"));
                        }
                        if (sharingSetting.has(NOTE_TEACHER_STUDENT)) {
                            classObj.setNoteTeacherStudentSharing(sharingSetting
                                    .getString(NOTE_TEACHER_STUDENT).equalsIgnoreCase("true"));
                        }
                        if (sharingSetting.has(HIGHLIGHT_STUDENT_STUDENT)) {
                            classObj.setHighLightStudentStudentSharing(sharingSetting
                                    .getString(HIGHLIGHT_STUDENT_STUDENT).equalsIgnoreCase("true"));
                        }
                        if (sharingSetting.has(HIGHLIGHT_STUDENT_TEACHER)) {
                            classObj.setHighLightStudentTeacherSharing(sharingSetting
                                    .getString(HIGHLIGHT_STUDENT_TEACHER).equalsIgnoreCase("true"));
                        }
                        if (sharingSetting.has(HIGHLIGHT_TEACHER_STUDENT)) {
                            classObj.setHighLightTeacherStudentSharing(sharingSetting
                                    .getString(HIGHLIGHT_TEACHER_STUDENT).equalsIgnoreCase("true"));
                        }
                    } else {
                        classObj.setNoteStudentStudentSharing(GlobalDataManager.getInstance().is_NoteStudentStudentEnable());
                        classObj.setNoteStudentTeacherSharing(GlobalDataManager.getInstance().is_isNoteTeacherStudentEnable());
                        classObj.setNoteTeacherStudentSharing(GlobalDataManager.getInstance().is_isNoteTeacherStudentEnable());
                        classObj.setHighLightStudentStudentSharing(GlobalDataManager.getInstance().is_isHighlightStudentStudentEnable());
                        classObj.setHighLightStudentTeacherSharing(GlobalDataManager.getInstance().is_isHighlightTeacherStudentEnable());
                        classObj.setHighLightTeacherStudentSharing(GlobalDataManager.getInstance().is_isHighlightTeacherStudentEnable());
                    }
                    JSONArray learners = obj.getJSONArray(LEARNERS_TAG);
                    //fill students
                    for (int k = 0; k < learners.length(); k++) {
                        //user
                        JSONObject userobj = learners.getJSONObject(k).getJSONObject(USER_TAG);
                        UserVO classUsers = new UserVO();
                        classUsers.setUserID(userobj.getLong(USER_ID_TAG));
                        classUsers.setUserName(userobj.getString(USER_NAME_TAG));
                        classUsers.setFirstName(userobj.getString(USER_FIRSTNAME_TAG));
                        classUsers.setLastName(userobj.getString(USER_LAST_NAME_TAG));
                        classUsers.setDisplayName(classUsers.getFirstName() + " " + classUsers.getLastName());
                        classUsers.setRoleName(Constants.STUDENT);


                        if (userId == classUsers.getUserID()) {
                            //UserController.getInstance(_context).getUserVO().setRoleName(Constants.STUDENT);
                            try {
                                JSONArray shareList = learners.getJSONObject(k).getJSONArray(USER_SHARED_LIST_TAG);

                                for (int l = 0; l < shareList.length(); l++) {
                                    classObj.getShareUserList().add(shareList.getInt(l));
                                }
                            } catch (Exception e) {

                            }

                            try {
                                JSONArray recieveList = learners.getJSONObject(k).getJSONArray(USER_RECEIVED_LIST_TAG);
                                for (int l = 0; l < recieveList.length(); l++) {
                                    classObj.getRecieveUserList().add(recieveList.getInt(l));
                                }
                            } catch (Exception e) {

                            }
                        } else {
                            classObj.getStudentList().add(classUsers);
                        }
                    }

                    //check instructors
                    JSONArray instructors = obj.getJSONArray(INSTRUCTOR_TAG);

                    for (int k = 0; k < instructors.length(); k++) {
                        //user
                        JSONObject userobj = instructors.getJSONObject(k).getJSONObject(USER_TAG);
                        UserVO classUsers = new UserVO();
                        classUsers.setUserID(userobj.getLong(USER_ID_TAG));
                        classUsers.setUserName(userobj.getString(USER_NAME_TAG));
                        classUsers.setFirstName(userobj.getString(USER_FIRSTNAME_TAG));
                        classUsers.setLastName(userobj.getString(USER_LAST_NAME_TAG));
                        classUsers.setDisplayName(classUsers.getFirstName() + " " + classUsers.getLastName());
                        classUsers.setRoleName(Constants.TEACHER);

                        if (userId == classUsers.getUserID()) {
                            //UserController.getInstance(_context).getUserVO().setRoleName(Constants.TEACHER);
                            try {
                                JSONArray shareList = instructors.getJSONObject(k).getJSONArray(USER_SHARED_LIST_TAG);
                                for (int l = 0; l < shareList.length(); l++) {
                                    classObj.getShareUserList().add(shareList.getInt(l));
                                }
                            } catch (Exception e) {

                            }

                            try {
                                JSONArray recieveList = instructors.getJSONObject(k).getJSONArray(USER_RECEIVED_LIST_TAG);
                                for (int l = 0; l < recieveList.length(); l++) {
                                    classObj.getRecieveUserList().add(recieveList.getInt(l));
                                }
                            } catch (Exception e) {

                            }
                        } else {
                            classObj.getStudentList().add(classUsers);
                        }
                    }
                    arrClassVo.add(classObj);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrClassVo;
    }

    public void setClassesList(ArrayList<IClass> classesList) {
        this._classesList = classesList;
        if (_classesList != null && !_classesList.isEmpty()) {
            for (int i = 0; i < _classesList.size(); i++) {
                UserClassVO userClass = ((UserClassVO) _classesList.get(i));
                for (IUser iuser : userClass.getStudentList()) {
                    for (Integer value : userClass.getShareUserList()) {
                        if (value == iuser.getUserID()) {
                            iuser.sethighlightRecieveWithUser(true);
                        }
                    }
                    for (Integer value : userClass.getRecieveUserList()) {
                        if (value == iuser.getUserID()) {
                            iuser.sethighlightRecieveWithUser(true);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<UserPageVO> getAnnotationFromResponse(JSONObject jsonobject) {
        ArrayList<UserPageVO> allpageswithPentoolandFIB = new ArrayList<UserPageVO>();
        try {
            JSONArray pageUGClist = jsonobject.getJSONArray(PAGE_UGC);

            for (int i = 0; i < pageUGClist.length(); i++) {
                JSONObject tmpobj = (JSONObject) pageUGClist.get(i);
                UserPageVO annotationpages = new UserPageVO();
                String folioID = "";
                if (tmpobj.has("folioID")) {
                    folioID = tmpobj.getString("folioID");
                }
                if (!folioID.equals("")) {
                    annotationpages.setFolioID(folioID);
                    JSONArray ugcListlist = tmpobj.getJSONArray("ugcList");
                    ArrayList<PentoolVO> pentools = new ArrayList<PentoolVO>();
                    ArrayList<LinkVO> activities = new ArrayList<LinkVO>();
                    for (int j = 0; j < ugcListlist.length(); j++) {
                        JSONObject jsonObj = ugcListlist.getJSONObject(j);
                        JSONObject createdBy = jsonObj.getJSONObject("createdBy");
                        int type = jsonObj.getInt("type");
                        if (type == 3) {
                            PentoolVO PentoolVO = new PentoolVO();
                            try {
                                PentoolVO.setPageId("0");
                                PentoolVO.setFolioID(jsonObj.getString("folioID"));
                                PentoolVO.setDateTime(jsonObj.getString("createdOn"));
                                PentoolVO.setUgcID(Utils.Donullcheck(
                                        jsonObj.getString("id")));
                                setpentoolpropertys(PentoolVO, com.hurix.epubreader.Utility.Utils.unescapeString(
                                        jsonObj.getString("metadata")));
                                PentoolVO.setSyncStatus(true);
                                if (!jsonObj.getString(Constants.UGC_PARAM_STATUS).equalsIgnoreCase(
                                        Constants.UGC_ITEM_DELETE)) {
                                    pentools.add(PentoolVO);
                                }
                            } catch (Exception e) {
                            }
                        } else if (type == 5) {
                            LinkVO linkObj = new LinkVO(mContext);
                            try {
                                linkObj.setPageID(0);
                                linkObj.setFolioID(jsonObj.getString("folioID"));
                                linkObj.setDateTime(jsonObj.getString("createdOn"));
                                linkObj.setUGCID(com.hurix.epubreader.Utility.Utils.Donullcheck(
                                        jsonObj.getString("id")));
                                linkObj.setUserAnswer(jsonObj.getString("ugcData"));
                                linkObj.setSyncStatus(true);
                                parseFIB(linkObj, jsonObj.getString("metadata"));
                                if (!jsonObj.getString(Constants.UGC_PARAM_STATUS).equalsIgnoreCase(Constants.UGC_ITEM_DELETE)) {
                                    activities.add(linkObj);
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                    annotationpages.setPenColl(pentools);
                    annotationpages.setLinkCollection(activities);
                    allpageswithPentoolandFIB.add(annotationpages);
                }
            }
        } catch (Exception e) {
            return allpageswithPentoolandFIB;
        }
        if (allpageswithPentoolandFIB.size() > 0) {
            Collections.sort(allpageswithPentoolandFIB, new Comparator<UserPageVO>() {
                public int compare(final UserPageVO p1, final UserPageVO p2) {
                    return p1.getPageID() < p2.getPageID() ? -1
                            : p1.getPageID() > p2.getPageID() ? 1 : 0;
                }
            });
        }
        return allpageswithPentoolandFIB;
    }

    private void setpentoolpropertys(PentoolVO vo, String metadata) {
        try {
            JSONObject metadatatmp = new JSONObject(Utils.unescapeString(metadata));
            vo.setStyle(Integer.parseInt(metadatatmp.getString("LineStyle")));
            vo.setThickness(Integer.parseInt(metadatatmp.getString("LineWidth")));
            vo.setColor(metadatatmp.getString("LineColor"));
            vo.setPointarray(getpoints(new JSONArray(metadatatmp.getString("PathPoints"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Point> getpoints(JSONArray pointjson) {
        ArrayList<Point> listofpoints = new ArrayList<Point>();
        for (int i = 0; i < pointjson.length(); i++) {
            Point point = new Point();
            try {
                point.set(pointjson.getJSONObject(i).getInt("x"), pointjson.getJSONObject(i).getInt("y"));
                listofpoints.add(point);
            } catch (Exception e) {

            }
        }
        return listofpoints;
    }

    private void parseFIB(LinkVO vo, String metadata) {
        try {
            JSONObject metadatatmp = new JSONObject(Utils.unescapeString(metadata));
            vo.setLinkID(Integer.parseInt(metadatatmp.getString("LinkID")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<IClass> get_classesList() {
        return _classesList;
    }

    public boolean saveDataIntoDB(long bookID, long userID, ArrayList<com.hurix.customui.datamodel.FetchTeacherAnnotationVO> _responseObj) {
        boolean isUpdateSuccessful = true;
        isUpdateSuccessful = DatabaseManager.getInstance(mContext).updateAnnotationDataFromServer(userID, _responseObj, bookID);
        return isUpdateSuccessful;
    }

    private boolean sessionexpire(ServiceException response) {
        boolean sessionexpired = false;
        ServiceException exceptionObj = response;
        if (null != exceptionObj && exceptionObj.getInvalidFields() != null && !exceptionObj.getInvalidFields().isEmpty()) {
            Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields()
                    .entrySet().iterator().next();
            if (/*PlayerController.getInstance(mContext).isAutoLogOffEnabled() &&*/ entry.getValue() == 103) {
                showSessionExpiredAlert();
                sessionexpired = true;
            }
        }
        return sessionexpired;
    }

    private void showSessionExpiredAlert() {
        int value = 103;
        com.hurix.commons.utils.DialogUtils.showOKAlert(new View(mContext), 1, mContext, mContext.getResources()
                        .getString(R.string.alert_title), com.hurix.kitaboo.constants.utils.Utils.getMessageForError(mContext, value),
                new com.hurix.commons.listener.OnDialogOkActionListner() {
                    @Override
                    public void onOKClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("Action","LOGOUT_BY_USERID");
                        intent.setAction("DBCONTROLLER_RECIVER");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        Utils.startLauncherActivity(mContext);
                        com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setWebViewClosedAfterTokenReceived(false);
                    }
                });
    }

    /*  private void showSessionExpiredAlertDialog() {
          int value=103;
          com.hurix.commons.utils.DialogUtils.showOKAlert(new View(mContext), 1, mContext, mContext.getResources()
                          .getString(R.string.alert_title), com.hurix.kitaboo.constants.utils.Utils.getMessageForError(mContext, value),
                  new com.hurix.commons.listener.OnDialogOkActionListner() {
                      @Override
                      public void onOKClick(View v) {
                      }
                  });
      }*/
    private void showErrorAlertDialog(int errorCode) {
        com.hurix.commons.utils.DialogUtils.showOKAlert(new View(mContext), 1, mContext, mContext.getResources()
                        .getString(R.string.alert_title), com.hurix.kitaboo.constants.utils.Utils.getMessageForError(mContext, errorCode),
                new com.hurix.commons.listener.OnDialogOkActionListner() {
                    @Override
                    public void onOKClick(View v) {
                    }
                });
    }

    public void sendHighlightSettingRequest(ArrayList<IClass> arrayList, long bookid, IServiceResponseListener iServiceResponseListener) {
        super.submitHighlightSettingRequest(
                KitabooSDKModel.getInstance().getUserToken(), bookid, arrayList, iServiceResponseListener);
    }

    public void SaveCollaborationDataRequest(final long bookid, final long userid) {

        super.saveCollaborationData(bookid, userid,
                KitabooSDKModel.getInstance().getUserToken(), new IServiceResponseListener() {

                    @Override
                    public void requestCompleted(IServiceResponse response) {
                        if (response != null) {
                            SaveCollaborationDataResponse responseObj = (SaveCollaborationDataResponse) response;
                            if (responseObj.isSuccess()) {
                                ArrayList<Long> idarray = responseObj.getUgcID();
                                if (DatabaseManager.getInstance(mContext).updateHighlightAfterCollabs(idarray)) {
                                    FetchCollaborationShareDataRequest(bookid, userid, responseObj.getTimestamp());
                                } else {
                                   /* if (mPagetrackdata != null && !mPagetrackdata.isEmpty()) {
                                        savePageTrackingData(KitabooSDKModel.getInstance().getUserToken(), bookid, userid, mPagetrackdata);
                                    }*/
                                }
                            }
                        }
                    }

                    @Override
                    public void requestErrorOccured(ServiceException exeption) {
                        ServiceException exceptionObj = exeption;
                        if (exceptionObj != null && !exceptionObj.getInvalidFields().isEmpty()) {
                            Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
                            if (PlayerController.getInstance(mContext).isAutoLogOffEnabled() && entry.getValue() == 103) {
                                showSessionExpiredAlert();
                            } else if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                                refreshUserToken(PlayerController.getInstance(mContext).getToken(), exeption.getResponseRequestType().toString(), new IServiceResponseListener() {
                                    @Override
                                    public void requestCompleted(IServiceResponse iServiceResponse) {
                                        RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) iServiceResponse;
                                        KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                        String token = responseObj.getUserVO().getToken();
                                        long userId = responseObj.getUserVO().getUserID();
                                        Intent intent = new Intent();
                                        intent.putExtra("Action","UPDATE_USER_TOKEN");
                                        intent.putExtra("UserID",userId);
                                        intent.putExtra("UserToken",token);
                                        intent.setAction("DBCONTROLLER_RECIVER");
                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                        SaveCollaborationDataRequest(bookid, userid);
                                    }

                                    @Override
                                    public void requestErrorOccured(ServiceException e) {
                                        // showSessionExpiredAlert();
                                    }
                                });
                            } else if (entry.getValue() == 103) {
                                showErrorAlertDialog(entry.getValue());
                            }

                        }
                        /*final String timestamp = getLastSyncedDateTime(bookid,userid);
                        final String _timestamp = Utils.escapeString(timestamp);
                        if(!sessionexpire(exeption)) {
                            //SaveCollaborationDataResponse responseObj = (SaveCollaborationDataResponse) exeption;
                            FetchCollaborationShareDataRequest(bookid, userid, _timestamp);
                        }*/
                    }
                });
    }

    private void sendFetchCollaborationDataRequest(final long bookid, final long userid, final String bookVersion) {
        final String timestamp = getLastSyncedDateTime(bookid, userid);
        final String _timestamp = Utils.escapeString(timestamp);
        super.fecthCollaborationData(
                KitabooSDKModel.getInstance().getUserToken(), bookid, _timestamp, bookVersion, new IServiceResponseListener() {
                    @Override
                    public void requestCompleted(IServiceResponse response) {
                        /*
                        https://hurixpdg.atlassian.net/browse/AH5-222 --- Service calling missed for share count
                         */
                        FetchCollaborationShareDataRequest(bookid, userid, _timestamp);
                        SaveCollaborationDataRequest(bookid, userid);
                        FetchCollaborationResponse responseObj = (FetchCollaborationResponse) response;
                        if (responseObj.isSuccess()) {
                            if (DatabaseManager.getInstance(mContext).updateCollabDataFromServer(userid, responseObj.getFetchCollabList(), bookid)) {

                            }
                        }
                    }

                    @Override
                    public void requestErrorOccured(ServiceException exeption) {
                        ServiceException exceptionObj = exeption;
                        if (exceptionObj != null && !exceptionObj.getInvalidFields().isEmpty()) {
                            Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
                            if (PlayerController.getInstance(mContext).isAutoLogOffEnabled() && entry.getValue() == 103) {
                                showSessionExpiredAlert();
                            } else if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                                refreshUserToken(PlayerController.getInstance(mContext).getToken(), exeption.getResponseRequestType().toString(), new IServiceResponseListener() {
                                    @Override
                                    public void requestCompleted(IServiceResponse iServiceResponse) {
                                        RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) iServiceResponse;
                                        KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                        String token = responseObj.getUserVO().getToken();
                                        long userId = responseObj.getUserVO().getUserID();
                                        Intent intent = new Intent();
                                        intent.putExtra("Action","UPDATE_USER_TOKEN");
                                        intent.putExtra("UserID",userId);
                                        intent.putExtra("UserToken",token);
                                        intent.setAction("DBCONTROLLER_RECIVER");
                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                        sendFetchCollaborationDataRequest(bookid, userid, bookVersion);
                                    }

                                    @Override
                                    public void requestErrorOccured(ServiceException e) {
                                        // showSessionExpiredAlert();
                                    }
                                });
                            } else if (entry.getValue() == 103) {
                                showErrorAlertDialog(entry.getValue());
                            }

                        }
                        /*if(exeption.getResponseRequestType().equals(SERVICETYPES.FETCH_COLLAB_DATA_REQUEST)) {
                            SaveCollaborationDataRequest(bookid, userid);
                        }*/
                    }
                });
    }

    public void FetchCollaborationShareDataRequest(final long bookid, final long userid, final String timestamp) {
        final String mtimestamp = getLastSyncedDateTime(bookid, userid);
        final String _timestamp = Utils.escapeString(mtimestamp);


        super.fetchCollaborationShareData(
                KitabooSDKModel.getInstance().getUserToken(), bookid, _timestamp, new IServiceResponseListener() {
                    @Override
                    public void requestCompleted(IServiceResponse response) {
                        FetchCollaborationShareDataResponse responseObj = (FetchCollaborationShareDataResponse) response;
                        if (responseObj.isSuccess()) {
                            try {
                                new FetchCollaborationMappingDataHandler(mContext).handleResponseRsponse(bookid, userid, responseObj.getFetchCollaborationShareDataList());
                            } catch (ServiceException e) {
                                e.printStackTrace();
                            }
                        }
                        /*if (mPagetrackdata != null && !mPagetrackdata.isEmpty()) {
                            savePageTrackingData(KitabooSDKModel.getInstance().getUserToken(), bookid, userid, mPagetrackdata);
                        }*/
                    }

                    @Override
                    public void requestErrorOccured(ServiceException exeption) {
                        ServiceException exceptionObj = exeption;
                        if (exceptionObj != null && !exceptionObj.getInvalidFields().isEmpty()) {
                            Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
                            if (PlayerController.getInstance(mContext).isAutoLogOffEnabled() && entry.getValue() == 103) {
                                showSessionExpiredAlert();
                            } else if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                                refreshUserToken(PlayerController.getInstance(mContext).getToken(), exeption.getResponseRequestType().toString(), new IServiceResponseListener() {
                                    @Override
                                    public void requestCompleted(IServiceResponse iServiceResponse) {
                                        RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) iServiceResponse;
                                        KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                        String token = responseObj.getUserVO().getToken();
                                        long userId = responseObj.getUserVO().getUserID();
                                        Intent intent = new Intent();
                                        intent.putExtra("Action","UPDATE_USER_TOKEN");
                                        intent.putExtra("UserID",userId);
                                        intent.putExtra("UserToken",token);
                                        intent.setAction("DBCONTROLLER_RECIVER");
                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                        FetchCollaborationShareDataRequest(bookid, userid, timestamp);
                                    }

                                    @Override
                                    public void requestErrorOccured(ServiceException e) {
                                        //showSessionExpiredAlert();
                                    }
                                });
                            } else if (entry.getValue() == 103) {
                                showErrorAlertDialog(entry.getValue());
                            }

                        }

                        /*if(mPagetrackdata != null && !mPagetrackdata.isEmpty()) {
                            savePageTrackingData(KitabooSDKModel.getInstance().getUserToken(), bookid, userid, mPagetrackdata);
                        }*/
                    }
                });
    }


    public void SendAcceptCollaborationDataRequest(HighlightVO vo, int status, String usertoken, IServiceResponseListener response) {
        super.notifyUserSharedRequest(vo.getUGCID(), status,
                usertoken, response);
    }

    public void sendSubmitDataRequest(long bookid, long userid, String bookVersion) {
        /*ArrayList<JSONArray> ugcDataPagenated = DatabaseManager.getInstance(mContext).getUGCDataPagenated(userid, bookid, chunkSize);
        for(int i=0 ; i<= (ugcDataPagenated.size()-1); i++){
            sendSaveUGCRequest(bookid, userid, bookVersion,ugcDataPagenated.get(i),i);
        }*/

        int ugcDataLength = DatabaseManager.getInstance(mContext).getUGCData(userid, bookid).length();

        ArrayList<JSONArray>paginatedList= sendBackGroundServiceDataInMBs(userid,bookid);

        if (ugcDataLength > 0) {

            for(int i=0 ; i<= (paginatedList.size()-1); i++){
                sendSaveUGCRequest(bookid, userid, bookVersion,paginatedList.get(i) ,i);
            }

        }
    }


    public void savePageTrackingData(final String userToken, final long bookid, final long userid, final String pagetrackdata) {
        //  Log.d("savePageTrackingData : ", "called");

        super.pageTracking(userToken, bookid, pagetrackdata, new IServiceResponseListener() {
            @Override
            public void requestCompleted(IServiceResponse response) {
                PageTrackingServiceResponse responseObj = (PageTrackingServiceResponse) response;
                if (responseObj.isSuccess()) {
                    //delete data from DB
                    DatabaseManager.getInstance(mContext).deleteAnalyticsData(bookid, userid);
                    //Log.d("savePageTrackingData : ", "sucess");


                    //Deleting stored cfid data after successfully submitted to the server.

                    boolean isDeleted = DatabaseManager.getInstance(mContext).deleteCFIDReadingData(bookid, userid);

                } else {
                    // AnalyticsManager.getInstance(mContext).setOfflinedata(pagetrackdata);
                    //Log.d("savePageTrackingData : ", "failed");
                }

            }

            @Override
            public void requestErrorOccured(ServiceException exeption) {
                ServiceException exceptionObj = exeption;
                if (exceptionObj != null && !exceptionObj.getInvalidFields().isEmpty()) {
                    Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
                    if (PlayerController.getInstance(mContext).isAutoLogOffEnabled() && entry.getValue() == 103) {
                        showSessionExpiredAlert();
                    } else if (entry.getValue() == 103 && (!exeption.getResponseRequestType().equals(SERVICETYPES.REFRESH_USER_TOKEN))) {
                        refreshUserToken(PlayerController.getInstance(mContext).getToken(), exeption.getResponseRequestType().toString(), new IServiceResponseListener() {
                            @Override
                            public void requestCompleted(IServiceResponse iServiceResponse) {
                                RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) iServiceResponse;
                                KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                String token = responseObj.getUserVO().getToken();
                                long userId = responseObj.getUserVO().getUserID();
                                Intent intent = new Intent();
                                intent.putExtra("Action","UPDATE_USER_TOKEN");
                                intent.putExtra("UserID",userId);
                                intent.putExtra("UserToken",token);
                                intent.setAction("DBCONTROLLER_RECIVER");
                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                savePageTrackingData(token, bookid, userid, pagetrackdata);
                            }

                            @Override
                            public void requestErrorOccured(ServiceException e) {
                                // showSessionExpiredAlert();
                            }
                        });
                    } else if (entry.getValue() == 103) {
                        showErrorAlertDialog(entry.getValue());
                    }

                }
                //Log.d("savePageTrackingData : ", "failed");
            }
        });
    }


    public void getSecureUrl(IServiceResponseListener iServiceResponseListener, ArrayList<NameValuePair> params) {
        super.secureUrl(iServiceResponseListener, params, KitabooSDKModel.getInstance().getUserToken());
    }

    public void getVimeoUrl(IServiceResponseListener iServiceResponseListener, ArrayList<NameValuePair> params) {
        super.vimeoUrl(iServiceResponseListener, params, KitabooSDKModel.getInstance().getUserToken());
    }

    public void getAnalytics(long bookid, String classid, String usertoken) {
        super.getAnalytics(bookid, classid, usertoken, new IServiceResponseListener() {
            @Override
            public void requestCompleted(IServiceResponse response) {

            }

            @Override
            public void requestErrorOccured(ServiceException exeption) {

            }
        });
    }

    public void sendBackgroundServiceforUGC(final long bookid, final long userid, String accountType, final String bookVersion, String pagetrackdata, IServiceResponseListener _iServiceResponse) {
        mPagetrackdata = pagetrackdata;
        if (mPagetrackdata != null && !mPagetrackdata.isEmpty()) {
            savePageTrackingData(KitabooSDKModel.getInstance().getUserToken(), bookid, userid, mPagetrackdata);
        }
        sendFetchUGCRequest(bookid, accountType, userid, bookVersion, _iServiceResponse);
    }

    public void sendServicesOnScheduler(final long bookid, final long userid, String accountType, final String bookVersion, String pagetrackdata, IServiceResponseListener _iServiceResponse) {
        mPagetrackdata = pagetrackdata;
        Log.e("sendservice", "schedular");
        int ugcDataLength = DatabaseManager.getInstance(mContext).getUGCData(userid, bookid).length();

        ArrayList<JSONArray>paginatedList= sendBackGroundServiceDataInMBs(userid,bookid);

        if (ugcDataLength > 0) {

            for(int i=0 ; i<= (paginatedList.size()-1); i++){
                sendSaveUGCRequest(bookid, userid, bookVersion,paginatedList.get(i) ,i);
            }

        }
        /*if (ugcDataLength > 0) {
            ArrayList<JSONArray> ugcDataPagenated = DatabaseManager.getInstance(mContext).getUGCDataPagenated(userid, bookid, chunkSize);
            for(int i=0 ; i<= (ugcDataPagenated.size()-1); i++){
                sendSaveUGCRequest(bookid, userid, bookVersion,ugcDataPagenated.get(i),i);
            }
        } */
        else if (mPagetrackdata != null && !mPagetrackdata.isEmpty()) {
            savePageTrackingData(KitabooSDKModel.getInstance().getUserToken(), bookid, userid, mPagetrackdata);
        }
    }

    public void sendBackgroundServiceOnBookClose(final long bookid, final long userid, final String bookVersion, String pagetrackdata) {
        mPagetrackdata = pagetrackdata;

        ArrayList<JSONArray>paginatedList= sendBackGroundServiceDataInMBs(userid,bookid);
        int ugcDataLength = DatabaseManager.getInstance(mContext).getUGCData(userid, bookid).length();

        if (ugcDataLength > 0) {

            for(int i=0 ; i<= (paginatedList.size()-1); i++){
                sendSaveUGCRequest(bookid, userid, bookVersion,paginatedList.get(i) ,i);

            }

        }


       /* ArrayList<JSONArray> ugcDataPagenated = DatabaseManager.getInstance(mContext).getUGCDataPagenated(userid, bookid, chunkSize);
        for(int i=0 ; i<= (ugcDataPagenated.size()-1); i++){
            sendSaveUGCRequest(bookid, userid, bookVersion,ugcDataPagenated.get(i),i);
        }*/
    }

    public void sendSecureUrlForExternallink(LinkVO vo, IServiceResponseListener iServiceResponseListener) {
        super.getSecureUrlForExternallink(vo, KitabooSDKModel.getInstance().getAuthUserToken(), iServiceResponseListener);
    }

    public void getTheamOfUser(IServiceResponseListener iServiceResponseListener) {
        super.getTheamOfUser(KitabooSDKModel.getInstance().getUserToken(), iServiceResponseListener);
    }

    public void fetchBookList(String token, boolean refresh, IServiceResponseListener serviceResponseListener) {
        super.fetchBookList(token, refresh, serviceResponseListener);
    }

    public void getUserBookDownload(long bookid, String token, String bookformat, IServiceResponseListener serviceResponseListener) {
        super.getUserBookDownload(bookid, token, bookformat, serviceResponseListener);
    }

    public void authContentServer(long bookid, String token, IServiceResponseListener serviceResponseListener) {
        super.authContentServer(bookid, token, serviceResponseListener);
    }

    public void tocTimeIndexRequest(String url, IServiceResponseListener serviceResponseListener) {
        super.tocTimeIndexRequest(url, serviceResponseListener);
    }

    public void bookLicenceConsumed(String usertoken, long bookID, String bookFormat, IServiceResponseListener serviceResponseListener) {
        super.bookLicenceConsumed(usertoken, bookID, bookFormat, serviceResponseListener);
    }

    public void bookLicenceRelease(String usertoken, long bookID, String bookFormat, IServiceResponseListener serviceResponseListener) {
        super.BookLicenceRelease(usertoken, bookID, bookFormat, serviceResponseListener);
    }

    @Override
    public void changePasswordOfUser(String userToken, String username, String password, String newpassword, IServiceResponseListener callbacklistner) {
        super.changePasswordOfUser(userToken, username, password, newpassword, callbacklistner);
    }

    @Override
    public void uploadProfilePic(String imageFile, String userToken, String firstname, String lastname, IServiceResponseListener callbacklistner) {
        super.uploadProfilePic(imageFile, userToken, firstname, lastname, callbacklistner);
    }

    @Override
    public void resetPasswordOfUser(String emailid, IServiceResponseListener callbacklistner) {
        super.resetPasswordOfUser(emailid, callbacklistner);
    }

    public void getuserOfflineScormList(String token, long bookID, ScormVO scormVO,
                                        IServiceResponseListener iServiceResponseListener) {
        super.getuserScormList(token, scormVO, iServiceResponseListener);

    }

    @Override
    public void refreshUserToken(String token, String requestType, IServiceResponseListener callbacklistner) {
        super.refreshUserToken(token, requestType, callbacklistner);
    }

    @Override
    public void fetchBookLastVisitedFolio(IServiceResponseListener callbacklistner, String mUserToken, long bookId) {
        super.fetchBookLastVisitedFolio(callbacklistner, mUserToken, bookId);
    }

    public void setServiceCompletedListener(ServiceCompletedListener listener) {
        mServiceCompletedListener = listener;
    }

    public void fetchCollectionCBMIdRequest(long bookid, String token, IServiceResponseListener serviceResponseListener) {
        super.fetchCollectionCBMIdRequest(token,bookid, serviceResponseListener);
    }
}
