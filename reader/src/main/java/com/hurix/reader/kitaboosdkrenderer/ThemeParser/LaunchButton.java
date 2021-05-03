
package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LaunchButton {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("text_color")
    @Expose
    private String textColor;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }


}
