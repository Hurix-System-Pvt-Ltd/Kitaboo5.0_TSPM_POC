
package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsightMoreinfo {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("icon_color")
    @Expose
    private String iconColor;
    @SerializedName("topic_color")
    @Expose
    private String topicColor;
    @SerializedName("title_color")
    @Expose
    private String titleColor;
    @SerializedName("description_color")
    @Expose
    private String descriptionColor;
    @SerializedName("download_button")
    @Expose
    private DownloadButton downloadButton;
    @SerializedName("Launch_button")
    @Expose
    private LaunchButton launchButton;
    @SerializedName("Delete_button")
    @Expose
    private DeleteButton deleteButton;

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

    public String getTopicColor() {
        return topicColor;
    }

    public void setTopicColor(String topicColor) {
        this.topicColor = topicColor;
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

    public DownloadButton getDownloadButton() {
        return downloadButton;
    }

    public void setDownloadButton(DownloadButton downloadButton) {
        this.downloadButton = downloadButton;
    }

    public LaunchButton getLaunchButton() {
        return launchButton;
    }

    public void setLaunchButton(LaunchButton launchButton) {
        this.launchButton = launchButton;
    }

    public DeleteButton getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(DeleteButton deleteButton) {
        this.deleteButton = deleteButton;
    }
}
