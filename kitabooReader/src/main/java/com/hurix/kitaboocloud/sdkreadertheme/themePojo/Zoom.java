package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Zoom implements Serializable {

    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("slider-color")
    @Expose
    private String sliderColor;
    @SerializedName("slider-filled-color")
    @Expose
    private String sliderFilledColor;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;

    public String getPopupBackground() {
        return popupBackground;
    }

    public void setPopupBackground(String popupBackground) {
        this.popupBackground = popupBackground;
    }

    public String getSliderColor() {
        return sliderColor;
    }

    public void setSliderColor(String sliderColor) {
        this.sliderColor = sliderColor;
    }

    public String getSliderFilledColor() {
        return sliderFilledColor;
    }

    public void setSliderFilledColor(String sliderFilledColor) {
        this.sliderFilledColor = sliderFilledColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }
}
