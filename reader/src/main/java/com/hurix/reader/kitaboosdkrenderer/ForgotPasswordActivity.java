//package com.hurix.reader.kitaboosdkrenderer;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.graphics.Color;
//import android.graphics.PorterDuff;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.webkit.WebView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//
//import com.hurix.reader.R;
//import com.hurix.reader.kitaboosdkrenderer.constants.Constants;
//import com.hurix.reader.kitaboosdkrenderer.controller.UserController;
//import com.hurix.reader.kitaboosdkrenderer.datamodel.UserSettingVO;
//import com.hurix.reader.kitaboosdkrenderer.dialogs.DialogUtils;
//import com.hurix.reader.kitaboosdkrenderer.iconify.Typefaces;
//import com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager;
//import com.hurix.reader.kitaboosdkrenderer.sdkUtils.Utils;
//import com.hurix.service.Interface.IServiceResponse;
//import com.hurix.service.Interface.IServiceResponseListener;
//import com.hurix.service.exception.ServiceException;
//import com.hurix.service.response.ResetPasswordServiceResponse;
//
//
//import java.lang.reflect.Field;
//
//import androidx.appcompat.app.AlertDialog;
//
///**
// * Created by biki.sah on 12/7/2016.
// */
//public class ForgotPasswordActivity extends Activity implements View.OnClickListener , IServiceResponseListener {
//    private EditText _edtEmailid;
//    private Button _btnSubmit;
//    private TextView _tvCloseButton, _txtCancel,tvDescriptionText;
//    private LinearLayout _forgotPassword;
//    private final int FORGET_PASSWORD_CHARACTER_UPER_LIMIT = 255;
//    UserSettingVO mUserSettingVO;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
////        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
////                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.forgotpassworddialog);
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        WindowManager.LayoutParams WMLP = getWindow().getAttributes();
//        WMLP.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
//        WMLP.dimAmount = 0.5f;
//        initView();
//
//        setThemeColor();
//
//
//        _edtEmailid.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() > 0) {
//                    _edtEmailid.setError(null);
//                }
//
//                if (s.length() > 0 && s.subSequence(0, 1).toString().equalsIgnoreCase(" ")) {
//                    _edtEmailid.setText("");
//                    _edtEmailid.setError(null);
//                } else if (s.toString().equals("")) {
//                    _edtEmailid.setError(null);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        try {
//            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
//            f.setAccessible(true);
//            f.set(_edtEmailid, R.drawable.cursor);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setThemeColor() {
//
//        tvDescriptionText.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getForgotPassword().getDescriptionTextColor()));
//        _edtEmailid.setBackgroundColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getForgotPassword().getInputPanel().getBackground()));
//        _edtEmailid.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getForgotPassword().getHintTextColor()));
//        _btnSubmit.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getForgotPassword().getButtonTextColor()));
//        _btnSubmit.setBackgroundColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getForgotPassword().getButtonColor()));
//
//    }
//
//    private void initView() {
//        mUserSettingVO = UserController.getInstance(ForgotPasswordActivity.this).getUserSettings();
//        findViewById(R.id.forgetPassowrdContainer).setBackgroundColor(Color.WHITE);
//        Typeface typeFace = Typefaces.get(this, getApplicationContext().getResources()
//                .getString(R.string.kitabooreader_font_file_name));
//        _edtEmailid = (EditText) findViewById(R.id.edtemailid);
//        _edtEmailid.getBackground().setColorFilter(Color.parseColor(UserController.getInstance(ForgotPasswordActivity.this)
//                .getUserSettings().getBookShelfIconsColor()), PorterDuff.Mode.SRC_ATOP);
//        _edtEmailid.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mUserSettingVO.getLoginInputColor()),
//                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mUserSettingVO.getLoginInputBorderColor())));
//        _edtEmailid.setTextSize(16);
//        _edtEmailid.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() > 0) {
//                    _btnSubmit.setEnabled(true);
//                    _btnSubmit.setAlpha(1);
//                } else {
//                    _btnSubmit.setEnabled(false);
//                    _btnSubmit.setAlpha(0.5f);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        _txtCancel = (TextView) findViewById(R.id.btncancel);
//        _txtCancel.setTypeface(typeFace);
//        _txtCancel.setAllCaps(false);
//        _txtCancel.setText(ShelfUIConstants.SHELF_LOGIN_BACK_IC_TEXT);
//        _txtCancel.setTextColor(Color.parseColor(UserController.getInstance(ForgotPasswordActivity.this)
//                .getUserSettings().getBookShelfIconsColor()));
//        _txtCancel.setBackgroundColor(Color.TRANSPARENT);
//        _txtCancel.setBackgroundColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getLogin().getForgotPassword().getButtonColor()));
//
//        _btnSubmit = (Button) findViewById(R.id.btnaccesssubmit);
//        _btnSubmit.setBackgroundColor(Color.parseColor(UserController.getInstance(ForgotPasswordActivity.this).getUserSettings()
//                .get_bookshelf_btnLaunch_background()));
//
//        _forgotPassword = (LinearLayout) findViewById(R.id.forgotPassword);
//
//        _btnSubmit.setOnClickListener(this);
//        _txtCancel.setOnClickListener(this);
//        _btnSubmit.setEnabled(false);
//        _btnSubmit.setAlpha(0.5f);
//        setParams();
//
//        tvDescriptionText=(TextView) findViewById(R.id.txtloginid);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.btnaccesssubmit) {
//            clickOnSubmit();
//        }
//        else if (i == R.id.btncancel) {
//            openLoginActivity();
//        }
//    }
//
//    @Override
//    public void requestCompleted(IServiceResponse response) {
//        if (response.getResponseRequestType().equals(Constants.SERVICETYPES.FORGOT_PASSWORD_REQUEST) || response
//                .getResponseRequestType().equals(Constants.SERVICETYPES.RESET_PASSWORD_REQUEST)) {
//            ResetPasswordServiceResponse forgotpasswordObj = (ResetPasswordServiceResponse) response;
//            if (forgotpasswordObj.isSuccess()) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog );
//                builder.setMessage( getApplicationContext().getResources()
//                        .getString(R.string.forgot_email_send_success_msg))
//                        .setCancelable(false)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                openLoginActivity();
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.show();
//            }
//            else
//            {
//                _btnSubmit.setEnabled(true);
//                _btnSubmit.setText(getApplicationContext().getResources().getString(R.string.access_code_submit_text));
//                DialogUtils.displayToast(this, getApplicationContext().getResources()
//                        .getString(R.string.forgot_email_send_fail_msg), Toast.LENGTH_LONG, Gravity.CENTER);
//            }
//        }
//        _btnSubmit.setEnabled(true);
//    }
//
//    @Override
//    public void requestErrorOccured(ServiceException response) {
//        DialogUtils.displayToast(this, getApplicationContext().getResources()
//                .getString(R.string.forgot_email_send_fail_msg), Toast.LENGTH_LONG, Gravity.CENTER);
//        _btnSubmit.setEnabled(true);
//        _btnSubmit.setText(getApplicationContext().getResources().getString(R.string.access_code_submit_text));
//    }
//
//    private void clickOnSubmit() {
//        String strEmailID = _edtEmailid.getText().toString().trim();
//        if (checkValidation(strEmailID)) {
//            if (Utils.checkInternetConnection(ForgotPasswordActivity.this)) {
//                _btnSubmit.setEnabled(false);
//                _btnSubmit.setText(getApplicationContext().getResources().getString(R.string.please_wait));
//                KitabooServiceAPI.getObject().getServiceAdapter()
//                        .resetPasswordOfUser(_edtEmailid.getText().toString(), this);
//            } else {
//                _btnSubmit.setEnabled(true);
//                DialogUtils.displayToast(ForgotPasswordActivity.this, getApplicationContext().getResources()
//                        .getString(R.string.network_error), Toast.LENGTH_LONG, Gravity.CENTER);
//            }
//        }
//    }
//
//    private boolean checkValidation(String emailID) {
//        boolean isDataValid = true;
//        if (emailID.length() == 0) {
//            _edtEmailid.setError(getApplicationContext().getResources().getString(R.string.alert_empty_mail_id));
//            _edtEmailid.requestFocus();
//            isDataValid = false;
//        } else if (emailID.length() > FORGET_PASSWORD_CHARACTER_UPER_LIMIT) {
//            _edtEmailid.setError(String.format(getApplicationContext().getResources()
//                    .getString(R.string.alert_emailid_character_limit), "" + FORGET_PASSWORD_CHARACTER_UPER_LIMIT));
//            _edtEmailid.requestFocus();
//            isDataValid = false;
//        } else if (!Utils.emailValidator(_edtEmailid.getText().toString().trim())) {
//            _edtEmailid.setError(getApplicationContext().getResources().getString(R.string.alert_invalid_emailid));
//            _edtEmailid.requestFocus();
//            isDataValid = false;
//        }
//        return isDataValid;
//    }
//
//    public void setParams() {
//        String ua = new WebView(ForgotPasswordActivity.this).getSettings().getUserAgentString();
//        if (ua.contains("Mobile")) {
//
//        }
//        String strBookShelfType = Utils.getSharedPreferenceStringValue(
//                ForgotPasswordActivity.this, Constants.SHELF_PREFS_NAME, Constants.BOOKSHELF_TYPE, "");
//        if (strBookShelfType.equalsIgnoreCase(KitabooBookShelfType.MOBILE.toString())) {
//            ViewGroup.LayoutParams params = _forgotPassword.getLayoutParams();
//            if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                params.width = getApplicationContext().getResources().getDisplayMetrics().widthPixels - 48;
//            } else {
//                params.width = getApplicationContext().getResources().getDisplayMetrics().heightPixels - 48;
//            }
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            _forgotPassword.setLayoutParams(params);
//        } else if ((ua.contains("Mobile") && (strBookShelfType.equalsIgnoreCase(KitabooBookShelfType.BOOK_COLLECTION.toString())))) {
//            ViewGroup.LayoutParams params = _forgotPassword.getLayoutParams();
//            if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                params.width = getApplicationContext().getResources().getDisplayMetrics().widthPixels - 48;
//            } else {
//                params.width = getApplicationContext().getResources().getDisplayMetrics().heightPixels - 48;
//            }
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            _forgotPassword.setLayoutParams(params);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        openLoginActivity();
//    }
//
//    public void openLoginActivity(){
//        Intent login = new Intent(this, LoginActivity.class);
//        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(login);
//        overridePendingTransition(R.anim.fade_in , R.anim.fade_out);
//    }
//
//}
