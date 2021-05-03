package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

public class SearchResult {

  private String textColor,resultTextColor;
  private SearchResultCard searchResultCard;

    public SearchResultCard getSearchResultCard() {
        return searchResultCard;
    }

    public void setSearchResultCard(SearchResultCard searchResultCard) {
        this.searchResultCard = searchResultCard;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getResultTextColor() {
        return resultTextColor;
    }

    public void setResultTextColor(String resultTextColor) {
        this.resultTextColor = resultTextColor;
    }
}
