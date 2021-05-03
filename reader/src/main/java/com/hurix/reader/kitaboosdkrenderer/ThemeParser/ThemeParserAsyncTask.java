package com.hurix.reader.kitaboosdkrenderer.ThemeParser;

import android.os.AsyncTask;


import com.hurix.reader.kitaboosdkrenderer.views.AddThemeParseCallback;

import org.json.JSONObject;

import java.util.Collections;

public class ThemeParserAsyncTask extends AsyncTask <String, Void, ThemeParent> {
        private AddThemeParseCallback mListener;
    public ThemeParserAsyncTask(AddThemeParseCallback listenr) {
        mListener = listenr ;
    }

    @Override
    protected void onPostExecute(ThemeParent parent) {
        super.onPostExecute(parent);
        mListener.onThemeParseCompleted(parent);
    }

    @Override
    protected ThemeParent doInBackground(String... strings) {
        ThemeParent themeParent = new ThemeParent();

        try {
            {
                JSONObject response = new JSONObject(strings[0]);

                Bookshelf bookshelf = new Bookshelf();

                JSONObject bookshelfJson = response.getJSONObject("bookshelf");
                if (bookshelfJson.has("font_face")) {
                    String fontFace = bookshelfJson.getString("font_face");
                    bookshelf.setFontFace(fontFace);
                }

                if (bookshelfJson.has("background")) {
                    String background = bookshelfJson.getString("background");
                    bookshelf.setBackground(background);
                }

                if (bookshelfJson.has("shelf_category_text_color")) {
                    String shelfCategoryTextColor = bookshelfJson.getString("shelf_category_text_color");
                    bookshelf.setShelfCategoryTextColor(shelfCategoryTextColor);
                }

                if (bookshelfJson.has("link_text_color")) {
                    String linkTextColor = bookshelfJson.getString("link_text_color");
                    bookshelf.setLinkTextColor(linkTextColor);
                }

                if (bookshelfJson.has("arrow_color")) {
                    String arrowColor = bookshelfJson.getString("arrow_color");
                    bookshelf.setArrowColor(arrowColor);
                }

                JSONObject topPanelJson = bookshelfJson.getJSONObject("toppanel");
                TopPanel topPanel = new TopPanel();
//background,iconsColor,categoriesTextColor,categoriesTextSelectedColor,categoriesTextSelectedBar,arrowColor,profileBorder;
                if (topPanelJson.has("background")) {
                    String background = topPanelJson.getString("background");
                    topPanel.setBackground(background);
                }

                if (topPanelJson.has("icons_color")) {
                    String iconsColor = topPanelJson.getString("icons_color");
                    topPanel.setIconsColor(iconsColor);
                }

                if (topPanelJson.has("categories_text_color")) {
                    String categoriesTextColor = topPanelJson.getString("categories_text_color");
                    topPanel.setCategoriesTextColor(categoriesTextColor);
                }

                if (topPanelJson.has("categories_text_selected_color")) {
                    String categoriesTextSelectedColor = topPanelJson.getString("categories_text_selected_color");
                    topPanel.setCategoriesTextSelectedColor(categoriesTextSelectedColor);
                }

                if (topPanelJson.has("categories_text_selected_bar")) {
                    String categoriesTextSelectedBar = topPanelJson.getString("categories_text_selected_bar");
                    topPanel.setCategoriesTextSelectedBar(categoriesTextSelectedBar);
                }

                if (topPanelJson.has("arrow_color")) {
                    String arrowColor = topPanelJson.getString("arrow_color");
                    topPanel.setArrowColor(arrowColor);
                }

                if (topPanelJson.has("profile_border")) {
                    String profileBorder = topPanelJson.getString("profile_border");
                    topPanel.setProfileBorder(profileBorder);
                }

                JSONObject toolTipJson = topPanelJson.getJSONObject("tooltip");
                ToolTip toolTip = new ToolTip();

                if (toolTipJson.has("background")) {
                    String background = toolTipJson.getString("background");
                    toolTip.setBackground(background);
                }
                if (toolTipJson.has("text_color")) {
                    String textColor = toolTipJson.getString("text_color");
                    toolTip.setTextColor(textColor);
                }

                topPanel.setToolTip(toolTip);

                JSONObject arrowColorDisabledJson = topPanelJson.getJSONObject("arrow_color_disabled");
                ArrowColorDisabled arrowColorDisabled = new ArrowColorDisabled();

                if (arrowColorDisabledJson.has("arrow_color")) {
                    String arrowColor = arrowColorDisabledJson.getString("arrow_color");
                    arrowColorDisabled.setArrowColor(arrowColor);
                }

                if (arrowColorDisabledJson.has("opacity")) {
                    String opacity = arrowColorDisabledJson.getString("opacity");
                    arrowColorDisabled.setOpacity(opacity);
                }
                if (arrowColorDisabledJson.has("arrow_color_HTML")) {
                    String arrowColorHTML = arrowColorDisabledJson.getString("arrow_color_HTML");
                    arrowColorDisabled.setArrowColorHTML(arrowColorHTML);
                }

                topPanel.setArrowColorDisabled(arrowColorDisabled);
                bookshelf.setTopPanel(topPanel);

                JSONObject cardJson = bookshelfJson.getJSONObject("card");
                Card card = new Card();
                //background,titleTextColor,typeTextColor,iconsColor,downloadingBorder,thumbnailBackground;
                if (cardJson.has("background")) {
                    String background = cardJson.getString("background");
                    card.setBackground(background);
                }

                if (cardJson.has("title_text_color")) {
                    String titleTextColor = cardJson.getString("title_text_color");
                    card.setTitleTextColor(titleTextColor);
                }

                if (cardJson.has("type_text_color")) {
                    String typeTextColor = cardJson.getString("type_text_color");
                    card.setTypeTextColor(typeTextColor);
                }

                if (cardJson.has("icons_color")) {
                    String iconsColor = cardJson.getString("icons_color");
                    card.setIconsColor(iconsColor);
                }

                if (cardJson.has("downloading_border")) {
                    String downloadingBorder = cardJson.getString("downloading_border");
                    card.setDownloadingBorder(downloadingBorder);
                }

                if (cardJson.has("thumbnail_background")) {
                    String thumbnailBackground = cardJson.getString("thumbnail_background");
                    card.setThumbnailBackground(thumbnailBackground);
                }
                bookshelf.setCard(card);

                JSONObject favouriteIconJson = bookshelfJson.getJSONObject("favourite_icon");
                FavouriteIcon favouriteIcon = new FavouriteIcon();
                //background,border,iconsColor,iconsColorSelected;
                if (favouriteIconJson.has("background")) {
                    String background = cardJson.getString("background");
                    favouriteIcon.setBackground(background);
                }

                if (favouriteIconJson.has("border")) {
                    String border = favouriteIconJson.getString("border");
                    favouriteIcon.setBorder(border);
                }

                if (favouriteIconJson.has("icons_color")) {
                    String iconsColor = favouriteIconJson.getString("icons_color");
                    favouriteIcon.setIconsColor(iconsColor);
                }

                if (favouriteIconJson.has("icons_color_selected")) {
                    String iconsColorSelected = favouriteIconJson.getString("icons_color_selected");
                    favouriteIcon.setIconsColorSelected(iconsColorSelected);
                }

                bookshelf.setFavouriteIcon(favouriteIcon);

                JSONObject bottomPanelJson = bookshelfJson.getJSONObject("bottompanel");
                BottomPanel bottomPanel = new BottomPanel();
                //background,textColor,iconsColor;
                if (bottomPanelJson.has("background")) {
                    String background = bottomPanelJson.getString("background");
                    bottomPanel.setBackground(background);
                }

                if (bottomPanelJson.has("text_color")) {
                    String textColor = bottomPanelJson.getString("text_color");
                    bottomPanel.setTextColor(textColor);
                }

                if (bottomPanelJson.has("icons_color")) {
                    String iconsColor = bottomPanelJson.getString("icons_color");
                    bottomPanel.setIconsColor(iconsColor);
                }
                bookshelf.setBottomPanel(bottomPanel);

                JSONObject collectionJson = bookshelfJson.getJSONObject("Collection");
                Collection collection = new Collection();

                if (collectionJson.has("background")) {
                    String background = collectionJson.getString("background");
                    collection.setBackground(background);
                }

                if (collectionJson.has("title_text_color")) {
                    String titleTextColor = collectionJson.getString("title_text_color");
                    collection.setTitleTextColor(titleTextColor);
                }

                if (collectionJson.has("items_text_color")) {
                    String itemsTextColor = collectionJson.getString("items_text_color");
                    collection.setItemsTextColor(itemsTextColor);
                }

                if (collectionJson.has("link_text_color_download")) {
                    String linkTextColorDownload = collectionJson.getString("link_text_color_download");
                    collection.setLinkTextColorDownload(linkTextColorDownload);
                }

                if (collectionJson.has("link_text_color_activated")) {
                    String linkTextColorActivated = collectionJson.getString("link_text_color_activated");
                    collection.setLinkTextColorActivated(linkTextColorActivated);
                }

                if (collectionJson.has("divider_color")) {
                    String dividerColor = collectionJson.getString("divider_color");
                    collection.setDividerColor(dividerColor);
                }

                if (collectionJson.has("icons_color")) {
                    String iconsColor = collectionJson.getString("icons_color");
                    collection.setIconsColor(iconsColor);
                }

                if (collectionJson.has("arrow_color")) {
                    String arrowColor = collectionJson.getString("arrow_color");
                    collection.setArrowColor(arrowColor);
                }

                JSONObject linkTextColorDisabledJson = collectionJson.getJSONObject("link_text_color_disabled");
                LinkTextColorDisabled linkTextColorDisabled = new LinkTextColorDisabled();

                if (linkTextColorDisabledJson.has("link_text_color_activated")) {
                    String linkTextColorActivated = linkTextColorDisabledJson.getString("link_text_color_activated");
                    linkTextColorDisabled.setLinkTextColorActivated(linkTextColorActivated);
                }

                if (linkTextColorDisabledJson.has("opacity")) {
                    String opacity = linkTextColorDisabledJson.getString("opacity");
                    linkTextColorDisabled.setOpacity(opacity);
                }

                if (linkTextColorDisabledJson.has("link_text_color_activated_HTML")) {
                    String linkTextColorActivatedHTML = linkTextColorDisabledJson.getString("link_text_color_activated_HTML");
                    linkTextColorDisabled.setLinkTextColorActivatedHTML(linkTextColorActivatedHTML);
                }
                collection.setLinkTextColorDisabled(linkTextColorDisabled);
                bookshelf.setCollection(collection);

                JSONObject moreinfoJson = bookshelfJson.getJSONObject("Moreinfo");
                Moreinfo moreinfo = new Moreinfo();

                JSONObject moreinfoOverlayPanelJson = moreinfoJson.getJSONObject("overlay_panel");
                OverlayPanel moreinfoOverlayPanel = new OverlayPanel();

                if (moreinfoOverlayPanelJson.has("background")) {
                    String background = moreinfoOverlayPanelJson.getString("background");
                    moreinfoOverlayPanel.setBackground(background);
                }

                if (moreinfoOverlayPanelJson.has("opacity")) {
                    String opacity = moreinfoOverlayPanelJson.getString("opacity");
                    moreinfoOverlayPanel.setOpacity(opacity);
                }

                if (moreinfoOverlayPanelJson.has("background_HTML")) {
                    String backgroundHTML = moreinfoOverlayPanelJson.getString("background_HTML");
                    moreinfoOverlayPanel.setBackgroundHTML(backgroundHTML);
                }
                moreinfo.setOverlayPanel(moreinfoOverlayPanel);

                JSONObject moreinfoPopupJson = moreinfoJson.getJSONObject("popup");
                MoreinfoPopup moreinfoPopup = new MoreinfoPopup();

                if (moreinfoPopupJson.has("background")) {
                    String background = moreinfoPopupJson.getString("background");
                    moreinfoPopup.setBackground(background);
                }

                if (moreinfoPopupJson.has("border")) {
                    String border = moreinfoPopupJson.getString("border");
                    moreinfoPopup.setBorder(border);
                }

                if (moreinfoPopupJson.has("Moreinfo_text_color")) {
                    String moreinfoTextColor = moreinfoPopupJson.getString("Moreinfo_text_color");
                    moreinfoPopup.setMoreinfoTextColor(moreinfoTextColor);
                }

                if (moreinfoPopupJson.has("title_text_color")) {
                    String titleTextColor = moreinfoPopupJson.getString("title_text_color");
                    moreinfoPopup.setTitleTextColor(titleTextColor);
                }

                if (moreinfoPopupJson.has("type_text_color")) {
                    String typeTextColor = moreinfoPopupJson.getString("type_text_color");
                    moreinfoPopup.setTypeTextColor(typeTextColor);
                }

                if (moreinfoPopupJson.has("metadata_text_color")) {
                    String metadataTextColor = moreinfoPopupJson.getString("metadata_text_color");
                    moreinfoPopup.setMetadataTextColor(metadataTextColor);
                }

                if (moreinfoPopupJson.has("description_text_color")) {
                    String descriptionTextColor = moreinfoPopupJson.getString("description_text_color");
                    moreinfoPopup.setDescriptionTextColor(descriptionTextColor);
                }

                if (moreinfoPopupJson.has("button_text_color")) {
                    String buttonTextColor = moreinfoPopupJson.getString("button_text_color");
                    moreinfoPopup.setButtonTextColor(buttonTextColor);
                }
                moreinfo.setPopup(moreinfoPopup);
                bookshelf.setMoreinfo(moreinfo);

                JSONObject searchJson = bookshelfJson.getJSONObject("Search");
                Search search = new Search();

                if (searchJson.has("background")) {
                    String background = searchJson.getString("background");
                    search.setBackground(background);
                }

                if (searchJson.has("default_icon")) {
                    String defaultIcon = searchJson.getString("default_icon");
                    search.setDefaultIcon(defaultIcon);
                }

                if (searchJson.has("default_text")) {
                    String defaultText = searchJson.getString("default_text");
                    search.setDefaultText(defaultText);
                }

                if (searchJson.has("icons_color")) {
                    String iconsColor = searchJson.getString("icons_color");
                    search.setIconsColor(iconsColor);
                }

                if (searchJson.has("close_icon_color")) {
                    String closeIconColor = searchJson.getString("close_icon_color");
                    search.setCloseIconColor(closeIconColor);
                }

                if (searchJson.has("line_color")) {
                    String lineColor = searchJson.getString("line_color");
                    search.setLineColor(lineColor);
                }

                if (searchJson.has("input_panel_background")) {
                    String inputPanelBackground = searchJson.getString("input_panel_background");
                    search.setInputPanelBackground(inputPanelBackground);
                }

                if (searchJson.has("hint_text_color")) {
                    String hintTextColor = searchJson.getString("hint_text_color");
                    search.setHintTextColor(hintTextColor);
                }

                if (searchJson.has("input_text_color")) {
                    String inputTextColor = searchJson.getString("input_text_color");
                    search.setInputTextColor(inputTextColor);
                }

                if (searchJson.has("instances_text_color")) {
                    String instancesTextColor = searchJson.getString("instances_text_color");
                    search.setInstancesTextColor(instancesTextColor);
                }

                if (searchJson.has("searchresult_text_color")) {
                    String searchresultTextColor = searchJson.getString("searchresult_text_color");
                    search.setSearchresultTextColor(searchresultTextColor);
                }
                bookshelf.setSearch(search);

                JSONObject searchResultJson = bookshelfJson.getJSONObject("SearchResult");
                SearchResult searchResult = new SearchResult();

                JSONObject searchResultCardJson = searchResultJson.getJSONObject("card");
                SearchResultCard searchResultCard = new SearchResultCard();

                if (searchResultCardJson.has("background")) {
                    String background = searchResultCardJson.getString("background");
                    searchResultCard.setBackground(background);
                }

                if (searchResultCardJson.has("title_text_color")) {
                    String titleTextColor = searchResultCardJson.getString("title_text_color");
                    searchResultCard.setTitleTextColor(titleTextColor);
                }

                if (searchResultCardJson.has("metadata_text_color")) {
                    String metadataTextColor = searchResultCardJson.getString("metadata_text_color");
                    searchResultCard.setMetadataTextColor(metadataTextColor);
                }

                if (searchResultCardJson.has("thumbnail_background")) {
                    String thumbnailBackground = searchResultCardJson.getString("thumbnail_background");
                    searchResultCard.setThumbnailBackground(thumbnailBackground);
                }

                if (searchResultCardJson.has("description_text_color")) {
                    String descriptionTextColor = searchResultCardJson.getString("description_text_color");
                    searchResultCard.setDescriptionTextColor(descriptionTextColor);
                }
                searchResult.setSearchResultCard(searchResultCard);

                if (searchResultJson.has("text_color")) {
                    String textColor = searchResultJson.getString("text_color");
                    searchResult.setTextColor(textColor);
                }

                if (searchResultJson.has("result_text_color")) {
                    String resultTextColor = searchResultJson.getString("result_text_color");
                    searchResult.setResultTextColor(resultTextColor);
                }
                bookshelf.setSearchResult(searchResult);

                JSONObject addContentJson = bookshelfJson.getJSONObject("AddContent");
                AddContent addContent = new AddContent();

                JSONObject addContentOverlayPanelJson = addContentJson.getJSONObject("overlay_panel");
                OverlayPanel addContentOverlayPanel = new OverlayPanel();

                if (addContentOverlayPanelJson.has("background")) {
                    String background = addContentOverlayPanelJson.getString("background");
                    addContentOverlayPanel.setBackground(background);
                }

                if (addContentOverlayPanelJson.has("opacity")) {
                    String opacity = addContentOverlayPanelJson.getString("opacity");
                    addContentOverlayPanel.setOpacity(opacity);
                }

                if (addContentOverlayPanelJson.has("background_HTML")) {
                    String backgroundHTML = addContentOverlayPanelJson.getString("background_HTML");
                    addContentOverlayPanel.setBackgroundHTML(backgroundHTML);
                }
                addContent.setOverlayPanel(addContentOverlayPanel);

                JSONObject addContentPopupJson = addContentJson.getJSONObject("popup");
                AddContentPopup addContentPopup = new AddContentPopup();


                JSONObject addContentPopupActionButtonJson = addContentPopupJson.getJSONObject("Action_button");
                ActionButton addContentPopupActionButton = new ActionButton();

                if (addContentPopupActionButtonJson.has("background")) {
                    String background = addContentPopupActionButtonJson.getString("background");
                    addContentPopupActionButton.setBackground(background);
                }

                if (addContentPopupActionButtonJson.has("text_color")) {
                    String textColor = addContentPopupActionButtonJson.getString("text_color");
                    addContentPopupActionButton.setTextColor(textColor);
                }
                addContentPopup.setPopupActionButton(addContentPopupActionButton);
                if (addContentPopupJson.has("background")) {
                    String background = addContentPopupJson.getString("background");
                    addContentPopup.setBackground(background);
                }

                if (addContentPopupJson.has("border")) {
                    String border = addContentPopupJson.getString("border");
                    addContentPopup.setBorder(border);
                }

                if (addContentPopupJson.has("AddContent_text_color")) {
                    String addContentTextColor = addContentPopupJson.getString("AddContent_text_color");
                    addContentPopup.setAddContentTextColor(addContentTextColor);
                }

                if (addContentPopupJson.has("hint_text_color")) {
                    String hintTextColor = addContentPopupJson.getString("hint_text_color");
                    addContentPopup.setHintTextColor(hintTextColor);
                }

                if (addContentPopupJson.has("input_text_color")) {
                    String inputTextColor = addContentPopupJson.getString("input_text_color");
                    addContentPopup.setInputTextColor(inputTextColor);
                }

                if (addContentPopupJson.has("line_color")) {
                    String lineColor = addContentPopupJson.getString("line_color");
                    addContentPopup.setLineColor(lineColor);
                }

                if (addContentPopupJson.has("button_text_color")) {
                    String buttonTextColor = addContentPopupJson.getString("button_text_color");
                    addContentPopup.setButtonTextColor(buttonTextColor);
                }

                if (addContentPopupJson.has("Action_button_text_color")) {
                    String actionButtonTextColor = addContentPopupJson.getString("Action_button_text_color");
                    addContentPopup.setActionButtonTextColor(actionButtonTextColor);
                }

                if (addContentPopupJson.has("description_text_color")) {
                    String descriptionTextColor = addContentPopupJson.getString("description_text_color");
                    addContentPopup.setDescriptionTextColor(descriptionTextColor);
                }
                addContent.setAddContentPopup(addContentPopup);
                bookshelf.setAddContent(addContent);

                JSONObject otherPopupsJson = bookshelfJson.getJSONObject("OtherPopups");
                OtherPopups otherPopups = new OtherPopups();

                JSONObject otherPopupsOverlayPanelJson = addContentJson.getJSONObject("overlay_panel");
                OverlayPanel otherPopupsOverlayPanel = new OverlayPanel();

                if (otherPopupsOverlayPanelJson.has("background")) {
                    String background = otherPopupsOverlayPanelJson.getString("background");
                    otherPopupsOverlayPanel.setBackground(background);
                }

                if (otherPopupsOverlayPanelJson.has("opacity")) {
                    String opacity = otherPopupsOverlayPanelJson.getString("opacity");
                    otherPopupsOverlayPanel.setOpacity(opacity);
                }

                if (otherPopupsOverlayPanelJson.has("background_HTML")) {
                    String backgroundHTML = otherPopupsOverlayPanelJson.getString("background_HTML");
                    otherPopupsOverlayPanel.setBackgroundHTML(backgroundHTML);
                }
                otherPopups.setOverlayPanel(otherPopupsOverlayPanel);

                if (otherPopupsJson.has("Popup_background")) {
                    String popupBackground = otherPopupsJson.getString("Popup_background");
                    otherPopups.setPopupBackground(popupBackground);
                }

                if (otherPopupsJson.has("title_text_color")) {
                    String titleTextColor = otherPopupsJson.getString("title_text_color");
                    otherPopups.setTitleTextColor(titleTextColor);
                }

                if (otherPopupsJson.has("description_text_color")) {
                    String descriptionTextColor = otherPopupsJson.getString("description_text_color");
                    otherPopups.setDescriptionTextColor(descriptionTextColor);
                }

                if (otherPopupsJson.has("action_button_text_color")) {
                    String actionButtonTextColor = otherPopupsJson.getString("action_button_text_color");
                    otherPopups.setActionButtonTextColor(actionButtonTextColor);
                }

                if (otherPopupsJson.has("button_text_color")) {
                    String buttonTextColor = otherPopupsJson.getString("button_text_color");
                    otherPopups.setButtonTextColor(buttonTextColor);
                }
                bookshelf.setOtherPopups(otherPopups);
                themeParent.setBookshelf(bookshelf);

                ProfilePopup profilePopup = new ProfilePopup();
                JSONObject profilePopupJson = response.getJSONObject("Profile_popup");

                ProfileMailid ProfilePopupProfileMailid = new ProfileMailid();
                JSONObject profileMailidJson = profilePopupJson.getJSONObject("profile_mailid");

                if (profileMailidJson.has("text_color")) {
                    String textColor = profileMailidJson.getString("text_color");
                    ProfilePopupProfileMailid.setTextColor(textColor);
                }

                if (profileMailidJson.has("opacity")) {
                    String opacity = profileMailidJson.getString("opacity");
                    ProfilePopupProfileMailid.setOpacity(opacity);
                }

                if (profileMailidJson.has("text_color_HTML")) {
                    String textColorHTML = profileMailidJson.getString("text_color_HTML");
                    ProfilePopupProfileMailid.setTextColorHTML(textColorHTML);
                }
                profilePopup.setProfileMailid(ProfilePopupProfileMailid);

                Signout signout = new Signout();
                JSONObject signoutJson = profilePopupJson.getJSONObject("signout");

                if (signoutJson.has("background")) {
                    String background = signoutJson.getString("background");
                    signout.setBackground(background);
                }

                if (signoutJson.has("icons_color")) {
                    String iconsColor = signoutJson.getString("icons_color");
                    signout.setIconsColor(iconsColor);
                }

                if (signoutJson.has("text_color")) {
                    String textColor = signoutJson.getString("text_color");
                    signout.setTextColor(textColor);
                }
                profilePopup.setSignout(signout);

                if (profilePopupJson.has("text_color")) {
                    String textColor = profilePopupJson.getString("text_color");
                    profilePopup.setTextColor(textColor);
                }

                if (profilePopupJson.has("background_top")) {
                    String backgroundTop = profilePopupJson.getString("background_top");
                    profilePopup.setBackgroundTop(backgroundTop);
                }

                if (profilePopupJson.has("background_bottom")) {
                    String backgroundBottom = profilePopupJson.getString("background_bottom");
                    profilePopup.setBackgroundBottom(backgroundBottom);
                }

                if (profilePopupJson.has("profile_border")) {
                    String profileBorder = profilePopupJson.getString("profile_border");
                    profilePopup.setProfileBorder(profileBorder);
                }

                if (profilePopupJson.has("profile_name")) {
                    String profileName = profilePopupJson.getString("profile_name");
                    profilePopup.setProfileName(profileName);
                }

                if (profilePopupJson.has("changePassword_link_text_color")) {
                    String changePasswordLinkTextColor = profilePopupJson.getString("changePassword_link_text_color");
                    profilePopup.setChangePasswordLinkTextColor(changePasswordLinkTextColor);
                }

                if (profilePopupJson.has("link_text_color")) {
                    String linkTextColor = profilePopupJson.getString("link_text_color");
                    profilePopup.setLinkTextColor(linkTextColor);
                }

                if (profilePopupJson.has("divider_color")) {
                    String dividerColor = profilePopupJson.getString("divider_color");
                    profilePopup.setDividerColor(dividerColor);
                }

                if (profilePopupJson.has("icons_color")) {
                    String iconsColor = profilePopupJson.getString("icons_color");
                    profilePopup.setIconsColor(iconsColor);
                }
                themeParent.setProfilePopup(profilePopup);

                Profile profile = new Profile();
                JSONObject profileJson = response.getJSONObject("Profile");

                ProfileEdit profileEdit = new ProfileEdit();
                JSONObject profileEditJson = profileJson.getJSONObject("Profile_edit");

                OverlayPanel profileEditOverlayPanel = new OverlayPanel();
                JSONObject profileEditOverlayPanelJson = profileEditJson.getJSONObject("overlay_panel");

                if (profileEditOverlayPanelJson.has("background")) {
                    String background = profileEditOverlayPanelJson.getString("background");
                    profileEditOverlayPanel.setBackground(background);
                }

                if (profileEditOverlayPanelJson.has("opacity")) {
                    String opacity = profileEditOverlayPanelJson.getString("opacity");
                    profileEditOverlayPanel.setOpacity(opacity);
                }

                if (profileEditOverlayPanelJson.has("background_HTML")) {
                    String backgroundHTML = profileEditOverlayPanelJson.getString("background_HTML");
                    profileEditOverlayPanel.setBackgroundHTML(backgroundHTML);
                }
                profileEdit.setOverlayPanel(profileEditOverlayPanel);

                ProfileImageEdit profileImageEdit = new ProfileImageEdit();
                JSONObject profileImageEditJson = profileEditJson.getJSONObject("profile_image_edit");

                Background background = new Background();
                JSONObject backgroundJson = profileImageEditJson.getJSONObject("background");

                if (backgroundJson.has("background_color")) {
                    String backgroundColor = backgroundJson.getString("background_color");
                    background.setBackgroundColor(backgroundColor);
                }

                if (backgroundJson.has("opacity")) {
                    String opacity = backgroundJson.getString("opacity");
                    background.setOpacity(opacity);
                }

                if (backgroundJson.has("text_color_HTML")) {
                    String textColorHTML = backgroundJson.getString("text_color_HTML");
                    background.setTextColorHTML(textColorHTML);
                }
                profileImageEdit.setBackground(background);

                if (profileImageEditJson.has("icons_color")) {
                    String iconsColor = profileImageEditJson.getString("icons_color");
                    profileImageEdit.setIconsColor(iconsColor);
                }

                if (profileImageEditJson.has("text_color")) {
                    String textColor = profileImageEditJson.getString("text_color");
                    profileImageEdit.setTextColor(textColor);
                }
                profileEdit.setProfileImageEdit(profileImageEdit);

                ActionButton profileEditActionButton = new ActionButton();
                JSONObject profileEditActionButtonJson = profileEditJson.getJSONObject("Action_button");

                if (profileEditActionButtonJson.has("background")) {
                    String actionButtonbackground = profileEditActionButtonJson.getString("background");
                    profileEditActionButton.setBackground(actionButtonbackground);
                }

                if (profileEditActionButtonJson.has("text_color")) {
                    String textColor = profileEditActionButtonJson.getString("text_color");
                    profileEditActionButton.setTextColor(textColor);
                }
                profileEdit.setActionButton(profileEditActionButton);

                ProfileMailid profileEditProfileMailid = new ProfileMailid();
                JSONObject profileProfileMailidJson = profileEditJson.getJSONObject("profile_mailid");

                if (profileProfileMailidJson.has("text_color")) {
                    String textColor = profileProfileMailidJson.getString("text_color");
                    profileEditProfileMailid.setTextColor(textColor);
                }

                if (profileProfileMailidJson.has("opacity")) {
                    String opacity = profileProfileMailidJson.getString("opacity");
                    profileEditProfileMailid.setOpacity(opacity);
                }

                if (profileProfileMailidJson.has("text_color_HTML")) {
                    String textColorHTML = profileProfileMailidJson.getString("text_color_HTML");
                    profileEditProfileMailid.setTextColorHTML(textColorHTML);
                }
                profileEdit.setProfileMailid(profileEditProfileMailid);

                if (profileEditJson.has("background")) {
                    String profileEditBackground = profileEditJson.getString("background");
                    profileEdit.setBackground(profileEditBackground);
                }

                if (profileEditJson.has("profile_border")) {
                    String profileBorder = profileEditJson.getString("profile_border");
                    profileEdit.setProfileBorder(profileBorder);
                }

                if (profileEditJson.has("line_color")) {
                    String lineColor = profileEditJson.getString("line_color");
                    profileEdit.setLineColor(lineColor);
                }

                if (profileEditJson.has("hint_text_color")) {
                    String hintTextColor = profileEditJson.getString("hint_text_color");
                    profileEdit.setHintTextColor(hintTextColor);
                }

                if (profileEditJson.has("input_text_color")) {
                    String inputTextColor = profileEditJson.getString("input_text_color");
                    profileEdit.setInputTextColor(inputTextColor);
                }

                if (profileEditJson.has("line_color_selected")) {
                    String lineColorSelected = profileEditJson.getString("line_color_selected");
                    profileEdit.setLineColorSelected(lineColorSelected);
                }

                if (profileEditJson.has("hint_text_color_selected")) {
                    String hintTextColorSelected = profileEditJson.getString("hint_text_color_selected");
                    profileEdit.setHintTextColorSelected(hintTextColorSelected);
                }

                if (profileEditJson.has("input_text_color_selected")) {
                    String inputTextColorSelected = profileEditJson.getString("input_text_color_selected");
                    profileEdit.setInputTextColorSelected(inputTextColorSelected);
                }

                if (profileEditJson.has("close_icon_color")) {
                    String closeIconColor = profileEditJson.getString("close_icon_color");
                    profileEdit.setCloseIconColor(closeIconColor);
                }

                if (profileEditJson.has("button_text_color")) {
                    String buttonTextColor = profileEditJson.getString("button_text_color");
                    profileEdit.setButtonTextColor(buttonTextColor);
                }
                profile.setProfileEdit(profileEdit);

                Transition transition = new Transition();
                JSONObject transitionJson = profileJson.getJSONObject("Transition");

                TransitionRadioButton transitionRadioButton = new TransitionRadioButton();
                JSONObject transitionRadioButtonJson = transitionJson.getJSONObject("radiobutton");

                if (transitionRadioButtonJson.has("background")) {
                    String RadioButtonbackground = transitionRadioButtonJson.getString("background");
                    transitionRadioButton.setBackground(RadioButtonbackground);
                }

                if (transitionRadioButtonJson.has("border")) {
                    String border = transitionRadioButtonJson.getString("border");
                    transitionRadioButton.setBorder(border);
                }
                transition.setRadioButton(transitionRadioButton);

                if (transitionJson.has("title_text_color")) {
                    String titleTextColor = transitionJson.getString("title_text_color");
                    transition.setTitleTextColor(titleTextColor);
                }

                if (transitionJson.has("radiobutton_selected")) {
                    String radioButtonSelected = transitionJson.getString("radiobutton_selected");
                    transition.setRadioButtonSelected(radioButtonSelected);
                }

                if (transitionJson.has("text_color")) {
                    String textColor = transitionJson.getString("text_color");
                    transition.setTextColor(textColor);
                }

                if (transitionJson.has("icons_color")) {
                    String iconsColor = transitionJson.getString("icons_color");
                    transition.setIconsColor(iconsColor);
                }

                if (transitionJson.has("icons_color_selected")) {
                    String iconsColorSelected = transitionJson.getString("icons_color_selected");
                    transition.setIconsColorSelected(iconsColorSelected);
                }
                profile.setTransition(transition);

                Language language = new Language();
                JSONObject languageJson = profileJson.getJSONObject("Language");

                if (languageJson.has("title_text_color")) {
                    String titleTextColor = languageJson.getString("title_text_color");
                    language.setTitleTextColor(titleTextColor);
                }

                if (languageJson.has("input_panel_background")) {
                    String inputPanelBackground = languageJson.getString("input_panel_background");
                    language.setInputPanelBackground(inputPanelBackground);
                }

                if (languageJson.has("input_panel_border")) {
                    String inputPanelBorder = languageJson.getString("input_panel_border");
                    language.setInputPanelBorder(inputPanelBorder);
                }

                if (languageJson.has("input_panel_selected")) {
                    String inputPanelSelected = languageJson.getString("input_panel_selected");
                    language.setInputPanelSelected(inputPanelSelected);
                }

                if (languageJson.has("hint_text_color")) {
                    String hintTextColor = languageJson.getString("hint_text_color");
                    language.setHintTextColor(hintTextColor);
                }

                if (languageJson.has("hint_text_color_selected")) {
                    String hintTextColorSelected = languageJson.getString("hint_text_color_selected");
                    language.setHintTextColorSelected(hintTextColorSelected);
                }

                if (languageJson.has("text_color")) {
                    String textColor = languageJson.getString("text_color");
                    language.setTextColor(textColor);
                }

                if (languageJson.has("arrow_color")) {
                    String arrowColor = languageJson.getString("arrow_color");
                    language.setArrowColor(arrowColor);
                }
                profile.setLanguage(language);

                ProfileEditPopup profileEditPopup = new ProfileEditPopup();
                JSONObject profileEditPopupJson = profileJson.getJSONObject("Profile_edit_popup");

                OverlayPanel profileEditPopupOverlayPanel = new OverlayPanel();
                JSONObject profileEditPopupOverlayPanelJson = profileEditPopupJson.getJSONObject("overlay_panel");

                if (profileEditPopupOverlayPanelJson.has("background")) {
                    String profileEditPopupOverlayPanelBackground = profileEditPopupOverlayPanelJson.getString("background");
                    profileEditPopupOverlayPanel.setBackground(profileEditPopupOverlayPanelBackground);
                }

                if (profileEditPopupOverlayPanelJson.has("opacity")) {
                    String opacity = profileEditPopupOverlayPanelJson.getString("opacity");
                    profileEditPopupOverlayPanel.setOpacity(opacity);
                }

                if (profileEditPopupOverlayPanelJson.has("background_HTML")) {
                    String backgroundHTML = profileEditPopupOverlayPanelJson.getString("background_HTML");
                    profileEditPopupOverlayPanel.setBackgroundHTML(backgroundHTML);
                }
                profileEditPopup.setOverlayPanel(profileEditPopupOverlayPanel);

                if (profileEditPopupJson.has("background")) {
                    String profileEditPopupBackground = profileEditPopupJson.getString("background");
                    profileEditPopup.setBackground(profileEditPopupBackground);
                }

                if (profileEditPopupJson.has("profile_border")) {
                    String profileBorder = profileEditPopupJson.getString("profile_border");
                    profileEditPopup.setProfileBorder(profileBorder);
                }

                if (profileEditPopupJson.has("title_text_color")) {
                    String titleTextColor = profileEditPopupJson.getString("title_text_color");
                    profileEditPopup.setTitleTextColor(titleTextColor);
                }

                if (profileEditPopupJson.has("icons_color")) {
                    String iconsColor = profileEditPopupJson.getString("icons_color");
                    profileEditPopup.setIconsColor(iconsColor);
                }

                if (profileEditPopupJson.has("icons_color_border")) {
                    String iconsColorBorder = profileEditPopupJson.getString("icons_color_border");
                    profileEditPopup.setIconsColorBorder(iconsColorBorder);
                }

                if (profileEditPopupJson.has("text_color")) {
                    String textColor = profileEditPopupJson.getString("text_color");
                    profileEditPopup.setTextColor(textColor);
                }
                profile.setProfileEditPopup(profileEditPopup);

                ChangePassword changePassword = new ChangePassword();
                JSONObject changePasswordJson = profileJson.getJSONObject("ChangePassword");

                OverlayPanel changePasswordOverlayPanel = new OverlayPanel();
                JSONObject changePasswordOverlayPanelJson = changePasswordJson.getJSONObject("overlay_panel");

                if (changePasswordOverlayPanelJson.has("background")) {
                    String profileEditPopupOverlayPanelBackground = changePasswordOverlayPanelJson.getString("background");
                    changePasswordOverlayPanel.setBackground(profileEditPopupOverlayPanelBackground);
                }

                if (changePasswordOverlayPanelJson.has("opacity")) {
                    String opacity = changePasswordOverlayPanelJson.getString("opacity");
                    changePasswordOverlayPanel.setOpacity(opacity);
                }

                if (changePasswordOverlayPanelJson.has("background_HTML")) {
                    String backgroundHTML = changePasswordOverlayPanelJson.getString("background_HTML");
                    changePasswordOverlayPanel.setBackgroundHTML(backgroundHTML);
                }
                changePassword.setOverlayPanel(changePasswordOverlayPanel);

                ProfileMailid changePasswordProfileMailid = new ProfileMailid();
                JSONObject changePasswordProfileMailidJson = changePasswordJson.getJSONObject("profile_mailid");

                if (changePasswordProfileMailidJson.has("text_color")) {
                    String textColor = changePasswordProfileMailidJson.getString("text_color");
                    changePasswordProfileMailid.setTextColor(textColor);
                }

                if (changePasswordProfileMailidJson.has("opacity")) {
                    String opacity = changePasswordProfileMailidJson.getString("opacity");
                    changePasswordProfileMailid.setOpacity(opacity);
                }

                if (changePasswordProfileMailidJson.has("text_color_HTML")) {
                    String textColorHTML = changePasswordProfileMailidJson.getString("text_color_HTML");
                    changePasswordProfileMailid.setTextColorHTML(textColorHTML);
                }
                changePassword.setProfileMailid(changePasswordProfileMailid);

                ActionButton changePasswordActionButton = new ActionButton();
                JSONObject changePasswordActionButtonJson = changePasswordJson.getJSONObject("Action_button");

                if (changePasswordActionButtonJson.has("background")) {
                    String actionButtonbackground = changePasswordActionButtonJson.getString("background");
                    changePasswordActionButton.setBackground(actionButtonbackground);
                }

                if (changePasswordActionButtonJson.has("text_color")) {
                    String textColor = changePasswordActionButtonJson.getString("text_color");
                    changePasswordActionButton.setTextColor(textColor);
                }
                changePassword.setActionButton(changePasswordActionButton);

                if (changePasswordJson.has("background")) {
                    String changePasswordBackground = changePasswordJson.getString("background");
                    changePassword.setBackground(changePasswordBackground);
                }

                if (changePasswordJson.has("profile_border")) {
                    String profileBorder = changePasswordJson.getString("profile_border");
                    changePassword.setProfileBorder(profileBorder);
                }

                if (changePasswordJson.has("profile_name")) {
                    String profileName = changePasswordJson.getString("profile_name");
                    changePassword.setProfileName(profileName);
                }

                if (changePasswordJson.has("title_text_color")) {
                    String titleTexColor = changePasswordJson.getString("title_text_color");
                    changePassword.setTitleTexColor(titleTexColor);
                }

                if (changePasswordJson.has("hint_text_color")) {
                    String hintTextColor = changePasswordJson.getString("hint_text_color");
                    changePassword.setHintTextColor(hintTextColor);
                }

                if (changePasswordJson.has("input_text_color")) {
                    String inputTextColor = changePasswordJson.getString("input_text_color");
                    changePassword.setInputTextColor(inputTextColor);
                }

                if (changePasswordJson.has("line_color")) {
                    String lineColor = changePasswordJson.getString("line_color");
                    changePassword.setLineColor(lineColor);
                }

                if (changePasswordJson.has("line_color_selected")) {
                    String lineColorSelected = changePasswordJson.getString("line_color_selected");
                    changePassword.setLineColorSelected(lineColorSelected);
                }

                if (changePasswordJson.has("hint_text_color_selected")) {
                    String hintTextColorSelected = changePasswordJson.getString("hint_text_color_selected");
                    changePassword.setHintTextColorSelected(hintTextColorSelected);
                }

                if (changePasswordJson.has("icons_color")) {
                    String iconsColor = changePasswordJson.getString("icons_color");
                    changePassword.setIconsColor(iconsColor);
                }


                if (changePasswordJson.has("button_text_color")) {
                    String buttonTextColor = changePasswordJson.getString("button_text_color");
                    changePassword.setButtonTextColor(buttonTextColor);
                }
                profile.setChangePassword(changePassword);
                themeParent.setProfile(profile);

                Analytics analytics = new Analytics();
                JSONObject analyticsJson = response.getJSONObject("Analytics");

                AnalyticsTopPanel analyticsTopPanel = new AnalyticsTopPanel();
                JSONObject analyticsTopPanelJson = analyticsJson.getJSONObject("toppanel");

                AnalyticsSidePanel analyticsSidePanel = new AnalyticsSidePanel();
                JSONObject analyticsSidePanelJson = analyticsJson.getJSONObject("sidepanel");

                if (analyticsTopPanelJson.has("background")) {
                    String analyticsTopPanelBackground = analyticsTopPanelJson.getString("background");
                    analyticsTopPanel.setBackground(analyticsTopPanelBackground);
                }

                if (analyticsTopPanelJson.has("icons_color")) {
                    String iconsColor = analyticsTopPanelJson.getString("icons_color");
                    analyticsTopPanel.setIconsColor(iconsColor);
                }

                if (analyticsTopPanelJson.has("Analytics_text_color")) {
                    String analyticsTextColor = analyticsTopPanelJson.getString("Analytics_text_color");
                    analyticsTopPanel.setAnalyticsTextColor(analyticsTextColor);
                }
                analytics.setAnalyticsTopPanel(analyticsTopPanel);


                if (analyticsSidePanelJson.has("background")) {
                    String analyticsSidePanelBackground = analyticsSidePanelJson.getString("background");
                    analyticsSidePanel.setBackground(analyticsSidePanelBackground);
                }

                if (analyticsSidePanelJson.has("title_text_color")) {
                    String titleTextColor = analyticsSidePanelJson.getString("title_text_color");
                    analyticsSidePanel.setTitleTextColor(titleTextColor);
                }
                if (analyticsSidePanelJson.has("type_text_color")) {
                    String typeTextColor = analyticsSidePanelJson.getString("type_text_color");
                    analyticsSidePanel.setTypeTextColor(typeTextColor);
                }

                if (analyticsSidePanelJson.has("metadata_text_color")) {
                    String metadataTextColor = analyticsSidePanelJson.getString("metadata_text_color");
                    analyticsSidePanel.setMetadataTextColor(metadataTextColor);
                }

                if (analyticsSidePanelJson.has("class_text_color")) {
                    String classTextColor = analyticsSidePanelJson.getString("class_text_color");
                    analyticsSidePanel.setClassTextColor(classTextColor);
                }

                if (analyticsSidePanelJson.has("arrow_color")) {
                    String arrowColor = analyticsSidePanelJson.getString("arrow_color");
                    analyticsSidePanel.setArrowColor(arrowColor);
                }
                analytics.setAnalyticsSidePanel(analyticsSidePanel);

                if (analyticsJson.has("background")) {
                    String analyticsBackground = analyticsJson.getString("background");
                    analytics.setBackground(analyticsBackground);
                }

                if (analyticsJson.has("card")) {
                    String analyticsCard = analyticsJson.getString("card");
                    analytics.setCard(analyticsCard);
                }

                if (analyticsJson.has("icons_color")) {
                    String iconsColor = analyticsJson.getString("icons_color");
                    analytics.setIconsColor(iconsColor);
                }

                if (analyticsJson.has("more_icon_color")) {
                    String moreIconColor = analyticsJson.getString("more_icon_color");
                    analytics.setMoreIconColor(moreIconColor);
                }

                if (analyticsJson.has("title_text_color")) {
                    String titleTextColor = analyticsJson.getString("title_text_color");
                    analytics.setTitleTextColor(titleTextColor);
                }

                if (analyticsJson.has("disabled_border_color")) {
                    String disabledBorderColor = analyticsJson.getString("disabled_border_color");
                    analytics.setDisabledBorderColor(disabledBorderColor);
                }

                if (analyticsJson.has("description_text_color")) {
                    String descriptionTextColor = analyticsJson.getString("description_text_color");
                    analytics.setDescriptionTextColor(descriptionTextColor);
                }

                if (analyticsJson.has("BookOpened_Assigned")) {
                    String bookOpenedAssigned = analyticsJson.getString("BookOpened_Assigned");
                    analytics.setBookOpenedAssigned(bookOpenedAssigned);
                }

                if (analyticsJson.has("AverageReading_Time")) {
                    String averageReadingTime = analyticsJson.getString("AverageReading_Time");
                    analytics.setAverageReadingTime(averageReadingTime);
                }

                if (analyticsJson.has("AveragePages_Read")) {
                    String averagePagesRead = analyticsJson.getString("AveragePages_Read");
                    analytics.setAveragePagesRead(averagePagesRead);
                }

                if (analyticsJson.has("AverageReading_Session")) {
                    String averageReadingSessions = analyticsJson.getString("AverageReading_Session");
                    analytics.setAverageReadingSessions(averageReadingSessions);
                }

                if (analyticsJson.has("AverageReading_Time_Session")) {
                    String averageReadingTimeSession = analyticsJson.getString("AverageReading_Time_Session");
                    analytics.setAverageReadingTimeSession(averageReadingTimeSession);
                }

                if (analyticsJson.has("AveragePages_Read_Session")) {
                    String averagePagesReadSession = analyticsJson.getString("AveragePages_Read_Session");
                    analytics.setAveragePagesReadSession(averagePagesReadSession);
                }

                if (analyticsJson.has("AverageNotes_Shared_Created")) {
                    String averageNotesSharedCreated = analyticsJson.getString("AverageNotes_Shared_Created");
                    analytics.setAverageNotesSharedCreated(averageNotesSharedCreated);
                }

                if (analyticsJson.has("AverageHighlights_Shared_Created")) {
                    String averageHighlightsSharedCreated = analyticsJson.getString("AverageHighlights_Shared_Created");
                    analytics.setAverageHighlightsSharedCreated(averageHighlightsSharedCreated);
                }

                if (analyticsJson.has("AverageResources_Viewed_Available")) {
                    String averageResourcesViewedAvailable = analyticsJson.getString("AverageResources_Viewed_Available");
                    analytics.setAverageResourcesViewedAvailable(averageResourcesViewedAvailable);
                }
                themeParent.setAnalytics(analytics);

                Login login = new Login();
                JSONObject loginJson = response.getJSONObject("Login");

                LoginScreen loginScreen = new LoginScreen();
                JSONObject loginScreenJson = loginJson.getJSONObject("login_screen");

                InputPanel loginScreenInputPanel = new InputPanel();
                JSONObject loginScreenInputPanelJson = loginScreenJson.getJSONObject("input_panel");

                Bottom bottom = new Bottom();
                JSONObject bottomJson = loginScreenJson.getJSONObject("bottom");


                Panel panel = new Panel();
                JSONObject panelJson = bottomJson.getJSONObject("panel");

                if (panelJson.has("background")) {
                    String panelBackground = panelJson.getString("background");
                    panel.setBackground(panelBackground);
                }

                if (panelJson.has("opacity")) {
                    String opacity = panelJson.getString("opacity");
                    panel.setOpacity(opacity);
                }

                if (panelJson.has("background_HTML")) {
                    String backgroundHTML = panelJson.getString("background_HTML");
                    panel.setBackgroundHTML(backgroundHTML);
                }

                if (panelJson.has("text_color")) {
                    String textColor = panelJson.getString("text_color");
                    panel.setTextColor(textColor);
                }
                bottom.setPanel(panel);
                loginScreen.setBottom(bottom);

                if (loginScreenInputPanelJson.has("background")) {
                    String inputPanelBackground = loginScreenInputPanelJson.getString("background");
                    loginScreenInputPanel.setBackground(inputPanelBackground);
                }

                if (loginScreenInputPanelJson.has("opacity")) {
                    String opacity = loginScreenInputPanelJson.getString("opacity");
                    loginScreenInputPanel.setOpacity(opacity);
                }

                if (loginScreenInputPanelJson.has("background_HTML")) {
                    String backgroundHTML = loginScreenInputPanelJson.getString("background_HTML");
                    loginScreenInputPanel.setBackgroundHTML(backgroundHTML);
                }
                loginScreen.setInputPanel(loginScreenInputPanel);

                if (loginScreenJson.has("background_HTML")) {
                    String backgroundHtml = loginScreenJson.getString("background_HTML");
                    loginScreen.setBackgroundHTML(backgroundHtml);
                }

                if (loginScreenJson.has("Title_selected")) {
                    String titleSelected = loginScreenJson.getString("Title_selected");
                    loginScreen.setTitleSelected(titleSelected);
                }

                if (loginScreenJson.has("Title_deselected")) {
                    String titleDeselected = loginScreenJson.getString("Title_deselected");
                    loginScreen.setTitleDeselected(titleDeselected);
                }

                if (loginScreenJson.has("tab_selected")) {
                    String tabSelected = loginScreenJson.getString("tab_selected");
                    loginScreen.setTabSelected(tabSelected);
                }

                if (loginScreenJson.has("line_color")) {
                    String lineColor = loginScreenJson.getString("line_color");
                    loginScreen.setLineColor(lineColor);
                }

                if (loginScreenJson.has("hint_text_color")) {
                    String hintTextColor = loginScreenJson.getString("hint_text_color");
                    loginScreen.setHintTextColor(hintTextColor);
                }

                if (loginScreenJson.has("input_panel_selected")) {
                    String inputPanelSelected = loginScreenJson.getString("input_panel_selected");
                    loginScreen.setInputPanelSelected(inputPanelSelected);
                }

                if (loginScreenJson.has("input_text_color_selected")) {
                    String inputTextColorSelected = loginScreenJson.getString("input_text_color_selected");
                    loginScreen.setInputTextColorSelected(inputTextColorSelected);
                }

                if (loginScreenJson.has("icons_color")) {
                    String iconsColor = loginScreenJson.getString("icons_color");
                    loginScreen.setIconsColor(iconsColor);
                }

                if (loginScreenJson.has("button_color")) {
                    String buttonColor = loginScreenJson.getString("button_color");
                    loginScreen.setButtonColor(buttonColor);
                }

                if (loginScreenJson.has("button_text_color")) {
                    String buttonTextColor = loginScreenJson.getString("button_text_color");
                    loginScreen.setButtonTextColor(buttonTextColor);
                }

                if (loginScreenJson.has("description_text_color")) {
                    String descriptionTextColor = loginScreenJson.getString("description_text_color");
                    loginScreen.setDescriptionTextColor(descriptionTextColor);
                }

                if (loginScreenJson.has("link_text_color")) {
                    String linkTextColor = loginScreenJson.getString("link_text_color");
                    loginScreen.setLinkTextColor(linkTextColor);
                }

                if (loginScreenJson.has("forgotpassword_text_color")) {
                    String forgotpasswordTextColor = loginScreenJson.getString("forgotpassword_text_color");
                    loginScreen.setForgotpasswordTextColor(forgotpasswordTextColor);
                }

                if (loginScreenJson.has("error_text_color")) {
                    String errorTextColor = loginScreenJson.getString("error_text_color");
                    loginScreen.setErrorTextColor(errorTextColor);
                }
                login.setLoginScreen(loginScreen);

                CookieAlert cookieAlert = new CookieAlert();
                JSONObject cookieAlertJson = loginJson.getJSONObject("Cookie_Alert");

                ActionButton cookieAlertActionButton = new ActionButton();
                JSONObject cookieAlertActionButtonJson = cookieAlertJson.getJSONObject("action_button");

                if (cookieAlertActionButtonJson.has("background")) {
                    String actionButtonBackground = cookieAlertActionButtonJson.getString("background");
                    cookieAlertActionButton.setBackground(actionButtonBackground);
                }

                if (cookieAlertActionButtonJson.has("text_color")) {
                    String actionButtonTextColor = cookieAlertActionButtonJson.getString("text_color");
                    cookieAlertActionButton.setTextColor(actionButtonTextColor);
                }
                cookieAlert.setActionButton(cookieAlertActionButton);

                if (cookieAlertJson.has("background")) {
                    String cookieAlertBackground = cookieAlertJson.getString("background");
                    cookieAlert.setBackground(cookieAlertBackground);
                }

                if (cookieAlertJson.has("text_color")) {
                    String cookieAlertTextColor = cookieAlertJson.getString("text_color");
                    cookieAlert.setTextColor(cookieAlertTextColor);
                }

                if (cookieAlertJson.has("link_text_color")) {
                    String linkTextColor = cookieAlertJson.getString("link_text_color");
                    cookieAlert.setLinkTextColor(linkTextColor);
                }
                login.setCookieAlert(cookieAlert);
                themeParent.setLogin(login);

                ForgotPassword forgotPassword = new ForgotPassword();
                JSONObject forgotPasswordJson = loginJson.getJSONObject("ForgotPassword");

                InputPanel forgotPasswordInputPanel = new InputPanel();
                JSONObject forgotPasswordInputPanelJson = forgotPasswordJson.getJSONObject("input_panel");

                if (forgotPasswordInputPanelJson.has("background")) {
                    String inputPanelBackground = forgotPasswordInputPanelJson.getString("background");
                    forgotPasswordInputPanel.setBackground(inputPanelBackground);
                }

                if (forgotPasswordInputPanelJson.has("opacity")) {
                    String opacity = forgotPasswordInputPanelJson.getString("opacity");
                    forgotPasswordInputPanel.setOpacity(opacity);
                }

                if (forgotPasswordInputPanelJson.has("background_HTML")) {
                    String backgroundHTML = forgotPasswordInputPanelJson.getString("background_HTML");
                    forgotPasswordInputPanel.setBackgroundHTML(backgroundHTML);
                }
                forgotPassword.setInputPanel(forgotPasswordInputPanel);

                if (forgotPasswordJson.has("title_text_color")) {
                    String titleTextColor = forgotPasswordJson.getString("title_text_color");
                    forgotPassword.setTitleTextColor(titleTextColor);
                }

                if (forgotPasswordJson.has("line_color")) {
                    String lineColor = forgotPasswordJson.getString("line_color");
                    forgotPassword.setLineColor(lineColor);
                }

                if (forgotPasswordJson.has("description_text_color")) {
                    String descriptionTextColor = forgotPasswordJson.getString("description_text_color");
                    forgotPassword.setDescriptionTextColor(descriptionTextColor);
                }

                if (forgotPasswordJson.has("hint_text_color")) {
                    String hintTextColor = forgotPasswordJson.getString("hint_text_color");
                    forgotPassword.setHintTextColor(hintTextColor);
                }

                if (forgotPasswordJson.has("input_panel_selected")) {
                    String inputPanelSelected = forgotPasswordJson.getString("input_panel_selected");
                    forgotPassword.setInputPanelSelected(inputPanelSelected);
                }

                if (forgotPasswordJson.has("input_text_color_selected")) {
                    String inputTextColorSelected = forgotPasswordJson.getString("input_text_color_selected");
                    forgotPassword.setInputTextColorSelected(inputTextColorSelected);
                }

                if (forgotPasswordJson.has("error_text_color")) {
                    String errorTextColor = forgotPasswordJson.getString("error_text_color");
                    forgotPassword.setErrorTextColor(errorTextColor);
                }

                if (forgotPasswordJson.has("button_color")) {
                    String buttonColor = forgotPasswordJson.getString("button_color");
                    forgotPassword.setButtonColor(buttonColor);
                }

                if (forgotPasswordJson.has("button_text_color")) {
                    String buttonTextColor = forgotPasswordJson.getString("button_text_color");
                    forgotPassword.setButtonTextColor(buttonTextColor);
                }

                if (forgotPasswordJson.has("cancel_button_color")) {
                    String cancelButtonColor = forgotPasswordJson.getString("cancel_button_color");
                    forgotPassword.setCancelButtonColor(cancelButtonColor);
                }

                if (forgotPasswordJson.has("cancel_button_text_color")) {
                    String cancelButtonTextColor = forgotPasswordJson.getString("cancel_button_text_color");
                    forgotPassword.setCancelButtonTextColor(cancelButtonTextColor);
                }
                login.setForgotPassword(forgotPassword);
                themeParent.setLogin(login);

                Help help = new Help();
                JSONObject helpJson = response.getJSONObject("Help");

                OverlayPanel helpOverlayPanel = new OverlayPanel();
                JSONObject helpOverlayPanelJson = helpJson.getJSONObject("overlay_panel");

                if (helpOverlayPanelJson.has("background")) {
                    String overlayPanelBackground = helpOverlayPanelJson.getString("background");
                    helpOverlayPanel.setBackground(overlayPanelBackground);
                }

                if (helpOverlayPanelJson.has("opacity")) {
                    String overlayPanelOpacity = helpOverlayPanelJson.getString("opacity");
                    helpOverlayPanel.setOpacity(overlayPanelOpacity);
                }

                if (helpOverlayPanelJson.has("background_HTML")) {
                    String overlayPanelBackgroundHTML = helpOverlayPanelJson.getString("background_HTML");
                    helpOverlayPanel.setBackgroundHTML(overlayPanelBackgroundHTML);
                }
                help.setOverlayPanel(helpOverlayPanel);

                if (helpJson.has("text_color")) {
                    String helpTextColor = helpJson.getString("text_color");
                    help.setTextColor(helpTextColor);
                }

                if (helpJson.has("description_text_color")) {
                    String helpDescriptionTextColor = helpJson.getString("description_text_color");
                    help.setDescriptionTextColor(helpDescriptionTextColor);
                }

                if (helpJson.has("line_color")) {
                    String helpLineColor = helpJson.getString("line_color");
                    help.setLineColor(helpLineColor);
                }

                if (helpJson.has("pointer_color")) {
                    String pointerColor = helpJson.getString("pointer_color");
                    help.setPointerColor(pointerColor);
                }
                themeParent.setHelp(help);


                AboutUs aboutUs = new AboutUs();
                JSONObject aboutUsJson = response.getJSONObject("AboutUs");

                GradientColor gradientColor = new GradientColor();
                JSONObject gradientColorJson = aboutUsJson.getJSONObject("gradient_color");

                if (gradientColorJson.has("color")) {
                    String color = gradientColorJson.getString("color");
                    gradientColor.setColor(color);
                }

                if (gradientColorJson.has("opacity")) {
                    String opacity = gradientColorJson.getString("opacity");
                    gradientColor.setOpacity(Collections.singletonList(opacity));
                }
                aboutUs.setGradientColor(gradientColor);

                Svg svg = new Svg();
                JSONObject svgJson = aboutUsJson.getJSONObject("svg");

                if (svgJson.has("color")) {
                    String color = svgJson.getString("color");
                    svg.setColor(color);
                }

                if (svgJson.has("opacity")) {
                    String opacity = svgJson.getString("opacity");
                    svg.setOpacity(opacity);
                }

                if (svgJson.has("background_HTML")) {
                    String backgroundHTML = svgJson.getString("background_HTML");
                    svg.setBackgroundHTML(backgroundHTML);
                }
                aboutUs.setSvg(svg);

                if (aboutUsJson.has("background")) {
                    String aboutUsBackground = aboutUsJson.getString("background");
                    aboutUs.setBackground(aboutUsBackground);
                }

                if (aboutUsJson.has("background-image")) {
                    String backgroundImage = aboutUsJson.getString("background-image");
                    aboutUs.setBackgroundImage(backgroundImage);
                }

                if (aboutUsJson.has("close_icon_color")) {
                    String closeIconColor = aboutUsJson.getString("close_icon_color");
                    aboutUs.setCloseIconColor(closeIconColor);
                }

                if (aboutUsJson.has("text_color")) {
                    String textColor = aboutUsJson.getString("text_color");
                    aboutUs.setTextColor(textColor);
                }

                if (aboutUsJson.has("description_text_color")) {
                    String descriptionTextColor = aboutUsJson.getString("description_text_color");
                    aboutUs.setDescriptionTextColor(descriptionTextColor);
                }

                if (aboutUsJson.has("icons_color")) {
                    String iconsColor = aboutUsJson.getString("icons_color");
                    aboutUs.setIconsColor(iconsColor);
                }
                themeParent.setAboutUs(aboutUs);

                /*------------------------Insight Theme Parser--------------------------*/
                InsightSigninSignUp insightSigninSignUp = new InsightSigninSignUp();
                JSONObject insightSigninSignUpJson = response.getJSONObject("Insight_Signin_SignUp");

                if(insightSigninSignUpJson.has("background"))
                {
                    String strBackground = insightSigninSignUpJson.getString("background");
                    insightSigninSignUp.setBackground(strBackground);
                }

                if(insightSigninSignUpJson.has("tab_text_color"))
                {
                    String tabTextColor = insightSigninSignUpJson.getString("tab_text_color");
                    insightSigninSignUp.setTabTextColor(tabTextColor);
                }

                if(insightSigninSignUpJson.has("tab_selected_color"))
                {
                    String tabSelectedColor = insightSigninSignUpJson.getString("tab_selected_color");
                    insightSigninSignUp.setTabSelectedColor(tabSelectedColor);
                }

                if(insightSigninSignUpJson.has("hint_text_color"))
                {
                    String hintTextColor = insightSigninSignUpJson.getString("hint_text_color");
                    insightSigninSignUp.setHintTextColor(hintTextColor);
                }

                if(insightSigninSignUpJson.has("input_text_color"))
                {
                    String inputTextColor = insightSigninSignUpJson.getString("input_text_color");
                    insightSigninSignUp.setInputTextColor(inputTextColor);
                }

                if(insightSigninSignUpJson.has("icon_color"))
                {
                    String iconColor = insightSigninSignUpJson.getString("icon_color");
                    insightSigninSignUp.setIconColor(iconColor);
                }

                if(insightSigninSignUpJson.has("input_text_color_HTML"))
                {
                    String inputTextColorHtml = insightSigninSignUpJson.getString("input_text_color_HTML");
                    insightSigninSignUp.setInputTextColorHTML(inputTextColorHtml);
                }

                if(insightSigninSignUpJson.has("input_border_bg"))
                {
                    String inputBorderBg = insightSigninSignUpJson.getString("input_border_bg");
                    insightSigninSignUp.setInputBorderBg(inputBorderBg);
                }

                if(insightSigninSignUpJson.has("forgotpassword_link"))
                {
                    String forgotpasswordLink = insightSigninSignUpJson.getString("forgotpassword_link");
                    insightSigninSignUp.setForgotpasswordLink(forgotpasswordLink);
                }

                Header header = new Header();
                if(insightSigninSignUpJson.has("header"))
                {
                    JSONObject headerJson = insightSigninSignUpJson.getJSONObject("header");

                    if(headerJson.has("background"))
                    {
                        String strBackground = headerJson.getString("background");
                        header.setBackground(strBackground);
                    }

                    if(headerJson.has("opacity"))
                    {
                        String opacity = headerJson.getString("opacity");
                        header.setOpacity(opacity);
                    }

                    if(headerJson.has("background_HTML"))
                    {
                        String backgroundHtml = headerJson.getString("background_HTML");
                        header.setBackgroundHTML(backgroundHtml);
                    }
                }
                insightSigninSignUp.setHeader(header);

                Tab tab = new Tab();
                if(insightSigninSignUpJson.has("tab"))
                {
                    JSONObject headerJson = insightSigninSignUpJson.getJSONObject("tab");

                    if(headerJson.has("background"))
                    {
                        String strBackground = headerJson.getString("background");
                        tab.setBackground(strBackground);
                    }

                    if(headerJson.has("opacity"))
                    {
                        String opacity = headerJson.getString("opacity");
                        tab.setOpacity(opacity);
                    }

                    if(headerJson.has("background_HTML"))
                    {
                        String backgroundHtml = headerJson.getString("background_HTML");
                        tab.setBackgroundHTML(backgroundHtml);
                    }
                }
                insightSigninSignUp.setTab(tab);

                InputPanel__ inputPanel__ = new InputPanel__();
                if(insightSigninSignUpJson.has("input_panel"))
                {
                    JSONObject headerJson = insightSigninSignUpJson.getJSONObject("input_panel");

                    if(headerJson.has("background"))
                    {
                        String strBackground = headerJson.getString("background");
                        inputPanel__.setBackground(strBackground);
                    }

                    if(headerJson.has("opacity"))
                    {
                        String opacity = headerJson.getString("opacity");
                        inputPanel__.setOpacity(opacity);
                    }

                    if(headerJson.has("background_HTML"))
                    {
                        String backgroundHtml = headerJson.getString("background_HTML");
                        inputPanel__.setBackgroundHTML(backgroundHtml);
                    }
                }
                insightSigninSignUp.setInputPanel(inputPanel__);

                ActionButton___ actionButton___ = new ActionButton___();
                if(insightSigninSignUpJson.has("action_button"))
                {
                    JSONObject actionButtonJson = insightSigninSignUpJson.getJSONObject("action_button");

                    if(actionButtonJson.has("background"))
                    {
                        String strBackground = actionButtonJson.getString("background");
                        actionButton___.setBackground(strBackground);
                    }

                    if(actionButtonJson.has("text_color"))
                    {
                        String opacity = actionButtonJson.getString("text_color");
                        actionButton___.setTextColor(opacity);
                    }
                }
                insightSigninSignUp.setActionButton(actionButton___);


                BottomPanel_ bottompanel_ = new BottomPanel_();
                if(insightSigninSignUpJson.has("bottom_panel"))
                {
                    JSONObject bottompanelJson = insightSigninSignUpJson.getJSONObject("bottom_panel");

                    if(bottompanelJson.has("background_HTML"))
                    {
                        String strBackground = bottompanelJson.getString("background_HTML");
                        bottompanel_.setBackground_HTML(strBackground);
                    }

                    if(bottompanelJson.has("title_color"))
                    {
                        String opacity = bottompanelJson.getString("title_color");
                        bottompanel_.setTitle_color(opacity);
                    }

                    if(bottompanelJson.has("icon_color"))
                    {
                        String backgroundHtml = bottompanelJson.getString("icon_color");
                        bottompanel_.setIcon_color(backgroundHtml);
                    }
                }
                insightSigninSignUp.setBottomPanel(bottompanel_);
                themeParent.setInsightSigninSignUp(insightSigninSignUp);
                /*end of signin signup parsing*/
                InsightMoreinfo insightMoreinfo = new InsightMoreinfo();
                JSONObject insightMoreinfoJson = response.getJSONObject("Insight_Moreinfo");

                if(insightMoreinfoJson.has("background"))
                {
                    String strBackground = insightMoreinfoJson.getString("background");
                    insightMoreinfo.setBackground(strBackground);
                }

                if(insightMoreinfoJson.has("icon_color"))
                {
                    String iconColor = insightMoreinfoJson.getString("icon_color");
                    insightMoreinfo.setIconColor(iconColor);
                }

                if(insightMoreinfoJson.has("topic_color"))
                {
                    String topicColor = insightMoreinfoJson.getString("topic_color");
                    insightMoreinfo.setTopicColor(topicColor);
                }

                if(insightMoreinfoJson.has("title_color"))
                {
                    String titleColor = insightMoreinfoJson.getString("title_color");
                    insightMoreinfo.setTitleColor(titleColor);
                }

                if(insightMoreinfoJson.has("description_color"))
                {
                    String descriptionColor = insightMoreinfoJson.getString("description_color");
                    insightMoreinfo.setDescriptionColor(descriptionColor);
                }

                DownloadButton downloadButton = new DownloadButton();
                if(insightMoreinfoJson.has("download_button"))
                {
                    JSONObject downloadButtonJson = insightMoreinfoJson.getJSONObject("download_button");

                    if(downloadButtonJson.has("background"))
                    {
                        String strBackground = downloadButtonJson.getString("background");
                        downloadButton.setBackground(strBackground);
                    }

                    if(downloadButtonJson.has("icon_color"))
                    {
                        String iconColor = downloadButtonJson.getString("icon_color");
                        downloadButton.setIconColor(iconColor);
                    }

                    if(downloadButtonJson.has("text_color"))
                    {
                        String textColor = downloadButtonJson.getString("text_color");
                        downloadButton.setTextColor(textColor);
                    }
                }
                insightMoreinfo.setDownloadButton(downloadButton);

                LaunchButton launchButton = new LaunchButton();
                if(insightMoreinfoJson.has("Launch_button"))
                {
                    JSONObject launchButtonJson = insightMoreinfoJson.getJSONObject("Launch_button");

                    if(launchButtonJson.has("background"))
                    {
                        String strBackground = launchButtonJson.getString("background");
                        launchButton.setBackground(strBackground);
                    }

                    if(launchButtonJson.has("icon_color"))
                    {
                        String iconColor = launchButtonJson.getString("icon_color");
                        launchButton.setIconColor(iconColor);
                    }

                    if(launchButtonJson.has("text_color"))
                    {
                        String textColor = launchButtonJson.getString("text_color");
                        launchButton.setTextColor(textColor);
                    }
                }
                insightMoreinfo.setLaunchButton(launchButton);

                DeleteButton deleteButton = new DeleteButton();
                if(insightMoreinfoJson.has("Delete_button"))
                {
                    JSONObject deleteButtonButtonJson = insightMoreinfoJson.getJSONObject("Delete_button");

                    if(deleteButtonButtonJson.has("background"))
                    {
                        String strBackground = deleteButtonButtonJson.getString("background");
                        deleteButton.setBackground(strBackground);
                    }

                    if(deleteButtonButtonJson.has("icon_color"))
                    {
                        String iconColor = deleteButtonButtonJson.getString("icon_color");
                        deleteButton.setIconColor(iconColor);
                    }

                    if(deleteButtonButtonJson.has("text_color"))
                    {
                        String textColor = deleteButtonButtonJson.getString("text_color");
                        deleteButton.setTextColor(textColor);
                    }
                }
                insightMoreinfo.setDeleteButton(deleteButton);
                themeParent.setInsightMoreinfo(insightMoreinfo);
                /*end of moreinfo parsing*/

                InsightForgotPassword insightForgotPassword = new InsightForgotPassword();
                JSONObject insightForgotPasswordJson = response.getJSONObject("Insight_Forgot_Password");

                if(insightForgotPasswordJson.has("background"))
                {
                    String strBackground = insightForgotPasswordJson.getString("background");
                    insightForgotPassword.setBackground(strBackground);
                }

                if(insightForgotPasswordJson.has("divider"))
                {
                    String strBackground = insightForgotPasswordJson.getString("divider");
                    insightForgotPassword.setDivider(strBackground);
                }

                if(insightForgotPasswordJson.has("title_color"))
                {
                    String titleColor = insightForgotPasswordJson.getString("title_color");
                    insightForgotPassword.setTitleColor(titleColor);
                }

                if(insightForgotPasswordJson.has("description_color"))
                {
                    String descriptionColor = insightForgotPasswordJson.getString("description_color");
                    insightForgotPassword.setDescriptionColor(descriptionColor);
                }

                if(insightForgotPasswordJson.has("hint_text_color"))
                {
                    String hintTextColor = insightForgotPasswordJson.getString("hint_text_color");
                    insightForgotPassword.setHintTextColor(hintTextColor);
                }

                if(insightForgotPasswordJson.has("input_text_color"))
                {
                    String inputTextColor = insightForgotPasswordJson.getString("input_text_color");
                    insightForgotPassword.setInputTextColor(inputTextColor);
                }

                if(insightForgotPasswordJson.has("input_text_color_HTML"))
                {
                    String inputTextColorHtml = insightForgotPasswordJson.getString("input_text_color_HTML");
                    insightForgotPassword.setInputTextColorHTML(inputTextColorHtml);
                }

                if(insightForgotPasswordJson.has("input_border_bg"))
                {
                    String inputBorderBg = insightForgotPasswordJson.getString("input_border_bg");
                    insightForgotPassword.setInputBorderBg(inputBorderBg);
                }

                OverlayPanel______ overlayPanel = new OverlayPanel______();
                if(insightForgotPasswordJson.has("overlay_panel"))
                {
                    JSONObject overlayPanelJson = insightForgotPasswordJson.getJSONObject("overlay_panel");

                    if(overlayPanelJson.has("background"))
                    {
                        String strBackground = overlayPanelJson.getString("background");
                        overlayPanel.setBackground(strBackground);
                    }

                    if(overlayPanelJson.has("opacity"))
                    {
                        String opacity = overlayPanelJson.getString("opacity");
                        overlayPanel.setOpacity(opacity);
                    }

                    if(overlayPanelJson.has("background_HTML"))
                    {
                        String backgroundHtml = overlayPanelJson.getString("background_HTML");
                        overlayPanel.setBackgroundHTML(backgroundHtml);
                    }
                }
                insightForgotPassword.setOverlayPanel(overlayPanel);

                Header_ header1 = new Header_();
                if(insightForgotPasswordJson.has("header"))
                {
                    JSONObject headerPanelJson = insightForgotPasswordJson.getJSONObject("header");

                    if(headerPanelJson.has("background"))
                    {
                        String strBackground = headerPanelJson.getString("background");
                        header1.setBackground(strBackground);
                    }

                    if(headerPanelJson.has("opacity"))
                    {
                        String opacity = headerPanelJson.getString("opacity");
                        header1.setOpacity(opacity);
                    }
                }
                insightForgotPassword.setHeader(header1);

                InputPanel___ inputPanel1 = new InputPanel___();
                if(insightForgotPasswordJson.has("input_panel"))
                {
                    JSONObject inputanelJson = insightForgotPasswordJson.getJSONObject("input_panel");

                    if(inputanelJson.has("background"))
                    {
                        String strBackground = inputanelJson.getString("background");
                        inputPanel1.setBackground(strBackground);
                    }

                    if(inputanelJson.has("opacity"))
                    {
                        String opacity = inputanelJson.getString("opacity");
                        inputPanel1.setOpacity(opacity);
                    }

                    if(inputanelJson.has("background_HTML"))
                    {
                        String backgroundHtml = inputanelJson.getString("background_HTML");
                        inputPanel1.setBackgroundHTML(backgroundHtml);
                    }
                }
                insightForgotPassword.setInputPanel(inputPanel1);

                ActionButton____ actionButton____ = new ActionButton____();
                if(insightForgotPasswordJson.has("action_button"))
                {
                    JSONObject actionButtonJson = insightForgotPasswordJson.getJSONObject("action_button");

                    if(actionButtonJson.has("background"))
                    {
                        String strBackground = actionButtonJson.getString("background");
                        actionButton____.setBackground(strBackground);
                    }

                    if(actionButtonJson.has("text_color"))
                    {
                        String textColor = actionButtonJson.getString("text_color");
                        actionButton____.setTextColor(textColor);
                    }
                }
                insightForgotPassword.setActionButton(actionButton____);

                CancelButton cancelButton = new CancelButton();
                if(insightForgotPasswordJson.has("cancel_button"))
                {
                    JSONObject cancelButtonJson = insightForgotPasswordJson.getJSONObject("cancel_button");

                    if(cancelButtonJson.has("background"))
                    {
                        String strBackground = cancelButtonJson.getString("background");
                        cancelButton.setBackground(strBackground);
                    }

                    if(cancelButtonJson.has("text_color"))
                    {
                        String opacity = cancelButtonJson.getString("text_color");
                        cancelButton.setTextColor(opacity);
                    }
                }
                insightForgotPassword.setCancelButton(cancelButton);
                themeParent.setInsightForgotPassword(insightForgotPassword);
                /*end of forgotPassword parsing*/

                InsightBookshelf insightBookshelf = new InsightBookshelf();
                JSONObject insightBookshelfJson = response.getJSONObject("Insight_Bookshelf");

                if(insightBookshelfJson.has("booktitleLabelColor"))
                {
                    String strBackground = insightBookshelfJson.getString("booktitleLabelColor");
                    insightBookshelf.setBooktitleLabelColor(strBackground);
                }

                if(insightBookshelfJson.has("Progressview_progresscolor"))
                {
                    String progressviewProgresscolor = insightBookshelfJson.getString("Progressview_progresscolor");
                    insightBookshelf.setProgressviewProgresscolor(progressviewProgresscolor);
                }

                if(insightBookshelfJson.has("Progressview_emptylinecolor"))
                {
                    String progressviewEmptylinecolor = insightBookshelfJson.getString("Progressview_emptylinecolor");
                    insightBookshelf.setProgressviewEmptylinecolor(progressviewEmptylinecolor);
                }

                if(insightBookshelfJson.has("infobutton_title_Color"))
                {
                    String infobuttonTitleColor = insightBookshelfJson.getString("infobutton_title_Color");
                    insightBookshelf.setInfobuttonTitleColor(infobuttonTitleColor);
                }

                if(insightBookshelfJson.has("downloadbutton_titlecolor"))
                {
                    String downloadbuttonTitlecolor = insightBookshelfJson.getString("downloadbutton_titlecolor");
                    insightBookshelf.setDownloadbuttonTitlecolor(downloadbuttonTitlecolor);
                }

                Downloadview downloadview = new Downloadview();
                if(insightBookshelfJson.has("Downloadview"))
                {
                    JSONObject downloadViewJson = insightBookshelfJson.getJSONObject("Downloadview");

                    if(downloadViewJson.has("background"))
                    {
                        String strBackground = downloadViewJson.getString("background");
                        downloadview.setBackground(strBackground);
                    }

                    if(downloadViewJson.has("opacity"))
                    {
                        String opacity = downloadViewJson.getString("opacity");
                        downloadview.setOpacity(opacity);
                    }
                }
                insightBookshelf.setDownloadview(downloadview);
                themeParent.setInsightBookshelf(insightBookshelf);
                /*-----------------------------------------------------------------------------*/

                String h = "abc";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return themeParent;
    }
}
