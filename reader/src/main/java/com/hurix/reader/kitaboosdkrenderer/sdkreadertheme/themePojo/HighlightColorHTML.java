package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HighlightColorHTML implements Serializable {


    @SerializedName("Orange")
    @Expose
    private String orange;
    @SerializedName("Pink")
    @Expose
    private String pink;
    @SerializedName("Purple")
    @Expose
    private String purple;
    @SerializedName("Green")
    @Expose
    private String green;
    @SerializedName("Blue")
    @Expose
    private String blue;

    @SerializedName("Yellow")
    @Expose
    private String yellow;
    @SerializedName("Red")
    @Expose
    private String red;



    public String getOrange() {
        return orange;
    }

    public void setOrange(String orange) {
        this.orange = orange;
    }

    public String getPink() {
        return pink;
    }

    public void setPink(String pink) {
        this.pink = pink;
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

    public String getYellow() {
        return yellow;
    }

    public void setYellow(String yellow) {
        this.yellow = yellow;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

}
