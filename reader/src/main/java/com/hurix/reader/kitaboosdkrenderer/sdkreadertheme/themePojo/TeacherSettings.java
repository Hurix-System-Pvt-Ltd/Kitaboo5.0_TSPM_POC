
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TeacherSettings implements Serializable {

    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("popup_border")
    @Expose
    private String popupBorder;
    @SerializedName("title-color")
    @Expose
    private String titleColor;
    @SerializedName("main_icon-color")
    @Expose
    private String mainIconColor;
    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("selected_icon-color")
    @Expose
    private String selectedIconColor;
    @SerializedName("selected_icon_bg")
    @Expose
    private String selectedIconBg;
    @SerializedName("pen1-color")
    @Expose
    private String pen1Color;
    @SerializedName("pen2-color")
    @Expose
    private String pen2Color;
    @SerializedName("box_border-color")
    @Expose
    private String boxBorderColor;
    @SerializedName("check_color")
    @Expose
    private String checkColor;

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

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getMainIconColor() {
        return mainIconColor;
    }

    public void setMainIconColor(String mainIconColor) {
        this.mainIconColor = mainIconColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getSelectedIconColor() {
        return selectedIconColor;
    }

    public void setSelectedIconColor(String selectedIconColor) {
        this.selectedIconColor = selectedIconColor;
    }

    public String getSelectedIconBg() {
        return selectedIconBg;
    }

    public void setSelectedIconBg(String selectedIconBg) {
        this.selectedIconBg = selectedIconBg;
    }

    public String getPen1Color() {
        return pen1Color;
    }

    public void setPen1Color(String pen1Color) {
        this.pen1Color = pen1Color;
    }

    public String getPen2Color() {
        return pen2Color;
    }

    public void setPen2Color(String pen2Color) {
        this.pen2Color = pen2Color;
    }

    public String getBoxBorderColor() {
        return boxBorderColor;
    }

    public void setBoxBorderColor(String boxBorderColor) {
        this.boxBorderColor = boxBorderColor;
    }

    public String getCheckColor() {
        return checkColor;
    }

    public void setCheckColor(String checkColor) {
        this.checkColor = checkColor;
    }

}
