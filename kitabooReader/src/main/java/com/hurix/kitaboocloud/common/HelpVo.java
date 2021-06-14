package com.hurix.kitaboocloud.common;

import java.io.Serializable;

/**
 * Created by harish.edara on 4/1/2016.
 */
public class HelpVo implements Serializable {

    private float mX;
    private float mY;
    private int mLeft;
    private int mTop;
    private int mWidth;
    private int mHeight;
    private String mViewText;
    private int mHelpText;
    private boolean bottomState;
    private boolean isBookShelf;
    private float textSize;
    private String type;

    public HelpVo(int mHelpText,float mX, float mY, int mLeft, int mTop, int mWidth, int mHeight ,String mViewText,float mtextSize,boolean bottomState,boolean isBookShelf) {
        this.mHelpText =  mHelpText;
        this.mX = mX;
        this.mY = mY;
        this.mLeft = mLeft;
        this.mTop = mTop;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.mViewText = mViewText;
        this.bottomState = bottomState;
        this.isBookShelf = isBookShelf;
        this.textSize=mtextSize;
    }
    public float getViewY() {
        return mY;
    }
    public void setViewY(float mY) {
        this.mY = mY;
    }
    public float getViewX() {
        return mX;
    }
    public void setViewX(float mX) {
        this.mX = mX;
    }
    public int getViewLeft() {
        return mLeft;
    }
    public void setViewLeft(int mLeft) {
        this.mLeft = mLeft;
    }
    public int getViewTop() {
        return mTop;
    }
    public int getViewWidth() {
        return mWidth;
    }
    public int getViewHeight() {
        return mHeight;
    }
    public void setViewTop(int mTop) {
        this.mTop = mTop;
    }
    public String getViewText() {
        return mViewText;
    }
    public int getHelpText() {
        return mHelpText;
    }
    public boolean getBottomState(){return  bottomState;}
    public boolean isBookShelf(){return  isBookShelf;}
    public void setViewText(String mViewText) {
        this.mViewText = mViewText;
    }

    public float getTextSize() {
        return textSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
