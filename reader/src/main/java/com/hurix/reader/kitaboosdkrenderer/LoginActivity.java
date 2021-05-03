//package com.hurix.reader.kitaboosdkrenderer;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.InputFilter;
//import android.text.InputType;
//import android.text.TextWatcher;
//import android.text.method.DigitsKeyListener;
//import android.text.method.PasswordTransformationMethod;
//import android.util.DisplayMetrics;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.inputmethod.EditorInfo;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hurix.bookreader.views.link.LinkWebViewPlayer;
//
//
//import com.hurix.reader.R;
//import com.hurix.reader.kitaboosdkrenderer.adapter.KitabooServiceAPI;
//import com.hurix.reader.kitaboosdkrenderer.constants.Constants;
//import com.hurix.reader.kitaboosdkrenderer.constants.ServiceConstants;
//import com.hurix.reader.kitaboosdkrenderer.constants.ShelfUIConstants;
//import com.hurix.reader.kitaboosdkrenderer.controller.DBController;
//import com.hurix.reader.kitaboosdkrenderer.controller.UserController;
//import com.hurix.reader.kitaboosdkrenderer.datamodel.UserCategoryVO;
//import com.hurix.reader.kitaboosdkrenderer.datamodel.UserSettingVO;
//import com.hurix.reader.kitaboosdkrenderer.datamodel.UserVO;
//import com.hurix.reader.kitaboosdkrenderer.dialogs.DialogUtils;
//import com.hurix.reader.kitaboosdkrenderer.enums.KitabooBookShelfType;
//import com.hurix.reader.kitaboosdkrenderer.iconify.Typefaces;
//import com.hurix.reader.kitaboosdkrenderer.listeners.OnDialogOkActionListner;
//import com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager;
//import com.hurix.reader.kitaboosdkrenderer.sdkUtils.Utils;
//import com.hurix.reader.kitaboosdkrenderer.utils.ThemeColorConstant;
//import com.hurix.service.Interface.IServiceResponse;
//import com.hurix.service.Interface.IServiceResponseListener;
//import com.hurix.service.exception.ServiceException;
//import com.hurix.service.response.LoginResponse;
//
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//
//
//
///**
// * Created by biki.sah on 11/29/2016.
// */
//public class LoginActivity extends Activity implements IServiceResponseListener, View.OnClickListener, TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {
//    private TextView _txtForgotpassword, mLoginHeader, _tvPowerByID, _txtSignUp, _newToProfil;
//    private TextView _kitabooLogo;
//    private TextView mHelpLogin;
//    private TextView _btnGo;
//    private TextView mHelpAccess, mTitleAccess;
//    private TextView _txtPreview;
//    private TextView mNoAccount;
//    private EditText _userIdEditText, _edtaccesskey, _passwordEditText;
//    private RelativeLayout mPasswordLayout;
//    private LinearLayout mTermsAndConditionContainer, _topContainer, _tvPowerByIDContainer;
//    private Button _loginButton;
//    private Button mSignUp, loginsummit;
//    private LinearLayout _loginLayout;
//    private final String EMPTY = "";
//    private final int USER_NAME_CHARACTER_UPER_LIMIT = 255;
//    private final int PASSWORD_CHARACTER_UPER_LIMIT = 64;
//    private int mPasswordMinCharLimit = 4;
//    private int mUsernameMinCharLimit = 6;
//    private boolean _isPasswordVisible = false;
//    private boolean IsDoubleBackPressed = false;
//    private ProgressDialog mDialog;
//    private View mTopContainerDivider, mTopAccessCodeDivider;
//    private ScrollView mScrollView;
//    private LinearLayout mSignUpLayout, _mLoginTopcontainer;
//    private UserSettingVO mUserSettingVO;
//    private Typeface mTypeface;
//    private Context mContext;
//    private LoginListner _listner;
//    protected UserVO _mUserVO;
//    private final String EMAIL_REGULAR_EXPRESSION = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
//    private TextView mTermsAndCondition, mPrivacyPolicy;
//    private View mAnd, mSpinnerLine;
//    private TextView internetoff;
//    private Spinner mLangSpinner;
//    private TextView mLangText;
//    Locale mLocale;
//    private ArrayAdapter myArrayAdapter;
//    String[] language = {"Kalaallisut", "Dansk"};
//    private ImageView mWebviewLogo;
//
//    public interface LoginListner {
//        void onLoginListnerCodeSuccess(String code, boolean isFromSignUp);
//
//        void onBackPressedFromLogin();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mContext = getApplicationContext();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (mContext.getResources().getBoolean(R.bool.webview_login)) {
//            setContentView(R.layout.webviewloginactivity);
//            WebViewinit();
//        } else {
//            setContentView(R.layout.login);
//            initView();
//        }
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
//        //initView();
//    }
//
//    private void WebViewinit() {
//        setDefaultThemeColor();
//        mTypeface = Typefaces.get(mContext, mContext.getResources().getString(R.string.text_font_file));
//        mScrollView = (ScrollView) findViewById(R.id.login_scroll);
//        mWebviewLogo = (ImageView) findViewById(R.id.logi_image);
//        mUserSettingVO = UserController.getInstance(mContext).getUserSettings();
//        if (_mUserVO != null) {
//            DBController.getInstance(this).getManager().logoutAllUsers();
//            long userID = DBController.getInstance(this).getManager().insertUser(_mUserVO);
//            String strBookshelfType = com.hurix.kitaboo.constants.utils.Utils.getSharedPreferenceStringValue(
//                    LoginActivity.this, Constants.SHELF_PREFS_NAME, Constants.BOOKSHELF_TYPE, "");
//            if (!(strBookshelfType.equalsIgnoreCase(KitabooBookShelfType.BOOK_COLLECTION.toString()))) {
//                DBController.getInstance(this).getManager().insertCategory(Constants.DEFAULT_BOOK_CATEGORY, userID);
//            }
//        }
//        loginsummit = (Button) findViewById(R.id.btnloginsummit);
//        if(GlobalDataManager.getInstance().getTheme() != null){
//            loginsummit.setBackgroundColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getLoginScreen().getButtonColor()));
//        }
//        loginsummit.setTextColor(Color.parseColor(mUserSettingVO.getLoginButtonColor()));
//        internetoff = (TextView) findViewById(R.id._error);
//        if (GlobalDataManager.getInstance().isWebViewClosedAfterTokenReceived()) {
//            loginsummit.setText(getResources().getString(R.string.loading_progress));
//            loginsummit.setClickable(false);
//        } else {
//            /*if (GlobalDataManager.getInstance().isNoCategoriesFound()) {
//                DialogUtils.showOKAlert(new View(LoginActivity.this), 1, LoginActivity.this, getResources()
//                                .getString(R.string.alert_title), "No Categories found for the logged user",
//                        new OnDialogOkActionListner() {
//                            @Override
//                            public void onOKClick(View v) {
//                            }
//                        });
//            }*/
//            loginsummit.setText(getResources().getString(R.string.login_btn_text));
//            loginsummit.setClickable(true);
//            loginsummit.setOnClickListener(this);
//        }
//
//        mLangText = (TextView) findViewById(R.id.languageText);
//        mLangSpinner = (Spinner) findViewById(R.id.langSelectSpinner);
//        mSpinnerLine = (View) findViewById(R.id.spinnerLine);
//        mSpinnerLine.setBackgroundColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getLoginScreen().getButtonColor()));
//        mLangSpinner.setOnItemSelectedListener(this);
//
//        if(getResources().getBoolean(R.bool.is_nanoq_greenland)){
//            mLangText.setVisibility(View.VISIBLE);
//            mLangSpinner.setVisibility(View.VISIBLE);
//            mSpinnerLine.setVisibility(View.VISIBLE);
//        }else{
//            mLangText.setVisibility(View.GONE);
//            mLangSpinner.setVisibility(View.GONE);
//            mSpinnerLine.setVisibility(View.GONE);
//        }
//
//        if(getResources().getBoolean(R.bool.is_ACEP_client)){
//            mWebviewLogo.setVisibility(View.INVISIBLE);
//        }
//
//        List<String> list = new ArrayList<String>();
//        list.add("Kalaallisut");
//        list.add("Dansk");
//
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mLangSpinner.setAdapter(dataAdapter);
//
//
//
//       mLangSpinner.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_OVER);
//        // myArrayAdapter = new ArrayAdapter(this, R.layout.simple_spinner_item, language);
//       // myArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//       // mLangSpinner.setAdapter(myArrayAdapter);
//
//        if(com.hurix.kitaboo.constants.utils.Utils.getSharedPreferenceStringValue
//                (this, Constants.SHELF_PREFS_NAME,Constants.DEFAULT_APP_LANGUAGE_POSITION,"").isEmpty()){
//            com.hurix.kitaboo.constants.utils.Utils.insertSharedPrefernceStringValues(this,
//                    Constants.SHELF_PREFS_NAME, Constants.DEFAULT_APP_LANGUAGE_POSITION, 0 + "");
//        }else {
//            mLangSpinner.setSelection(Integer.parseInt(com.hurix.kitaboo.constants.utils.Utils.getSharedPreferenceStringValue(
//                    this, Constants.SHELF_PREFS_NAME, Constants.DEFAULT_APP_LANGUAGE_POSITION, "")));
//        }
//
//    }
//
//
//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_DONE) {
//            if (v == _passwordEditText) {
//                clickOnLogin();
//            } else if (v == _edtaccesskey) {
//                clickOnGo();
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.btnloginsummit) {
//            if (Utils.checkInternetConnection(mContext)) {
//                if (getResources().getBoolean(R.bool.is_ltpm_client)) {
//                    loginWebViewLink(ServiceConstants.LTPM_WEBVIEW_LOGOUT_URL);
//                }
//                else if(getResources().getBoolean(R.bool.is_nanoq_greenland)) {
//                    loginWebViewLink(ServiceConstants.GREENLAND_LOGIN_URL);
//                    internetoff.setVisibility(View.GONE);
//                }
//                else if(getResources().getBoolean(R.bool.is_ACEP_client)){
//                    loginWebViewLink(ServiceConstants.ACEP_LOGIN_URL);
//                   // loginWebViewLink(ServiceConstants.ACEP_LOGIN_URL + generatesSamlRequest());
//                }
//                else {
//                    loginWebViewLink(ServiceConstants.SUMMIT_LOGIN_URL);
//                }
//                internetoff.setVisibility(View.GONE);
//            } else {
//                invalidAccessde();
//                internetoff.setVisibility(View.VISIBLE);
//            }
//        }
//        if (v == mTermsAndCondition) {
//            openWebViewLink(ServiceConstants.TERMS_AND_CONDITION_WORLD_BOOK);
//        }
//        if (v == mPrivacyPolicy) {
//            openWebViewLink(ServiceConstants.PRIVACY_POLICY_WORLD_BOOK);
//        }
//        if (i == R.id.btncancel) {
//
//        } else if (i == R.id.signUp) {
//            if (_listner != null) {
//                try {
//                    _listner.onLoginListnerCodeSuccess(_edtaccesskey.getText().toString().trim(), true);
//                    _edtaccesskey.setEnabled(true);
//                    _btnGo.setEnabled(true);
//                    _edtaccesskey.setText("");
//                } catch (Exception e) {
//                    _edtaccesskey.setEnabled(true);
//                    _btnGo.setEnabled(true);
//                }
//            }
//        } else if (i == R.id.btngo) {
//            Utils.hideKeyboard(mContext, _edtaccesskey);
//            clickOnGo();
//        } else if (i == R.id.btnlogin) {
//            clickOnLogin();
//        } else if (i == R.id.txtforgotpassword) {
//            openForgotPassword();
//        } else if (i == R.id.txtPreview) {
//            _isPasswordVisible = !_isPasswordVisible;
//            togglePasswordVisibility();
//        } else if (v == mSignUp) {
//            openWebViewLink(ServiceConstants.SPIRAL_SIGN_UP_URL);
//        }
//    }
//
//    public String generatesSamlRequest(){
//
//        String samlReq = "<?xml version= 1.0 encoding=UTF-8?><samlp:AuthnRequest xmlns:samlp=urn:oasis:names:tc:SAML:2.0:protocol" +
//                "AssertionConsumerServiceURL=acep://" +
//                "Destination=https://signin.acep.org/idp/SAML/SSOService ForceAuthn=false ID=e793ebc5-8106-457a-90aa-111fdd0bd258" +
//                "IsPassive=false IssueInstant=2019-10-01T07:48:43.206Z" +
//                "ProtocolBinding=urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" +
//                "Version=2.0><saml2:Issuer xmlns:saml2=urn:oasis:names:tc:SAML:2.0:assertion>urn:oasis:names:tc:SAML:2.0:assertion www.acep.org.acepmanaged:ServiceProvider</saml2:Issuer>" +
//                "<saml2p:NameIDPolicy xmlns:saml2p=urn:oasis:names:tc:SAML:2.0:protocol" +
//                "AllowCreate=true Format=urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified/></samlp:AuthnRequest>";
//
//        return "jZJLT8MwEIT%2FSrT3vEp4xGqKChWiEo%2BIBA7cXGdTLCV28Dot%2FHvcpEA5gLhY0Xo8M%2Fns6flb23gbNCS1yiAOIvBQCV1Jtc7gsbzyz%2BB8NiXeNh2b9%2FZFPeBrj2Q9d04RGzYy6I1impMkpniLxKxgxfz2hk2CiHVGWy10A96cCI11QZdaUd%2BiKdBspMDHh5sMuMCOhSF4C%2BcuFbdDoRdrO3JjkmslVbATBdqsQ1l14S4hLIr7vQt4V9oIHEpmUPOG3Gi5yCAVeHaSJImfnFZuSdJjf8VR%2BCvkSVrjURqltVNSzonkBr%2FPEvW4VGS5shlMojj148iPTss4ZkcRS%2BLAzZ7By%2Fc%2FeCHViO0vGqtRROy6LHM%2Fvy9K8J4%2B8TsBjLAnbEg3B5gnfxvzT7gw28m22%2B0XreGj5YqvsWJ7Wq70RlZopuFh3D68Y3fOf7nIdSPF%2B2GH%2F9910%2BjtpUFuHVBr%2BvF6Wm5%2FN4iDeJjIyq8HKesVdShkLbGCcDZW%2FfkMZx8%3D&RelayState=http%3A%2F%2Flocalhost%3A8181%3FarticleId%3D1234";
//                //"jZLNTuMwFIX3SLxD5L3zRzJtraaoUKGpxNCIBBbsXOemWErsjK%2FTzrw9blJmygLEzro%2BPuf4s%2BfXf9rG24NBqVVGIj8kHiihK6l2GXkq7%2BiUXC%2FmyNumY8vevqpH%2BN0DWs%2BdU8iGjYz0RjHNUSJTvAVkVrBi%2BeuexX7IOqOtFroh3hIRjHVBt1ph34IpwOylgKfH%2B4xwAR0LAuKtnLtU3A6FXq3t0I1R7pRU%2FlHka7MLZNUFx4SgKDYnF%2BLdaSNgKJmRmjfoRutVRiazuE6vakG3Na9pkk4SOp0lWyqmacxnsUhTSJwSc44o9%2FD%2FLGIPa4WWK5uROIxmNAppmJThD3Y1ZVHiTybpC%2FHy0wVvpBqxfUVjO4qQ%2FSzLnOaboiTe8zt%2BJyAj7JgN6eYMc%2Fy1MX%2BHSxZH2eFw%2BEdrWLRc8R1U7ETLld7LCsw8OI87hXfswfmvV7lupPh73uH7b900%2BnBrgFsH1Jp%2BfJ6W288NIj8aJrKi9SBlvcIOhKwlVCRYjFU%2FfsPF5cXlxRs%3D";
//    }
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        String item = parent.getItemAtPosition(position).toString();
//        switch (position) {
//            case 0:
//                setLocale("kl");
//                com.hurix.kitaboo.constants.utils.Utils.insertSharedPrefernceStringValues(this,
//                        Constants.SHELF_PREFS_NAME, Constants.DEFAULT_APP_LANGUAGE_POSITION, 0 + "");
//                break;
//            case 1:
//                setLocale("da");
//                com.hurix.kitaboo.constants.utils.Utils.insertSharedPrefernceStringValues(this,
//                        Constants.SHELF_PREFS_NAME, Constants.DEFAULT_APP_LANGUAGE_POSITION, 1 + "");
//                break;
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//
//
//
//    @Override
//    public void requestCompleted(IServiceResponse response) {
//        if (response.getResponseRequestType().equals(Constants.SERVICETYPES.LOGINREQUEST)) {
//            LoginResponse responseObj = (LoginResponse) response;
//            if (responseObj.isSuccess()) {
//                responseObj.setPassword(_passwordEditText.getText().toString().trim());
//                com.hurix.commons.notifier.GlobalDataManager.getInstance().setAccountType(responseObj.getRoleName());
//                long time = System.currentTimeMillis();
//                SharedPreferences pref = mContext.getSharedPreferences(Constants.SHELF_PREFS_NAME, 0);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putLong("LOGIN_TIME", time);
//                editor.commit();
//                responseObj.setLoginTime(time);
//                UserController.getInstance(this)
//                        .fillUserVOFromResponse(responseObj.getUserVoFromResponse());
//
//                DBController.getInstance(this).getManager().insertUser(responseObj.getUserVoFromResponse());
//
//                //Hit for Fetch Category
//                KitabooServiceAPI.getObject().getServiceAdapter().fetchCategory(responseObj.getToken(), this);
//            }
//        } else if (response.getResponseRequestType().equals(Constants.SERVICETYPES.FETCH_CATEGORY_REQUREST)) {
//            Intent intent = null;
//            FetchCategoryResponse fetchCategoryResponse = (FetchCategoryResponse) response;
//            if (fetchCategoryResponse.isSuccess()) {
//                final ArrayList<UserCategoryVO> categoryObjList = fetchCategoryResponse.getCategoryObjList();
//
//                //Insert All Categories Into database :
//                DBController.getInstance(getApplicationContext()).getManager()
//                        .insertCategories(categoryObjList, UserController.getInstance(getApplicationContext()).getUserVO().getUserID(), "");
//
//              //  intent = new Intent(LoginActivity.this, BookShelfActivity.class);
//                intent.putExtra(Constants.FROM_CUSTUME_USER, UserController.getInstance(mContext).getUserVO());
//                intent.putExtra(Constants.CUSTOME_BOOK_ID, UserController.getInstance(mContext).getUserVO());
//
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivityForResult(intent, Constants.BOOKSHELF_REQUEST_CODE);
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            finish();
//        } else if (response.getResponseRequestType().equals(Constants.SERVICETYPES.ACCESSCODEREQUEST)) {
//            AccessCodeServiceResponse responseObj = (AccessCodeServiceResponse) response;
//            if (responseObj.isSuccess()) {
//
//                if (getApplicationContext().getResources().getBoolean(R.bool.LoginScreen_V2)) {
//
//                   // Intent registration = new Intent(this, NewRegistrationActivity.class);
////                    registration.putExtra("isFromSignUp", false);
////                    registration.putExtra("accesscode", _edtaccesskey.getText().toString().trim());
////                    startActivity(registration);
////                    registration.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    finish();
//                    _edtaccesskey.setEnabled(true);
//                    _btnGo.setEnabled(true);
//                    _edtaccesskey.setText("");
//
//                } else {
////                    Intent registration = new Intent(this, RegistrationActivity.class);
////                    registration.putExtra("isFromSignUp", false);
////                    registration.putExtra("accesscode", _edtaccesskey.getText().toString().trim());
////                    startActivity(registration);
////                    registration.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                    finish();
//                    _edtaccesskey.setEnabled(true);
//                    _btnGo.setEnabled(true);
//                    _edtaccesskey.setText("");
//                }
//
//            } else {
//                _edtaccesskey.setEnabled(true);
//                _btnGo.setEnabled(true);
//                _edtaccesskey.setError(mContext.getResources()
//                        .getString(R.string.alert_invalid_accesscode));
//                _edtaccesskey.setBackgroundResource(R.drawable.username_invalid_bg);
//                _edtaccesskey.requestFocus();
//            }
//        } else if (response.getResponseRequestType().equals(Constants.SERVICETYPES.REGISTRATIONREQUEST)) {
//            RegistrationServiceResponse responseObj = (RegistrationServiceResponse) response;
//            if (responseObj.isSuccess()) {
//                DialogUtils.displayToast(mContext, mContext.getResources()
//                        .getString(R.string.porto_registration_success), Toast.LENGTH_LONG, Gravity.CENTER);
//                _edtaccesskey.setEnabled(true);
//                _btnGo.setEnabled(true);
//                _edtaccesskey.setText("");
//            } else {
//                _edtaccesskey.setEnabled(true);
//                _btnGo.setEnabled(true);
//                _edtaccesskey.setError(mContext.getResources()
//                        .getString(R.string.alert_invalid_porto_email));
//                _edtaccesskey.setBackgroundResource(R.drawable.username_invalid_bg);
//                _edtaccesskey.requestFocus();
//            }
//        } else if (response.getResponseRequestType().equals(Constants.SERVICETYPES.FORGOT_PASSWORD_REQUEST_URL)) {
//            dismissProgress();
//            ForgotPasswordResponse response1 = (ForgotPasswordResponse) response;
//            if (response1.isSuccess()) {
//                openWebViewLink(response1.getForgotUrl());
//            } else {
//                callNativeForgotPassword();
//            }
//        }
//    }
//
//    @Override
//    public void requestErrorOccured(ServiceException response) {
//        _userIdEditText.setEnabled(true);
//        _passwordEditText.setEnabled(true);
//        _edtaccesskey.setEnabled(true);
//        _loginButton.setEnabled(true);
//        _btnGo.setEnabled(true);
//        if (response != null) {
//            if (response.getResponseRequestType().equals(Constants.SERVICETYPES.ACCESSCODEREQUEST)) {
//                ServiceException exception = response;
//                if (exception.getInvalidFields() != null && !exception.getInvalidFields().isEmpty()) {
//                    Map.Entry<String, Integer> entry = exception.getInvalidFields()
//                            .entrySet().iterator().next();
//                    DialogUtils.displayToast(mContext, com.hurix.kitaboo.constants.utils.Utils
//                                    .getMessageForError(mContext, entry.getValue()), Toast.LENGTH_LONG,
//                            Gravity.CENTER);
//                }
//                invalidAccesscode();
//            } else if (response.getResponseRequestType().equals(Constants.SERVICETYPES.LOGINREQUEST)) {
//                ServiceException exception = response;
//                if (exception.getInvalidFields() != null && !exception.getInvalidFields().isEmpty()) {
//                    Map.Entry<String, Integer> entry = exception.getInvalidFields().entrySet()
//                            .iterator().next();
//                    if (entry.getValue() == 101) {
//                        DialogUtils.displayToast(mContext, mContext.getResources()
//                                        .getString(R.string.msg_invalid_login),
//                                Toast.LENGTH_LONG, Gravity.CENTER);
//                    } else if (entry.getKey().toString().equalsIgnoreCase("deviceID") && entry.getValue() == 800) {
//                        DialogUtils.displayToast(mContext, mContext.getResources()
//                                        .getString(R.string.msg_max_authorised_device),
//                                Toast.LENGTH_LONG, Gravity.CENTER);
//                    } else {
//                        DialogUtils.displayToast(mContext, com.hurix.kitaboo.constants.utils.Utils
//                                        .getMessageForError(mContext, entry.getValue()), Toast.LENGTH_LONG,
//                                Gravity.CENTER);
//                    }
//                }
//                invalidUser();
//            } else if (response.getResponseRequestType().equals(Constants.SERVICETYPES.FETCH_CATEGORY_REQUREST)) {
//                ServiceException exceptionObj = response;
//                if (exceptionObj != null) {
//                    if (exceptionObj.getInvalidFields() != null && !exceptionObj.getInvalidFields().isEmpty()) {
//                        Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
//                        if (UserController.getInstance(LoginActivity.this).getUserSettings().getIsAutoLogOffEnabled()
//                                && entry.getValue() == 103) {
//                            showSessionExpiredAlert();
//                        } else {
//                            if (entry.getValue() == 103 && exceptionObj.getResponseRequestType() != Constants.SERVICETYPES.REFRESH_USER_TOKEN) {
//                                KitabooServiceAPI.getObject().getServiceAdapter()
//                                        .refreshUserToken(UserController.getInstance(mContext).getUserVO().getToken(), exceptionObj.getResponseRequestType().toString(), this);
//                            } else if (exceptionObj.getResponseRequestType().equals(Constants.SERVICETYPES.REFRESH_USER_TOKEN)) {
//                                DialogUtils.displayToast(mContext, mContext.getResources().getString(R.string.alert_session_expired), Toast.LENGTH_SHORT, Gravity.CENTER);
//                            }
//                        }
//                    }
//                }
//            } else if (response.getResponseRequestType().equals(Constants.SERVICETYPES.REGISTRATIONREQUEST)) {
//                ServiceException exception = response;
//                if (exception.getInvalidFields() != null && !exception.getInvalidFields().isEmpty()) {
//                    Map.Entry<String, Integer> entry = exception.getInvalidFields()
//                            .entrySet().iterator().next();
//                    DialogUtils.displayToast(mContext, com.hurix.kitaboo.constants.utils.Utils
//                                    .getMessageForError(mContext, entry.getValue()), Toast.LENGTH_LONG,
//                            Gravity.CENTER);
//                }
//                invalidAccesscode();
//            } else if (response.getResponseRequestType().equals(Constants.SERVICETYPES.FORGOT_PASSWORD_REQUEST_URL)) {
//                dismissProgress();
//                callNativeForgotPassword();
//            }
//        } else {
//            dismissProgress();
//            DialogUtils.displayToast(mContext, com.hurix.kitaboo.constants.utils.Utils
//                    .getMessageForError(mContext, 400), Toast.LENGTH_LONG, Gravity.CENTER);
//        }
//    }
//
//    public void showSessionExpiredAlert() {
//        DialogUtils.showOKAlert(new View(mContext), 1, mContext, getResources()
//                        .getString(R.string.alert_title), getResources().getString(R.string.alert_session_expired),
//                new OnDialogOkActionListner() {
//                    @Override
//                    public void onOKClick(View v) {
//                        DBController.getInstance(mContext).getManager()
//                                .logoutUserByID(UserController.getInstance(mContext).getUserVO().getUserID());
//                        com.hurix.kitaboo.constants.utils.Utils.startLauncherActivity(mContext);
//                    }
//                });
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (IsDoubleBackPressed) {
//            super.onBackPressed();
//            return;
//        }
//        this.IsDoubleBackPressed = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//    }
//
//    public void invalidUser() {
//        Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shakefordialog);
//        _userIdEditText.startAnimation(shake);
//        _passwordEditText.startAnimation(shake);
//        _loginButton.setEnabled(true);
//        _loginButton.setText(mContext.getResources().getString(R.string.login_header_text));
//        _userIdEditText.requestFocus();
//        setClientConfig();
//    }
//
//    public void invalidAccesscode() {
//        Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shakefordialog);
//        _edtaccesskey.startAnimation(shake);
//        _edtaccesskey.requestFocus();
//        _btnGo.setEnabled(true);
//    }
//
//    public void invalidAccessde() {
//        Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shakefordialog);
//        loginsummit.startAnimation(shake);
//    }
//
//
//    private void dismissProgress() {
//        if (mDialog != null) {
//            mDialog.dismiss();
//        }
//    }
//
//    private void clickOnLogin() {
//        if (Utils.checkInternetConnection(mContext)) {
//            String strUserName = _userIdEditText.getText().toString().trim();
//            String strPassword = _passwordEditText.getText().toString();
//
//            if (isDataValid(strUserName, strPassword)) {
//
//                if (mContext.getResources().getBoolean(R.bool.webview_login)) {
//                    Constants.CUSTOME_URL_TOKEN = GlobalDataManager.getInstance().getCustomeUrlToken();
//                    KitabooServiceAPI.getObject().getServiceAdapter()
//                            .validateUserToken(Constants.CUSTOME_URL_TOKEN, this);
//                } else {
//                    _userIdEditText.setEnabled(false);
//                    _passwordEditText.setEnabled(false);
//                    _loginButton.setEnabled(false);
//                    _loginButton.setText(mContext.getResources().getString(R.string.please_wait));
//                    UserController.getInstance(mContext).getUserVO().setUserName(strUserName);
//                    UserController.getInstance(mContext).getUserVO().setUserPassword(strPassword);
//                    KitabooServiceAPI.getObject().getServiceAdapter()
//                            .login(strUserName, strPassword, this);
//                }
//
//            }
//        } else {
//            _loginButton.setEnabled(true);
//            DialogUtils.displayToast(mContext, mContext.getResources()
//                            .getString(R.string.network_error),
//                    Toast.LENGTH_LONG, Gravity.CENTER);
//        }
//    }
//
//    private void togglePasswordVisibility() {
//        if (_isPasswordVisible) {
//            _passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
//            _txtPreview.setText(ShelfUIConstants.SHELF_LOGIN_PREVIEW_IC_ENABLE_TEXT);
//            _passwordEditText.setTypeface(mLoginHeader.getTypeface());
//        } else {
//            _passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            _passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            _txtPreview.setText(ShelfUIConstants.SHELF_LOGIN_PREVIEW_IC_DISABLE_TEXT);
//            _passwordEditText.setTypeface(mLoginHeader.getTypeface());
//        }
//        if (_passwordEditText.getText().toString().trim().length() > 0) {
//            _passwordEditText.setSelection(_passwordEditText.getText().length());
//            _passwordEditText.setTypeface(mLoginHeader.getTypeface());
//        }
//    }
//
//    private void openForgotPassword() {
//        if (com.hurix.kitaboo.constants.utils.Utils.isOnline(this)) {
//            if (!this.getResources().getString(R.string.clientid).isEmpty()) {
//                KitabooServiceAPI.getObject().getServiceAdapter()
//                        .fetchForgotPasswordWebURL(Constants.FORGOT_PASSWORD_TYPE, this);
//                showProgress();
//            } else {
//                callNativeForgotPassword();
//            }
//        } else {
//            DialogUtils.displayToast(this, mContext.getResources()
//                    .getString(R.string.network_not_available_msg), Toast.LENGTH_SHORT, Gravity.CENTER);
//        }
//    }
//
//    private void callNativeForgotPassword() {
//        Intent forgotPassword = null;
//        if (mContext.getResources().getBoolean(R.bool.LoginScreen_V2)) {
//          //  forgotPassword = new Intent(this, NewForgotScreen.class);
//        } else {
//            forgotPassword = new Intent(this, ForgotPasswordActivity.class);
//        }
//        forgotPassword.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(forgotPassword);
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//        finish();
//
//    }
//
//    private void openWebViewLink(String path) {
//        Intent webViewLink = new Intent(mContext, LinkWebViewPlayer.class);
//        Bundle b = new Bundle();
//        b.putBoolean("isOriantationLocked", false);
//        b.putString("path", path);
//        webViewLink.putExtras(b);
//        webViewLink.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//        mContext.startActivity(webViewLink);
//
//    }
//
//    private void loginWebViewLink(String path) {
////        Intent webViewLink = new Intent(mContext, WebViewlogin.class);
////        Bundle b = new Bundle();
////        b.putBoolean("isOriantationLocked", false);
////        b.putString("path", path);
////        webViewLink.putExtras(b);
////        webViewLink.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
////        mContext.startActivity(webViewLink);
//
//    }
//
//    private boolean isDataValid(String userName, String password) {
//        boolean isDataValid = true;
//        if (userName.length() == 0) {
//            _userIdEditText.setError(mContext.getResources().getString(R.string.alert_empty_mail_id));
//            _userIdEditText.requestFocus();
//            isDataValid = false;
//        } else if (userName.length() > USER_NAME_CHARACTER_UPER_LIMIT) {
//            _userIdEditText.setError(String.format(mContext.getResources()
//                    .getString(R.string.alert_emailid_character_limit), ""
//                    + USER_NAME_CHARACTER_UPER_LIMIT));
//            _userIdEditText.requestFocus();
//            isDataValid = false;
//        } else if (password.length() == 0) {
//            _passwordEditText.setError(mContext.getResources().getString(R.string.alert_empty_password));
//            _txtPreview.setVisibility(View.INVISIBLE);
//            _passwordEditText.requestFocus();
//            isDataValid = false;
//        } else if (password.length() > PASSWORD_CHARACTER_UPER_LIMIT) {
//            _passwordEditText.setError(String.format(mContext.getResources()
//                            .getString(R.string.alert_password_character_limit),
//                    "1", "" + PASSWORD_CHARACTER_UPER_LIMIT));
//            _txtPreview.setVisibility(View.INVISIBLE);
//            _passwordEditText.requestFocus();
//            isDataValid = false;
//        } else if (mContext.getResources().getBoolean(R.bool.user_name_email_validation) &&
//                !userName.matches(EMAIL_REGULAR_EXPRESSION)) {
//            if (getResources().getBoolean(R.bool.kois_Login_Error_Message)) {
//                DialogUtils.displayToast(mContext, mContext.getResources()
//                        .getString(R.string.msg_invalid_login_kois), Toast.LENGTH_SHORT, Gravity.CENTER);
//            } else {
//                DialogUtils.displayToast(mContext, mContext.getResources()
//                        .getString(R.string.msg_invalid_login), Toast.LENGTH_SHORT, Gravity.CENTER);
//            }
//            isDataValid = false;
//        } else {
//            //user id is valid.
//            _userIdEditText.setError(null);
//
//            //Validate password
//            if (password.length() == 0) {
//                _passwordEditText.setError(mContext.getResources().getString(R.string.alert_empty_password));
//                _txtPreview.setVisibility(View.INVISIBLE);
//                _passwordEditText.requestFocus();
//                isDataValid = false;
//            } else {
//                _passwordEditText.setError(null);
//                _txtPreview.setVisibility(View.VISIBLE);
//            }
//        }
//        return isDataValid;
//    }
//
//    private void clickOnGo() {
//        if (Utils.checkInternetConnection(mContext)) {
//            if (_edtaccesskey.getText().toString().trim().length() != 0) {
//                _btnGo.setEnabled(false);
//                _edtaccesskey.setEnabled(false);
//                if (com.hurix.kitaboo.constants.utils.Utils.clientIDCheck(mContext).equals(Constants.PORTO)) {
//                    if (_edtaccesskey.getText().toString().matches(EMAIL_REGULAR_EXPRESSION)) {
//                        KitabooServiceAPI.getObject().getServiceAdapter()
//                                .registrationOfUser(EMPTY, _edtaccesskey.getText().toString().trim(),
//                                        EMPTY, EMPTY, EMPTY, EMPTY, this);
//                    } else {
//                        DialogUtils.displayToast(mContext, mContext.getResources()
//                                .getString(R.string.alert_invalid_porto_email), Toast.LENGTH_SHORT, Gravity.CENTER);
//                        _btnGo.setEnabled(true);
//                        _edtaccesskey.setEnabled(true);
//                    }
//                } else {
//                    KitabooServiceAPI.getObject().getServiceAdapter()
//                            .accessCodeValidator(_edtaccesskey
//                                    .getText().toString().trim(), "", this);
//                }
//            } else {
//                _edtaccesskey.setError(mContext.getResources().getString(R.string.alert_empty_accesscode));
//                _edtaccesskey.requestFocus();
//            }
//        } else {
//            _btnGo.setEnabled(true);
//            DialogUtils.displayToast(mContext, mContext.getResources().getString(R.string.network_error),
//                    Toast.LENGTH_LONG, Gravity.CENTER);
//        }
//    }
//
//    private void showProgress() {
//        if (mDialog == null) {
//            mDialog = new ProgressDialog(LoginActivity.this);
//            mDialog.setMessage(mContext.getResources().getString(R.string.please_wait));
//            mDialog.setCancelable(false);
//            mDialog.setIndeterminate(true);
//            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        }
//        mDialog.show();
//    }
//
//    private void initView() {
//        setDefaultThemeColor();
//
//        mPrivacyPolicy = (TextView) findViewById(R.id.privacyPolicy);
//        mAnd = findViewById(R.id.and);
//        mTermsAndCondition = (TextView) findViewById(R.id.terms);
//        mTermsAndConditionContainer = (LinearLayout) findViewById(R.id.terms_condition_container);
//        if (getResources().getBoolean(R.bool.is_it_worldbook)) {
//            mPrivacyPolicy.setVisibility(View.VISIBLE);
//            mAnd.setVisibility(View.VISIBLE);
//            mTermsAndCondition.setVisibility(View.VISIBLE);
//            mTermsAndConditionContainer.setVisibility(View.VISIBLE);
//        } else {
//            mPrivacyPolicy.setVisibility(View.GONE);
//            mAnd.setVisibility(View.GONE);
//            mTermsAndCondition.setVisibility(View.GONE);
//            mTermsAndConditionContainer.setVisibility(View.GONE);
//        }
//        _newToProfil = (TextView) findViewById(R.id.txtNewToProfil);
//        _txtSignUp = (TextView) findViewById(R.id.signUp);
//        _txtSignUp.setPaintFlags(_txtSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//
//        if (mContext.getResources().getBoolean(R.bool.new_to_profili)) {
//            _newToProfil.setVisibility(View.VISIBLE);
//            _txtSignUp.setVisibility(View.VISIBLE);
//        } else {
//            _newToProfil.setVisibility(View.GONE);
//            _txtSignUp.setVisibility(View.GONE);
//        }
//        _tvPowerByID = (TextView) findViewById(R.id.tvPowerByID);
//        _kitabooLogo = (TextView) findViewById(R.id.kitabooLogo);
//
//        if (mContext.getResources().getBoolean(R.bool.show_powered_by_kitaboo)) {
//            _tvPowerByID.setVisibility(View.VISIBLE);
//            _kitabooLogo.setVisibility(View.VISIBLE);
//        } else {
//            _tvPowerByID.setVisibility(View.GONE);
//            _kitabooLogo.setVisibility(View.GONE);
//        }
//        Typeface typeFace = Typefaces.get(mContext, mContext.getResources().getString(R.string.kitabooreader_font_file_name));
//        _kitabooLogo.setTypeface(typeFace);
//        _kitabooLogo.setAllCaps(false);
//        _kitabooLogo.setText(ShelfUIConstants.POWEREDBY_LOGO_IC_TEXT);
//        mTypeface = Typefaces.get(mContext, mContext.getResources().getString(R.string.text_font_file));
//        _btnGo = (TextView) findViewById(R.id.btngo);
//        mScrollView = (ScrollView) findViewById(R.id.login_scroll);
//        _loginLayout = (LinearLayout) findViewById(R.id.login_layout);
//        _edtaccesskey = (EditText) findViewById(R.id.edtaccesskey);
//        _userIdEditText = (EditText) findViewById(R.id.useridEditText);
//        _userIdEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(USER_NAME_CHARACTER_UPER_LIMIT)});
//
//        mSignUpLayout = (LinearLayout) findViewById(R.id.signUplayout);
//        _loginButton = (Button) findViewById(R.id.btnlogin);
//        mLoginHeader = (TextView) findViewById(R.id.txtloginHeader);
//        mSignUp = (Button) findViewById(R.id.btnSignUp);
//        mNoAccount = (TextView) findViewById(R.id.tvNoAccount);
//        mHelpAccess = (TextView) findViewById(R.id.txtuserid);
//        mHelpLogin = (TextView) findViewById(R.id.txtloginhelp);
//        mTitleAccess = (TextView) findViewById(R.id.textView4);
//        _mLoginTopcontainer = (LinearLayout) findViewById(R.id.LogintopContainer);
//
//        _loginButton.setEnabled(false);
//        _loginButton.setAlpha(0.5f);
//        _btnGo.setEnabled(false);
//        _btnGo.setAlpha(0.5f);
//        _passwordEditText = (EditText) findViewById(R.id.passwordEditText);
//        _passwordEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(PASSWORD_CHARACTER_UPER_LIMIT)});
//        mPasswordLayout = (RelativeLayout) findViewById(R.id.password_layout);
//        _userIdEditText.setTypeface(mLoginHeader.getTypeface());
//        _passwordEditText.setTypeface(mLoginHeader.getTypeface());
//        _passwordEditText.setHint("Password");
//        mUserSettingVO = UserController.getInstance(mContext).getUserSettings();
//        _loginButton.setBackgroundColor(Color.parseColor(mUserSettingVO.getLoginButtonBackground()));
//        _loginButton.setTextColor(Color.parseColor(mUserSettingVO.getLoginButtonColor()));
//        _kitabooLogo.setTextColor(Color.parseColor(mUserSettingVO.getmKitabooLogoColor()));
//        _tvPowerByID.setTextColor(Color.parseColor(UserController.getInstance(getApplicationContext())
//                .getUserSettings().getmKitabooTextColor()));
//        _mLoginTopcontainer.setBackgroundColor(Color.parseColor(UserController.getInstance(getApplicationContext())
//                .getUserSettings().getProfileHeaderBackground()));
//        if (mContext.getResources().getInteger(R.integer.minimum_password_limit) != 0) {
//            mPasswordMinCharLimit = mContext.getResources().getInteger(R.integer.minimum_password_limit);
//        }
//        if (mContext.getResources().getInteger(R.integer.minimum_username_limit) != 0) {
//            mUsernameMinCharLimit = mContext.getResources().getInteger(R.integer.minimum_username_limit);
//        }
//        if (!mContext.getResources().getBoolean(R.bool.show_sign_up)) {
//            mSignUpLayout.setVisibility(View.GONE);
//        }
//        _txtForgotpassword = (TextView) findViewById(R.id.txtforgotpassword);
//        _txtPreview = (TextView) findViewById(R.id.txtPreview);
//        mTopContainerDivider = findViewById(R.id.topContainerDivider);
//        mTopAccessCodeDivider = findViewById(R.id.divider);
//        _txtForgotpassword.setPaintFlags(_txtForgotpassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        _txtForgotpassword.setTextColor(Color.parseColor(mUserSettingVO.getLoginForgotPasswordColor()));
//        try {
//            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
//            f.setAccessible(true);
//            f.set(_edtaccesskey, R.drawable.cursor);
//            f.set(_userIdEditText, R.drawable.cursor);
//            f.set(_passwordEditText, R.drawable.cursor);
//        } catch (Exception e) {
//
//        }
//        if (mContext.getResources().getBoolean(R.bool.border_visibility_under_logo)) {
//            mTopContainerDivider.setVisibility(View.VISIBLE);
//        }
//        if (_mUserVO != null) {
//            DBController.getInstance(this).getManager().logoutAllUsers();
//            long userID = DBController.getInstance(this).getManager().insertUser(_mUserVO);
//            String strBookshelfType = com.hurix.kitaboo.constants.utils.Utils.getSharedPreferenceStringValue(
//                    LoginActivity.this, Constants.SHELF_PREFS_NAME, Constants.BOOKSHELF_TYPE, "");
//            if (!(strBookshelfType.equalsIgnoreCase(KitabooBookShelfType.BOOK_COLLECTION.toString()))) {
//                DBController.getInstance(this).getManager().insertCategory(Constants.DEFAULT_BOOK_CATEGORY, userID);
//            }
//        }
//
//        if (Constants.IS_DEBUG_ENABLED) {
//
//        }
//        setParams();
//        mPrivacyPolicy.setOnClickListener(this);
//        mTermsAndCondition.setOnClickListener(this);
//        _btnGo.setOnClickListener(this);
//        _loginButton.setOnClickListener(this);
//        _txtForgotpassword.setOnClickListener(this);
//        _txtPreview.setOnClickListener(this);
//        mSignUp.setOnClickListener(this);
//        _passwordEditText.setOnEditorActionListener(this);
//        _edtaccesskey.setOnEditorActionListener(this);
//        _edtaccesskey.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                _txtPreview.setVisibility(View.VISIBLE);
//                if (s.length() > 0) {
//                    _btnGo.setEnabled(true);
//                    _btnGo.setAlpha(1);
//                    _edtaccesskey.setError(null);
//                    _userIdEditText.setError(null);
//                    _passwordEditText.setError(null);
//                } else {
//                    _btnGo.setEnabled(false);
//                    _btnGo.setAlpha(0.5f);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        _userIdEditText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                if (s.length() > 0) {
//                    if (_passwordEditText.getText().toString().length() >= mPasswordMinCharLimit &&
//                            _userIdEditText.getText().toString().length() >= mUsernameMinCharLimit) {
//                        _loginButton.setEnabled(true);
//                        _loginButton.setAlpha(1);
//                    }
//                    _edtaccesskey.setError(null);
//                    _userIdEditText.setError(null);
//                    _passwordEditText.setError(null);
//                    _txtPreview.setVisibility(View.VISIBLE);
//                } else {
//                    _loginButton.setEnabled(false);
//                    _loginButton.setAlpha(0.5f);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        _passwordEditText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                _passwordEditText.setError(null);
//                _txtPreview.setVisibility(View.VISIBLE);
//                if (s.length() >= mPasswordMinCharLimit) {
//
//                    if (!_userIdEditText.getText().toString().isEmpty()) {
//                        _loginButton.setEnabled(true);
//                        _loginButton.setAlpha(1);
//                    }
//                    _edtaccesskey.setError(null);
//                    _userIdEditText.setError(null);
//
//                } else {
//                    _loginButton.setEnabled(false);
//                    _loginButton.setAlpha(0.5f);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        _edtaccesskey.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    if (!_passwordEditText.getText().toString().isEmpty()) {
//                        _loginButton.setEnabled(false);
//                        _loginButton.setAlpha(0.5f);
//                        _passwordEditText.setText(Constants.BLANK);
//                    }
//                    if (!_userIdEditText.getText().toString().isEmpty()) {
//                        _loginButton.setEnabled(false);
//                        _loginButton.setAlpha(0.5f);
//                        _userIdEditText.setText(Constants.BLANK);
//                    }
//                }
//            }
//        });
//
//        _userIdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    if (!_edtaccesskey.getText().toString().isEmpty()) {
//                        _btnGo.setEnabled(false);
//                        _btnGo.setAlpha(0.5f);
//                        _edtaccesskey.setText(Constants.BLANK);
//                    }
//                }
//            }
//        });
//
//        _passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    if (!_edtaccesskey.getText().toString().isEmpty()) {
//                        _btnGo.setEnabled(false);
//                        _btnGo.setAlpha(0.5f);
//                        _edtaccesskey.setText(Constants.BLANK);
//                    }
//                }
//            }
//        });
//        setupConfig();
//        setClientConfig();
//        setThemeChanges();
//        if (!mUserSettingVO.getLoginHeaderFontFace().isEmpty()) {
//            setCustomTypeFace();
//        }
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        if(getResources().getBoolean(R.bool.is_nanoq_greenland)) {
//            if (mLocale != null) {
//                newConfig.locale = mLocale;
//                Locale.setDefault(mLocale);
//                getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
//
//            }
//            mLangText.setText(R.string.language);
//            loginsummit.setText(getResources().getString(R.string.login_btn_text));
//            super.onConfigurationChanged(newConfig);
//        }else {
//            super.onConfigurationChanged(newConfig);
//        }
//        if (mContext.getResources().getBoolean(R.bool.webview_login)) {
//            setContentView(R.layout.webviewloginactivity);
//            WebViewinit();
//        }
//    }
//    private void setLocale(String localeName) {
//        mLocale = new Locale(localeName);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = mLocale;
//        res.updateConfiguration(conf, dm);
//        onConfigurationChanged(conf);
//        com.hurix.kitaboo.constants.utils.Utils.insertSharedPrefernceStringValues(this,
//                Constants.SHELF_PREFS_NAME, Constants.DEFAULT_APP_LANGUAGE, localeName);
//
//    }
//
//
//    public void setParams() {
//        if (com.hurix.kitaboo.constants.utils.Utils.isMobile(this)) {
//
//        }
//        String strBookShelfType = com.hurix.kitaboo.constants.utils.Utils.getSharedPreferenceStringValue(
//                mContext, Constants.SHELF_PREFS_NAME, Constants.BOOKSHELF_TYPE, "");
//        if (strBookShelfType.equalsIgnoreCase(KitabooBookShelfType.MOBILE.toString())) {
//            ViewGroup.LayoutParams params = _loginLayout.getLayoutParams();
//            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                params.width = mContext.getResources().getDisplayMetrics().widthPixels - 48;
//            } else {
//                params.width = mContext.getResources().getDisplayMetrics().heightPixels - 48;
//            }
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            _loginLayout.setLayoutParams(params);
//        } else if ((com.hurix.kitaboo.constants.utils.Utils.isMobile(this) && (strBookShelfType.equalsIgnoreCase(KitabooBookShelfType.BOOK_COLLECTION.toString())))) {
//            ViewGroup.LayoutParams params = _loginLayout.getLayoutParams();
//            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                params.width = mContext.getResources().getDisplayMetrics().widthPixels - 48;
//            } else {
//                params.width = mContext.getResources().getDisplayMetrics().heightPixels - 48;
//            }
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            _loginLayout.setLayoutParams(params);
//        }
//    }
//
//    @SuppressLint("StringFormatInvalid")
//    private void setupConfig() {
//        _txtForgotpassword.setVisibility(mContext.getResources()
//                .getBoolean(R.bool.show_forgot_password) ? View.VISIBLE : View.GONE);
//        findViewById(R.id.institutegholder).setVisibility((mContext.getResources()
//                .getBoolean(R.bool.show_access_code) ? View.VISIBLE : View.GONE));
//        mTopAccessCodeDivider.setVisibility((mContext.getResources()
//                .getBoolean(R.bool.show_access_code) ? View.VISIBLE : View.GONE));
//        Typeface typeFace = Typefaces.get(mContext, mContext.getResources()
//                .getString(R.string.kitabooreader_font_file_name));
//        _btnGo.setTypeface(typeFace);
//        _btnGo.setAllCaps(false);
//        _btnGo.setText(ShelfUIConstants.SHELF_GO_IC_ENABLE_TEXT);
//        _btnGo.setTextColor(Color.parseColor(mUserSettingVO.getLoginButtonBackground()));
//        _txtPreview.setTypeface(typeFace);
//        _txtPreview.setAllCaps(false);
//        _txtPreview.setText(ShelfUIConstants.SHELF_LOGIN_PREVIEW_IC_DISABLE_TEXT);
//        _txtPreview.setTextColor(Color.parseColor(mUserSettingVO.getmToggleIconColor()));
//        _userIdEditText.getBackground().setColorFilter(Color.parseColor(UserController.getInstance(mContext)
//                        .getUserSettings().getLoginInputBorderColor()),
//                PorterDuff.Mode.SRC_ATOP);
//        _userIdEditText.setHint(mContext.getResources().getString(R.string.login_username_hint_text));
//
//        try {
//            mPasswordLayout.getBackground().setColorFilter(Color.parseColor(UserController.getInstance(mContext)
//                            .getUserSettings().getLoginInputBorderColor()),
//                    PorterDuff.Mode.SRC_ATOP);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        _edtaccesskey.getBackground().setColorFilter(Color.parseColor(UserController.getInstance(mContext)
//                        .getUserSettings().getLoginInputBorderColor()),
//                PorterDuff.Mode.SRC_ATOP);
//        if (!getResources().getBoolean(R.bool.is_it_worldbook)) {
//            mHelpLogin.setText(String.format(mContext.getResources()
//                    .getString(R.string.dialog_login_kitaboo_message_text), mContext.getResources().
//                    getString(R.string.account_sync_label)));
//        } else {
//            mHelpLogin.setText(String.format(mContext.getResources()
//                    .getString(R.string.login_help_worldbook)));
//        }
//
//        if (com.hurix.kitaboo.constants.utils.Utils.isMobile(this)) {
//            _txtPreview.setPadding(0, 8, 0, 0);
//            _tvPowerByID.setPadding(0, 0, 0, 0);
//        } else {
//            _txtPreview.setPadding(0, 10, 0, 0);
//        }
//    }
//
//    private void setClientConfig() {
//        if (com.hurix.kitaboo.constants.utils.Utils.clientIDCheck(mContext).equals(Constants.PORTO)) {
//            mHelpLogin.setVisibility(View.GONE);
//            _txtForgotpassword.setVisibility(View.INVISIBLE);
//            mTopAccessCodeDivider.setBackgroundResource(R.drawable.straight_line);
//            Typeface typeFace = Typefaces.get(mContext, mContext.getResources()
//                    .getString(R.string.kitabooreader_font_file_name));
//            _btnGo.setTypeface(typeFace);
//            _btnGo.setAllCaps(false);
//            _btnGo.setText("Q");
//            _btnGo.setTextColor(Color.parseColor(mUserSettingVO.getLoginButtonBackground()));
//            _loginButton.setText(mContext.getResources().getString(R.string.login_btn_text));
//            mHelpAccess.setVisibility(View.GONE);
//            mTitleAccess.setText(mContext.getResources().getString(R.string.porto_title_access));
//            _edtaccesskey.setHint(mContext.getResources().getString(R.string.porto_hint_access));
//            _edtaccesskey.setKeyListener(DigitsKeyListener.getInstance(mContext.getResources()
//                    .getString(R.string.emailid_field_character_allowed)));
//            _edtaccesskey.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//            InputFilter[] fArray = new InputFilter[1];
//            fArray[0] = new InputFilter.LengthFilter(USER_NAME_CHARACTER_UPER_LIMIT);
//            _edtaccesskey.setFilters(fArray);
//        } else {
//
//        }
//        if (mContext.getResources().getBoolean(R.bool.login_back_arrow)) {
//        }
//
//        if (!mContext.getResources().getBoolean(R.bool.show_login_help)) {
//            mHelpLogin.setVisibility(View.GONE);
//        }
//    }
//
//    private void setThemeChanges() {
//        mScrollView.setBackgroundColor(Color.parseColor(mUserSettingVO.getLoginBackground()));
//        mLoginHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getLoginHeaderFontSize()));
//        mLoginHeader.setTextColor(Color.parseColor(mUserSettingVO.getLoginHeaderColor()));
//        _userIdEditText.setBackgroundDrawable(com.hurix.kitaboo.constants.utils.Utils.
//                getRectAngleDrawable(Color.parseColor(mUserSettingVO.getLoginInputColor()),
//                        new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mUserSettingVO.getLoginInputBorderColor())));
//        mPasswordLayout.setBackgroundDrawable(com.hurix.kitaboo.constants.utils.Utils.
//                getRectAngleDrawable(Color.parseColor(mUserSettingVO.getLoginInputColor()),
//                        new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mUserSettingVO.getLoginInputBorderColor())));
//        _edtaccesskey.setBackgroundDrawable(com.hurix.kitaboo.constants.utils.Utils.
//                getRectAngleDrawable(Color.parseColor(mUserSettingVO.getLoginInputColor()),
//                        new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mUserSettingVO.getLoginInputBorderColor())));
//        mNoAccount.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getLoginOtherLinkFontSize()));
//        mNoAccount.setTextColor(Color.parseColor(mUserSettingVO.getLoginOtherLinkColor()));
//        mSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getLoginButtonFontSize()));
//        mSignUp.setTextColor(Color.parseColor(mUserSettingVO.getLoginButtonColor()));
//        mSignUp.setBackgroundColor(Color.parseColor(mUserSettingVO.getLoginButtonBackground()));
//        mLangText.setBackgroundColor(Color.parseColor(mUserSettingVO.getLoginButtonBackground()));
//    }
//
//    private void setCustomTypeFace() {
//        mLoginHeader.setTypeface(mTypeface);
//        _userIdEditText.setTypeface(mTypeface);
//        _passwordEditText.setTypeface(mTypeface);
//        _txtForgotpassword.setTypeface(mTypeface);
//        _loginButton.setTypeface(mTypeface);
//        mNoAccount.setTypeface(mTypeface);
//        mSignUp.setTypeface(mTypeface);
//        _edtaccesskey.setTypeface(mTypeface);
//    }
//
//    public void setOnAccessCodeListner(LoginListner listner) {
//        this._listner = listner;
//    }
//
//    public void setDefaultThemeColor() {
//        try {
//            UserSettingVO localVo = UserController.getInstance(getApplicationContext()).getUserSettings();
//            JSONObject response = new JSONObject(com.hurix.kitaboo.constants.utils.Utils
//                    .getJsonString(getResources()
//                            .getString(R.string.kitaboo_theme_file_name), this, ""));
//            JSONObject jKitabooLogoObj = response.getJSONObject(ThemeColorConstant.KITABOO_LOGO);
//            if (jKitabooLogoObj.has(ThemeColorConstant.LOGO_COLOR)) {
//                localVo.setmKitabooLogoColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jKitabooLogoObj, ThemeColorConstant.LOGO_COLOR));
//            }
//            if (jKitabooLogoObj.has(ThemeColorConstant.TEXT_COLOR)) {
//                localVo.setmKitabooTextColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jKitabooLogoObj, ThemeColorConstant.TEXT_COLOR));
//            }
//
//            JSONObject jbookshelfObj = response.getJSONObject(ThemeColorConstant.BOOKSHELF);
//            if (jbookshelfObj.has(ThemeColorConstant.HEADER)) {
//                JSONObject jHeaderObj = jbookshelfObj.getJSONObject(ThemeColorConstant.HEADER);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.setBookShelfHeader(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.setBookShelfIconsColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//            }
//            if (jbookshelfObj.has(ThemeColorConstant.SIDE_MENU_ITEM)) {
//                JSONObject jHeaderObj = jbookshelfObj.getJSONObject(ThemeColorConstant.SIDE_MENU_ITEM);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.setSideMenuBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.DOTTED_LINE)) {
//                    localVo.setSideMenuDottedLine(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.DOTTED_LINE));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.HEADER_TITLE)) {
//                    JSONObject jSideObject = jHeaderObj.getJSONObject(ThemeColorConstant.HEADER_TITLE);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.COLOR)) {
//                        localVo.setSideMenuHeaderTitleColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setSideMenuHeaderTitleBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setSideMenuHeaderTitleFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setSideMenuHeaderTitleFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.CATEGORY_ITEM)) {
//                    JSONObject jSideObject = jHeaderObj.getJSONObject(ThemeColorConstant.CATEGORY_ITEM);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.COLOR)) {
//                        localVo.setSideMenuCategoryItemColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.ICON_COLOR)) {
//                        localVo.setSideMenuCategoryItemIconColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.ICON_COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setSideMenuCategoryItemBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.SELECTED_BACKGROUND)) {
//                        localVo.setSideMenuCategoryItemSelectedBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.SELECTED_BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setSideMenuCategoryItemFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setSideMenuCategoryItemFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.PROFILE_SETTING)) {
//                    JSONObject jSideObject = jHeaderObj.getJSONObject(ThemeColorConstant.PROFILE_SETTING);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.COLOR)) {
//                        localVo.setSideMenuProfileSettingColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setSideMenuProfileSettingBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.ICON_COLOR)) {
//                        localVo.setSideMenuProfileSettingIconColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.ICON_COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setSideMenuProfileSettingFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setSideMenuProfileSettingFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.SIGN_OUT)) {
//                    JSONObject jSideObject = jHeaderObj.getJSONObject(ThemeColorConstant.SIGN_OUT);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.COLOR)) {
//                        localVo.setSideMenuSignOutColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setSideMenuSignOutBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.ICON_COLOR)) {
//                        localVo.setSideMenuSignOutIconColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.ICON_COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setSideMenuSignOutFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jSideObject, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setSideMenuSignOutFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jSideObject, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//            }
//            if (jbookshelfObj.has(ThemeColorConstant.CAROUSEL)) {
//                JSONObject jHeaderObj = jbookshelfObj.getJSONObject(ThemeColorConstant.CAROUSEL);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.setBookShelfDownloadedArea(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.setBookShelfUsertxt(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLLECTION_BACKGROUND)) {
//                    localVo.setBookCollectionBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLLECTION_BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLLECTION_COLOR)) {
//                    localVo.setBookCollectionColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLLECTION_COLOR));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.FONT_SIZE)) {
//                    localVo.setBookShelfFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.FONT_SIZE));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.HEADER_FONT_SIZE)) {
//                    localVo.setBookShelfHeaderFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.HEADER_FONT_SIZE));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.FONT_FACE)) {
//                    localVo.setBookShelfHeaderFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.FONT_FACE));
//                }
//            }
//            if (jbookshelfObj.has(ThemeColorConstant.DOWNLOAD_ALL)) {
//                JSONObject jHeaderObj = jbookshelfObj.getJSONObject(ThemeColorConstant.DOWNLOAD_ALL);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.setDownloadColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.FONT_SIZE)) {
//                    localVo.setDownloadFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.FONT_SIZE));
//                }
//            }
//            if (jbookshelfObj.has(ThemeColorConstant.SELECTED_BOOK)) {
//                JSONObject jHeaderObj = jbookshelfObj.getJSONObject(ThemeColorConstant.SELECTED_BOOK);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.set_bookshelf_book_selection_background(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.set_bookshelf_book_selection_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.INNER_SELECTED_BACKGROUND)) {
//                    localVo.setBookSelectedBorderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.INNER_SELECTED_BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.INNER_UNSELECTED_BACKGROUND)) {
//                    localVo.setBookUnselectedBorderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.INNER_UNSELECTED_BACKGROUND));
//                }
//            }
//            if (jbookshelfObj.has(ThemeColorConstant.BOOK_DETAILS)) {
//                JSONObject jHeaderObj = jbookshelfObj.getJSONObject(ThemeColorConstant.BOOK_DETAILS);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.set_bookshelf_book_detail_background(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.set_bookshelf_book_detail_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//            }
//            if (jbookshelfObj.has(ThemeColorConstant.FOOTER)) {
//                JSONObject jHeaderObj = jbookshelfObj.getJSONObject(ThemeColorConstant.FOOTER);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.setBookShelfFooter(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.setBookShelfPoweredByKitaboo(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//            }
//            if (jbookshelfObj.has(ThemeColorConstant.BUTTONS)) {
//                JSONObject jbuttonsObj = jbookshelfObj.getJSONObject(ThemeColorConstant.BUTTONS);
//                if (jbuttonsObj.has(ThemeColorConstant.LAUNCH)) {
//                    JSONObject jLaunchButtonsObj = jbuttonsObj.getJSONObject(ThemeColorConstant.LAUNCH);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jLaunchButtonsObj, ThemeColorConstant.BACKGROUND)) {
//                        localVo.set_bookshelf_btnLaunch_background(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jLaunchButtonsObj, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jLaunchButtonsObj, ThemeColorConstant.COLOR)) {
//                        localVo.set_bookshelf_btnLaunch_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jLaunchButtonsObj, ThemeColorConstant.COLOR));
//                    }
//                }
//                if (jbuttonsObj.has(ThemeColorConstant.ARCHIVE)) {
//                    JSONObject jLaunchButtonsObj = jbuttonsObj.getJSONObject(ThemeColorConstant.ARCHIVE);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jLaunchButtonsObj, ThemeColorConstant.BACKGROUND)) {
//                        localVo.set_bookshelf_btnDelete_background(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jLaunchButtonsObj, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jLaunchButtonsObj, ThemeColorConstant.COLOR)) {
//                        localVo.set_bookshelf_btnDelete_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jLaunchButtonsObj, ThemeColorConstant.COLOR));
//                    }
//                }
//                if (jbuttonsObj.has(ThemeColorConstant.STATISTICS)) {
//                    JSONObject jLaunchButtonsObj = jbuttonsObj.getJSONObject(ThemeColorConstant.STATISTICS);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jLaunchButtonsObj, ThemeColorConstant.BACKGROUND)) {
//                        localVo.set_bookshelf_btnStatistics_background(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jLaunchButtonsObj, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jLaunchButtonsObj, ThemeColorConstant.COLOR)) {
//                        localVo.set_bookshelf_btnStatistics_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jLaunchButtonsObj, ThemeColorConstant.COLOR));
//                    }
//                }
//            }
//            JSONObject jReaderobj = response.getJSONObject(ThemeColorConstant.READER);
//            if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jReaderobj, ThemeColorConstant.HEADER)) {
//                localVo.set_reader_header(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jReaderobj, ThemeColorConstant.HEADER));
//            }
//            if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jReaderobj, ThemeColorConstant.FOOTER)) {
//                localVo.setReaderFooter(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jReaderobj, ThemeColorConstant.FOOTER));
//            }
//            if (jReaderobj.has(ThemeColorConstant.ICON)) {
//                JSONObject jHeaderObj = jReaderobj.getJSONObject(ThemeColorConstant.ICON);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.set_reader_icon_background(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.set_reader_icon_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//            }
//            if (jReaderobj.has(ThemeColorConstant.ICON_SELECTED)) {
//                JSONObject jHeaderObj = jReaderobj.getJSONObject(ThemeColorConstant.ICON_SELECTED);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.set_reader_icon_selected_background(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.set_reader_icon_selected_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//            }
//            if (jReaderobj.has(ThemeColorConstant.READ_ALOUD_MENU)) {
//                JSONObject jHeaderObj = jReaderobj.getJSONObject(ThemeColorConstant.READ_ALOUD_MENU);
//                if (jHeaderObj.has(ThemeColorConstant.BACKGROUND)) {
//                    JSONObject jMoreObject = jHeaderObj.getJSONObject(ThemeColorConstant.BACKGROUND);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setRAMBGBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.BORDER_COLOR)) {
//                        localVo.setRAMBGBorderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.BORDER_COLOR));
//                    }
//                }
//                if (jHeaderObj.has(ThemeColorConstant.HEADER_TITLE)) {
//                    JSONObject jMoreObject = jHeaderObj.getJSONObject(ThemeColorConstant.HEADER_TITLE);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setRAMTitleBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.COLOR)) {
//                        localVo.setRAMTitleColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setRAMTitleFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setRAMTitleFontFile(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (jHeaderObj.has(ThemeColorConstant.CLOSE_ICON)) {
//                    JSONObject jMoreObject = jHeaderObj.getJSONObject(ThemeColorConstant.CLOSE_ICON);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setRAMCloseBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.COLOR)) {
//                        localVo.setRAMCloseColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setRAMCloseFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setRAMCloseFontFile(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (jHeaderObj.has(ThemeColorConstant.OPTION_TEXT)) {
//                    JSONObject jMoreObject = jHeaderObj.getJSONObject(ThemeColorConstant.OPTION_TEXT);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setRAMOptionTextBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.COLOR)) {
//                        localVo.setRAMOptionTextColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setRAMOptionTextFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreObject, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setRAMOptionTextFontFile(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreObject, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (jHeaderObj.has(ThemeColorConstant.BUTTONS)) {
//                    JSONObject jMoreObject = jHeaderObj.getJSONObject(ThemeColorConstant.BUTTONS);
//                    if (jMoreObject.has(ThemeColorConstant.LET_ME_READ)) {
//                        JSONObject jButtonObject = jMoreObject.getJSONObject(ThemeColorConstant.LET_ME_READ);
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.BACKGROUND)) {
//                            localVo.setRAMButtonLMRBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.BACKGROUND));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.BORDER_COLOR)) {
//                            localVo.setRAMButtonLMRBorderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.BORDER_COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.ICON_COLOR)) {
//                            localVo.setRAMButtonLMRIconColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.ICON_COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.ICON_BORDER_COLOR)) {
//                            localVo.setRAMButtonLMRIconBorderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.ICON_BORDER_COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.COLOR)) {
//                            localVo.setRAMButtonLMRColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.FONT_SIZE)) {
//                            localVo.setRAMButtonLMRFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.FONT_SIZE));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.FONT_FACE)) {
//                            localVo.setRAMButtonLMRFontFile(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.FONT_FACE));
//                        }
//                    }
//                    if (jMoreObject.has(ThemeColorConstant.AUTO_PLAY)) {
//                        JSONObject jButtonObject = jMoreObject.getJSONObject(ThemeColorConstant.AUTO_PLAY);
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.BACKGROUND)) {
//                            localVo.setRAMButtonAutoPlayBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.BACKGROUND));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.BORDER_COLOR)) {
//                            localVo.setRAMButtonAutoPlayBorderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.BORDER_COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.ICON_COLOR)) {
//                            localVo.setRAMButtonAutoPlayIconColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.ICON_COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.ICON_BORDER_COLOR)) {
//                            localVo.setRAMButtonAutoPlayIconBorderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.ICON_BORDER_COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.COLOR)) {
//                            localVo.setRAMButtonAutoPlayColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.FONT_SIZE)) {
//                            localVo.setRAMButtonAutoPlayFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.FONT_SIZE));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.FONT_FACE)) {
//                            localVo.setRAMButtonAutoPlayFontFile(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.FONT_FACE));
//                        }
//                    }
//                    if (jMoreObject.has(ThemeColorConstant.READ_TO_ME)) {
//                        JSONObject jButtonObject = jMoreObject.getJSONObject(ThemeColorConstant.READ_TO_ME);
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.BACKGROUND)) {
//                            localVo.setRAMButtonRTMBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.BACKGROUND));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.BORDER_COLOR)) {
//                            localVo.setRAMButtonRTMBorderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.BORDER_COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.ICON_COLOR)) {
//                            localVo.setRAMButtonRTMIconColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.ICON_COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.ICON_BORDER_COLOR)) {
//                            localVo.setRAMButtonRTMIconBorderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.ICON_BORDER_COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.COLOR)) {
//                            localVo.setRAMButtonRTMColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.COLOR));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.FONT_SIZE)) {
//                            localVo.setRAMButtonRTMFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.FONT_SIZE));
//                        }
//                        if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jButtonObject, ThemeColorConstant.FONT_FACE)) {
//                            localVo.setRAMButtonRTMFontFile(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jButtonObject, ThemeColorConstant.FONT_FACE));
//                        }
//                    }
//                }
//            }
//            if (jReaderobj.has(ThemeColorConstant.DEFAULT_PANEL)) {
//                JSONObject jHeaderObj = jReaderobj.getJSONObject(ThemeColorConstant.DEFAULT_PANEL);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.set_reader_default_panel_background(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.set_reader_default_panel_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.DIVIDER)) {
//                    localVo.set_reader_default_panel_divider(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.DIVIDER));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.METADATA)) {
//                    localVo.set_reader_default_panel_metadata(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.METADATA));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.ACTION)) {
//                    localVo.set_reader_default_panel_actions(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.ACTION));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.HEADER_FONT_SIZE)) {
//                    localVo.setDefaultPanelHeaderFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.HEADER_FONT_SIZE));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.CHAPTER_FONT_SIZE)) {
//                    localVo.setDefaultPanelChapterFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.CHAPTER_FONT_SIZE));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.PAGE_FONT_SIZE)) {
//                    localVo.setDefaultPanelPageFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.PAGE_FONT_SIZE));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.FONT_FACE)) {
//                    localVo.setDefaultPanelFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.FONT_FACE));
//                }
//            }
//            if (jReaderobj.has(ThemeColorConstant.TAB)) {
//                JSONObject jHeaderObj = jReaderobj.getJSONObject(ThemeColorConstant.TAB);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.set_reader_tab_background(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.set_reader_tab_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//            }
//            if (jReaderobj.has(ThemeColorConstant.BOOKMARK)) {
//                JSONObject jHeaderObj = jReaderobj.getJSONObject(ThemeColorConstant.BOOKMARK);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.set_reader_bookmark_default_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.SELECTED)) {
//                    localVo.set_reader_bookmark_selected_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.SELECTED));
//                }
//            }
//            if (jReaderobj.has(ThemeColorConstant.THUMBNAIL_PANEL)) {
//                JSONObject jHeaderObj = jReaderobj.getJSONObject(ThemeColorConstant.THUMBNAIL_PANEL);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.set_reader_thumbnail_panel_background(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.set_reader_thumbnail_panel_color(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.SELECTION)) {
//                    localVo.set_reader_thumbnail_panel_selection(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.SELECTION));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.FONT_SIZE)) {
//                    localVo.setThumbnailPanelFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.FONT_SIZE));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.FONT_FACE)) {
//                    localVo.setThumbnailPanelFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.FONT_FACE));
//                }
//            }
//            if (jReaderobj.has(ThemeColorConstant.IMAGE_CAPTION)) {
//                JSONObject jHeaderObj = jReaderobj.getJSONObject(ThemeColorConstant.IMAGE_CAPTION);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.setImageCaptionMainBackgroundColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.COLOR)) {
//                    localVo.setImageCaptionTextColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.COLOR));
//                }
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jHeaderObj, ThemeColorConstant.IMAGE_BACKGROUND)) {
//                    localVo.setImageCaptionBackgroundColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jHeaderObj, ThemeColorConstant.IMAGE_BACKGROUND));
//                }
//            }
//            if (response.has(ThemeColorConstant.LOGIN)) {
//                JSONObject jLoginObj = response.getJSONObject(ThemeColorConstant.LOGIN);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jLoginObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.setLoginBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jLoginObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (jLoginObj.has(ThemeColorConstant.HEADER)) {
//                    JSONObject jMoreValue = jLoginObj.getJSONObject(ThemeColorConstant.HEADER);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.COLOR)) {
//                        localVo.setLoginHeaderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setLoginHeaderBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setLoginHeaderFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setLoginHeaderFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (jLoginObj.has(ThemeColorConstant.INPUT_BOX)) {
//                    JSONObject jMoreValue = jLoginObj.getJSONObject(ThemeColorConstant.INPUT_BOX);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.COLOR)) {
//                        localVo.setLoginInputColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.BORDER_COLOR)) {
//                        localVo.setLoginInputBorderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.BORDER_COLOR));
//                    }
//
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setLoginInputFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setLoginInputFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (jLoginObj.has(ThemeColorConstant.TOGGLE_ICON)) {
//                    JSONObject jMoreValue = jLoginObj.getJSONObject(ThemeColorConstant.TOGGLE_ICON);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.COLOR)) {
//                        localVo.setmToggleIconColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.COLOR));
//                    }
//                }
//                if (jLoginObj.has(ThemeColorConstant.FORGOT_PASSWORD)) {
//                    JSONObject jMoreValue = jLoginObj.getJSONObject(ThemeColorConstant.FORGOT_PASSWORD);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.COLOR)) {
//                        localVo.setLoginForgotPasswordColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setLoginForgotPasswordBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setLoginForgotPasswordFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setLoginForgotPasswordFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (jLoginObj.has(ThemeColorConstant.OTHER_LINK)) {
//                    JSONObject jMoreValue = jLoginObj.getJSONObject(ThemeColorConstant.OTHER_LINK);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.COLOR)) {
//                        localVo.setLoginOtherLinkColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setLoginOtherLinkBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setLoginOtherLinkFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setLoginOtherLinkFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (jLoginObj.has(ThemeColorConstant.BUTTONS)) {
//                    JSONObject jMoreValue = jLoginObj.getJSONObject(ThemeColorConstant.BUTTONS);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.COLOR)) {
//                        localVo.setLoginButtonColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setLoginButtonBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setLoginButtonFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setLoginButtonFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//            }
//            if (response.has(ThemeColorConstant.PROFILE_SETTING)) {
//                JSONObject jProfileObj = response.getJSONObject(ThemeColorConstant.PROFILE_SETTING);
//                if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jProfileObj, ThemeColorConstant.BACKGROUND)) {
//                    localVo.setProfileBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jProfileObj, ThemeColorConstant.BACKGROUND));
//                }
//                if (jProfileObj.has(ThemeColorConstant.HEADER)) {
//                    JSONObject jMoreValue = jProfileObj.getJSONObject(ThemeColorConstant.HEADER);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.COLOR)) {
//                        localVo.setProfileHeaderColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setProfileHeaderBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setProfileHeaderFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setProfileHeaderFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (jProfileObj.has(ThemeColorConstant.USER_NAME)) {
//                    JSONObject jMoreValue = jProfileObj.getJSONObject(ThemeColorConstant.USER_NAME);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.COLOR)) {
//                        localVo.setProfileUsernameColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setProfileUsernameBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setProfileUsernameFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setProfileUsernameFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (jProfileObj.has(ThemeColorConstant.EMAIL_LINK)) {
//                    JSONObject jMoreValue = jProfileObj.getJSONObject(ThemeColorConstant.EMAIL_LINK);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.COLOR)) {
//                        localVo.setProfileEmailLinkColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setProfileEmailLinkBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setProfileEmailLinkFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setProfileEmailLinkFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//                if (jProfileObj.has(ThemeColorConstant.BUTTONS)) {
//                    JSONObject jMoreValue = jProfileObj.getJSONObject(ThemeColorConstant.BUTTONS);
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.COLOR)) {
//                        localVo.setProfileButtonColor(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.COLOR));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.BACKGROUND)) {
//                        localVo.setProfileButtonBackground(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.BACKGROUND));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_SIZE)) {
//                        localVo.setProfileButtonFontSize(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_SIZE));
//                    }
//                    if (com.hurix.kitaboo.constants.utils.Utils.hasValue(jMoreValue, ThemeColorConstant.FONT_FACE)) {
//                        localVo.setProfileButtonFontFace(com.hurix.kitaboo.constants.utils.Utils.getValueFromJsonObj(jMoreValue, ThemeColorConstant.FONT_FACE));
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            if (Constants.IS_DEBUG_ENABLED) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        if (GlobalDataManager.getInstance().isWebViewClosedAfterTokenReceived()) {
//            loginsummit.setText(getResources().getString(R.string.loading_progress));
//            loginsummit.setClickable(false);
//        }
//        super.onResume();
//    }
//
//}
