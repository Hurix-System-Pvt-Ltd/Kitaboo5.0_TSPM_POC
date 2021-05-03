package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Share implements Serializable {

    @SerializedName("share_popup_background")
    @Expose
    private String sharePopupBackground;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("share_settings")
    @Expose
    private ShareSettings shareSettings;

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
}
