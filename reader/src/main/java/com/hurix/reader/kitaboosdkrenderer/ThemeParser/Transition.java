package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

public class Transition {

    private String titleTextColor,radioButtonSelected,textColor,iconsColor,iconsColorSelected;
    private TransitionRadioButton transitionRadioButton;

    public String getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(String titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    public String getRadioButtonSelected() {
        return radioButtonSelected;
    }

    public void setRadioButtonSelected(String radioButtonSelected) {
        this.radioButtonSelected = radioButtonSelected;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getIconsColor() {
        return iconsColor;
    }

    public void setIconsColor(String iconsColor) {
        this.iconsColor = iconsColor;
    }

    public String getIconsColorSelected() {
        return iconsColorSelected;
    }

    public void setIconsColorSelected(String iconsColorSelected) {
        this.iconsColorSelected = iconsColorSelected;
    }

    public TransitionRadioButton getRadioButtom() {
        return transitionRadioButton;
    }

    public void setRadioButton(TransitionRadioButton transitionRadioButton) {
        this.transitionRadioButton = transitionRadioButton;
    }
}
