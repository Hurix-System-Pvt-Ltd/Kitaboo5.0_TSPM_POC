package com.hurix.reader.kitaboosdkrenderer.sdkRenderer.sdkHelper;

import android.content.Context;
import android.os.AsyncTask;

import com.hurix.epubreader.database.DatabaseManager;
import com.hurix.epubreader.interfaces.UGCDataAsynCompleteListener;

import java.util.ArrayList;

/**
 * Created by Amit Raj on 22-05-2019.
 */
public class GetUGCDataAsync extends AsyncTask<String, Void, ArrayList<com.hurix.customui.datamodel.HighlightVO>> {
    private UGCDataAsynCompleteListener mListnerMydata,mListner;
    private Context mContext;
    private long userID, bookID;

    public GetUGCDataAsync(Context mContext) {
        this.mContext = mContext;
    }

    public void setUGCData(long _userID, long _bookID) {
        userID = _userID;
        bookID = _bookID;
    }

    public void settaskCompleteListner(UGCDataAsynCompleteListener listner) {
        this.mListner = listner;
    }
    public void settaskMydataCompleteListner(UGCDataAsynCompleteListener listner) {
        this.mListnerMydata = listner;
    }

    @Override
    protected ArrayList<com.hurix.customui.datamodel.HighlightVO> doInBackground(String... params) {
        ArrayList<com.hurix.customui.datamodel.HighlightVO> list = DatabaseManager.getInstance(mContext).getHighlight(userID, bookID);

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<com.hurix.customui.datamodel.HighlightVO> result) {
        super.onPostExecute(result);

        if (mListner != null) {
            mListner.onHighlightTaskCompleted(result);
        }else {
            mListnerMydata.onTaskCompletedMydata(result);
        }
    }
}
