package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TeacherStudentList implements Serializable {

    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("popup_border")
    @Expose
    private String popupBorder;
    @SerializedName("shadow")
    @Expose
    private String shadow;
    @SerializedName("title-color")
    @Expose
    private String titleColor;
    @SerializedName("hint_text-color")
    @Expose
    private String hintTextColor;

    @SerializedName("tab_border")
    @Expose
    private String tabBorder;
    @SerializedName("tab_selected_bar")
    @Expose
    private String tabSelectedBar;
    @SerializedName("tab_text-color")
    @Expose
    private String tabTextColor;
    @SerializedName("name_color")
    @Expose
    private String nameColor;
    @SerializedName("data_added_color")
    @Expose
    private String dataAddedColor;
    @SerializedName("nodata_added_color")
    @Expose
    private String noDataAddedColor;
    @SerializedName("selected_color")
    @Expose
    private String selectedColor;
    @SerializedName("refresh")
    @Expose
    private Refresh refresh;

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

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getHintTextColor() {
        return hintTextColor;
    }

    public void setHintTextColor(String hintTextColor) {
        this.hintTextColor = hintTextColor;
    }

    public String getTabBorder() {
        return tabBorder;
    }

    public void setTabBorder(String tabBorder) {
        this.tabBorder = tabBorder;
    }

    public String getTabSelectedBar() {
        return tabSelectedBar;
    }

    public void setTabSelectedBar(String tabSelectedBar) {
        this.tabSelectedBar = tabSelectedBar;
    }

    public String getTabTextColor() {
        return tabTextColor;
    }

    public void setTabTextColor(String tabTextColor) {
        this.tabTextColor = tabTextColor;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getDataAddedColor() {
        return dataAddedColor;
    }

    public void setDataAddedColor(String dataAddedColor) {
        this.dataAddedColor = dataAddedColor;
    }

    public String getNoDataAddedColor() {
        return noDataAddedColor;
    }

    public void setNoDataAddedColor(String noDataAddedColor) {
        this.noDataAddedColor = noDataAddedColor;
    }

    public String getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Refresh getRefresh() {
        return refresh;
    }

    public void setRefresh(Refresh refresh) {
        this.refresh = refresh;
    }

}
