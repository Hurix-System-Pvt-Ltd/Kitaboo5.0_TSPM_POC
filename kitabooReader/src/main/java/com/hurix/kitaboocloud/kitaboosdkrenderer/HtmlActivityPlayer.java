package com.hurix.kitaboocloud.kitaboosdkrenderer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.hurix.bookreader.encryption.EncryptionManager;
import com.hurix.commons.iconify.Typefaces;
import com.hurix.commons.notifier.GlobalDataManager;
import com.hurix.customui.datamodel.EncryptionVO;
import com.hurix.kitaboocloud.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@SuppressLint("SetJavaScriptEnabled")
public class HtmlActivityPlayer extends Activity implements View.OnClickListener {
    boolean mIsOriantationLocked = false;
    WebView mWebView;
    TextView mBackToBookShelfIcon;
    RelativeLayout mTopButtonContainer;
    RelativeLayout mWebViewContainer;
    ProgressBar mProgressBar;
    String mIsSecureUrlType;
    private TextView mTvMessage;
    private ArrayList<EncryptionVO> _mCollArray;
    String path = "";
    private String isbn;
    private Handler mHandler;
    private FrameLayout progressbarLayout;
    private Typeface mTypeface;
    private String mBookID;
    private Typeface _typeFace;
    private TextView mBookTitle;
    private String strBookTitle;

    private String bookExpiryDate = "";
    long currentTimeInMillis = 0;
    private AlarmManager bookExpiryAlarmManager;



    public enum SecureUrlType {
        SECURE_URL_TYPE_NONE("none"),
        SECURE_URL_TYPE_CLIENT_SECURE("Client Secured");

        private SecureUrlType(final String text) {
            this.text = text;
        }

        private String text;

        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * Called when the activity is first created.
     */
    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        com.hurix.epubreader.Utility.Utils.setSecureWindowFlags(HtmlActivityPlayer.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bookshelf_webviewplayer);
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            _typeFace = com.hurix.customui.iconify.Typefaces.get(this, fontfile);
        } else {
            _typeFace = com.hurix.customui.iconify.Typefaces.get(this, "kitabooread.ttf");
        }
        initialization();
        setListner();
        setReaderTyface();

        progressbarLayout = (FrameLayout) findViewById(R.id.progressbarlayout);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().getJavaScriptCanOpenWindowsAutomatically();
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setInitialScale(1);

