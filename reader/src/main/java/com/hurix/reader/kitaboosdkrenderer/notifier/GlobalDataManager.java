package com.hurix.reader.kitaboosdkrenderer.notifier;

import android.content.Context;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Pair;
import android.view.View;
import android.webkit.DownloadListener;

import androidx.fragment.app.Fragment;

import com.hurix.commons.Constants.EBookType;
import com.hurix.commons.datamodel.IBook;
import com.hurix.commons.datamodel.IDownloadable;
import com.hurix.commons.datamodel.LinkVO;
import com.hurix.commons.datamodel.UserPageVO;
import com.hurix.commons.interfaces.IDestroyable;
import com.hurix.commons.listener.PlayerStateChangedListener;
import com.hurix.commons.listener.RefreshListener;
import com.hurix.commons.listener.YoutubeChangeListener;
import com.hurix.customui.datamodel.PentoolVO;


import com.hurix.reader.kitaboosdkrenderer.ThemeParser.ThemeParent;
//import com.hurix.reader.kitaboosdkrenderer.analytics.AddAnalyticsStudentSelectCallback;
//import com.hurix.reader.kitaboosdkrenderer.analytics.AddOnBottomDialogDismissCall;

import com.hurix.reader.kitaboosdkrenderer.constants.Constants;
import com.hurix.reader.kitaboosdkrenderer.views.StudentListFragment;
import com.hurix.service.datamodel.UserBookVO;


