package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlignPopup {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("selected_border_color")
    @Expose
    private String selectedBorderColor;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getSelectedBorderColor() {
        return selectedBorderColor;
    }

    public void setSelectedBorderColor(String selectedBorderColor) {
        this.selectedBorderColor = selectedBorderColor;
    }
}
