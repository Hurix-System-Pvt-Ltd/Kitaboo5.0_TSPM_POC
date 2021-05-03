package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

import java.util.List;

public class GradientColor {
    private String color;
    private  List<String> opacity;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public   List<String> getOpacity() {
        return opacity;
    }

    public void setOpacity( List<String> opacity) {
        this.opacity = opacity;
    }
}
