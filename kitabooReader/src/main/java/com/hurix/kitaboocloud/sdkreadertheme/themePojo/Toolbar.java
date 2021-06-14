
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Toolbar implements Serializable {

    @SerializedName("top")
    @Expose
    private Top top;
    @SerializedName("side_bottom")
    @Expose
    private SideBottom sideBottom;

    public Top getTop() {
        return top;
    }

    public void setTop(Top top) {
        this.top = top;
    }

    public SideBottom getSideBottom() {
        return sideBottom;
    }

    public void setSideBottom(SideBottom sideBottom) {
        this.sideBottom = sideBottom;
    }

}
