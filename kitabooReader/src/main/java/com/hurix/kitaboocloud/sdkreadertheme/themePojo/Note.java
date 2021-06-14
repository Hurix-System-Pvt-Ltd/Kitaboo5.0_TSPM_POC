
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Note implements Serializable {

    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("popup_border")
    @Expose
    private String popupBorder;
    @SerializedName("title-color")
    @Expose
    private String titleColor;
    @SerializedName("back_icon-color")
    @Expose
    private String backIconColor;
    @SerializedName("selected_icon_border")
    @Expose
    private String selectedIconBorder;
    @SerializedName("contextualtext-color")
    @Expose
    private String contextualtextColor;
    @SerializedName("hint_text-color")
    @Expose
    private String hintTextColor;
    @SerializedName("description-color")
    @Expose
    private String descriptionColor;
    @SerializedName("action_button_color")
    @Expose
    private String actionButtonColor;
    @SerializedName("metadata-color")
    @Expose
    private String metadataColor;
    @SerializedName("share_popup_background")
    @Expose
    private String sharePopupBackground;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("share_settings")
    @Expose
    private ShareSettings shareSettings;
    @SerializedName("action_button_color_disabled")
    @Expose
    private ActionButtonColorDisabled actionButtonColorDisabled;

    public String getPopupBackground() {
        return popupBackground;
    }

    public void setPopupBackground(String popupBackground) {
        this.popupBackground = popupBackground;
    }

    public String getPopupBorder() {
        return popupBorder;
    }

    public void setPopupBorder(String popupBorder) {
        this.popupBorder = popupBorder;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getBackIconColor() {
        return backIconColor;
    }

    public void setBackIconColor(String backIconColor) {
        this.backIconColor = backIconColor;
    }

    public String getSelectedIconBorder() {
        return selectedIconBorder;
    }

    public void setSelectedIconBorder(String selectedIconBorder) {
        this.selectedIconBorder = selectedIconBorder;
    }

    public String getContextualtextColor() {
        return contextualtextColor;
    }

    public void setContextualtextColor(String contextualtextColor) {
        this.contextualtextColor = contextualtextColor;
    }

    public String getHintTextColor() {
        return hintTextColor;
    }

    public void setHintTextColor(String hintTextColor) {
        this.hintTextColor = hintTextColor;
    }

    public String getDescriptionColor() {
        return descriptionColor;
    }

    public void setDescriptionColor(String descriptionColor) {
        this.descriptionColor = descriptionColor;
    }

    public String getActionButtonColor() {
        return actionButtonColor;
    }

    public void setActionButtonColor(String actionButtonColor) {
        this.actionButtonColor = actionButtonColor;
    }

    public String getMetadataColor() {
        return metadataColor;
    }

    public void setMetadataColor(String metadataColor) {
        this.metadataColor = metadataColor;
    }

    public String getSharePopupBackground() {
        return sharePopupBackground;
    }

    public void setSharePopupBackground(String sharePopupBackground) {
        this.sharePopupBackground = sharePopupBackground;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public ShareSettings getShareSettings() {
        return shareSettings;
    }

    public void setShareSettings(ShareSettings shareSettings) {
        this.shareSettings = shareSettings;
    }

    public ActionButtonColorDisabled getActionButtonColorDisabled() {
        return actionButtonColorDisabled;
    }

    public void setActionButtonColorDisabled(ActionButtonColorDisabled actionButtonColorDisabled) {
        this.actionButtonColorDisabled = actionButtonColorDisabled;
    }

}
