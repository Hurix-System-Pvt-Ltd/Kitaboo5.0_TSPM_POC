
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DisabledIcon_ implements Serializable {

    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("opacity")
    @Expose
    private String opacity;
    @SerializedName("background_HTML")
    @Expose
    private String backgroundHTML;

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getOpacity() {
        return opacity;
    }

    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }

    public String getBackgroundHTML() {
        return backgroundHTML;
    }

    public void setBackgroundHTML(String backgroundHTML) {
        this.backgroundHTML = backgroundHTML;
    }

}
