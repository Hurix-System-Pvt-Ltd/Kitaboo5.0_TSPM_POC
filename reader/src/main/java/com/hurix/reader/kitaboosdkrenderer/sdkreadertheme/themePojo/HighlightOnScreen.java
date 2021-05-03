package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HighlightOnScreen implements Serializable {

    @SerializedName("highlightColor")
    @Expose
    private List<String> highlightColor = null;
    @SerializedName("highlightColor_HTML")
    @Expose
    private HighlightColorHTML highlightColorHTML;

    public List<String> getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(List<String> highlightColor) {
        this.highlightColor = highlightColor;
    }

    public HighlightColorHTML getHighlightColorHTML() {
        return highlightColorHTML;
    }

    public void setHighlightColorHTML(HighlightColorHTML highlightColorHTML) {
        this.highlightColorHTML = highlightColorHTML;
    }

}
