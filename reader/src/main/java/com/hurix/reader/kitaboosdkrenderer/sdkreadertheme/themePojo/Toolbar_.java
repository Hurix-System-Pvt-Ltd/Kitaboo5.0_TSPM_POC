
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Toolbar_ implements Serializable {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("icons-color")
    @Expose
    private String iconsColor;
    @SerializedName("disabled_icon")
    @Expose
    private DisabledIcon_ disabledIcon;
    @SerializedName("selected_icon_background")
    @Expose
    private String selectedIconBackground;
    @SerializedName("selected_icon_color")
    @Expose
    private String selectedIconColor;

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

    public DisabledIcon_ getDisabledIcon() {
        return disabledIcon;
    }

    public void setDisabledIcon(DisabledIcon_ disabledIcon) {
        this.disabledIcon = disabledIcon;
    }

    public String getSelectedIconBackground() {
        return selectedIconBackground;
    }

    public void setSelectedIconBackground(String selectedIconBackground) {
        this.selectedIconBackground = selectedIconBackground;
    }

    public String getSelectedIconColor() {
        return selectedIconColor;
    }

    public void setSelectedIconColor(String selectedIconColor) {
        this.selectedIconColor = selectedIconColor;
    }

}
