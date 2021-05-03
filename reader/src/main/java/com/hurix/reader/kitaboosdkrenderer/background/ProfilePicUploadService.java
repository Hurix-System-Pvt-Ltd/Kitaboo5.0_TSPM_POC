//
//package com.hurix.reader.kitaboosdkrenderer.background;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.widget.Toast;
//
//
//import com.hurix.database.dbfactory.DBController;
//
//import com.hurix.reader.R;
//import com.hurix.reader.kitaboosdkrenderer.adapter.KitabooServiceAPI;
//import com.hurix.reader.kitaboosdkrenderer.constants.Constants;
//import com.hurix.reader.kitaboosdkrenderer.controller.UserController;
//import com.hurix.reader.kitaboosdkrenderer.dialogs.DialogUtils;
//import com.hurix.service.Interface.IServiceResponse;
//import com.hurix.service.Interface.IServiceResponseListener;
//import com.hurix.service.exception.ServiceException;
//import com.hurix.service.response.RefreshUserTokenResponse;
//import com.hurix.service.response.UploadProfilePicResponse;
//
//import java.util.Map;
//
///**
// * Created by Ravi Ranjan on 5/8/2015.
// */
//public class ProfilePicUploadService extends IntentService implements IServiceResponseListener {
//
//    public ProfilePicUploadService() {
//        super("ProfilePicUploadService");
//    }
//
//    private String errorMessage;
//    private String firstName;
//    private String lastName;
//    String profileString;
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Bundle profileData = intent.getExtras();
//        if (profileData != null) {
//            profileString = profileData.getString(Constants.PROFILE_PIC_DATA);
//            errorMessage = profileData.getString(Constants.PROFILE_PIC_MESSAGE);
//            firstName = profileData.getString(Constants.PREF_USER_FIRST_NAME);
//            lastName = profileData.getString(Constants.PREF_USER_LAST_NAME);
//            if(firstName != null && lastName != null) {
//                KitabooServiceAPI.getObject().getServiceAdapter().uploadProfilePic(profileString,
//                        UserController.getInstance(this).getUserVO().getToken(), firstName, lastName, this);
//            }
//        }
//    }
//
//
//    @Override
//    public void requestCompleted(IServiceResponse response) {
//        if (response.getResponseRequestType().equals(Constants.SERVICETYPES.UPLOAD_PROFILE_PIC)) {
//            UploadProfilePicResponse uploadProfilePicResponse = (UploadProfilePicResponse) response;
//            if (uploadProfilePicResponse.isSuccess()) {
//                UserController.getInstance(this).getUserVO().setProfilePic(uploadProfilePicResponse
//                        .getProfilePicUrl());
//                firstName = uploadProfilePicResponse.getFirstName();
//                lastName = uploadProfilePicResponse.getLastName();
//                if(firstName.equals("") && lastName.equals("")){
//                    UserController.getInstance(this).getUserVO().setFirstName(UserController
//                            .getInstance(this).getUserVO().getFirstName());
//                    UserController.getInstance(this).getUserVO().setLastName(UserController
//                            .getInstance(this).getUserVO().getLastName());
//                }else{
//                    UserController.getInstance(this).getUserVO().setFirstName(uploadProfilePicResponse
//                            .getFirstName());
//                    UserController.getInstance(this).getUserVO().setLastName(uploadProfilePicResponse
//                            .getLastName());
//                }
//                UserController.getInstance(this).getUserVO().setDisplayName(null);
//                DBController.getInstance(this).getManager().updateUserDetailsFromProfileSettingServer
//                        (UserController.getInstance(this).getUserVO().getUserID(),
//                                UserController.getInstance(this).getUserVO());
//                UserController.getInstance(this).getUserVO().notifyProfilePicStatus(true, null);
//            }
//        }
//        else if (response.getResponseRequestType().equals(Constants.SERVICETYPES.REFRESH_USER_TOKEN)) {
//            RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) response;
//            DBController.getInstance(ProfilePicUploadService.this).getManager().updateUserToken(responseObj
//                    .getUserVO());
//            KitabooServiceAPI.getObject().getServiceAdapter().uploadProfilePic(profileString,
//                    ((RefreshUserTokenResponse) response).getUserVO().getToken(), firstName, lastName, this);
//        }
//    }
//
//    @Override
//    public void requestErrorOccured(ServiceException response) {
//        if (response != null) {
//            ServiceException exception = response;
//            if (exception.getInvalidFields() != null && !exception.getInvalidFields().isEmpty()) {
//                Map.Entry<String, Integer> entry = exception.getInvalidFields().entrySet().iterator().next();
//                if (UserController.getInstance(ProfilePicUploadService.this).getUserSettings().getIsAutoLogOffEnabled() && entry.getValue() == Constants.SESSION_EXPIRE_ERRORCODE) {
//
//                    DialogUtils.displayToast(ProfilePicUploadService.this, getResources().getString(R.string.alert_session_expired), Toast.LENGTH_SHORT, Gravity.CENTER);
//                }
//                else if (entry.getValue() == Constants.SESSION_EXPIRE_ERRORCODE && (!exception.getResponseRequestType().equals(Constants.SERVICETYPES.REFRESH_USER_TOKEN))) {
//                    KitabooServiceAPI.getObject().getServiceAdapter()
//                            .refreshUserToken(UserController.getInstance(ProfilePicUploadService.this).getUserVO().getToken(), exception.getResponseRequestType().toString(), this);
//                    return;
//                }
//                else{
//                    UserController.getInstance(this).getUserVO().notifyProfilePicStatus(false, entry);
//                }
//            }
//        } else {
//            if(errorMessage != null) {
//                DialogUtils.displayToast(this, errorMessage, Toast.LENGTH_SHORT, Gravity.CENTER);
//                UserController.getInstance(this).getUserVO().notifyProfilePicStatus(false, null);
//            }
//        }
//    }
//}
