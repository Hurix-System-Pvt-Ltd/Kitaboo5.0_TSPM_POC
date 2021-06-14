package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Highlight implements Serializable {

    @SerializedName("popup")
    @Expose
    private Popup popUp;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;

    @SerializedName("selected_border_color")
    @Expose
    private String selectedBorderColor;
    @SerializedName("highlightColor")
    @Expose
    private List<String> highlightColor = null;

    @SerializedName("highlightColor_HTML")
    @Expose
    private HighlightColorHTML highlightColorHTML;

    public String getSelectedBorderColor() {
        return selectedBorderColor;
    }

    public void setSelectedBorderColor(String selectedBorderColor) {
        this.selectedBorderColor = selectedBorderColor;
    }

    public List<String> getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(List<String> highlightColor) {
        this.highlightColor = highlightColor;
    }

    public HighlightColorHTML getHighlightColorHTML() {
        return highlightColorHTML;
    }

    public void setHighlightColorHTML(HighlightColorHTML highlightColorHTML) {
        this.highlightColorHTML = highlightColorHTML;
    }

    public Popup getPopup() {
        return popUp;
    }

    public void setPopup(Popup popUp) {
        this.popUp = popUp;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }
}