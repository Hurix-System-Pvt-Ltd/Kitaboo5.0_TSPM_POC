package com.hurix.reader.kitaboosdkrenderer.sdkRenderer.sdkHelper;

import android.content.Context;
import android.os.AsyncTask;

import com.hurix.epubreader.database.DatabaseManager;
import com.hurix.epubreader.interfaces.UGCDataAsynCompleteListener;
import com.hurix.service.datamodel.UGCFetchResponseObject;
import com.hurix.service.serviceconstant.ServiceConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Amit Raj on 17-07-2019.
 */
public class AddAllOldUgcToDB extends AsyncTask<UgcVo, Void, Boolean> {
    private Context mContext;
    JSONObject mainJSONObj;
    JSONArray ugcUsersList = new JSONArray();
    UgcVo ugcVo;
    int bookCount=0,userCount=0;
    private UGCDataAsynCompleteListener listner;

    @Override
    protected Boolean doInBackground(UgcVo... ugcVos) {
        if(ugcVos[0]!=null && ugcVos[0].getUgcFetchResponseObjects()!=null)
        return DatabaseManager.getInstance(mContext).updateUGCDataFromOldDb(ugcVos[0].getUserID(),ugcVos[0].getUgcFetchResponseObjects(),ugcVos[0].getBookID());
        else
            return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
        if(mainJSONObj!=null && mainJSONObj.has("ugcUserList")){
            try {
                ugcUsersList= (JSONArray) mainJSONObj.get("ugcUserList");
                try {
                    JSONObject object=(JSONObject) ugcUsersList.get(userCount);
                    long userID, bookID;
                    userID= (long) object.get("userId");
                    JSONArray ugcData= (JSONArray) object.get("ugcData");
                    bookCount=bookCount+1;
                    if(ugcData!=null && ugcData.length()>0 && bookCount<ugcData.length()){
                        JSONObject bookObj=new JSONObject(ugcData.get(bookCount).toString());
                        bookID = Long.parseLong(bookObj.get("bookID").toString());
                        ArrayList<UGCFetchResponseObject> ugcFetchResponseObjects = parseOldUGCResponseList(bookObj,userID,bookID);
                        AddAllOldUgcToDB addAllOldUgcToDB =new AddAllOldUgcToDB();
                        addAllOldUgcToDB.setListner(listner);
                        ugcVo=new UgcVo(userID,bookID,ugcFetchResponseObjects);
                        addAllOldUgcToDB.setData(mContext,mainJSONObj,bookCount,userCount);
                        addAllOldUgcToDB.execute(ugcVo);
                    }else {
                        userCount=userCount+1;
                        bookCount=0;
                        if(ugcUsersList!=null && ugcUsersList.length()>0 && userCount<ugcUsersList.length()){
                            try{
                                object=(JSONObject) ugcUsersList.get(userCount);
                                userID= (long) object.get("userId");
                                ugcData= (JSONArray) object.get("ugcData");
                                if(ugcData!=null && ugcData.length()>0 && bookCount<ugcData.length()){
                                   // bookCount=bookCount+1;
                                    JSONObject bookObj=new JSONObject(ugcData.get(bookCount).toString());
                                    bookID = Long.parseLong(bookObj.get("bookID").toString());
                                    ArrayList<UGCFetchResponseObject> ugcFetchResponseObjects = parseOldUGCResponseList(bookObj,userID,bookID);
                                    AddAllOldUgcToDB addAllOldUgcToDB =new AddAllOldUgcToDB();
                                    addAllOldUgcToDB.setListner(listner);
                                    ugcVo=new UgcVo(userID,bookID,ugcFetchResponseObjects);
                                    addAllOldUgcToDB.setData(mContext,mainJSONObj,bookCount,userCount);
                                    addAllOldUgcToDB.execute(ugcVo);
                                }
                            } catch (Exception e){

                            }
                        }else {
                            listner.onTaskCompleted(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setData(Context context,JSONObject _mainJSONObj,int _bookCount,int _userCount)
    {
        mContext=context;
        mainJSONObj=_mainJSONObj;
        bookCount=_bookCount;
        userCount=_userCount;
    }

    private ArrayList<UGCFetchResponseObject> parseOldUGCResponseList(JSONObject response, long userID, long bookID) {
        ArrayList<UGCFetchResponseObject> parsedResponseData = new ArrayList<UGCFetchResponseObject>();

        try {
            JSONArray ugcList = response.getJSONArray("ugcList");
            for (int j = 0; j < ugcList.length(); j++) {
                JSONObject item = ugcList.getJSONObject(j);

                if (item.length() > 0) {
                    UGCFetchResponseObject ugcItem = new UGCFetchResponseObject();
                    ugcItem.setUGCID(item.getString(ServiceConstant.UGC_PARAM_ID));
                    ugcItem.setLocalID(item.has(ServiceConstant.UGC_PARAM_LOCAL_ID) ? item.getString(ServiceConstant.UGC_PARAM_LOCAL_ID) : "");
                    ugcItem.setUGCData(item.has(ServiceConstant.UGC_PARAM_UGC_DATA) ? item.getString(ServiceConstant.UGC_PARAM_UGC_DATA) : "");
                    ugcItem.setType(item.has(ServiceConstant.UGC_PARAM_TYPE) ? item.getString(ServiceConstant.UGC_PARAM_TYPE) : "");
                    ugcItem.setCreatedOn(item.has(ServiceConstant.UGC_PARAM_CREATED_ON) ? item.getString(ServiceConstant.UGC_PARAM_CREATED_ON) : "");
                    ugcItem.setMetaData(item.has(ServiceConstant.UGC_PARAM_META_DATA) ? item.getString(ServiceConstant.UGC_PARAM_META_DATA) : "");
                    ugcItem.setPageID(item.has(ServiceConstant.UGC_PARAM_PAGE_ID) ? item.getString(ServiceConstant.UGC_PARAM_PAGE_ID) : "");
                    ugcItem.setStatus(item.has(ServiceConstant.UGC_PARAM_STATUS) ? item.getString(ServiceConstant.UGC_PARAM_STATUS) : "");
                    ugcItem.setImportant(item.has(ServiceConstant.UGC_PARAM_IMPORTANT) ? item.getString(ServiceConstant.UGC_PARAM_IMPORTANT) : "");
                    ugcItem.setFolioID(item.has(ServiceConstant.UGC_PARAM_FOLIO_ID) ? item.getString(ServiceConstant.UGC_PARAM_FOLIO_ID) : "");
                    if (ugcItem.getType().equalsIgnoreCase("3") || ugcItem.getType().equalsIgnoreCase("5")) {
                        ugcItem.setsubmited(item.has("submitted") ? item.getBoolean("submitted") : false);
                    }
                    ugcItem.setTimestamp(item.has(ServiceConstant.LAST_SYNCED_ON) ? item.getString(ServiceConstant.LAST_SYNCED_ON) : "");
                    ugcItem.setMcreatedBy( item.has("createdBy") ? item.getString("createdBy") : "");
                    ugcItem.setSynced(item.has("isSynced") ? item.getBoolean("isSynced") : false);
                    ugcItem.setSharedWith( item.has("sharedWith") ? item.getString("sharedWith") : "");
                    parsedResponseData.add(ugcItem);
                }
            }
            //  }
        } catch (JSONException e) {
            parsedResponseData = null;
            e.printStackTrace();

        } catch (Exception e) {
            parsedResponseData = null;
            e.printStackTrace();
        }

        return parsedResponseData;
    }

    public void setListner(UGCDataAsynCompleteListener mListner) {
        listner=mListner;
    }
}
