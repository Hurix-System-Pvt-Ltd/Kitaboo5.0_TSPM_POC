
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DayMode_ implements Serializable {

    @SerializedName("canvas_background")
    @Expose
    private String canvasBackground;
    @SerializedName("default_text-color")
    @Expose
    private String defaultTextColor;

    public String getCanvasBackground() {
        return canvasBackground;
    }

    public void setCanvasBackground(String canvasBackground) {
        this.canvasBackground = canvasBackground;
    }

    public String getDefaultTextColor() {
        return defaultTextColor;
    }

    public void setDefaultTextColor(String defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

}
