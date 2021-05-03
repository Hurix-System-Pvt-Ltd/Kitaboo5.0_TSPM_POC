
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShareSettings implements Serializable {

    @SerializedName("section_title-color")
    @Expose
    private String sectionTitleColor;
    @SerializedName("box_border-color")
    @Expose
    private String boxBorderColor;
    @SerializedName("all_box_border-color")
    @Expose
    private String allBoxBorderColor;
    @SerializedName("check_color")
    @Expose
    private String checkColor;
    @SerializedName("bottom_background")
    @Expose
    private String bottomBackground;
    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("main_action_color")
    @Expose
    private String mainActionColor;

    public String getSectionTitleColor() {
        return sectionTitleColor;
    }

    public void setSectionTitleColor(String sectionTitleColor) {
        this.sectionTitleColor = sectionTitleColor;
    }

    public String getBoxBorderColor() {
        return boxBorderColor;
    }

    public void setBoxBorderColor(String boxBorderColor) {
        this.boxBorderColor = boxBorderColor;
    }

    public String getAllBoxBorderColor() {
        return allBoxBorderColor;
    }

    public void setAllBoxBorderColor(String allBoxBorderColor) {
        this.allBoxBorderColor = allBoxBorderColor;
    }

    public String getCheckColor() {
        return checkColor;
    }

    public void setCheckColor(String checkColor) {
        this.checkColor = checkColor;
    }

    public String getBottomBackground() {
        return bottomBackground;
    }

    public void setBottomBackground(String bottomBackground) {
        this.bottomBackground = bottomBackground;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getMainActionColor() {
        return mainActionColor;
    }

    public void setMainActionColor(String mainActionColor) {
        this.mainActionColor = mainActionColor;
    }

}
