
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FontSettings implements Serializable {

    @SerializedName("overlay_panel")
    @Expose
    private OverlayPanel__ overlayPanel;
    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("popup_border")
    @Expose
    private String popupBorder;
    @SerializedName("tab_border")
    @Expose
    private String tabBorder;
    @SerializedName("tab_text-color")
    @Expose
    private String tabTextColor;
    @SerializedName("selected_tab_border")
    @Expose
    private String selectedTabBorder;
    @SerializedName("selected_text-color")
    @Expose
    private String selectedTextColor;
    @SerializedName("reset-color")
    @Expose
    private String resetColor;
    @SerializedName("Font")
    @Expose
    private Font font;
    @SerializedName("Other")
    @Expose
    private Other other;

    public OverlayPanel__ getOverlayPanel() {
        return overlayPanel;
    }

    public void setOverlayPanel(OverlayPanel__ overlayPanel) {
        this.overlayPanel = overlayPanel;
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

    public String getTabBorder() {
        return tabBorder;
    }

    public void setTabBorder(String tabBorder) {
        this.tabBorder = tabBorder;
    }

    public String getTabTextColor() {
        return tabTextColor;
    }

    public void setTabTextColor(String tabTextColor) {
        this.tabTextColor = tabTextColor;
    }

    public String getSelectedTabBorder() {
        return selectedTabBorder;
    }

    public void setSelectedTabBorder(String selectedTabBorder) {
        this.selectedTabBorder = selectedTabBorder;
    }

    public String getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(String selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public String getResetColor() {
        return resetColor;
    }

    public void setResetColor(String resetColor) {
        this.resetColor = resetColor;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Other getOther() {
        return other;
    }

    public void setOther(Other other) {
        this.other = other;
    }

}
