
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Settings implements Serializable {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("title-color")
    @Expose
    private String titleColor;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("box_border-color")
    @Expose
    private String boxBorderColor;
    @SerializedName("section_title-color")
    @Expose
    private String sectionTitleColor;
    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("check_color")
    @Expose
    private String checkColor;
    @SerializedName("action_button")
    @Expose
    private ActionButton actionButton;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getBoxBorderColor() {
        return boxBorderColor;
    }

    public void setBoxBorderColor(String boxBorderColor) {
        this.boxBorderColor = boxBorderColor;
    }

    public String getSectionTitleColor() {
        return sectionTitleColor;
    }

    public void setSectionTitleColor(String sectionTitleColor) {
        this.sectionTitleColor = sectionTitleColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getCheckColor() {
        return checkColor;
    }

    public void setCheckColor(String checkColor) {
        this.checkColor = checkColor;
    }

    public ActionButton getActionButton() {
        return actionButton;
    }

    public void setActionButton(ActionButton actionButton) {
        this.actionButton = actionButton;
    }

}
