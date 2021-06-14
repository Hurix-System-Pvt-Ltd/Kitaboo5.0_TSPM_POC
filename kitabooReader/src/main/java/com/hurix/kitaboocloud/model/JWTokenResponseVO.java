package com.hurix.kitaboocloud.model;

public class JWTokenResponseVO {

    private String userType;
    private String readerPageMode;
    private String readerScrollingMode;
    private String readerNightMode;
    private String readerReadingMode;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getReaderPageMode() {
        return readerPageMode;
    }

    public void setReaderPageMode(String readerPageMode) {
        this.readerPageMode = readerPageMode;
    }

    public String getReaderScrollingMode() {
        return readerScrollingMode;
    }

    public void setReaderScrollingMode(String readerScrollingMode) {
        this.readerScrollingMode = readerScrollingMode;
    }

    public String getReaderNightMode() {
        return readerNightMode;
    }

    public void setReaderNightMode(String readerNightMode) {
        this.readerNightMode = readerNightMode;
    }

    public String getReaderReadingMode() {
        return readerReadingMode;
    }

    public void setReaderReadingMode(String readerReadingMode) {
        this.readerReadingMode = readerReadingMode;
    }
}
