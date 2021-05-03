
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SelectedToc implements Serializable {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("divider")
    @Expose
    private String divider;
    @SerializedName("title-color")
    @Expose
    private String titleColor;
    @SerializedName("description-color")
    @Expose
    private String descriptionColor;
    @SerializedName("arrow_color")
    @Expose
    private String arrowColor;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("side_tab_background")
    @Expose
    private String sideTabBackground;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getDivider() {
        return divider;
    }

    public void setDivider(String divider) {
        this.divider = divider;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getDescriptionColor() {
        return descriptionColor;
    }

    public void setDescriptionColor(String descriptionColor) {
        this.descriptionColor = descriptionColor;
    }

    public String getArrowColor() {
        return arrowColor;
    }

    public void setArrowColor(String arrowColor) {
        this.arrowColor = arrowColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getSideTabBackground() {
        return sideTabBackground;
    }

    public void setSideTabBackground(String sideTabBackground) {
        this.sideTabBackground = sideTabBackground;
    }

}
