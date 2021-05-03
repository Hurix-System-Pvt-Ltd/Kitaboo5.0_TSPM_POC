
package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsightSigninSignUp {

    @SerializedName("background")
    @Expose
    private String background = "";
    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("tab_text_color")
    @Expose
    private String tabTextColor = "";
    @SerializedName("tab")
    @Expose
    private Tab tab;
    @SerializedName("tab_selected_color")
    @Expose
    private String tabSelectedColor = "";
    @SerializedName("input_panel")
    @Expose
    private InputPanel__ inputPanel;
    @SerializedName("hint_text_color")
    @Expose
    private String hintTextColor = "";
    @SerializedName("input_text_color")
    @Expose
    private String inputTextColor = "";
    @SerializedName("icon_color")
    @Expose
    private String iconColor = "";
    @SerializedName("input_text_color_HTML")
    @Expose
    private String inputTextColorHTML = "";
    @SerializedName("input_border_bg")
    @Expose
    private String inputBorderBg = "";
    @SerializedName("action_button")
    @Expose
    private ActionButton___ actionButton;
    @SerializedName("forgotpassword_link")
    @Expose
    private String forgotpasswordLink = "";
    @SerializedName("bottom_panel")
    @Expose
    private BottomPanel_ bottomPanel;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getTabTextColor() {
        return tabTextColor;
    }

    public void setTabTextColor(String tabTextColor) {
        this.tabTextColor = tabTextColor;
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public String getTabSelectedColor() {
        return tabSelectedColor;
    }

    public void setTabSelectedColor(String tabSelectedColor) {
        this.tabSelectedColor = tabSelectedColor;
    }

    public InputPanel__ getInputPanel() {
        return inputPanel;
    }

    public void setInputPanel(InputPanel__ inputPanel) {
        this.inputPanel = inputPanel;
    }

    public String getHintTextColor() {
        return hintTextColor;
    }

    public void setHintTextColor(String hintTextColor) {
        this.hintTextColor = hintTextColor;
    }

    public String getInputTextColor() {
        return inputTextColor;
    }

    public void setInputTextColor(String inputTextColor) {
        this.inputTextColor = inputTextColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getInputTextColorHTML() {
        return inputTextColorHTML;
    }

    public void setInputTextColorHTML(String inputTextColorHTML) {
        this.inputTextColorHTML = inputTextColorHTML;
    }

    public String getInputBorderBg() {
        return inputBorderBg;
    }

    public void setInputBorderBg(String inputBorderBg) {
        this.inputBorderBg = inputBorderBg;
    }

    public ActionButton___ getActionButton() {
        return actionButton;
    }

    public void setActionButton(ActionButton___ actionButton) {
        this.actionButton = actionButton;
    }

    public String getForgotpasswordLink() {
        return forgotpasswordLink;
    }

    public void setForgotpasswordLink(String forgotpasswordLink) {
        this.forgotpasswordLink = forgotpasswordLink;
    }

    public BottomPanel_ getBottomPanel() {
        return bottomPanel;
    }

    public void setBottomPanel(BottomPanel_ bottomPanel) {
        this.bottomPanel = bottomPanel;
    }

}
