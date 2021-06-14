package com.hurix.kitaboocloud.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.commons.notifier.GlobalDataManager;

import com.hurix.kitaboocloud.R;



import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import static com.hurix.commons.KitabooSDKModel.themeUserSettingVo;

public class WebViewActivity extends Activity implements View.OnClickListener {
    Button mDoneBtn;
    RelativeLayout mTopButtonContainer;
    RelativeLayout mWebViewContainer;
    ProgressBar  mProgressBar;
    TextView mTvMessage;
    String URL = new String();
    WebView wv;
    private String data;
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        com.hurix.epubreader.Utility.Utils.setSecureWindowFlags(WebViewActivity.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = this.getIntent().getExtras();
        setContentView(R.layout.webview_activity);

        wv = (WebView) findViewById(R.id.linkwebviewID);
        mTopButtonContainer = (RelativeLayout) findViewById(R.id.buttonContainerID);
        mDoneBtn = (Button) findViewById(R.id.webviewdonebtnID);
        mDoneBtn.setBackgroundColor((Color.parseColor(themeUserSettingVo.getmKitabooMainColor())));
        mDoneBtn.setOnClickListener(this);
        mWebViewContainer = (RelativeLayout) findViewById(R.id.webViewContainerID);
        mProgressBar = (ProgressBar) findViewById(R.id.webVIewProgressBar);
        mTvMessage = (TextView) findViewById(R.id.tv_webview_message);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.setFocusable(true);
        wv.setFocusableInTouchMode(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv.setWebViewClient(new MyBrowser());// how plugin is enabled change in API 8
        if(bundle != null) {
            try {
                data=bundle.getString("path");
            } catch (Exception e) {
               e.printStackTrace();
            }
        }



        wv.loadUrl(data);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.webviewdonebtnID) {
            GlobalDataManager.getInstance().setAnyPopupVisible(false);
            finish();
        }

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
          //  view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

            try {
                //Get certificate
                InputStream inStream = getAssets().open("kitaboocom.crt");
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509Certificate x509Certificate = (X509Certificate)cf.generateCertificate(inStream);

                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

                // creating a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", x509Certificate);
                tmf.init(keyStore);

                // Get hold of the default trust manager
                X509TrustManager x509Tm = null;
                for (TrustManager tm : tmf.getTrustManagers()) {
                    if (tm instanceof X509TrustManager) {
                        x509Tm = (X509TrustManager) tm;
                        break;
                    }
                }

                // Wrap it in your own class.
                final X509TrustManager trustManager = x509Tm;

                X509Certificate[] x509Certificates = new X509Certificate[1];
                x509Certificates[0] = x509Certificate;

                // check weather the certificate is trusted
                trustManager.checkServerTrusted(x509Certificates, "ECDH_RSA");

               // Log.e("WebViewActivity", "Certificate from " + error.getUrl() + " is trusted.");
                handler.proceed();
            } catch (Exception e) {
               // Log.e("WebViewActivity", "Failed to access " + error.getUrl() + ". Error: " + error.getPrimaryError());
                final AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }
                message += " Do you want to continue anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

}