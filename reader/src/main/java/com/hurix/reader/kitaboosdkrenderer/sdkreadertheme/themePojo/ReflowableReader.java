
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReflowableReader implements Serializable {

    @SerializedName("DayMode")
    @Expose
    private DayMode_ dayMode;
    @SerializedName("SepiaMode")
    @Expose
    private SepiaMode sepiaMode;
    @SerializedName("NightMode")
    @Expose
    private NightMode nightMode;

    public DayMode_ getDayMode() {
        return dayMode;
    }

    public void setDayMode(DayMode_ dayMode) {
        this.dayMode = dayMode;
    }

    public SepiaMode getSepiaMode() {
        return sepiaMode;
    }

    public void setSepiaMode(SepiaMode sepiaMode) {
        this.sepiaMode = sepiaMode;
    }

    public NightMode getNightMode() {
        return nightMode;
    }

    public void setNightMode(NightMode nightMode) {
        this.nightMode = nightMode;
    }

}
