package com.hurix.reader.kitaboosdkrenderer.enums;


public enum KitabooBookShelfType {
    TAB_OLD("tabold"),
    TAB_ENTERPRISE("tab_enterprise"),
    MOBILE("mobile"),
    BOOK_COLLECTION("bookcollection");

    KitabooBookShelfType(final String text) {
        this.text = text;
    }

    private String text;

    @Override
    public String toString() {
        return text;
    }
}