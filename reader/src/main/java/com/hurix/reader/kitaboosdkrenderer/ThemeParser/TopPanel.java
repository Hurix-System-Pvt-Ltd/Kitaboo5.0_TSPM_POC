package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

public class TopPanel {


   private String background,iconsColor,categoriesTextColor,categoriesTextSelectedColor,categoriesTextSelectedBar,arrowColor,profileBorder;
   private ToolTip toolTip;
   private ArrowColorDisabled arrowColorDisabled;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getIconsColor() {
        return iconsColor;
    }

    public ToolTip getToolTip() {
        return toolTip;
    }

    public void setToolTip(ToolTip toolTip) {
        this.toolTip = toolTip;
    }

    public ArrowColorDisabled getArrowColorDisabled() {
        return arrowColorDisabled;
    }

    public void setArrowColorDisabled(ArrowColorDisabled arrowColorDisabled) {
        this.arrowColorDisabled = arrowColorDisabled;
    }

    public void setIconsColor(String iconsColor) {
        this.iconsColor = iconsColor;
    }

    public String getCategoriesTextColor() {
        return categoriesTextColor;
    }

    public void setCategoriesTextColor(String categoriesTextColor) {
        this.categoriesTextColor = categoriesTextColor;
    }

    public String getCategoriesTextSelectedColor() {
        return categoriesTextSelectedColor;
    }

    public void setCategoriesTextSelectedColor(String categoriesTextSelectedColor) {
        this.categoriesTextSelectedColor = categoriesTextSelectedColor;
    }

    public String getCategoriesTextSelectedBar() {
        return categoriesTextSelectedBar;
    }

    public void setCategoriesTextSelectedBar(String categoriesTextSelectedBar) {
        this.categoriesTextSelectedBar = categoriesTextSelectedBar;
    }

    public String getArrowColor() {
        return arrowColor;
    }

    public void setArrowColor(String arrowColor) {
        this.arrowColor = arrowColor;
    }

    public String getProfileBorder() {
        return profileBorder;
    }

    public void setProfileBorder(String profileBorder) {
        this.profileBorder = profileBorder;
    }
}
