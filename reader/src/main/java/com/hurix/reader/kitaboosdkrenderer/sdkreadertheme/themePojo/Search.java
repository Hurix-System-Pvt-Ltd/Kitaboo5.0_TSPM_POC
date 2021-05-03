
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Search implements Serializable {

    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("popup_border")
    @Expose
    private String popupBorder;
    @SerializedName("main_icon-color")
    @Expose
    private String mainIconColor;
    @SerializedName("hint_text-color")
    @Expose
    private String hintTextColor;
    @SerializedName("title-color")
    @Expose
    private String titleColor;
    @SerializedName("tab_border")
    @Expose
    private String tabBorder;
    @SerializedName("selected_text-color")
    @Expose
    private String selectedTextColor;
    @SerializedName("seperation_background")
    @Expose
    private String seperationBackground;
    @SerializedName("cross_icon-color")
    @Expose
    private String crossIconColor;
    @SerializedName("description-color")
    @Expose
    private String descriptionColor;
    @SerializedName("subtext-color")
    @Expose
    private String subtextColor;
    @SerializedName("selection_text_color_bg")
    @Expose
    private String selectionTextColorBG;
    @SerializedName("selected_text_color_bg")
    @Expose
    private String selectedTextColorBG;


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

    public String getMainIconColor() {
        return mainIconColor;
    }

    public void setMainIconColor(String mainIconColor) {
        this.mainIconColor = mainIconColor;
    }

    public String getHintTextColor() {
        return hintTextColor;
    }

    public void setHintTextColor(String hintTextColor) {
        this.hintTextColor = hintTextColor;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getTabBorder() {
        return tabBorder;
    }

    public void setTabBorder(String tabBorder) {
        this.tabBorder = tabBorder;
    }

    public String getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(String selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public String getSeperationBackground() {
        return seperationBackground;
    }

    public void setSeperationBackground(String seperationBackground) {
        this.seperationBackground = seperationBackground;
    }

    public String getCrossIconColor() {
        return crossIconColor;
    }

    public void setCrossIconColor(String crossIconColor) {
        this.crossIconColor = crossIconColor;
    }

    public String getDescriptionColor() {
        return descriptionColor;
    }

    public void setDescriptionColor(String descriptionColor) {
        this.descriptionColor = descriptionColor;
    }

    public String getSubtextColor() {
        return subtextColor;
    }

    public void setSubtextColor(String subtextColor) {
        this.subtextColor = subtextColor;
    }

    public String getSelectionTextColorBG() {
        return selectionTextColorBG;
    }

    public void setSelectionTextColorBG(String selectionTextColorBG) {
        this.selectionTextColorBG = selectionTextColorBG;
    }

    public String getSelectedTextColorBG() {
        return selectedTextColorBG;
    }

    public void setSelectedTextColorBG(String selectedTextColorBG) {
        this.selectedTextColorBG = selectedTextColorBG;
    }

}
