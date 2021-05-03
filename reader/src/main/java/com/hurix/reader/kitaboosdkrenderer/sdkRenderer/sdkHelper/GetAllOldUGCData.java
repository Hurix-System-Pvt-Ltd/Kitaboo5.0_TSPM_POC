//package com.hurix.reader.kitaboosdkrenderer.sdkRenderer.sdkHelper;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import com.hurix.commons.datamodel.IBook;
//
//import com.hurix.database.dbfactory.DBController;
//import com.hurix.epubreader.database.DatabaseManager;
//import com.hurix.epubreader.interfaces.UGCDataAsynCompleteListener;
//
//import com.hurix.reader.kitaboosdkrenderer.constants.Constants;
//import com.hurix.service.datamodel.UGCFetchResponseObject;
//import com.hurix.service.datamodel.UserBookVO;
//import com.hurix.service.serviceconstant.ServiceConstant;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// * Created by Amit Raj on 11-07-2019.
// */
//public class GetAllOldUGCData extends AsyncTask<Long, Void, String> {
//
//    private UGCDataAsynCompleteListener mListner;
//    private Context mContext;
//    private long userID, bookID;
//    private String bookVersion;
//    private HashMap<Long ,ArrayList<IBook>> userBasedBookId=new HashMap<>();
//    int count=0,bookCount;
//    private GetAllOldUGCData userdataListner;
//    private ArrayList<IBook> users;
//    ArrayList<IBook>  bookIds = null;
//    JSONObject mainJSONObj = new JSONObject();
//    JSONArray ugcUsersIDBased = new JSONArray();
//    JSONArray nosUsersID = new JSONArray();
//    private int indexCount=0,userCount=0;
//    JSONArray ugcUsersList = new JSONArray();
//
//    public GetAllOldUGCData(Context mContext) {
//        this.mContext = mContext;
//    }
//
//    public void setUGCData(long _userID, long _bookID, ArrayList<IBook>  _bookIds, int _count, JSONArray _nosUsersID, int _userCount, ArrayList<IBook> _users, HashMap<Long ,ArrayList<IBook>> _userBasedBookId, JSONArray _ugcUsersIDBased ) {
//        userID = _userID;
//        userdataListner=this;
//        bookID=_bookID;
//        bookIds=_bookIds;
//        bookCount=_count;
//        nosUsersID=_nosUsersID;
//        userCount=_userCount;
//        users=_users;
//        userBasedBookId=_userBasedBookId;
//        ugcUsersIDBased=_ugcUsersIDBased;
//    }
//
//    public void setTaskCompleteListner(UGCDataAsynCompleteListener listner) {
//        this.mListner = listner;
//    }
//
//    @Override
//    protected String doInBackground(Long... longs) {
//
//        return addAllUGCDataToDatabse(userID,bookID);
//    }
//
//
//    @Override
//    protected void onPostExecute(String result) {
//        if(result!=null && !result.isEmpty()){
//
//            nosUsersID.put(result);
//        }
//        super.onPostExecute(result);
//        bookCount=this.bookCount+1;
//        if(bookIds!=null && bookIds.size()>0 && bookCount < bookIds.size()){
//
//            GetAllOldUGCData getAllUGCData = new GetAllOldUGCData(mContext);
//            getAllUGCData.setTaskCompleteListner(mListner);
//            getAllUGCData.setUGCData(userID,((UserBookVO) bookIds.get(bookCount)).getBookID(),bookIds,bookCount,nosUsersID,userCount,users,userBasedBookId,ugcUsersIDBased);
//            getAllUGCData.execute();
//        }else {
//            if(nosUsersID!=null){
//                try {
//                    JSONObject userUgcData = new JSONObject();
//                    userUgcData.put("userId",userID);
//                    userUgcData.put("ugcData",nosUsersID);
//                    ugcUsersIDBased.put(userCount,userUgcData);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            userCount=userCount+1;
//            if(userCount<users.size() && users.size()>0){
//                bookCount=0;
//                nosUsersID=new JSONArray();
//                bookIds = userBasedBookId.get(((UserBookVO) users.get(userCount)).getuserId());
//                GetAllOldUGCData getAllUGCData = new GetAllOldUGCData(mContext);
//                getAllUGCData.setTaskCompleteListner(mListner);
//                getAllUGCData.setUGCData(((UserBookVO) users.get(userCount)).getuserId(),((UserBookVO) bookIds.get(bookCount)).getBookID(),bookIds,bookCount,nosUsersID,userCount,users,userBasedBookId,ugcUsersIDBased);
//                getAllUGCData.execute();
//            }else {
//                if(userCount==users.size()) {
//                    try {
//                        mainJSONObj.put("ugcUserList", ugcUsersIDBased);
//
//                        if( mainJSONObj.has("ugcUserList")){
//                            int bookCount=0;
//                            try {
//                                ugcUsersList= (JSONArray) mainJSONObj.get("ugcUserList");
//                                try {
//                                    JSONObject object=(JSONObject) ugcUsersList.get(0);
//                                    long userID, bookID;
//                                    userID= (long) object.get("userId");
//                                    JSONArray ugcData= (JSONArray) object.get("ugcData");
//                                    if(ugcData!=null && ugcData.length()>0){
//                                        JSONObject bookObj=new JSONObject(ugcData.get(bookCount).toString());
//                                        bookID = Long.parseLong(bookObj.get("bookID").toString());
//                                        ArrayList<UGCFetchResponseObject> ugcFetchResponseObjects = parseOldUGCResponseList(bookObj,userID,bookID);
//                                        AddAllOldUgcToDB addAllOldUgcToDB =new AddAllOldUgcToDB();
//                                        addAllOldUgcToDB.setListner(mListner);
//                                        UgcVo ugcVo=new UgcVo(userID,bookID,ugcFetchResponseObjects);
//                                        addAllOldUgcToDB.setData(mContext,mainJSONObj,0,0);
//                                        addAllOldUgcToDB.execute(ugcVo);
//                                    }else {
//                                        AddAllOldUgcToDB addAllOldUgcToDB =new AddAllOldUgcToDB();
//                                        addAllOldUgcToDB.setListner(mListner);
//                                        UgcVo ugcVo=new UgcVo(userID,0,null);
//                                        addAllOldUgcToDB.setData(mContext,mainJSONObj,0,0);
//                                        addAllOldUgcToDB.execute(ugcVo);
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }
//        /*if (mListner != null) {
//            mListner.onTaskCompleted(result);
//        }*/
//    }
//
//    public String addAllUGCDataToDatabse(long userID,long bookID){
//        String jsonData = null;
//        try {
//            if(((UserBookVO) bookIds.get(bookCount)).getBookType().toString().equalsIgnoreCase("EPUB") ||((UserBookVO) bookIds.get(bookCount)).getBookType().toString().equalsIgnoreCase("FIXED_EPUB_IMAGE")){
//                jsonData =getJSONUGCSyncStringEpub(userID,bookID);
//            }else {
//                jsonData = getJSONUGCSyncString(userID,bookID);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return jsonData;
//    }
//
//    public String getJSONUGCSyncString(long userID,long bookID) throws JSONException {
//        JSONArray ugcData = new JSONArray();
//
//        JSONObject ugcList = new JSONObject();
//        JSONArray bookmarks = new JSONArray();
//        JSONArray highlightAndNotes = new JSONArray();
//        //if (!checkSubmitted) {
//        bookmarks = DBController.getInstance(mContext).getManager().getAllBookmarksForUGCN(
//                mContext, userID,bookID);
//        highlightAndNotes = DBController.getInstance(mContext).getManager().getAllHighlightForUGCN(mContext, userID,bookID);
//        //}
//        JSONArray penToolData = DBController.getInstance(mContext).getManager().getAllPentoolDataForUGCN(mContext, userID,bookID);
//        JSONArray penToolProtractorData = DBController.getInstance(mContext).getManager().getAllProtractorDataForUGCN(
//                mContext, userID,bookID);
//
//        JSONArray fftData = DBController.getInstance(mContext).getManager().getAllFFTDataForUGCN(mContext, userID,bookID);
//
//        if (bookmarks.length() > 0 || highlightAndNotes.length() > 0 || penToolData.length() > 0
//                || fftData.length() > 0 || penToolProtractorData.length() >0) {
//            indexCount=indexCount+1;
//            JSONArray concatenatedArray = new JSONArray();
//            try {
//                concatenatedArray = concatArray(bookmarks, highlightAndNotes, penToolData, fftData,penToolProtractorData);
//            } catch (Exception ex) {
//                throw new JSONException("JSON EXCEPTION");
//            }
//
//            ugcList.put(Constants.UGC_PARAM_BOOK_ID, bookID + "");
//
//            ugcList.put(Constants.UGC_PARAM_LIST, concatenatedArray);
//
//            return ugcList.toString();
//        }
//        return null;
//    }
//
//    public String getJSONUGCSyncStringEpub(long userID,long bookID) throws JSONException {
//        JSONObject mainJSONObj = new JSONObject();
//        JSONArray ugcData = new JSONArray();
//
//        JSONObject ugcList = new JSONObject();
//        JSONArray bookmarks = new JSONArray();
//        JSONArray highlightAndNotes = new JSONArray();
//        bookmarks = DatabaseManager.getInstance(mContext).getAllBookmarksForUGC(userID,bookID);
//        highlightAndNotes = DatabaseManager.getInstance(mContext).getAllHighlightForUGC
//                (userID, bookID);
//
//        if (bookmarks.length() > 0 || highlightAndNotes.length() > 0) {
//            JSONArray concatenatedArray = new JSONArray();
//            try {
//                concatenatedArray = concatArray(bookmarks, highlightAndNotes);
//            } catch (Exception ex) {
//                throw new JSONException("JSON EXCEPTION");
//            }
//
//            ugcList.put(Constants.UGC_PARAM_BOOK_ID, bookID + "");
//            ugcList.put(Constants.UGC_PARAM_LIST, concatenatedArray);
//            return ugcList.toString();
//        }
//        return null;
//    }
//
//    private JSONArray concatArray(JSONArray... arrs) throws JSONException {
//        JSONArray result = new JSONArray();
//        for (JSONArray arr : arrs) {
//            for (int i = 0; i < arr.length(); i++) {
//                result.put(arr.get(i));
//            }
//        }
//        return result;
//    }
//
//    public void createUserBookId(){
//        users= DBController.getInstance(mContext).getManager().collectAlluserIDForUGC();
//        if(users!=null && users.size()>0) {
//            userID=((UserBookVO) users.get(count)).getuserId();
//            new CollectUserBookId(userID).execute();
//        }else {
//           if(mListner!=null)
//               mListner.onTaskCompleted(false);
//        }
//
//    }
//
//
//    public class CollectUserBookId extends AsyncTask<Integer,Void,ArrayList<IBook>>{
//        private long userID;
//
//        public CollectUserBookId(long userID) {
//            this.userID = userID;
//        }
//
//        @Override
//        protected ArrayList<IBook> doInBackground(Integer... Integer) {
//            return DBController.getInstance(mContext).getManager().collectAllBookIdForOldUgc(userID);
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<IBook> iBooks) {
//            userBasedBookId.put(userID,iBooks);
//            count=count+1;
//            if(users!=null && users.size()>0 && count < users.size()){
//                userID=((UserBookVO) users.get(count)).getuserId();
//                new CollectUserBookId(userID).execute();
//            }else {
//                count=0;
//                bookIds = userBasedBookId.get(((UserBookVO) users.get(count)).getuserId());
//                if(users!=null && users.size()>0 && userBasedBookId.size()==users.size() && bookIds.size()>0){
//                    GetAllOldUGCData getAllUGCData = new GetAllOldUGCData(mContext);
//                    getAllUGCData.setTaskCompleteListner(mListner);
//                    userID=((UserBookVO) users.get(count)).getuserId();
//                    ArrayList<IBook>  bookIds =  userBasedBookId.get(((UserBookVO) users.get(count)).getuserId());
//                    getAllUGCData.setUGCData(((UserBookVO) users.get(count)).getuserId(),((UserBookVO) bookIds.get(count)).getBookID(),bookIds,count,nosUsersID,userCount,users,userBasedBookId,ugcUsersIDBased);
//                    getAllUGCData.execute();
//                }else if(bookIds!=null && bookIds.size()==0){
//                    if(mListner!=null)
//                        mListner.onTaskCompleted(false);
//                }else if(bookIds!=null) {
//                    if(mListner!=null)
//                        mListner.onTaskCompleted(false);
//                }
//            }
//            super.onPostExecute(iBooks);
//        }
//    }
//
//   /* public interface UserUGCDataCompleteListener
//    {
//        void collectUserBookID(ArrayList<IBook> iBooks);
//    }*/
//
//    private ArrayList<UGCFetchResponseObject> parseOldUGCResponseList(JSONObject response, long userID, long bookID) {
//        ArrayList<UGCFetchResponseObject> parsedResponseData = new ArrayList<UGCFetchResponseObject>();
//
//        try {
//
//            JSONArray ugcList = response.getJSONArray("ugcList");
//            for (int j = 0; j < ugcList.length(); j++) {
//                JSONObject item = ugcList.getJSONObject(j);
//
//                if (item.length() > 0) {
//                    UGCFetchResponseObject ugcItem = new UGCFetchResponseObject();
//                    ugcItem.setUGCID(item.getString(ServiceConstant.UGC_PARAM_ID));
//                    ugcItem.setLocalID(item.has(ServiceConstant.UGC_PARAM_LOCAL_ID) ? item.getString(ServiceConstant.UGC_PARAM_LOCAL_ID) : "");
//                    ugcItem.setUGCData(item.has(ServiceConstant.UGC_PARAM_UGC_DATA) ? item.getString(ServiceConstant.UGC_PARAM_UGC_DATA) : "");
//                    ugcItem.setType(item.has(ServiceConstant.UGC_PARAM_TYPE) ? item.getString(ServiceConstant.UGC_PARAM_TYPE) : "");
//                    ugcItem.setCreatedOn(item.has(ServiceConstant.UGC_PARAM_CREATED_ON) ? item.getString(ServiceConstant.UGC_PARAM_CREATED_ON) : "");
//                    ugcItem.setMetaData(item.has(ServiceConstant.UGC_PARAM_META_DATA) ? item.getString(ServiceConstant.UGC_PARAM_META_DATA) : "");
//                    ugcItem.setPageID(item.has(ServiceConstant.UGC_PARAM_PAGE_ID) ? item.getString(ServiceConstant.UGC_PARAM_PAGE_ID) : "");
//                    ugcItem.setStatus(item.has(ServiceConstant.UGC_PARAM_STATUS) ? item.getString(ServiceConstant.UGC_PARAM_STATUS) : "");
//                    ugcItem.setImportant(item.has(ServiceConstant.UGC_PARAM_IMPORTANT) ? item.getString(ServiceConstant.UGC_PARAM_IMPORTANT) : "");
//                    ugcItem.setFolioID(item.has(ServiceConstant.UGC_PARAM_FOLIO_ID) ? item.getString(ServiceConstant.UGC_PARAM_FOLIO_ID) : "");
//                    if (ugcItem.getType().equalsIgnoreCase("3") || ugcItem.getType().equalsIgnoreCase("5")) {
//                        ugcItem.setsubmited(item.has("submitted") ? item.getBoolean("submitted") : false);
//                    }
//                    ugcItem.setTimestamp(item.has(ServiceConstant.LAST_SYNCED_ON) ? item.getString(ServiceConstant.LAST_SYNCED_ON) :"");
//                    ugcItem.setMcreatedBy( item.has("createdBy") ? item.getString("createdBy") : "");
//                    ugcItem.setSynced(item.has("isSynced") ? item.getBoolean("isSynced") : false);
//                    ugcItem.setSharedWith( item.has("sharedWith") ? item.getString("sharedWith") : "");
//                    parsedResponseData.add(ugcItem);
//                }
//            }
//            //  }
//        } catch (JSONException e) {
//            parsedResponseData = null;
//            e.printStackTrace();
//
//        } catch (Exception e) {
//            parsedResponseData = null;
//            e.printStackTrace();
//        }
//
//        return parsedResponseData;
//    }
//}
