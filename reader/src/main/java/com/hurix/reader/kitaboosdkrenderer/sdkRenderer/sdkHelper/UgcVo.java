package com.hurix.reader.kitaboosdkrenderer.sdkRenderer.sdkHelper;

import com.hurix.service.datamodel.UGCFetchResponseObject;

import java.util.ArrayList;

/**
 * Created by Amit Raj on 17-07-2019.
 */
public class UgcVo {
    private long userID, bookID;
    private ArrayList<UGCFetchResponseObject> ugcFetchResponseObjects;

    public UgcVo(long userID, long bookID, ArrayList<UGCFetchResponseObject> ugcFetchResponseObjects) {
        this.userID = userID;
        this.bookID = bookID;
        this.ugcFetchResponseObjects = ugcFetchResponseObjects;
    }

    public long getUserID() {
        return userID;
    }

    public long getBookID() {
        return bookID;
    }

    public ArrayList<UGCFetchResponseObject> getUgcFetchResponseObjects() {
        return ugcFetchResponseObjects;
    }
}
