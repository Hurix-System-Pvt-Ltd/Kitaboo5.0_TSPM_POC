
package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tab {

    @SerializedName("background")
    @Expose
    private String background = "";
    @SerializedName("opacity")
    @Expose
    private String opacity = "";
    @SerializedName("background_HTML")
    @Expose
    private String backgroundHTML = "";

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getOpacity() {
        return opacity;
    }

    public void setOpacity(String opacity) {
        this.opacity = opacity;
    }

    public String getBackgroundHTML() {
        return backgroundHTML;
    }

    public void setBackgroundHTML(String backgroundHTML) {
        this.backgroundHTML = backgroundHTML;
    }

}
