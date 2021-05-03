
package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsightBookshelf {

    @SerializedName("booktitleLabelColor")
    @Expose
    private String booktitleLabelColor;
    @SerializedName("Progressview_progresscolor")
    @Expose
    private String progressviewProgresscolor;
    @SerializedName("Progressview_emptylinecolor")
    @Expose
    private String progressviewEmptylinecolor;
    @SerializedName("infobutton_title_Color")
    @Expose
    private String infobuttonTitleColor;
    @SerializedName("Downloadview")
    @Expose
    private Downloadview downloadview;
    @SerializedName("downloadbutton_titlecolor")
    @Expose
    private String downloadbuttonTitlecolor;

    public String getBooktitleLabelColor() {
        return booktitleLabelColor;
    }

    public void setBooktitleLabelColor(String booktitleLabelColor) {
        this.booktitleLabelColor = booktitleLabelColor;
    }

    public String getProgressviewProgresscolor() {
        return progressviewProgresscolor;
    }

    public void setProgressviewProgresscolor(String progressviewProgresscolor) {
        this.progressviewProgresscolor = progressviewProgresscolor;
    }

    public String getProgressviewEmptylinecolor() {
        return progressviewEmptylinecolor;
    }

    public void setProgressviewEmptylinecolor(String progressviewEmptylinecolor) {
        this.progressviewEmptylinecolor = progressviewEmptylinecolor;
    }

    public String getInfobuttonTitleColor() {
        return infobuttonTitleColor;
    }

    public void setInfobuttonTitleColor(String infobuttonTitleColor) {
        this.infobuttonTitleColor = infobuttonTitleColor;
    }

    public Downloadview getDownloadview() {
        return downloadview;
    }

    public void setDownloadview(Downloadview downloadview) {
        this.downloadview = downloadview;
    }

    public String getDownloadbuttonTitlecolor() {
        return downloadbuttonTitlecolor;
    }

    public void setDownloadbuttonTitlecolor(String downloadbuttonTitlecolor) {
        this.downloadbuttonTitlecolor = downloadbuttonTitlecolor;
    }

}
