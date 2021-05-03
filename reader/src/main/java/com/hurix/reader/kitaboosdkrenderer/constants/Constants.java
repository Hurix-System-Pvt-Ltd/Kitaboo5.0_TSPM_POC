package com.hurix.reader.kitaboosdkrenderer.constants;

public class Constants {

    public static final String SLEEP_HOURS = "sleep_hours";
    public static final String SLEEP_MINS = "sleep_mins";
    public static final String SLEEP_SECS = "sleep_secs";

    public static boolean IS_DEBUG_ENABLED = false;
    public static final int HELPSCREEN_CLOSE_REQUESTCODE = 777;

    public static final String HELPSCREEN_REQUIRED_READER = "bookplayerfirsttime";
    public static final String HELPSCREEN_REQUIRED_REVIEW = "reviewfirsttime";
    public static final String DEFAULT_APP_LANGUAGE = "locale";

    public static final String SHELF_PREFS_NAME = "myPrefs";
    public static final String BOOK_SHELF_LAUNCH_FIRST_TIME = "bookshelffirsttime";
    public static final String BOOK_PLAYER_LAUNCH_FIRST_TIME = "bookplayerfirsttime";
    public static final String HELPSCREEN_REQUIRED = "bookplayerfirsttime";

    public static final String PREFS_NAME = "UnzipBooks";

    public static String DATA = "";



    public static String getROOTFOLDER() {
        return ROOTFOLDER;
    }

    // Root folder for all ebook assets
    public static String ROOTFOLDER = ".cloudreaderbooks/";

    //DB
    public static final String XML_ROOT_BIN = "assets/xml-bin/";
    public static String ALLBOOKROOTPATH = DATA + ROOTFOLDER;
    public static final String PROFILE_PICS_FOLDER = "ProfilePics";
    public static final String PROFILE_PICS_BACKUP = "backup";
    public static final String SERVER_THEME_FONT_PICS_FOLDER = "ThemeAndFont";

    public static final String DB_NAME = "xml.sqlite";

    //epub Db version code
    public static final String EBOOK_PATH = "/" + XML_ROOT_BIN + "eBook.xml";
    // Pages thumbnail root path

    public static final int HIGHLIGHT = 1;


    public static final String UGC_ITEM_MODE_NEW = "N";
    //	public static final String UGC_ITEM_MODE_UPDATED = "U";
    public static final String UGC_ITEM_MODE_MODIFIED = "M";
    public static final String UGC_ITEM_MODE_DELETED = "D";
    //	public static final String UGC_ITEM_MODE_UPTODATE = "M"; //

    public static final String PENTOOL_COLOR_DEFAULT = "010002";

    public static final String PENTOOL_ASSESSMENTS_COLOR_RED = "ff0000";
    public static final String PENTOOL_ASSESSMENTS_COLOR_GREEN = "93c50c";

    public static final int PENTOOL_SIZE_MEDIUM = 12;
    public static final int PENTOOL_SIZE_SMALL = 1;


    public static final String USER_ID = "ID";


