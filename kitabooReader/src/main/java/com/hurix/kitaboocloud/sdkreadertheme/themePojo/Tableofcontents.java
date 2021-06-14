
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tableofcontents implements Serializable {

    @SerializedName("overlay_panel")
    @Expose
    private OverlayPanel overlayPanel;
    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("popup_border")
    @Expose
    private String popupBorder;
    @SerializedName("tab_bg")
    @Expose
    private String tabBg;
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
    @SerializedName("selected_toc")
    @Expose
    private SelectedToc selectedToc;
    @SerializedName("title-color")
    @Expose
    private String titleColor;
    @SerializedName("description-color")
    @Expose
    private String descriptionColor;
    @SerializedName("pageno-color")
    @Expose
    private String pagenoColor;
    @SerializedName("icons-color")
    @Expose
    private String iconsColor;
    @SerializedName("more_icon-color")
    @Expose
    private String moreIconColor;
    @SerializedName("divider-color")
    @Expose
    private String dividerColor;
    @SerializedName("close_slider")
    @Expose
    private CloseSlider closeSlider;

    public OverlayPanel getOverlayPanel() {
        return overlayPanel;
    }

    public void setOverlayPanel(OverlayPanel overlayPanel) {
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

    public String getTabBg() {
        return tabBg;
    }

    public void setTabBg(String tabBg) {
        this.tabBg = tabBg;
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

    public SelectedToc getSelectedToc() {
        return selectedToc;
    }

    public void setSelectedToc(SelectedToc selectedToc) {
        this.selectedToc = selectedToc;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getDescriptionColor() {
        return descriptionColor;
    }

    public void setDescriptionColor(String descriptionColor) {
        this.descriptionColor = descriptionColor;
    }

    public String getPagenoColor() {
        return pagenoColor;
    }

    public void setPagenoColor(String pagenoColor) {
        this.pagenoColor = pagenoColor;
    }

    public String getIconsColor() {
        return iconsColor;
    }

    public void setIconsColor(String iconsColor) {
        this.iconsColor = iconsColor;
    }

    public String getMoreIconColor() {
        return moreIconColor;
    }

    public void setMoreIconColor(String moreIconColor) {
        this.moreIconColor = moreIconColor;
    }

    public String getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(String dividerColor) {
        this.dividerColor = dividerColor;
    }

    public CloseSlider getCloseSlider() {
        return closeSlider;
    }

    public void setCloseSlider(CloseSlider closeSlider) {
        this.closeSlider = closeSlider;
    }

}
