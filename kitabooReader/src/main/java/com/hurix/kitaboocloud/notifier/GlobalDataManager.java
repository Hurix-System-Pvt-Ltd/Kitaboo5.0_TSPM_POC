package com.hurix.kitaboocloud.notifier;

import android.content.Context;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Pair;
import android.view.View;

import androidx.fragment.app.Fragment;
import com.hurix.kitaboo.constants.Constants;
import com.hurix.kitaboocloud.StudentListFragment;

import com.hurix.kitaboocloud.common.YoutubeChangeListener;

import com.hurix.kitaboocloud.interfaces.IDestroyable;


import org.json.JSONArray;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class GlobalDataManager implements IDestroyable {

    private View isViewSet;
    private JSONArray penColors;
    private HashMap<String, Fragment> mStoreFragmentInstance;
    private HashMap<String, ArrayList<com.hurix.commons.datamodel.UserPageVO>> ugcListPerUserIdMap;
    private HashMap<String, Fragment> mStoreTeacherReviewFragmentInstance;
    private ArrayList<String> mSelectedChapterTitles;
    public boolean isLMS = false;
    public  boolean isMobile=false;
    private int languageSelectedPosition;
    private String selectedLanguage;
    private boolean isBookOpen;
    private boolean mWebViewClosedAfterTokenReceived;

    public void setCurrentSelectedBookLaunch(boolean isBookOpen) {
        this.isBookOpen = isBookOpen;
    }


    public Boolean isCurrentSelectedBookLaunched() {
        return isBookOpen;
    }

    public enum PlayerState {
        NAVIGATION, HIGHLIGHT, PEN_ENABLE, BOOKMARK, ASSESSMENT, PEN_DISABLE, STICKYNOTE, SEARCH, PROTRACTOR
    }

    public static GlobalDataManager mInstance;
    private boolean mIsFlingEnabled = true;
    private boolean mIsReviewMode = false;
    private boolean mProtectoeDrawRefresh;
    private int mHighlightType;
    private long mTocSelectLinkId = 0;
    private boolean mAutoPlay = false;
    private boolean mIsZoomInProgress;
    private PlayerState mState;
    /*private PDFCore mCore;
    private PDFPageView mPDFPageView;*/
    private float mCurrScale = 1.0F;
    private float mSourceScale = 1.0F;
    private LinkedList<Integer> mHistoryStack;
    private int mHistoryStackPosition;
    private String mPenColor = Constants.PENTOOL_COLOR_DEFAULT;
    private int mMediumPenSize = Constants.PENTOOL_SIZE_MEDIUM;
    private int mPenSize = Constants.PENTOOL_SIZE_SMALL;
    private boolean mPenDeleteMode;
    private Pair<Long, RectF> mCurrAudioSyncRect;
    private boolean mIsAutoFling = false;
    private String mSearchText;
    private boolean mIsAnyPopupVisible = false;
    private boolean mIsAnyClickClicked = false;
    private int mScreenOrientation;
    private YoutubeChangeListener mYoutubeChangeListener;
    public int i = 0;
    private boolean isHistoryNavigationRecordRequired = true;
    /* Required for the permission of Alert window in Android M.*/
    private boolean isConfigChanges = false;
    private boolean isUndoClicked = false;
    private boolean mIsBookClicked;
    private boolean mPreviewCllicked;
    private int count;
    private boolean mProtractorAvailable;
    private String protractorPageId = "";
    public static String CUSTOME_URL_TOKEN = "";
    private boolean ltifirstlaunch;
    private boolean isSessionExpired;
    private boolean firstshareenable = false;
    private boolean firstsharetrue;



    private GlobalDataManager() {
        mState = PlayerState.NAVIGATION;
        mHistoryStack = new LinkedList<Integer>();
        mStoreFragmentInstance = new HashMap<>();
        ugcListPerUserIdMap = new HashMap<>();
        mStoreTeacherReviewFragmentInstance = new HashMap<>();


    }


    public static GlobalDataManager getInstance() {
        if (mInstance == null) {
            mInstance = new GlobalDataManager();
        }
        return mInstance;

    }


    public void setTocSelectLinkId(long tocSelectLinkId) {
        this.mTocSelectLinkId = tocSelectLinkId;
    }



    @Override
    public void destroy() {

        //mBookData.destroy();
        // mCore = null;
        //  mBookData = null;
        mHistoryStack = null;
        mInstance = null;

    }


    public void setWebViewClosedAfterTokenReceived(boolean mWebViewClosedAfterTokenReceived) {
        this.mWebViewClosedAfterTokenReceived = mWebViewClosedAfterTokenReceived;
    }

    public void addTeacherReviewFragmentInstance(String classId, StudentListFragment studentListFragment) {
        mStoreTeacherReviewFragmentInstance.put(classId, studentListFragment);
    }

    public StudentListFragment getTeacherReviewFragmentInstance(String classId) {
        return (StudentListFragment) mStoreTeacherReviewFragmentInstance.get(classId);
    }

    public void clearTeacherReviewFragmentHashMap() {
        this.mStoreTeacherReviewFragmentInstance.clear();
    }

    public ArrayList<com.hurix.commons.datamodel.UserPageVO> getUgcListPerUserIdMap(String studentId) {
        return ugcListPerUserIdMap.get(studentId);
    }

    public void setUgcListPerUserIdMap(String studentId, ArrayList<com.hurix.commons.datamodel.UserPageVO> ugcDataArrayList) {
        if (ugcListPerUserIdMap.containsKey(studentId)) {
            ugcListPerUserIdMap.remove(studentId);
        }
        this.ugcListPerUserIdMap.put(studentId, ugcDataArrayList);
    }

    public void clearUgcListHashMap() {
        this.ugcListPerUserIdMap.clear();
    }



    public ArrayList<String> getSelectedChapterTitles() {
        return mSelectedChapterTitles;
    }

    public void setSelectedChapterTitles(ArrayList<String> mSelectedChapterTitles) {
        this.mSelectedChapterTitles = mSelectedChapterTitles;
    }


}
