
package com.hurix.kitaboocloud.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Pentool implements Serializable {

    @SerializedName("toolbar")
    @Expose
    private Toolbar_ toolbar;
    @SerializedName("Pen")
    @Expose
    private Pen pen;
    @SerializedName("Thickness")
    @Expose
    private Thickness thickness;
    @SerializedName("pen-color_HTML")
    @Expose
    private PenColorHTML penColorHTML;

    @SerializedName("pen-color")
    @Expose
    private List<String> penColor;

    public Toolbar_ getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar_ toolbar) {
        this.toolbar = toolbar;
    }

    public Pen getPen() {
        return pen;
    }

    public void setPen(Pen pen) {
        this.pen = pen;
    }

    public Thickness getThickness() {
        return thickness;
    }

    public void setThickness(Thickness thickness) {
        this.thickness = thickness;
    }

    public PenColorHTML getPenColorHTML() {
        return penColorHTML;
    }

    public void setPenColorHTML(PenColorHTML penColorHTML) {
        this.penColorHTML = penColorHTML;
    }

   /* public String getPenColor() {
        return penColor;
    }

    public void setPenColor(String penColor) {
        this.penColor = penColor;
    }*/

    public List<String> getPenColor() {
        return penColor;
    }

    public void setPenColor(List<String> penColor) {
        this.penColor = penColor;
    }

}
