package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Help implements Serializable {

    @SerializedName("overlay_panel")
    @Expose
    private OverlayPanel overlayPanel;
    @SerializedName("text_color")
    @Expose
    private String textColor;
    @SerializedName("description_text_color")
    @Expose
    private String descriptionTextColor;
    @SerializedName("line_color")
    @Expose
    private String lineColor;
    @SerializedName("pointer_color")
    @Expose
    private String pointerColor;

    public OverlayPanel getOverlayPanel() {
        return overlayPanel;
    }

    public void setOverlayPanel(OverlayPanel overlayPanel) {
        this.overlayPanel = overlayPanel;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getDescriptionTextColor() {
        return descriptionTextColor;
    }

    public void setDescriptionTextColor(String descriptionTextColor) {
        this.descriptionTextColor = descriptionTextColor;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public String getPointerColor() {
        return pointerColor;
    }

    public void setPointerColor(String pointerColor) {
        this.pointerColor = pointerColor;
    }

}
