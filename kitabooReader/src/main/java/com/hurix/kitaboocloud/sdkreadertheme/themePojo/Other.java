
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Other implements Serializable {

    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("selected_icon-color")
    @Expose
    private String selectedIconColor;
    @SerializedName("Brightness")
    @Expose
    private Brightness brightness;
    @SerializedName("Mode")
    @Expose
    private Mode mode;
    @SerializedName("ScrollView")
    @Expose
    private ScrollView scrollView;

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getSelectedIconColor() {
        return selectedIconColor;
    }

    public void setSelectedIconColor(String selectedIconColor) {
        this.selectedIconColor = selectedIconColor;
    }

    public Brightness getBrightness() {
        return brightness;
    }

    public void setBrightness(Brightness brightness) {
        this.brightness = brightness;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public ScrollView getScrollView() {
        return scrollView;
    }

    public void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

}
