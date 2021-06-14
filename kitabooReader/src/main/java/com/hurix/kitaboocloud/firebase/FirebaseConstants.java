package com.hurix.kitaboocloud.firebase;

public class FirebaseConstants {


    // Sign in
    public static final String FIREBASE_SIGN_IN_EVENT_KEY = "sign_in_live";
    public static final String FIREBASE_SIGN_IN_NAME_KEY = "name";

    // access code
    public static  final String ACCESS_CODE_KEY="access_code_redemption_live";
    public static  final String ACCESS_CODE="access_code";





    // Forgot password
    public static  final String FORGOT_PASSWORD_CLICK="forgot_password_live";
    public static  final String FORGOT_PASSWORD="forgot_password";

    // show password
    public static  final String SHOW_PASSWORD_CLICK="show_Password_live";
    public static  final String SHOW_PASSWORD="show_Password";



    // remember user
    public static final String FIREBASE_CHECK_BOX_CLICKED_ACTION_KEY = "remember_user_live";


    // language
    public static final String LANGUAGE_CHANGE_EVENT = "change_language_live";
    public static final String LANGUAGE_CHANGE_EVENT_KEY = "language";

    // Book Click
    public static final String BOOK_CLICK = "book_click_live";
    public static final String BOOK_TITLE_KEY = "book_title";

    // BookShelf Tab
    public static  final String TAB_CLICK="tab_click_live";
    public  static  final String TAB_TITLE="title";

    //Book Download
    public static  final String BOOK_DOWNLOAD_CLICK="book_download_live";
    public static  final String BOOK_DOWNLOAD="NA";
    public static  final String BOOK_TYPE="book_type";
    public  static  final String BOOK_TITLE="book_title";

    // Resume Download
    public static  final String RESUME_DOWNLOAD_CLICK="download_resume_live";
    public static  final String RESUME_BOOK_DOWNLOAD="NA";


    //More info
    public static  final String MORE_INFO_CLICK="more_info_live";
    public static  final String MORE_INFO="more_info";

    //Favourite
    public static  final String FAVOURITE_CLICK="mark_as_favourite_live";
   // public static  final String FAVOURITE="NA";

    //Bookshelf search
    public static  final String BOOKSHELF_SEARCH_CLICK="bookshelf_search_performed_live";
    public static  final String BOOKSHELF_SEARCH="searchedText";

    //Bookshelf access code
    public static  final String BOOKSHELF_ACCESS_CODE_CLICK="bookshelf_access_code_live";
    public static  final String BOOKSHELF_ACCESS_CODE="bookshelfAccessCode";

    //Analytics
    public static  final String BOOKSHELF_ANALYTICS_VIEW_CLICK="analytics_view_live";
    public static  final String BOOKSHELF_ANALYTICS_VIEW_TITLE="booktitle";

    //Profile setting
    public static  final String PROFILE_SETTING_CLICK="profile_view_live";
    public static  final String PROFILE_SETTING="profile";

    // Edit Profile
    public static  final String PROFILE_SETTING_EDIT_CLICK="edit_profile_live";
    public static  final String PROFILE_SETTING_EDIT="NA";

    //Change Password
    public static  final String CHANGE_PASSWORD_CLICK="change_password_live";
    public static  final String CHANGE_PASSWORD="NA";

    //About Us
    public static  final String ABOUT_US_CLICK="about_us_live";
    public static  final String ABOUT_US="NA";

    //Privacy Policy
    public static  final String PRIVACY_POLICY_CLICK="privacy_policy_live";
    public static  final String PRIVACY_POLICY="NA";

    //Terms & Conditions
    public static  final String TERMS_CONDITIONS_CLICK="terms_and_conditions_live";

    //Sign Out
    public static  final String SIGN_OUT_CLICK="sign_out_live";
    public static  final String SIGN_OUT="NA";



    //Bookshelf language selection... used above

    //Home button use
    public static  final String HOME_BUTTON_CLICK="home_button_use_live";
    public static  final String HOME_BUTTON="NA";

    //TOC button use
    public static  final String TOC_BUTTON_CLICK="tOC_icon_use_live";
    public static  final String TOC_BUTTON_NA="NA";

    //“Search” icon click/ touch/ select
    public static  final String SEARCH_BUTTON_CLICK="search_performed_live";
    public static  final String SEARCH_BUTTON="searchtext";

    //UGC data addition on page
    public static  final String PEN_TOOL_CLICK="pen_tool_data_live";
    public static  final String PEN_TOOL="event";

    //Sticky note
    public static  final String STICKY_NOTE_CLICK="sticky_note_data_live";
    public static  final String STICKY_NOTE="noteColor";

    //Highlight
    public static  final String HIGHLIGHT_COLOR_CLICK="highlight_data_live";
    public static  final String HIGHLIGHT_COLOR="noteColor";


    //Contextual note
    public  static  final  String CONTEXTUAL_NOTE_CLICK="contextual_note_data_live";
    public  static  final  String CONTEXTUAL_NOTE="context_data";

    // Thumbnail
    public  static  final  String THUMBNAIL_DATA_CLICK="thumbnail_data_live";
    public  static  final  String THUMBNAIL_DATA="NA";


    //Bookmark
    public  static  final  String BOOKMARK_DATA_CLICK="bookmark_data_live";
    public  static  final  String BOOKMARK_DATA="NA";

    //Teacher Review
    public  static  final  String TEACHER_REVIEW_CLICK="teacher_review_data_live";
    public  static  final  String TEACHER_REVIEW="NA";

    //Full Screen View
    public  static  final  String FULL_SCREEN_VIEW_CLICK="full_screen_view_live";
    public  static  final  String FULL_SCREEN_VIEW="NA";

