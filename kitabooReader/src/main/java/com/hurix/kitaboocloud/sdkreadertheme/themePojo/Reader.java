
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Reader implements Serializable {

    @SerializedName("DayMode")
    @Expose
    private DayMode dayMode;

    public DayMode getDayMode() {
        return dayMode;
    }

    public void setDayMode(DayMode dayMode) {
        this.dayMode = dayMode;
    }

}
