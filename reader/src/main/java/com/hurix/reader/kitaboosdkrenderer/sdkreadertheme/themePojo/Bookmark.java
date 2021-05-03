
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bookmark implements Serializable {

    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("selected_icon-color")
    @Expose
    private String selectedIconColor;
    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("popup_border")
    @Expose
    private String popupBorder;
    @SerializedName("input_panel-bg")
    @Expose
    private String inputPanelBg;
    @SerializedName("hint_text-color")
    @Expose
    private String hintTextColor;
    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("button_background")
    @Expose
    private String buttonBackground;
    @SerializedName("button_text-color")
    @Expose
    private String buttonTextColor;
    @SerializedName("cross_icon-color")
    @Expose
    private String crossIconColor;

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

    public String getInputPanelBg() {
        return inputPanelBg;
    }

    public void setInputPanelBg(String inputPanelBg) {
        this.inputPanelBg = inputPanelBg;
    }

    public String getHintTextColor() {
        return hintTextColor;
    }

    public void setHintTextColor(String hintTextColor) {
        this.hintTextColor = hintTextColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getButtonBackground() {
        return buttonBackground;
    }

    public void setButtonBackground(String buttonBackground) {
        this.buttonBackground = buttonBackground;
    }

    public String getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonTextColor(String buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public String getCrossIconColor() {
        return crossIconColor;
    }

    public void setCrossIconColor(String crossIconColor) {
        this.crossIconColor = crossIconColor;
    }

}
