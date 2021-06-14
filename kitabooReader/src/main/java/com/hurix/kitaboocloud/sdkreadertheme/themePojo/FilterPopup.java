
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FilterPopup implements Serializable {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("border_color")
    @Expose
    private String borderColor;
    @SerializedName("all_box_border-color")
    @Expose
    private String allBoxBorderColor;
    @SerializedName("box_border-color")
    @Expose
    private String boxBorderColor;
    @SerializedName("check_color")
    @Expose
    private String checkColor;
    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("arrow_color")
    @Expose
    private String arrowColor;
    @SerializedName("action_text-color")
    @Expose
    private String actionTextColor;

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

    public String getAllBoxBorderColor() {
        return allBoxBorderColor;
    }

    public void setAllBoxBorderColor(String allBoxBorderColor) {
        this.allBoxBorderColor = allBoxBorderColor;
    }

    public String getBoxBorderColor() {
        return boxBorderColor;
    }

    public void setBoxBorderColor(String boxBorderColor) {
        this.boxBorderColor = boxBorderColor;
    }

    public String getCheckColor() {
        return checkColor;
    }

    public void setCheckColor(String checkColor) {
        this.checkColor = checkColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getArrowColor() {
        return arrowColor;
    }

    public void setArrowColor(String arrowColor) {
        this.arrowColor = arrowColor;
    }

    public String getActionTextColor() {
        return actionTextColor;
    }

    public void setActionTextColor(String actionTextColor) {
        this.actionTextColor = actionTextColor;
    }

}
