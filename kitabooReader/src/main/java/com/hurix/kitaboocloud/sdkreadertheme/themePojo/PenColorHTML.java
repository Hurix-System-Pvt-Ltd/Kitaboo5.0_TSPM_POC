package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PenColorHTML implements Serializable {

    @SerializedName("Black")
    @Expose
    private String black;
    @SerializedName("Red")
    @Expose
    private String red;
    @SerializedName("Purple")
    @Expose
    private String purple;
    @SerializedName("Green")
    @Expose
    private String green;
    @SerializedName("Blue")
    @Expose
    private String blue;

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public String getPurple() {
        return purple;
    }

    public void setPurple(String purple) {
        this.purple = purple;
    }

    public String getGreen() {
        return green;
    }

    public void setGreen(String green) {
        this.green = green;
    }

    public String getBlue() {
        return blue;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }

}
