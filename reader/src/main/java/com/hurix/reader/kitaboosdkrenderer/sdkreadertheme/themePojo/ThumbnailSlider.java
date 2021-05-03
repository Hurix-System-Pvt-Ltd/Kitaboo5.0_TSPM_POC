
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ThumbnailSlider implements Serializable {

    @SerializedName("popup_background")
    @Expose
    private String popupBackground;
    @SerializedName("slider-color")
    @Expose
    private String sliderColor;
    @SerializedName("chapter_icon_color")
    @Expose
    private String chapterIconColor;
    @SerializedName("slider-filled-color")
    @Expose
    private String sliderFilledColor;
    @SerializedName("default_thumbnail_color")
    @Expose
    private String defaultThumbnailColor;
    @SerializedName("selected_thumbnail_color")
    @Expose
    private String selectedThumbnailColor;
    @SerializedName("thumbnail_text-color")
    @Expose
    private String thumbnailTextColor;
    @SerializedName("selected_title_color")
    @Expose
    private String selectedTitleColor;
    @SerializedName("title-color")
    @Expose
    private String titleColor;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("text-color")
    @Expose
    private String textColor;

    public String getPopupBackground() {
        return popupBackground;
    }

    public void setPopupBackground(String popupBackground) {
        this.popupBackground = popupBackground;
    }

    public String getSliderColor() {
        return sliderColor;
    }

    public void setSliderColor(String sliderColor) {
        this.sliderColor = sliderColor;
    }

    public String getChapterIconColor() {
        return chapterIconColor;
    }

    public void setChapterIconColor(String chapterIconColor) {
        this.chapterIconColor = chapterIconColor;
    }

    public String getSliderFilledColor() {
        return sliderFilledColor;
    }

    public void setSliderFilledColor(String sliderFilledColor) {
        this.sliderFilledColor = sliderFilledColor;
    }

    public String getDefaultThumbnailColor() {
        return defaultThumbnailColor;
    }

    public void setDefaultThumbnailColor(String defaultThumbnailColor) {
        this.defaultThumbnailColor = defaultThumbnailColor;
    }

    public String getSelectedThumbnailColor() {
        return selectedThumbnailColor;
    }

    public void setSelectedThumbnailColor(String selectedThumbnailColor) {
        this.selectedThumbnailColor = selectedThumbnailColor;
    }

    public String getThumbnailTextColor() {
        return thumbnailTextColor;
    }

    public void setThumbnailTextColor(String thumbnailTextColor) {
        this.thumbnailTextColor = thumbnailTextColor;
    }

    public String getSelectedTitleColor() {
        return selectedTitleColor;
    }

    public void setSelectedTitleColor(String selectedTitleColor) {
        this.selectedTitleColor = selectedTitleColor;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

}
