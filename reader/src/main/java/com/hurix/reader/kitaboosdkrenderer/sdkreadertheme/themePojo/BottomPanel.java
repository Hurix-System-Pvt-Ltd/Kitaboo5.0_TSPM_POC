package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BottomPanel implements Serializable {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("border_color")
    @Expose
    private String borderColor;
    @SerializedName("hint_text-color")
    @Expose
    private String hintTextColor;
    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("icon_color_disabled")
    @Expose
    private String iconColorDisabled;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getHintTextColor() {
        return hintTextColor;
    }

    public void setHintTextColor(String hintTextColor) {
        this.hintTextColor = hintTextColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getIconColorDisabled() {
        return iconColorDisabled;
    }

    public void setIconColorDisabled(String iconColorDisabled) {
        this.iconColorDisabled = iconColorDisabled;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

}