        mWebView.setWebViewClient(new SampleWebViewClient());
        if (bundle != null) {
            try {
                mIsOriantationLocked = bundle.getBoolean("isOriantationLocked");
                path = bundle.getString("path");
                isbn = bundle.getString("isbn");
                mBookID = bundle.getString("bookID");
                strBookTitle = bundle.getString("bookTitle");

                bookExpiryDate = bundle.getString("expiryDate");
                currentTimeInMillis = bundle.getLong("currentTime");

                mBookTitle.setText(strBookTitle);
                _mCollArray = convertNodesFromXml(new File(path).getParent() + "/encryption.xml");


                playContent(path);
                resizeWebView();
            } catch (Exception e) {
                playContent(path);
                resizeWebView();
                e.printStackTrace();
            }
        }

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressbarLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.INVISIBLE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getBaseContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

        });

       // setBookExpiry();
    }



    private void setReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            mTypeface = Typefaces.get(this, fontfile);
        } else {
            mTypeface = Typefaces.get(this, "kitabooread.ttf");
        }
    }

    private void initialization() {
        mWebView = (WebView) findViewById(R.id.linkwebviewID);
        mTopButtonContainer = (RelativeLayout) findViewById(R.id.buttonContainerID);
        mBackToBookShelfIcon = (TextView) findViewById(R.id.buttonDone);
        mBackToBookShelfIcon.setTypeface(_typeFace);
        mBackToBookShelfIcon.setText("a");
        mBackToBookShelfIcon.setTextColor(getResources().getColor(R.color.kitaboo_main_color));
        mBackToBookShelfIcon.setTextSize(30);
        mBackToBookShelfIcon.setOnClickListener(this);

        mBookTitle = (TextView) findViewById(R.id.toptitle);
        mBookTitle.setTextColor(getResources().getColor(R.color.kitaboo_main_color));
        mWebViewContainer = (RelativeLayout) findViewById(R.id.webViewContainerID);
        mProgressBar = (ProgressBar) findViewById(R.id.webVIewProgressBar);
        mTvMessage = (TextView) findViewById(R.id.tv_webview_message);

    }

    public ArrayList<EncryptionVO> convertNodesFromXml(String xml) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(xml);

        Element doc = document.getDocumentElement();

        return createMap(xml, document.getDocumentElement());
    }

    public static ArrayList<EncryptionVO> createMap(String fileName, Node node) {
        ArrayList<EncryptionVO> mColl = new ArrayList<EncryptionVO>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            EncryptionVO encryptionVO = new EncryptionVO();
            if (currentNode.hasAttributes()) {

                Node item = currentNode.getAttributes().item(0);

                encryptionVO.setmType(item.getTextContent());

            }
            if (node.getFirstChild() != null && node.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {

                encryptionVO.setmValue(currentNode.getFirstChild().getTextContent());
            }
            mColl.add(encryptionVO);
        }
        return mColl;
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();

        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.clearCache(true);
            mWebView.destroy();
        }
      //  BaseActivity.isReaderOpen = false;
        com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setCurrentSelectedBookLaunch(false);
        mWebView = null;

    }



    private class SampleWebViewClient extends WebViewClient {
        @SuppressWarnings("NewApi")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

            if (!isFinishing()) {
                if (URLUtil.isValidUrl(request.getUrl().toString())) {

                    String resourseType = null;
                    if (!isFinishing()) {

                        resourseType = checkUrlEncryptedOrNot(request.getUrl().toString());
                    }
                    if (resourseType != null && !resourseType.isEmpty()) {
                        if (!isFinishing()) {
                            return getResource(resourseType, request.getUrl().toString());
                        }
                    }
                }
                return null;
            }
            return null;

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, final String url) {
            if (URLUtil.isValidUrl(url)) {
                if (!isFinishing()) {
                    if (URLUtil.isValidUrl(url)) {

                        String resourseType = null;
                        if (!isFinishing()) {
                            resourseType = checkUrlEncryptedOrNot(url);
                        }
                        if (resourseType != null && !resourseType.isEmpty()) {
                            if (!isFinishing()) {
                                return getResource(resourseType, url);
                            }
                        }
                    }
                    return null;
                }
                return null;
            }

            return null;

        }

        private void setProgressVisibility(final int visibility) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mProgressBar.setVisibility(visibility);
                }
            });
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressbarLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        }
    }

    private String checkUrlEncryptedOrNot(String string) {
        String resourseType = "";
        if (_mCollArray != null) {
            for (EncryptionVO vo : _mCollArray) {
                if (string.contains(vo.getmValue())) {
                    return vo.getmType();
                }
            }
        }
        return resourseType;
    }

    public WebResourceResponse getResource(String type, String url) {
        if (!isFinishing()) {
            WebResourceResponse res = null;
            String filepath = null;
            if (url.contains("?r")) {
                filepath = url.substring(0, url.indexOf("?r"));
            } else {
                filepath = url;
            }
            if (type.equalsIgnoreCase("full")) {
                try {
                    File file = new File(filepath.replace("file://", ""));
                    ByteArrayInputStream bis = null;
                    bis = new ByteArrayInputStream(returnByteArray(file));
                    res = new WebResourceResponse(getMimeType(filepath), "UTF-8", bis);
                } catch (Exception e) {
                    //      Log.e("e", "" + e + " url" + url);
                }
            } else if (type.equalsIgnoreCase("header")) {
                try {
                    File file = new File(filepath.replace("file://", ""));
                    ByteArrayInputStream bis = null;
                    bis = new ByteArrayInputStream(returnByteArrayOfHeader(file));
                    res = new WebResourceResponse(getMimeType(filepath), "UTF-8", bis);
                } catch (Exception e) {
                    //     Log.e("e", "" + e + " url" + url);
                }
            }
            return res;
        }
        return null;
    }

    public String getMimeType(String url) {
        String type = null;
        if (!isFinishing()) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
            return type;
        }
        return null;

    }

    private byte[] returnByteArray(File file) {
        try {
            if (!isFinishing()) {
                String key = isbn + file.getName();

                //  File file = new File(path);
                long len = file.length();
                byte[] data = new byte[(int) len];
                FileInputStream in = new FileInputStream(file);
                in.read(data);
                in.close();
                byte[] fileda = EncryptionManager.decrypt(data, key, EncryptionManager.LIGHT_ENCRYPTION_SIZE);
                return fileda;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //    Log.e("e", "" + e + " file" + file);
        }
        return null;
    }

    private byte[] returnByteArrayOfHeader(File file) {
        try {
            if (!isFinishing()) {
                String key = isbn + file.getName();

                //  File file = new File(path);
                long len = file.length();
                byte[] data = new byte[(int) len];
                FileInputStream in = new FileInputStream(file);
                in.read(data);
                in.close();
                byte[] fileda = EncryptionManager.decryptHeaderToByte(this, file.getAbsolutePath(), key, EncryptionManager.LIGHT_ENCRYPTION_SIZE);
                return fileda;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //   Log.e("e", "" + e + " file" + file);
        }
        return null;
    }

    private void setListner() {
        //mDoneBtn.setOnClickListener(this);
    }

    private void playContent(String path) {
        mWebView.loadUrl(path);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mIsOriantationLocked) {
            finish();
        } else {
            resizeWebView();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonDone) {
            GlobalDataManager.getInstance().setAnyPopupVisible(false);
            //removeEncryptedFiles();

            Intent intent = new Intent();
            intent.putExtra("Trackingdata", "");
            intent.putExtra("bookID", mBookID);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void resizeWebView() {
        RelativeLayout.LayoutParams linearlayoutlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mTopButtonContainer.setVisibility(View.VISIBLE);
        linearlayoutlp.setMargins(0, 0, 0, 0);
        linearlayoutlp.addRule(RelativeLayout.BELOW, R.id.buttonContainerID);
        mWebViewContainer.setPadding(0, 0, 0, 0);
        mWebViewContainer.setLayoutParams(linearlayoutlp);
        mWebViewContainer.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
        GlobalDataManager.getInstance().setAnyPopupVisible(false);
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed();
    }


    private void setErrorLayout() {
        mProgressBar.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setTextColor(Color.BLACK);
        mTvMessage.setText(getResources().getString(R.string.alert_webView_loading_error));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            GlobalDataManager.getInstance().setAnyPopupVisible(false);
            Intent intent = new Intent();
            intent.putExtra("Trackingdata", "");
            intent.putExtra("bookID", mBookID);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

