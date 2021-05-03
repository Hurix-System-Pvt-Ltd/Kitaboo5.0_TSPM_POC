
package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Main implements Serializable {

    @SerializedName("canvas_background")
    @Expose
    private String canvasBackground;

    @SerializedName("navigation_arrows")
    @Expose
    private String navigationArrows;

    @SerializedName("toolbar")
    @Expose
    private Toolbar toolbar;

    public String getCanvasBackground() {
        return canvasBackground;
    }

    public void setCanvasBackground(String canvasBackground) {
        this.canvasBackground = canvasBackground;
    }

    public String getNavigationArrows() {
        return navigationArrows;
    }

    public void setNavigationArrows(String navigationArrows) {
        this.navigationArrows = navigationArrows;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

}