    //Page View
    public  static  final  String PAGE_VIEW_CLICK="page_view_data_live";
    public  static  final  String PAGE_VIEW="NA";

    // Zoom
    public  static  final  String ZOOM_ICON_CLICK="content_zoom_data_live";
    public  static  final  String ZOOM_ICON="NA";

    //Font size //Click and drag seek bar
    public  static  final  String FONT_SIZE_SLIDER_CLICK="font_size_data_live";
    public  static  final  String FONT_SIZE="font_size_value";

    //Alignment selection
    public  static  final  String ALIGNMENT_SELECTION_CLICK="alignment_data_live";
    public  static  final  String ALIGNMENT_LEFT="font alignment";
    public  static  final  String ALIGNMENT_RIGHT="Right align";
    public  static  final  String ALIGNMENT_JUSTIFY="Justify align";
    public  static  final  String ALIGNMENT_CENTER="Center align";

    //Font selection
    public  static  final  String FONT_SELECTION_CLICK="font_data_live";
    public  static  final  String FONT_SELECTION="font_value";


    //Line Spacing Selection
    public  static  final  String LINE_SPACING_SELECTION_CLICK="line_spacing_data_live";
    public  static  final  String LINE_SPACING_NARROW="font line spacing";
    public  static  final  String LINE_SPACING_NORMAL="font line spacing";
    public  static  final  String LINE_SPACING_WIDE="font line spacing";
   // public  static  final  String LINE_SPACING_NORMAL="Normal Line Spacing";
    //public  static  final  String LINE_SPACING_WIDE="Wide Line Spacing";

    //Reading mode
    public  static  final  String READING_MODE_CLICK="reading_mode_data_live";
    public  static  final  String READING_MODE_NIGHT="font reading mode";
    public  static  final  String READING_MODE_DAY="Day Mode";
    public  static  final  String READING_MODE_SEPIA="font reading mode";

    //Margin Selection
    public  static  final  String MARGIN_SELECTION_CLICK="margin_data_live";
    public  static  final  String MARGIN_SELECTION_NARROW="Font Margin";
    public  static  final  String MARGIN_SELECTION_NORMAL="Font Margin";
    public  static  final  String MARGIN_SELECTION_WIDE="Font Margin";

    //Scroll view button selection
    public  static  final  String SCROLL_VIEW_SELECTION_CLICK="scroll_view_data_live";
    public  static  final  String SCROLL_VIEW_ON="ON";
    public  static  final  String SCROLL_VIEW_OFF="OFF";

    //Reset to default button selection
    public  static  final  String RESET_SELECTION_CLICK="reset_data_live";
    public  static  final  String RESET_SELECTION="NA";

    //Deep Link Click .. it is in html (online reader)
    public  static  final  String DEEP_LINK_CLICK="Deep_Link_live";
    public  static  final  String DEEP_LINK="NA";

    //Protractor icon click
    public  static  final  String PROTRACTOR_ICON_CLICK="protractor_data_live";
    public  static  final  String PROTRACTOR_ICON="NA";

    //Equation Editor keyboard click
    public  static  final  String EQUATION_EDITOR_CLICK="equation_editor_data_live";
    public  static  final  String EQUATION_EDITOR="NA";

    //Markup button click
    public  static  final  String MARKUP_BUTTON_CLICK="markup_live";
    public  static  final  String MARKUP_TYPE="markup_type";
    public  static  final  String MARKUP_IMAGE="Image";
    public  static  final  String MARKUP_IMAGE_SLIDE="Image Slide show";
    public  static  final  String MARKUP_INSTRUCTION="Instruction markup";
    public  static  final  String MARKUP_AUDIO="Audio markup";
    public  static  final  String MARKUP_WEB_LINK="Web Link";
    public  static  final  String MARKUP_GOTO_PAGE="Goto page";
    public  static  final  String MARKUP_HTML_ACTIVITY="HTML Activity";
    public  static  final  String MARKUP_HTML_ACTIVITY_FLOATABLE="HTML Activity Floatable";
    public  static  final  String MARKUP_HTML_ACTIVITY_QUER_PARAM="HTML Activity Quer Parameter";
    public  static  final  String MARKUP_IMAGE_MAGNIFICATION="Image Magnification";
    public  static  final  String MARKUP_NORMAL_VIDEO="Normal Video";
    public  static  final  String MARKUP_YOUTUBE_VIDEO="You tube video";
    public  static  final  String MARKUP_INLINE_VIDEO="Inline video";
    public  static  final  String MARKUP_KALTURA_VIDEO="Kaltura Video";
    public  static  final  String MARKUP_DOC_PDF="Document PDF";
    public  static  final  String MARKUP_DOC_EXCEL="Document Excel";
    public  static  final  String MARKUP_DOC_PPT=" Document PPT";
    public  static  final  String MARKUP_JUMP_TO_BOOK=" Goto/Jump to Book";
    public  static  final  String MARKUP_AUDIO_SYNC=" Audio Sync";
    public  static  final  String MARKUP_KITABOO_WIDGET="Kitaboo Widget";
    public  static  final  String MARKUP_MULTI_LINK="Multilink Markup";
    public  static  final  String MARKUP_DROPDOWN="Drop Down";
    public  static  final  String MARKUP_FIB="FIB";


    //
    //Audio sync click
    public  static  final  String AUDIO_SYNC_CLICK="audio_sync_data";
    public  static  final  String AUDIO_SYNC="NA";

    //Read Aloud
    public  static  final  String READ_ALOUD_CLICK="read_aloud_data_live";
    public  static  final  String READ_ALOUD="NA";


















}
