
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Top implements Serializable {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("icons-color")
    @Expose
    private String iconsColor;
    @SerializedName("shadow")
    @Expose
    private String shadow;
    @SerializedName("selected_icon-color")
    @Expose
    private String selectedIconColor;
    @SerializedName("selected_icon_bg")
    @Expose
    private String selectedIconBg;
    @SerializedName("Title_text_color")
    @Expose
    private String titleTextColor;
    @SerializedName("navigation_arrow_color")
    @Expose
    private String navigationArrowColor;
    @SerializedName("profile_border")
    @Expose
    private String profileBorder;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getIconsColor() {
        return iconsColor;
    }

    public void setIconsColor(String iconsColor) {
        this.iconsColor = iconsColor;
    }

    public String getShadow() {
        return shadow;
    }

    public void setShadow(String shadow) {
        this.shadow = shadow;
    }

    public String getSelectedIconColor() {
        return selectedIconColor;
    }

    public void setSelectedIconColor(String selectedIconColor) {
        this.selectedIconColor = selectedIconColor;
    }

    public String getSelectedIconBg() {
        return selectedIconBg;
    }

    public void setSelectedIconBg(String selectedIconBg) {
        this.selectedIconBg = selectedIconBg;
    }

    public String getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(String titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    public String getNavigationArrowColor() {
        return navigationArrowColor;
    }

    public void setNavigationArrowColor(String navigationArrowColor) {
        this.navigationArrowColor = navigationArrowColor;
    }

    public String getProfileBorder() {
        return profileBorder;
    }

    public void setProfileBorder(String profileBorder) {
        this.profileBorder = profileBorder;
    }

}
