package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

public class Bookshelf {
    private String fontFace,background,shelfCategoryTextColor,linkTextColor,arrowColor;
    private TopPanel topPanel;
    private Card card;
    private FavouriteIcon favouriteIcon;
    private BottomPanel bottomPanel;
    private Collection collection;
    private Moreinfo moreinfo;
    private Search search;
    private SearchResult searchResult;
    private AddContent addContent;
    private OtherPopups otherPopups;

    public String getFontFace() {
        return fontFace;
    }

    public void setFontFace(String fontFace) {
        this.fontFace = fontFace;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getShelfCategoryTextColor() {
        return shelfCategoryTextColor;
    }

    public void setShelfCategoryTextColor(String shelfCategoryTextColor) {
        this.shelfCategoryTextColor = shelfCategoryTextColor;
    }

    public String getLinkTextColor() {
        return linkTextColor;
    }

    public void setLinkTextColor(String linkTextColor) {
        this.linkTextColor = linkTextColor;
    }

    public String getArrowColor() {
        return arrowColor;
    }

    public void setArrowColor(String arrowColor) {
        this.arrowColor = arrowColor;
    }

    public TopPanel getTopPanel() {
        return topPanel;
    }

    public void setTopPanel(TopPanel topPanel) {
        this.topPanel = topPanel;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public FavouriteIcon getFavouriteIcon() {
        return favouriteIcon;
    }

    public void setFavouriteIcon(FavouriteIcon favouriteIcon) {
        this.favouriteIcon = favouriteIcon;
    }

    public BottomPanel getBottomPanel() {
        return bottomPanel;
    }

    public void setBottomPanel(BottomPanel bottomPanel) {
        this.bottomPanel = bottomPanel;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Moreinfo getMoreinfo() {
        return moreinfo;
    }

    public void setMoreinfo(Moreinfo moreinfo) {
        this.moreinfo = moreinfo;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public AddContent getAddContent() {
        return addContent;
    }

    public void setAddContent(AddContent addContent) {
        this.addContent = addContent;
    }

    public OtherPopups getOtherPopups() {
        return otherPopups;
    }

    public void setOtherPopups(OtherPopups otherPopups) {
        this.otherPopups = otherPopups;
    }
}
