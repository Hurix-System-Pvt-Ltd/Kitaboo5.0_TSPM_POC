
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ActionButton implements Serializable {

    @SerializedName("main")
    @Expose
    private Main_ main;
    @SerializedName("cancel")
    @Expose
    private Cancel cancel;

    public Main_ getMain() {
        return main;
    }

    public void setMain(Main_ main) {
        this.main = main;
    }

    public Cancel getCancel() {
        return cancel;
    }

    public void setCancel(Cancel cancel) {
        this.cancel = cancel;
    }

}
