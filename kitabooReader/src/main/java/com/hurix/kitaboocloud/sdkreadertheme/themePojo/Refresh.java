package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Refresh implements Serializable {

    @SerializedName("box_border-color")
    @Expose
    private String boxBorderColor;
    @SerializedName("button_text-color")
    @Expose
    private String buttonTextColor;

    public String getBoxBorderColor() {
        return boxBorderColor;
    }

    public void setBoxBorderColor(String boxBorderColor) {
        this.boxBorderColor = boxBorderColor;
    }

    public String getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonTextColor(String buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }
}
