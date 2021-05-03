
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Thickness implements Serializable {

    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("popup_border")
    @Expose
    private String popupBorder;
    @SerializedName("shadow")
    @Expose
    private String shadow;
    @SerializedName("slider-color")
    @Expose
    private String sliderColor;
    @SerializedName("slider-filled-color")
    @Expose
    private String sliderFilledColor;

    public String getPopupBackground() {
        return popupBackground;
    }

    public void setPopupBackground(String popupBackground) {
        this.popupBackground = popupBackground;
    }

    public String getPopupBorder() {
        return popupBorder;
    }

    public void setPopupBorder(String popupBorder) {
        this.popupBorder = popupBorder;
    }

    public String getShadow() {
        return shadow;
    }

    public void setShadow(String shadow) {
        this.shadow = shadow;
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

}
