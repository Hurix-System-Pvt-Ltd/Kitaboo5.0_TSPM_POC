package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;

public class WorldbookSearchItemPOJO {

    private boolean isAuthorChecked,isBookTitleChecked, isSubjectChecked, isSeriesTitleChecked, isPublisherChecked,isBookContentChecked,isCategoryChecked, isISBNChecked;
    private String  isInterestLevelSelected;

    public WorldbookSearchItemPOJO() {

    }


    public WorldbookSearchItemPOJO(boolean isAuthorChecked, boolean isBookTitleChecked, boolean isSubjectChecked, boolean isSeriesTitleChecked,
                                   boolean isPublisherChecked, boolean isBookContentChecked, String isInterestLevelSelected, boolean isCategoryChecked, boolean isISBNChecked) {
        this.isAuthorChecked = isAuthorChecked;
        this.isBookTitleChecked = isBookTitleChecked;
        this.isSubjectChecked = isSubjectChecked;
        this.isSeriesTitleChecked = isSeriesTitleChecked;
        this.isPublisherChecked = isPublisherChecked;
        this.isBookContentChecked = isBookContentChecked;
        this.isInterestLevelSelected = isInterestLevelSelected;
        this.isCategoryChecked = isCategoryChecked;
        this.isISBNChecked = isISBNChecked;
    }
    public boolean getIsAuthorChecked() {
        return isAuthorChecked;
    }

    public void setIsAuthorChecked(boolean isAuthorChecked) {
        this.isAuthorChecked = isAuthorChecked;
    }

    public boolean getIsBookTitleChecked() {
        return isBookTitleChecked;
    }

    public void setIsBookTitleChecked(boolean isBookTitleChecked) {
        this.isBookTitleChecked = isBookTitleChecked;
    }

    public boolean getIsSubjectChecked() {
        return isSubjectChecked;
    }

    public void setIsSubjectChecked(boolean isSubjectChecked) {
        this.isSubjectChecked = isSubjectChecked;
    }

    public boolean getIsSeriesTitleChecked() {
        return isSeriesTitleChecked;
    }

    public void setIsSeriesTitleChecked(boolean isSeriesTitleChecked) {
        this.isSeriesTitleChecked = isSeriesTitleChecked;
    }

    public boolean getIsPublisherChecked() {
        return isPublisherChecked;
    }

    public void setIsPublisherChecked(boolean isPublisherChecked) {
        this.isPublisherChecked = isPublisherChecked;
    }

    public boolean getIsBookContentChecked() {
        return isBookContentChecked;
    }

    public void setIsBookContentChecked(boolean isBookContentChecked) {
        this.isBookContentChecked = isBookContentChecked;
    }

    public String getIsInterestLevelSelected() {
        return isInterestLevelSelected;
    }

    public void setIsInterestLevelSelected(String isInterestLevelSelected) {
        this.isInterestLevelSelected = isInterestLevelSelected;
    }

    public boolean  getIsISBNChecked() {
        return isISBNChecked;
    }

    public void setIsISBNChecked(boolean isISBNChecked) {
        this.isISBNChecked = isISBNChecked;
    }

    public boolean getIsCategoryChecked() {
        return isCategoryChecked;
    }

    public void setCategoryChecked(boolean categoryChecked) {
        isCategoryChecked = categoryChecked;
    }

}
