
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyData implements Serializable {

    @SerializedName("overlay_panel")
    @Expose
    private OverlayPanel_ overlayPanel;
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
    @SerializedName("selected_button")
    @Expose
    private SelectedButton selectedButton;
    @SerializedName("de-selected_button")
    @Expose
    private DeSelectedButton deSelectedButton;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("disabled_icon")
    @Expose
    private DisabledIcon disabledIcon;
    @SerializedName("text-color")
    @Expose
    private String textColor;
    @SerializedName("metadata-color")
    @Expose
    private String metadataColor;
    @SerializedName("contextualtext-color")
    @Expose
    private String contextualtextColor;
    @SerializedName("description-color")
    @Expose
    private String descriptionColor;
    @SerializedName("sub_button")
    @Expose
    private SubButton subButton;
    @SerializedName("filter_popup")
    @Expose
    private FilterPopup filterPopup;
    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("notification_circle_color")
    @Expose
    private String notificationCircleColor;

    public OverlayPanel_ getOverlayPanel() {
        return overlayPanel;
    }

    public void setOverlayPanel(OverlayPanel_ overlayPanel) {
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

    public SelectedButton getSelectedButton() {
        return selectedButton;
    }

    public void setSelectedButton(SelectedButton selectedButton) {
        this.selectedButton = selectedButton;
    }

    public DeSelectedButton getDeSelectedButton() {
        return deSelectedButton;
    }

    public void setDeSelectedButton(DeSelectedButton deSelectedButton) {
        this.deSelectedButton = deSelectedButton;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public DisabledIcon getDisabledIcon() {
        return disabledIcon;
    }

    public void setDisabledIcon(DisabledIcon disabledIcon) {
        this.disabledIcon = disabledIcon;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getMetadataColor() {
        return metadataColor;
    }

    public void setMetadataColor(String metadataColor) {
        this.metadataColor = metadataColor;
    }

    public String getContextualtextColor() {
        return contextualtextColor;
    }

    public void setContextualtextColor(String contextualtextColor) {
        this.contextualtextColor = contextualtextColor;
    }

    public String getDescriptionColor() {
        return descriptionColor;
    }

    public void setDescriptionColor(String descriptionColor) {
        this.descriptionColor = descriptionColor;
    }

    public SubButton getSubButton() {
        return subButton;
    }

    public void setSubButton(SubButton subButton) {
        this.subButton = subButton;
    }

    public FilterPopup getFilterPopup() {
        return filterPopup;
    }

    public void setFilterPopup(FilterPopup filterPopup) {
        this.filterPopup = filterPopup;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getNotificationCircleColor() {
        return notificationCircleColor;
    }

    public void setNotificationCircleColor(String notificationCircleColor) {
        this.notificationCircleColor = notificationCircleColor;
    }

}
