
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Brightness implements Serializable {

    @SerializedName("slider-color")
    @Expose
    private String sliderColor;
    @SerializedName("slider-selected-color")
    @Expose
    private String sliderSelectedColor;
    @SerializedName("icon-faded")
    @Expose
    private String iconFaded;
    @SerializedName("icon-bright")
    @Expose
    private String iconBright;

    public String getSliderColor() {
        return sliderColor;
    }

    public void setSliderColor(String sliderColor) {
        this.sliderColor = sliderColor;
    }

    public String getSliderSelectedColor() {
        return sliderSelectedColor;
    }

    public void setSliderSelectedColor(String sliderSelectedColor) {
        this.sliderSelectedColor = sliderSelectedColor;
    }

    public String getIconFaded() {
        return iconFaded;
    }

    public void setIconFaded(String iconFaded) {
        this.iconFaded = iconFaded;
    }

    public String getIconBright() {
        return iconBright;
    }

    public void setIconBright(String iconBright) {
        this.iconBright = iconBright;
    }

}
