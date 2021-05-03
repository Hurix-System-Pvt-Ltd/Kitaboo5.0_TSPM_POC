
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Mode implements Serializable {

    @SerializedName("selected_tab-border")
    @Expose
    private String selectedTabBorder;
    @SerializedName("Day")
    @Expose
    private Day day;
    @SerializedName("Night")
    @Expose
    private Night night;
    @SerializedName("Sepia")
    @Expose
    private Sepia sepia;

    public String getSelectedTabBorder() {
        return selectedTabBorder;
    }

    public void setSelectedTabBorder(String selectedTabBorder) {
        this.selectedTabBorder = selectedTabBorder;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Night getNight() {
        return night;
    }

    public void setNight(Night night) {
        this.night = night;
    }

    public Sepia getSepia() {
        return sepia;
    }

    public void setSepia(Sepia sepia) {
        this.sepia = sepia;
    }

}
