package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TextAnnotation implements Serializable {

    @SerializedName("background")
    @Expose
    private String background;

    @SerializedName("line_color")
    @Expose
    private String lineColor ;

    @SerializedName("icons-color")
    @Expose
    private String iconColor ;

    @SerializedName("disabled_icon")
    @Expose
    private DisabledIcon disableIcon ;

    @SerializedName("selected_icon-bg")
    @Expose
    private String selectedIconBG ;

    @SerializedName("align_popup")
    @Expose
    private AlignPopup alignPopup ;

    @SerializedName("color_popup")
    @Expose
    private ColorPopup colorPopup ;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public DisabledIcon getDisableIcon() {
        return disableIcon;
    }

    public void setDisableIcon(DisabledIcon disableIcon) {
        this.disableIcon = disableIcon;
    }

    public String getSelectedIconBG() {
        return selectedIconBG;
    }

    public void setSelectedIconBG(String selectedIconBG) {
        this.selectedIconBG = selectedIconBG;
    }

    public AlignPopup getAlignPopup() {
        return alignPopup;
    }

    public void setAlignPopup(AlignPopup alignPopup) {
        this.alignPopup = alignPopup;
    }

    public ColorPopup getColorPopup() {
        return colorPopup;
    }

    public void setColorPopup(ColorPopup colorPopup) {
        this.colorPopup = colorPopup;
    }

}