import org.json.JSONArray;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class GlobalDataManager implements IDestroyable {

    private View isViewSet;
    private ArrayList<PentoolVO> penCollections;
    //private int count;

    private JSONArray penColors;

    private HashMap<String, Fragment> mStoreFragmentInstance;
    private int mPosition;
    private HashMap<Long, Boolean> mDownloadingStatus;
    private boolean searchItemClicked;
    ThemeParent theme;
    //private AddAnalyticsStudentSelectCallback mAnalyticListener;
    private long currentActivatedAnalyticUserId;
    private String currentActivatedAnalyticClassid = "";
    private String currentUserName = "";
    //    private AddOnBottomDialogDismissCall dismissListener;
    private boolean mWebViewClosedAfterTokenReceived;
    private boolean mExploreCategory;
    private HashMap<String, ArrayList<com.hurix.commons.datamodel.UserPageVO>> ugcListPerUserIdMap;
    private HashMap<String, Fragment> mStoreTeacherReviewFragmentInstance;
    private boolean mSignedOutFromApp;
    private String timeInterval;
    private boolean noCategoriesFound;
    private static ArrayList<IDownloadable> downloadingList;
    //   private BookCollectionBookView bookViewObj;
    private boolean isBookOpen;
    private String mCategoryName = "";
    private ArrayList<IBook> mUserCategoryBooks;
    private Context context;
    private boolean isDownloadAllclicked;
    private boolean isFetchAllBookRequired;
    private boolean isFetchAllCategoryAfterUpdateRequired;

    private ArrayList<String> mSelectedChapterTitles;

    public String lmsCustomBookID = "";
    public boolean isLMS = false;

    public  boolean isMobile=false;



    public void setLMS(boolean LMS) {
        this.isLMS = LMS;
    }

    public boolean isLMS() {

        return isLMS;
    }


    public void setIsMobile(boolean isMobile) {
        this.isMobile = isMobile;
    }

    public boolean isMobile() {

        return isMobile;
    }

    public void setLmsCustomBookID(String customBookID) {

        this.lmsCustomBookID = customBookID;
    }

    public String getLmsCustomBookID() {
        return lmsCustomBookID;
    }

    public void setLanguageSelectedPosition(int languageSelectedPosition) {
        this.languageSelectedPosition = languageSelectedPosition;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public int getLanguageSelectedPosition() {
        return languageSelectedPosition;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    private int languageSelectedPosition;
    private String selectedLanguage;

    public static ArrayList<IDownloadable> getDownloadingList() {
        return downloadingList;
    }

    /*

     private ArrayList<UserCategoryVO> categoryBookCollection;
    private HashMap<String, ArrayList<UserCategoryVO>> booklistPerCategoryMap;

    public void setCategoryBookCollection(ArrayList<UserCategoryVO> categoryBookCollection) {
        this.categoryBookCollection = categoryBookCollection;
    }

    public ArrayList<UserCategoryVO> getCategoryBookCollection() {
        return this.categoryBookCollection;
    }

    public void setBooklistPerCategoryMap(HashMap<String, ArrayList<UserCategoryVO>> booklistPerCategoryMap) {
        this.booklistPerCategoryMap = booklistPerCategoryMap;
    }

    public HashMap<String, ArrayList<UserCategoryVO>> getBooklistPerCategoryMap() {
        return this.booklistPerCategoryMap;
    }*/


    public void setTheme(ThemeParent theme) {
        this.theme = theme;
    }

    public ThemeParent getTheme() {
        return theme;
    }

//    public void setDismissListener(AddOnBottomDialogDismissCall dismissListener) {
//        this.dismissListener = dismissListener;
//    }
//
//    public void callDismissDialog() {
//        dismissListener.onDismissBottomDialog();
//    }

//    public void setbookViewObj(BookCollectionBookView bookViewObj) {
//        this.bookViewObj = bookViewObj;
//    }

//    public BookCollectionBookView getBookViewObj() {
//        return bookViewObj;
//    }

    public void setCurrentSelectedBookLaunch(boolean isBookOpen) {
        this.isBookOpen = isBookOpen;
    }

    public Boolean isCurrentSelectedBookLaunched() {
        return isBookOpen;
    }

//    public void setContext(BookShelfActivity context) {
//        this.context = context;
//    }
//
//    public BookShelfActivity getContext() {
//        return (BookShelfActivity) context;
//    }

    public void setCurrentDownloadingBook(UserBookVO mBookDao) {
        bookDao = mBookDao;
    }

    public UserBookVO getBookDao() {
        return bookDao;
    }

    public void setDownloadAllClicked(boolean b) {
        isDownloadAllclicked = b;
    }

    public boolean isDownloadAllclicked() {
        return isDownloadAllclicked;
    }

    public enum PlayerState {
        NAVIGATION, HIGHLIGHT, PEN_ENABLE, BOOKMARK, ASSESSMENT, PEN_DISABLE, STICKYNOTE, SEARCH, PROTRACTOR
    }

    public static GlobalDataManager mInstance;
    private boolean mIsFlingEnabled = true;
    private boolean mIsReviewMode = false;

    public boolean ismProtectoeDrawRefresh() {
        return mProtectoeDrawRefresh;
    }

    public void setmProtectoeDrawRefresh(boolean mProtectoeDrawRefresh) {
        this.mProtectoeDrawRefresh = mProtectoeDrawRefresh;
    }

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
    private int mContainerWidth;
    private int mContainerHeight;
    private EBookType mBookType = EBookType.DEFAULT;
    // private LocalBookVO mBookData;
    private LinkedList<Integer> mHistoryStack;
    private int mHistoryStackPosition;
    private String mPenColor = Constants.PENTOOL_COLOR_DEFAULT;
    private int mMediumPenSize = Constants.PENTOOL_SIZE_MEDIUM;
    private int mPenSize = Constants.PENTOOL_SIZE_SMALL;
    private boolean mPenDeleteMode;
    private Pair<Long, RectF> mCurrAudioSyncRect;
    private ArrayList<PentoolVO> mSelectedPenPathVos;
    private ArrayList<SoftReference<PlayerStateChangedListener>> mPlayerStateListenerColl;
    private ArrayList<UserPageVO> mAssessmentpages;
    private LinkedList<SoftReference<RefreshListener>> mRefreshListenerColl;
    private boolean mIsAutoFling = false;
    private String mSearchText;
    private boolean mIsAnyPopupVisible = false;
    private boolean mIsAnyClickClicked = false;
    private int mScreenOrientation;
    private YoutubeChangeListener mYoutubeChangeListener;
    public int i = 0;
    private boolean isHistoryNavigationRecordRequired = true;
    /* Required for the permission of Alert window in Android M.*/
    private LinkVO mLastClickedLinkVo;
    private boolean isConfigChanges = false;
    private boolean isUndoClicked = false;
    private boolean mIsBookClicked;
    private boolean mPreviewCllicked;
    private int count;
    private boolean mProtractorAvailable;
    private UserBookVO bookDao;
    private HashMap<Long, UserBookVO> collectionbookdownloading;
    /*private ArrayList<UserCategoryVO> mCateList;

    public ArrayList<UserCategoryVO> getCateList() {
        if (mCateList == null) {
            mCateList = new ArrayList<>();
        }
        return mCateList;
    }
     */

    public void updateFavorite(Context mContext, IBook bookID, String isFavorite) {

        /*for (UserCategoryVO userCategoryVO : GlobalDataManager.getInstance().getCateList()) {
            for (UserBookVO userBookVO : userCategoryVO.getCategoryBooks()) {
                if(userBookVO.getBookID()==bookID.getBookID()){
                    if (isFavorite.equalsIgnoreCase("0")) {
                        userBookVO.setFavourite("1");
                    } else {
                        userBookVO.setFavourite("0");
                    }
                }
            }

        }*/
    }

    public boolean ismBookDescriptionScrolling() {
        return mBookDescriptionScrolling;
    }

    public void setmBookDescriptionScrolling(boolean mBookDescriptionScrolling) {
        this.mBookDescriptionScrolling = mBookDescriptionScrolling;
    }

    private boolean mBookDescriptionScrolling;

    public Point getmTouchedPoint() {
        return mTouchedPoint;
    }

    public void setmTouchedPoint(Point mTouchedPoint) {
        this.mTouchedPoint = mTouchedPoint;
    }

    private Point mTouchedPoint;

    public boolean ismProtractorAreaAvailable() {
        return mProtractorAreaAvailable;
    }

    public void setmProtractorAreaAvailable(boolean mProtractorAreaAvailable) {
        this.mProtractorAreaAvailable = mProtractorAreaAvailable;
    }

    private boolean mProtractorAreaAvailable;

    public boolean ismProtractorAvailable() {
        return mProtractorAvailable;
    }

    public void setmProtractorAvailable(boolean mProtractorAvailable) {
        this.mProtractorAvailable = mProtractorAvailable;
    }

    public HashMap<Long, Boolean> getDownloadingStatus() {
        if (mDownloadingStatus == null) {
            mDownloadingStatus = new HashMap<>();
        }
        return mDownloadingStatus;
    }


    public float getScalex() {
        return scalex;
    }

    public void setScalex(float scalex) {
        this.scalex = scalex;
    }

    float scalex;

    public float getScaley() {
        return scaley;
    }

    public void setScaley(float scaley) {
        this.scaley = scaley;
    }

    float scaley;
    private String protractorPageId = "";
    private PentoolVO protractorPathVO;
    public static String CUSTOME_URL_TOKEN = "";

    public ArrayList<PentoolVO> getCount() {
        return pentoolCount;
    }

    public void setCount(ArrayList<PentoolVO> count) {
        this.pentoolCount = count;
    }

    private ArrayList<PentoolVO> pentoolCount;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    private int counter;

    private boolean ltifirstlaunch;

    public boolean isLtifirstlaunch() {
        return ltifirstlaunch;
    }

    public void setLtifirstlaunch(boolean ltifirstlaunch) {
        this.ltifirstlaunch = ltifirstlaunch;
    }

    public static String getCustomeUrlToken() {
        return CUSTOME_URL_TOKEN;
    }

    public boolean isSessionExpired() {
        return isSessionExpired;
    }

    public void setSessionExpired(boolean sessionExpired) {
        isSessionExpired = sessionExpired;
    }

    private boolean isSessionExpired;
    private DownloadListener downloadListener;
    private boolean firstshareenable = false;
    private boolean firstsharetrue;

    public boolean isFirstsharetrue() {
        return firstsharetrue;
    }

    public void setFirstsharetrue(boolean firstsharetrue) {
        this.firstsharetrue = firstsharetrue;
    }

    public boolean isFirstshareenable() {
        return firstshareenable;
    }

    public void setFirstshareenable(boolean firstshareenable) {
        this.firstshareenable = firstshareenable;
    }

    private GlobalDataManager() {
        mState = PlayerState.NAVIGATION;
        mHistoryStack = new LinkedList<Integer>();
        mSelectedPenPathVos = new ArrayList<PentoolVO>();
        mPlayerStateListenerColl = new ArrayList<SoftReference<PlayerStateChangedListener>>();
        mAssessmentpages = new ArrayList<UserPageVO>();
        mRefreshListenerColl = new LinkedList<SoftReference<RefreshListener>>();
        mStoreFragmentInstance = new HashMap<>();
        ugcListPerUserIdMap = new HashMap<>();
        mStoreTeacherReviewFragmentInstance = new HashMap<>();

        mUserCategoryBooks = new ArrayList<>();
    }

    /**
     * Add listener from YoutubeCustomView
     *
     * @param youtubeChangeListener
     */
    public void addCloseYoutubeChangeListener(YoutubeChangeListener youtubeChangeListener) {
        this.mYoutubeChangeListener = youtubeChangeListener;
    }

    /**
     * Notify YouTube Player
     */
    public void notifyCloseYoutubeChange() {
        if (this.mYoutubeChangeListener != null) {
            this.mYoutubeChangeListener.closeYoutubePlayer();
        }
    }

    public void removeCloseYoutubeChangeListener() {
        this.mYoutubeChangeListener = null;
    }

    /*public ArrayList<PentoolVO> getPenData() {
        ArrayList<PentoolVO> listofpenmarks = new ArrayList<PentoolVO>();
        for (int i = 0; i < GlobalDataManager.getInstance().getLocalBookData()
                .getPageViewerPageColl().size(); i++) {
            for (int j = 0; j < GlobalDataManager.getInstance().getLocalBookData()
                    .getPageViewerPageColl().get(i).length; j++) {
                ArrayList<PentoolVO> pentoolColl = GlobalDataManager.getInstance()
                        .getLocalBookData().getPageViewerPageColl().get(i)[j].getPenColl();

                for (int k = 0; k < pentoolColl.size(); k++) {
                    if (!pentoolColl.get(k).isSubmitted()) {
                        if (pentoolColl.get(k).getPointarray().size() > 0) {
                            listofpenmarks.add(pentoolColl.get(k));
                        }
                    }
                }
            }
        }
        return listofpenmarks;
    }*/

    public boolean isHistoryNavigationRecordRequired() {
        return isHistoryNavigationRecordRequired;
    }

    public void setHistoryNavigationRecordRequired(boolean isHistoryNavigationRecordRequired) {
        this.isHistoryNavigationRecordRequired = isHistoryNavigationRecordRequired;
    }

    public void setAssessmentData(ArrayList<UserPageVO> list) {
        mAssessmentpages = list;
    }

    public ArrayList<UserPageVO> getAssessmentPenMarks() {
        return mAssessmentpages;
    }

    public UserPageVO getAssessmentPenMark(String folioID) {
        for (int i = 0; i < mAssessmentpages.size(); i++) {
            if (mAssessmentpages.get(i).getFolioID().equals(folioID)) {
                return mAssessmentpages.get(i);
            }
        }
        return new UserPageVO();
    }

    public UserPageVO getAssessment(String folioID) {
        for (UserPageVO userPageVO : mAssessmentpages) {
            if (userPageVO.getFolioID().equals(folioID)) {
                return userPageVO;
            }

        }
        return new UserPageVO();
    }

    public static GlobalDataManager getInstance() {
        if (mInstance == null) {
            mInstance = new GlobalDataManager();
        }
        return mInstance;

    }

    public void addFragmentInstance(String categoryName, Fragment expandableListViewHashMap) {
        if (mStoreFragmentInstance == null) {
            mStoreFragmentInstance = new HashMap<>();
        }
        mStoreFragmentInstance.put(categoryName, expandableListViewHashMap);
    }


    public HashMap<String, Fragment> getStoreFragmentInstance() {
        return mStoreFragmentInstance;
    }

    public Fragment getFragmentInstance(String categoryName) {
        return mStoreFragmentInstance.get(categoryName);
    }

    public void updatePageHistory(int pageId) {
        try {
            while (mHistoryStackPosition != (mHistoryStack.size())) {
                mHistoryStack.removeLast();
            }
            if (mHistoryStack.size() != 0) {
                if (mHistoryStack.get(mHistoryStack.size() - 1) != pageId) {
                    mHistoryStack.add(pageId);
                }
            } else {
                mHistoryStack.add(pageId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPreviousHistoryPage() {
        try {
            int pageId = mHistoryStack.get(mHistoryStackPosition - 2);
            mHistoryStackPosition = mHistoryStackPosition - 1;
            return pageId;
        } catch (Exception e) {
            return -1;
        }
    }

    public int getNextHistoryPage() {
        try {
            int pageId = mHistoryStack.get(mHistoryStackPosition);
            mHistoryStackPosition = mHistoryStackPosition + 1;
            return pageId;
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean isAutoPlay() {
        return mAutoPlay;
    }

    public void setAutoPlay(boolean mAutoPlay) {
        this.mAutoPlay = mAutoPlay;
    }

    public long getTocSelectLinkId() {
        return mTocSelectLinkId;
    }

    public void setTocSelectLinkId(long tocSelectLinkId) {
        this.mTocSelectLinkId = tocSelectLinkId;
    }

    public int getHighlightType() {
        return mHighlightType;
    }

    public void setHighlightType(int highlightType) {
        this.mHighlightType = highlightType;
    }

    public boolean isZoomEnabled() {
        return true;
    }

    public boolean arePagesLoading() {
        return false;
    }

    public void setFlingEnabled(boolean value) {
        mIsFlingEnabled = value;
    }

    public boolean isZoomInProgress() {
        return mIsZoomInProgress;
    }

    public void setZoomInProgress(boolean value) {
        mIsZoomInProgress = value;
    }

    public boolean isInDefaultMode() {
        return mState == PlayerState.NAVIGATION;
    }

    public boolean isFlingEnabled() {
        return mIsFlingEnabled;
    }

  /*  public void setPDFDoc(PDFCore value) {
        mCore = value;
    }

    public PDFCore getPdfDoc() {
        return mCore;
    }*/

    public float getScale() {
        return mCurrScale;
    }

    public void setScale(float scale) {
        mCurrScale = scale;
    }

    public float getSourceScale() {
        return mSourceScale;
    }

    public void setSourceScale(float scale) {
        mSourceScale = scale;
    }

    public int getPagesContainerWidth() {
        return mContainerWidth;
    }

    public int getPagesContainerHeight() {
        return mContainerHeight;
    }

    public void setPagesContainerWidth(int value) {
        mContainerWidth = value;
    }

    public void setPagesContainerHeight(int value) {
        mContainerHeight = value;
    }

    public int getScreenOrientation() {
        return mScreenOrientation;
    }

    public void setScreenOrientation(int value) {
        mScreenOrientation = value;
    }

    public EBookType getBookType() {
        return mBookType;
    }

    public void setBookType(EBookType _bookType) {
        this.mBookType = _bookType;
    }

   /* public void setLocalBookData(LocalBookVO bookObj) {
        mBookData = bookObj;
    }

    public LocalBookVO getLocalBookData() {
        if (mBookData == null) {
            mBookData = new LocalBookVO();
        }
        return mBookData;
    }*/

    /**
     * @return the mHistoryStack
     */
    public LinkedList<Integer> getHistoryStack() {
        return mHistoryStack;
    }

    @Override
    public void destroy() {
        mSelectedPenPathVos.clear();
        mPlayerStateListenerColl.clear();
        mAssessmentpages.clear();
        mRefreshListenerColl.clear();
        //mBookData.destroy();
        // mCore = null;
        //  mBookData = null;
        mHistoryStack = null;
        mInstance = null;
        mUserCategoryBooks.clear();
    }

    public PlayerState getCurrMode() {
        return mState;
    }

    public void setCurrMode(PlayerState mode) {
        this.mState = mode;
        broadcastStateChange();
    }

    public void refreshAssestOnPage() {
        broadcastPageRefresh();
    }

    public void refreshHighLightOnPage() {
        broadcastHighLightRefresh();
    }

    public String getPenColor() {
        return mPenColor;
    }

    public void setPenColor(String _penColor) {
        this.mPenColor = _penColor;
    }


    public int getPenSize() {

        return mPenSize;
    }

    public void setPenSize(int _penSize) {
        this.mPenSize = _penSize;
    }

    public void setMediumPenSize(int size) {
        this.mMediumPenSize = size;
    }

    public int getMediumPenSize() {
        return mMediumPenSize;
    }

    /**
     * @return the mPenDeleteMode
     */
    public boolean isInPenDeleteMode() {
        return mPenDeleteMode;
    }

    /**
     * @param _penDeleteMode the mPenDeleteMode to set
     */
    public void setPenDeleteMode(boolean _penDeleteMode) {
        this.mPenDeleteMode = _penDeleteMode;
    }


    public ArrayList<PentoolVO> getSelectedPenPathVos() {

        return mSelectedPenPathVos;
    }

    public void setSelectedPenPathVos(ArrayList<PentoolVO> _selectedPenPathVos) {
        this.mSelectedPenPathVos = _selectedPenPathVos;
    }

    private void broadcastStateChange() {
        try {
            for (int i = 0; i < mPlayerStateListenerColl.size(); i++) {
                PlayerStateChangedListener obj = mPlayerStateListenerColl.get(i).get();
                if (obj != null) {
                    obj.playerStateChanged();
                }
            }
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }

    }


    private void broadcastPageRefresh() {
        try {
            for (int i = 0; i < mPlayerStateListenerColl.size(); i++) {
                PlayerStateChangedListener obj = mPlayerStateListenerColl.get(i).get();
                if (obj != null) {
                    obj.playerPageRefresh();
                }
            }
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }

    }

    private void broadcastHighLightRefresh() {
        try {
            for (int i = 0; i < mPlayerStateListenerColl.size(); i++) {
                PlayerStateChangedListener obj = mPlayerStateListenerColl.get(i).get();
                if (obj != null) {
                    obj.playerHighlightRefresh();
                }
            }
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }

    }

    public void addPageStateChangedListener(SoftReference<PlayerStateChangedListener> softReference) {
        mPlayerStateListenerColl.add(softReference);

    }

    public void removePageStateChangedListener(PlayerStateChangedListener softReference) {
        try {
            for (int i = 0; i < mPlayerStateListenerColl.size(); i++) {
                if (mPlayerStateListenerColl.get(i).get() == softReference) {
                    mPlayerStateListenerColl.remove(i);
                    break;
                }
            }
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }

        }


    }

    public Pair<Long, RectF> getCurrAudioSyncRect() {
        return mCurrAudioSyncRect;
    }

    public void setCurrAudioSyncRect(Pair<Long, RectF> _currAudioSyncRect) {
        try {
            this.mCurrAudioSyncRect = _currAudioSyncRect;
            broadcastRefresh();
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }

    }

    public void addRefreshListeners(RefreshListener listener) {
        mRefreshListenerColl.add(new SoftReference<RefreshListener>(listener));
    }

    public void removeRefreshListeners(RefreshListener listener) {
        try {
            for (int i = 0; i < mRefreshListenerColl.size(); i++) {
                if (mRefreshListenerColl.get(i).get() != null) {
                    if (mRefreshListenerColl.get(i).get() == listener) {
                        mRefreshListenerColl.remove(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }

    }

    public void broadcastRefresh() {
        try {
            for (int i = 0; i < mRefreshListenerColl.size(); i++) {
                if (mRefreshListenerColl.get(i).get() != null) {
                    mRefreshListenerColl.get(i).get().refresh();
                }
            }
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }

    }

    public boolean isReviewMode() {
        return mIsReviewMode;
    }

    public void setisReviewMode(boolean _isReviewMode) {
        this.mIsReviewMode = _isReviewMode;
    }

   /* public boolean isCheckForSubmitPenData() {
        for (int i = 0; i < GlobalDataManager.getInstance().getLocalBookData().getPageViewerPageColl()
                .size(); i++) {
            for (int j = 0; j < GlobalDataManager.getInstance().getLocalBookData().getPageViewerPageColl()
                    .get(i).length; j++) {
                ArrayList<PentoolVO> pentoolColl = GlobalDataManager.getInstance().getLocalBookData()
                        .getPageViewerPageColl().get(i)[j].getPenColl();

                for (int k = 0; k < pentoolColl.size(); k++) {
                    if (!pentoolColl.get(k).isSubmitted()) {
                        return true;
                    }

                }
            }
        }
        return false;
    }*/

    public int getHistoryStackPosition() {
        return mHistoryStackPosition;
    }

    public void setHistoryStackPosition(int _historyStackPosition) {
        this.mHistoryStackPosition = _historyStackPosition;
    }

    public boolean isAutoFling() {
        return mIsAutoFling;
    }

    public void setAutoFling(boolean isAutoFling) {
        this.mIsAutoFling = isAutoFling;
    }

    public String getSearchText() {
        return mSearchText;
    }

    public void setSearchText(String _searchText) {
        this.mSearchText = _searchText;
    }

    public boolean IsAnyPopupVisible() {
        return mIsAnyPopupVisible;
    }

    public void setAnyPopupVisible(boolean _isAnyPopupVisible) {
        this.mIsAnyPopupVisible = _isAnyPopupVisible;
    }

    public void setLastClickedLinkVo(LinkVO objVo) {
        mLastClickedLinkVo = objVo;
    }

    public LinkVO getLastClickedLinkVo() {
        return mLastClickedLinkVo;
    }

    public boolean isConfigChanges() {
        return isConfigChanges;
    }

    public void setUndoClicked(boolean undoClicked) {
        isUndoClicked = undoClicked;
    }

    public boolean isUndoClicked() {
        return isUndoClicked;
    }

    public void setConfigChanges(boolean configChanges) {
        isConfigChanges = configChanges;
    }

    public boolean IsAnyLinkClicked() {
        return mIsAnyClickClicked;
    }

    public void setAnyLinkClicked(boolean _isAnyLinkClicked) {
        this.mIsAnyClickClicked = _isAnyLinkClicked;
    }

    public DownloadListener getDownloadListener() {
        return downloadListener;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void setView(View isViewSet) {
        this.isViewSet = isViewSet;
    }

    public View getView() {
        return isViewSet;
    }


    public void setPenCollections(ArrayList<PentoolVO> penCollections) {
        this.penCollections = penCollections;
    }

    public ArrayList<PentoolVO> getPenCollections(ArrayList<PentoolVO> totalPenCollections) {
        return penCollections;
    }

    public void setPenDeleteCount(int i) {
        this.count = i;
    }

    public int getPenDeleteCount() {
        return count;
    }

    public boolean isBookClicked() {
        return mIsBookClicked;
    }

    public void setIsBookClicked(boolean mIsBookClicked) {
        this.mIsBookClicked = mIsBookClicked;
    }


    public boolean isPreviewSearchClicked() {
        return mPreviewCllicked;
    }

    public void setPreviewSearchClicked(boolean mPreviewCllicked) {
        this.mPreviewCllicked = mPreviewCllicked;
    }


    public PentoolVO getProtractorPathVO() {
        return protractorPathVO;
    }

    public void setProtractorPathVO(PentoolVO protractorPathVO) {
        this.protractorPathVO = protractorPathVO;
    }

    public void setPenColors(JSONArray penColors) {
        this.penColors = penColors;
    }

    public JSONArray getPenColors() {
        return penColors;
    }

    public String getProtractorPageId() {
        return protractorPageId;
    }

    public void setProtractorPageId(String protractorPageId) {
        this.protractorPageId = protractorPageId;
    }

    public int getCurrenttabPosition() {
        return mPosition;
    }

    public void setCurrenttabPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public boolean isSearchItemClicked() {
        return searchItemClicked;
    }

    public void setSearchItemClicked(boolean searchItemClicked) {
        this.searchItemClicked = searchItemClicked;
    }

//    public void setAnalyticCallback(AddAnalyticsStudentSelectCallback listener) {
//        this.mAnalyticListener = listener;
//    }
//
//    public void updateAnalyticData(String profileUrl, AnalyticsDataVo dataVo, IClass mClass, boolean isOverAll, boolean fetchDataReq) {
//        mAnalyticListener.onStudentSelected(profileUrl, dataVo, mClass, isOverAll, fetchDataReq);
//    }

    public void setCurrentActivatedAnalyticUserId(long currentActivatedAnalyticUserId) {
        this.currentActivatedAnalyticUserId = currentActivatedAnalyticUserId;
    }

    public long getCurrentActivatedAnalyticUserId() {
        return currentActivatedAnalyticUserId;
    }

    public void setCurrentActivatedAnalyticClassid(String currentActivatedAnalyticClassid) {
        this.currentActivatedAnalyticClassid = currentActivatedAnalyticClassid;
    }

    public String getCurrentActivatedAnalyticClassid() {
        return currentActivatedAnalyticClassid;
    }

    public boolean isWebViewClosedAfterTokenReceived() {
        return mWebViewClosedAfterTokenReceived;
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

    public boolean isSignedOutFromApp() {
        return mSignedOutFromApp;
    }

    public void setSignedOutFromApp(boolean mSignedOutFromApp) {
        this.mSignedOutFromApp = mSignedOutFromApp;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public boolean isNoCategoriesFound() {
        return noCategoriesFound;
    }

    public void setNoCategoriesFound(boolean noCategoriesFound) {
        this.noCategoriesFound = noCategoriesFound;
    }

    public boolean isExploreCategory() {
        return mExploreCategory;
    }

    public void setExploreCategory(boolean mExploreCategory) {
        this.mExploreCategory = mExploreCategory;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public ArrayList<IBook> getUserCategoryBooks() {
        return mUserCategoryBooks;
    }

    public void setUserCategoryBooks(ArrayList<IBook> mUserCategoryBooks) {
        this.mUserCategoryBooks = mUserCategoryBooks;
    }

    public HashMap<Long, UserBookVO> getCollectionbookdownloading() {
        if (collectionbookdownloading == null)
            collectionbookdownloading = new HashMap<>();
        return collectionbookdownloading;
    }

    public boolean isFetchAllBookRequired() {
        return isFetchAllBookRequired;
    }

    public void setFetchAllBookRequired(boolean fetchAllBookRequired) {
        isFetchAllBookRequired = fetchAllBookRequired;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public boolean isFetchAllCategoryAfterUpdateRequired() {
        return isFetchAllCategoryAfterUpdateRequired;
    }

    public void setFetchAllCategoryAfterUpdateRequired(boolean fetchAllCategoryAfterUpdateRequired) {
        isFetchAllCategoryAfterUpdateRequired = fetchAllCategoryAfterUpdateRequired;
    }

    public ArrayList<String> getSelectedChapterTitles() {
        return mSelectedChapterTitles;
    }

    public void setSelectedChapterTitles(ArrayList<String> mSelectedChapterTitles) {
        this.mSelectedChapterTitles = mSelectedChapterTitles;
    }
}
