package com.hurix.kitaboo.constants.enums;

public enum KitabooPlayerType {
    TAB_OLD("tabold"),
    TAB_ENTERPRISE("tabenterprise"),
    MOBILE("mobile");

    KitabooPlayerType(final String text)
    {
        this.text = text;
    }

    private String text;

    @Override
    public String toString()
    {
        return text;
    }
}