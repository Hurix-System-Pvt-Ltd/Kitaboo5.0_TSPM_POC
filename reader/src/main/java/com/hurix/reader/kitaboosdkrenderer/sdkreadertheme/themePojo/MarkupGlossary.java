package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MarkupGlossary implements Serializable {

    @SerializedName("link_line_color")
    @Expose
    private String linkLineColor;
    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("icon_border_color")
    @Expose
    private String iconBorderColor;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("title-color")
    @Expose
    private String titleColor;
    @SerializedName("language_text_color")
    @Expose
    private String languageTextColor;
    @SerializedName("description_text_color")
    @Expose
    private String descriptionTextColor;
    @SerializedName("synonym_text_color")
    @Expose
    private String synonymTextColor;

    public String getLinkLineColor() {
        return linkLineColor;
    }

    public void setLinkLineColor(String linkLineColor) {
        this.linkLineColor = linkLineColor;
    }

    public String getPopupBackground() {
        return popupBackground;
    }

    public void setPopupBackground(String popupBackground) {
        this.popupBackground = popupBackground;
    }

    public String getIconBorderColor() {
        return iconBorderColor;
    }

    public void setIconBorderColor(String iconBorderColor) {
        this.iconBorderColor = iconBorderColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getLanguageTextColor() {
        return languageTextColor;
    }

    public void setLanguageTextColor(String languageTextColor) {
        this.languageTextColor = languageTextColor;
    }

    public String getDescriptionTextColor() {
        return descriptionTextColor;
    }

    public void setDescriptionTextColor(String descriptionTextColor) {
        this.descriptionTextColor = descriptionTextColor;
    }

    public String getSynonymTextColor() {
        return synonymTextColor;
    }

    public void setSynonymTextColor(String synonymTextColor) {
        this.synonymTextColor = synonymTextColor;
    }

}