    public enum SERVICETYPES {
        ACCESSCODEREQUEST,
        BOOKBACKGROUNDREQUEST,
        BOOKLISTREQUEST,
        DOWNLOADREQUEST,
        FETCHANALYTICSREQUEST,
        LOGINREQUEST,
        LOGINFROMWEBVIEW,
        PAGETRACKINGREQUEST,
        REGISTRATIONREQUEST,
        FETCHBOOKCLASSES,
        FETCHTEACHERANNOTATIONREQUEST,
        FETCHSTUDENTANNOTATIONS,
        SAVETEACHERANNOTATIONS,
        SAVETSTUDENTANNOTATIONS,
        HIGHLIGHTSETTING,
        POSTASSESSMENTSPENTOOLREQUEST,
        SAVE_COLLAB_DATA_REQUEST,
        FETCH_COLLAB_DATA_REQUEST,
        ACCEPT_COLLABORATION_DATA_REQUSET,
        FORGOT_PASSWORD_REQUEST,
        CHANGE_PASSEORD_REQUEST,
        RESET_PASSWORD_REQUEST,
        FETCH_COLLAB_SHARE_DATA_REQUEST,
        VALID_USER_TOKEN,
        SAVE_USER_SETTING,
        MOBIL_SESSION_USER_LOGIN,
        GREENLAND_MOBIL_SESSION_USER_LOGIN,
        USER_SETTING_REQUEST,
        SECURE_URL_REQUEST,
        KALTURA_SECURE_URL_REQUEST,
        MARK_DEVICE_CONSUMED_REQUEST,
        MARK_DEVICE_RELEASE_REQUEST,
        MARK_COLLECTION_RELEASE_REQUEST,
        UPLOAD_PROFILE_PIC,
        FETCH_GLOSSARY,
        FORGOT_PASSWORD_REQUEST_URL,
        VIMEO_URL_REQUEST,
        REFRESH_USER_TOKEN,
        ANATA_BOOK_DOWNLOAD_REQUEST,
        ANAYA_BOOK_UPDATE_REQUEST,
        ANAYA_BOOK_UPDATE_STATUS_REQUEST,
        ANAYA_BOOK_DELETE_REQUEST,
        ANAYA_BOOK_DOWNLOAD_FAIL_REQUEST,
        SCORMLISTREQUEST,
        FETCH_BOOKLIST_PER_CATEGORY_REQUREST,
        FETCH_BOOKLIST_PER_SUB_CATEGORY_REQUREST,
        FETCH_BOOKS_FOR_COLLECTION,
        FETCH_CATEGORY_REQUREST,
        FETCH_SUB_CATEGORY_REQUEST,
        RECENTLY_VIEWED_BOOKLIST_REQUEST,
        DOWNLOAD_ALL_BOOKS_REQUEST,
        DELETE_ALL_BOOKS_REQUEST,
        ELASTIC_SEARCH_FETCH_BOOK_REQUEST,
        REFRESH_BOOKLIST,
        SAVESESSION_HISTORY_FORMUTIPLEBOOKS_REQUEST,
        FAVOURITE_BOOK_LIST,
        MARK_AS_FAVOURITE,
        UNMARK_AS_FAVOURITE,
        APP_LANGUAGE_REQUEST,
        HIT_LIKE_SERVICE_REQUEST,
        RECENTLY_ADDED_BOOKS_SERVICE_REQUEST,
        RECENTLY_VIWED_MORE_INFO_BOOKS_SERVICE_REQUEST,
        MARK_AS_VISITED_MORE_INFO,
        UNMARK_AS_VISITED_MORE_INFO,
        FETCH_MULTICATEGORIES_BOOKLIST_REQUEST,
        FETCH_CATEGORIES_BOOKLIST_REQUEST;


    }

    public static final String STUDENT = "LEARNER";
    public static final String TEACHER = "INSTRUCTOR";
    public static final int SHARE_DATA_ACCEPTED = 1;
    public static final int SHARE_DATA_REJECTED = 0;


    public static final int BOOKPLAYER_OPEN_PDF_REQUESTCODE = 1003;
    public static final int BOOKPLAYER_PROCESS_LINKTYPE_REQUESTCODE = 1004;


    //epub related constants
    public static final String TOC = "toc";
    public static final String ID = "id";




    public static final int REQ_START_STANDALONE_PLAYER = 1;
    public static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    public static final int REQ_YOUTUBE_PLAYER = 3;


    public static final int ERROR_CODE_CONNECTIONTIMEOUT = 1001;
    public static final int ERROR_CODE_SOCKETTIMEOUT = 1002;
    public static final int ERROR_CODE_UNKNOWN = 1003;
    public static final int CAMERA_GALLERY_ALL_REQUEST_CODE = 1009;
    public static final String PROFILE_IMAGE_EXTENSION = ".jpg";
    public static final String PROFILE_IMAGE_EXTENSION_OLD = ".png";

    public static final String AUDIO_BOOK_TYPE = "Auto Read Aloud";


    public static final Integer SESSION_EXPIRE_ERRORCODE = 103;


    public static final int REQUEST_PERMISSION_CAMERA = 4;
    public static final int REQUEST_ALERTWINDOW_HTMLWRAP = 6;


    public static final String FLEXIBLE_ICON_URL_HITAREA = "flexibleicon.png";
    public static final String USER_NAME = "user_name";

    //     5.0 reader
    public static final String LASTVISITED_PAGE_FILE_NAME = "lastvisited_page.png";


}

