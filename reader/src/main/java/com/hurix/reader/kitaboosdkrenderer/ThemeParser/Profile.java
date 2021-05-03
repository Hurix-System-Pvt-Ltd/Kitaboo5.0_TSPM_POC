package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {
    private ProfileEdit profileEdit;
    private Transition transition;
    private Language language;
    private ProfileEditPopup profileEditPopup;
    private ChangePassword changePassword;

    @SerializedName("profile_border")
    @Expose
    private String profileBorder;
    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("action_text-color")
    @Expose
    private String actionTextColor;
    @SerializedName("link_color_selected")
    @Expose
    private String linkColorSelected;
    @SerializedName("signout")
    @Expose
    private Signout signout;

    public String getProfileBorder() {
        return profileBorder;
    }

    public void setProfileBorder(String profileBorder) {
        this.profileBorder = profileBorder;
    }

    public String getPopupBackground() {
        return popupBackground;
    }

    public void setPopupBackground(String popupBackground) {
        this.popupBackground = popupBackground;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getActionTextColor() {
        return actionTextColor;
    }

    public void setActionTextColor(String actionTextColor) {
        this.actionTextColor = actionTextColor;
    }

    public String getLinkColorSelected() {
        return linkColorSelected;
    }

    public void setLinkColorSelected(String linkColorSelected) {
        this.linkColorSelected = linkColorSelected;
    }

    public Signout getSignout() {
        return signout;
    }

    public void setSignout(Signout signout) {
        this.signout = signout;
    }

    public ProfileEdit getProfileEdit() {
        return profileEdit;
    }

    public void setProfileEdit(ProfileEdit profileEdit) {
        this.profileEdit = profileEdit;
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ProfileEditPopup getProfileEditPopup() {
        return profileEditPopup;
    }

    public void setProfileEditPopup(ProfileEditPopup profileEditPopup) {
        this.profileEditPopup = profileEditPopup;
    }

    public ChangePassword getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(ChangePassword changePassword) {
        this.changePassword = changePassword;
    }
}
