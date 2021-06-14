
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Font implements Serializable {

    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("divider-color")
    @Expose
    private String dividerColor;
    @SerializedName("pointer_bg")
    @Expose
    private String pointerBg;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("box_border-color")
    @Expose
    private String boxBorderColor;
    @SerializedName("selected_icon-bg")
    @Expose
    private String selectedIconBg;
    @SerializedName("selected_icon-border")
    @Expose
    private String selectedIconBorder;
    @SerializedName("dropdown_bg")
    @Expose
    private String dropdownBg;
    @SerializedName("dropdown_text-color")
    @Expose
    private String dropdownTextColor;
    @SerializedName("more_icon-color")
    @Expose
    private String moreIconColor;

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(String dividerColor) {
        this.dividerColor = dividerColor;
    }

    public String getPointerBg() {
        return pointerBg;
    }

    public void setPointerBg(String pointerBg) {
        this.pointerBg = pointerBg;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getBoxBorderColor() {
        return boxBorderColor;
    }

    public void setBoxBorderColor(String boxBorderColor) {
        this.boxBorderColor = boxBorderColor;
    }

    public String getSelectedIconBg() {
        return selectedIconBg;
    }

    public void setSelectedIconBg(String selectedIconBg) {
        this.selectedIconBg = selectedIconBg;
    }

    public String getSelectedIconBorder() {
        return selectedIconBorder;
    }

    public void setSelectedIconBorder(String selectedIconBorder) {
        this.selectedIconBorder = selectedIconBorder;
    }

    public String getDropdownBg() {
        return dropdownBg;
    }

    public void setDropdownBg(String dropdownBg) {
        this.dropdownBg = dropdownBg;
    }

    public String getDropdownTextColor() {
        return dropdownTextColor;
    }

    public void setDropdownTextColor(String dropdownTextColor) {
        this.dropdownTextColor = dropdownTextColor;
    }

    public String getMoreIconColor() {
        return moreIconColor;
    }

    public void setMoreIconColor(String moreIconColor) {
        this.moreIconColor = moreIconColor;
    }

}
