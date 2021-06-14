
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubButton implements Serializable {

    @SerializedName("text-icon_color")
    @Expose
    private String textIconColor;
    @SerializedName("disabled_text-icon_color")
    @Expose
    private String disabledTextIconColor;

    public String getTextIconColor() {
        return textIconColor;
    }

    public void setTextIconColor(String textIconColor) {
        this.textIconColor = textIconColor;
    }

    public String getDisabledTextIconColor() {
        return disabledTextIconColor;
    }

    public void setDisabledTextIconColor(String disabledTextIconColor) {
        this.disabledTextIconColor = disabledTextIconColor;
    }

}
