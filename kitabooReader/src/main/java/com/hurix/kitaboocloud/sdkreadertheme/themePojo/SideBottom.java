
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SideBottom implements Serializable {

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

}