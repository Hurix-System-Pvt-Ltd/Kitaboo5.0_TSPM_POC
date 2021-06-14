
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pen implements Serializable {

    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("popup_border")
    @Expose
    private String popupBorder;
    @SerializedName("shadow")
    @Expose
    private String shadow;
    @SerializedName("selected_border_color")
    @Expose
    private String selectedBorderColor;

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

    public String getSelectedBorderColor() {
        return selectedBorderColor;
    }

    public void setSelectedBorderColor(String selectedBorderColor) {
        this.selectedBorderColor = selectedBorderColor;
    }

}
