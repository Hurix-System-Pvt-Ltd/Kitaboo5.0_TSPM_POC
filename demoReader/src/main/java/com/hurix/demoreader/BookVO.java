package com.hurix.demoreader;

import com.hurix.commons.datamodel.IDownloadable;
import com.hurix.commons.renderClient.bus.AssetType;
import com.hurix.downloadbook.listener.SDKDownloadListener;
import com.hurix.epubreader.fixedepubreader.enums.BookState;

public class BookVO implements IDownloadable {

    private String mBookPath;
    private String mBookType;
    private Long mBookID;
    private boolean mIsBookEncrypt;
    private String mISBN;
    private String mBookVersion;
    private String mSearchQuery;
    private String mBookEncryptionType;
    private String mLastPage;
    private String mBookDict;
    private String mAssetType;


    public void setBookAssetType(String mAssetType) {
        this.mAssetType = mAssetType;
    }

    public String getBookAssetType() {
        return mAssetType;
    }

    private boolean mIsPhysicalPackageAvailable = false;
    private boolean mIsBookDownloading = false;
    private String mDownloadUrl;
    private BookState mState = BookState.NOT_STARTED;
    private float mCurrSize;

    public float getCurrSize() {
        return mCurrSize;
    }

    public float getTotalSize() {
        return mTotalSize;
    }

    private float mTotalSize;
    private SDKDownloadListener mListener;



    public String getBookDict() {
        return mBookDict;
    }

    public void setBookDict(String mBookDict) {
        this.mBookDict = mBookDict;
    }

    public String getLastPage() {
        return mLastPage;
    }

    public void setLastPage(String mLastPage) {
        this.mLastPage = mLastPage;
    }

    public String getBookEncryptionType() {
        return mBookEncryptionType;
    }

    public void setBookEncryptionType(String mBookEncryptionType) {
        this.mBookEncryptionType = mBookEncryptionType;
    }

    public String getBookPath() {
        return mBookPath;
    }

    public void setBookPath(String mBookPath) {
        this.mBookPath = mBookPath;
    }

    public String getBookType() {
        return mBookType;
    }

    public void setBookType(String mBookType) {
        this.mBookType = mBookType;
    }

    @Override
    public String getDownloadURI() {
        return mDownloadUrl;
    }

    public void setDownloadURI(String s) {
        mDownloadUrl = s;
    }

    @Override
    public BookState getCurrentState() {
        return mState;
    }

    @Override
    public void setCurrentState(BookState bookState) {
        mState = bookState;

        switch (mState) {
            case UNZIP_ERROR:
                mIsBookDownloading = false;
                unzipError();
                break;
            case COMPLETED:
                downloadCompleted();
                break;
            case DOWNLOADED:
                downloadInQueue();
                break;
            case DOWNLOADING:
                mIsBookDownloading = true;
                //downloadInQueue();
                break;
            case DOWNLOAD_ERROR:
                mIsBookDownloading = false;
                downloadError();
                break;
            case IN_QUEUE:
                downloadInQueue();
                break;
            case STARTED:
                downloadStart();
                break;
            case UNZIPPED:
                mIsBookDownloading = false;
                unzipCompleted();
                break;
            case UNZIPPING:
                unzipInQueue();
                break;
            case WAITING_TO_BE_IN_QUEUE:
                mIsBookDownloading = false;
                waitingToBeInQueue();
                break;
            case NOT_STARTED:
                updateListener();
                break;
            case INITIALIZE:
                mIsBookDownloading = true;
                updateListener();
                break;
            default:
                break;
        }
    }

    @Override
    public void updateProgress(float current, float total) {
        mTotalSize = total;
        mCurrSize = current;
        mIsPhysicalPackageAvailable = true;
        updateListener();
    }

    @Override
    public void waitingToBeInQueue() {
        updateListener();
    }

    @Override
    public void downloadStart() {
        updateListener();
    }

    @Override
    public void downloadInQueue() {
        updateListener();
    }

    @Override
    public void downloadCompleted() {
        updateListener();
    }

    @Override
    public void downloadError() {
        updateListener();
    }

    @Override
    public long getBookID() {
        return mBookID;
    }


    @Override
    public void unzipStart() {
        updateListener();
    }

    @Override
    public void unzipInQueue() {
        updateListener();
    }

    @Override
    public void unzipCompleted() {
        updateListener();
    }

    @Override
    public void unzipError() {
        updateListener();
    }



    @Override
    public String getBookTitle() {
        return null;
    }

    @Override
    public String getBookISBN() {
        return mISBN;
    }

    @Override
    public void setDownloadStateListener(SDKDownloadListener sdkDownloadListener) {
        mListener = sdkDownloadListener;
    }

    @Override
    public boolean IsPhysicalPackageAvailable() {
        return false;
    }

    public void setBookID(Long mBookID) {
        this.mBookID = mBookID;
    }

    public boolean getIsBookEncrypt() {
        return mIsBookEncrypt;
    }

    public void setIsBookEncrypt(boolean mIsBookEncrypt) {
        this.mIsBookEncrypt = mIsBookEncrypt;
    }

    public String getISBN() {
        return mISBN;
    }

    public void setISBN(String mISBN) {
        this.mISBN = mISBN;
    }

    public String getBookVersion() {
        return mBookVersion;
    }

    public void setBookVersion(String mBookVersion) {
        this.mBookVersion = mBookVersion;
    }

    public String getSearchQuery() {
        return mSearchQuery;
    }

    public void setSearchQuery(String mSearchQuery) {
        this.mSearchQuery = mSearchQuery;
    }

    private void updateListener() {
        /*if (Constants.IS_DEBUG_ENABLED)
            Log.d("TEST", "updateListener called with state: " + mState + " title: " + mTitle);*/
        if (mListener != null) {
            SDKDownloadListener obj = mListener;
            if (obj != null) {
                obj.stateUpdated(mState, this);
            }
        }
    }
}
