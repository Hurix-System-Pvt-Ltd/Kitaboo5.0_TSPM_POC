package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyMessage implements Serializable {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("border_color")
    @Expose
    private String borderColor;
    @SerializedName("name_color")
    @Expose
    private String nameColor;
    @SerializedName("description-color")
    @Expose
    private String descriptionColor;
    @SerializedName("time_text_color")
    @Expose
    private String timeTextColor;

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

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getDescriptionColor() {
        return descriptionColor;
    }

    public void setDescriptionColor(String descriptionColor) {
        this.descriptionColor = descriptionColor;
    }

    public String getTimeTextColor() {
        return timeTextColor;
    }

    public void setTimeTextColor(String timeTextColor) {
        this.timeTextColor = timeTextColor;
    }

}
