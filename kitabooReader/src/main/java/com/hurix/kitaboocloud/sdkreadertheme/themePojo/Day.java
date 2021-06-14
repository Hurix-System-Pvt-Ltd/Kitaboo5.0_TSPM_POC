
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Day implements Serializable {

    @SerializedName("tab_bg")
    @Expose
    private String tabBg;
    @SerializedName("text-color")
    @Expose
    private String textColor;

    public String getTabBg() {
        return tabBg;
    }

    public void setTabBg(String tabBg) {
        this.tabBg = tabBg;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

}
