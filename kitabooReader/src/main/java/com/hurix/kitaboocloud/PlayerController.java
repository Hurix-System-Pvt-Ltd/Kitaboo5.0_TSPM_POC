package com.hurix.kitaboocloud;
import android.content.Context;

import com.hurix.kitaboocloud.interfaces.IDestroyable;
import com.hurix.kitaboocloud.model.JWTokenResponseVO;

public class PlayerController implements IDestroyable {

    public static PlayerController mInstance;
    private String profilePic="";
    private String loginHeaderFontFace;
    private String token;
    private String eMail ="sdk.test@yopmail.com";
    private boolean isAutoLogOffEnabled;
    private boolean isUgcShareEnabled;
    private boolean checkLogin;
    private boolean isBookPlayerHelpScreenSeen;
    private boolean isBookDownloaded;

    private String startDate ="";
    private String expiryDate =" ";
    private String roleName = "SDK Role ";
    private long userID = 1234;
    private String displayName =" Default Name";
    private String readerMenuIconsColor ="#000000";
    private String defaultPanelChapterFontSize ="16";
    private String reader_default_panel_actions ="#000000";
    private String sideMenuHeaderTitleColor = "#000000";
    private String defaultChapterNameColor = "#000000";


    public static PlayerController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PlayerController();
        }
        return mInstance;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getLoginHeaderFontFace() {
        return loginHeaderFontFace;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public long getUserID() {
        return userID;
    }

    public String getSideMenuHeaderTitleColor() {
        return sideMenuHeaderTitleColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getToken() {
        return token;
    }

    public String getRoleName() {
        return roleName;
    }

    public String geteMail() {
        return eMail;
    }

    public boolean isAutoLogOffEnabled() {
        return isAutoLogOffEnabled;
    }

    public boolean isUgcShareEnabled() {
        return isUgcShareEnabled;
    }

    public String getReaderMenuIconsColor() {
        return readerMenuIconsColor;
    }

    public String getDefaultPanelChapterFontSize() {
        return defaultPanelChapterFontSize;
    }

    public String getReader_default_panel_actions() {
        return reader_default_panel_actions;
    }


    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setExpireDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUserToken(String token) {
        this.token = token;
    }

    public void setUserRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setUserMail(String eMail) {
        this.eMail = eMail;
    }

    public void setIsAutoLogOffEnabled(boolean isAutoLogOffEnabled) {
        this.isAutoLogOffEnabled = isAutoLogOffEnabled;
    }

    public void setIsUgcShareEnable(boolean isUgcShareEnabled) {
        this.isUgcShareEnabled = isUgcShareEnabled;
    }

    public void setReaderIconColor(String readerMenuIconsColor) {
        this.readerMenuIconsColor = readerMenuIconsColor;
    }

    public void setDefaultPanelFontSize(String defaultPanelChapterFontSize) {
        this.defaultPanelChapterFontSize = defaultPanelChapterFontSize;
    }

    public void setReaderDefaultPanelAction(String reader_default_panel_actions) {
        this.reader_default_panel_actions = reader_default_panel_actions;
    }

    public void setLoginHeaderFontFace(String loginHeaderFontFace) {
        this.loginHeaderFontFace = loginHeaderFontFace;
    }

    public void setSideMenuHeaderTitleColor(String sideMenuHeaderTitleColor) {
        this.sideMenuHeaderTitleColor = sideMenuHeaderTitleColor;
    }

    public void setDefaultChapterNameColor(String defaultChapterNameColor) {
        this.defaultChapterNameColor = defaultChapterNameColor;
    }
    public String getDefaultChapterNameColor() {
        return defaultChapterNameColor;
    }

    public void setIsUserLogin(boolean checkLogin) {
        this.checkLogin = checkLogin;
    }

    public boolean isCheckLogin() {
        return checkLogin;
    }

    public boolean isBookPlayerHelpScreenSeen() {
        return isBookPlayerHelpScreenSeen;
    }

    public void setBookPlayerHelpScreenSeen(boolean bookPlayerHelpScreenSeen) {
        isBookPlayerHelpScreenSeen = bookPlayerHelpScreenSeen;
    }

    public void setIsBookDownloaded(boolean isBookDownloaded) {
        this.isBookDownloaded = isBookDownloaded;
    }

    public boolean isBookDownloaded() {
        return isBookDownloaded;
    }



    @Override
    public void destroy() {
        mInstance = null;
    }



}
