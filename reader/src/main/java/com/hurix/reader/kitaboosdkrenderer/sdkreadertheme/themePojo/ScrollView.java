
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ScrollView implements Serializable {

    @SerializedName("tab_bg")
    @Expose
    private String tabBg;
    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("selected_Tab_bg")
    @Expose
    private String selectedTabBg;
    @SerializedName("selected_text-color")
    @Expose
    private String selectedTextColor;

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

    public String getSelectedTabBg() {
        return selectedTabBg;
    }

    public void setSelectedTabBg(String selectedTabBg) {
        this.selectedTabBg = selectedTabBg;
    }

    public String getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(String selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

}
