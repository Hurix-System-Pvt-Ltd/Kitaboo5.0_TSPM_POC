package com.hurix.kitaboo.constants;

import android.Manifest;
import android.os.Environment;

public class Constants {
    //Sleep timer
    public static final String SLEEP_HOURS = "sleep_hours";
    public static final String SLEEP_MINS = "sleep_mins";
    public static final String SLEEP_SECS = "sleep_secs";
    public static final String SLEEP_TIMER_POSITION = "position";

    public static final int RESULT_BOOK_OPENED = 111;
    public static final int RESULT_NO_BOOK_OPENED = 222;
    public static final int BOOK_DELETE_REQUEST = 333;
    public static final String USER_THEME = "UserTheme";
    public static boolean IS_DEBUG_ENABLED = false;
    public static final int HELPSCREEN_CLOSE_REQUESTCODE = 777;

    public static  String BLANK = "";
    public static final String SCORM_PARAM_LIST = "scormDataList" ;
    public static String BOOKSHELF_TYPE ="bookShelf_type";
    public static String SELECTED_CATEGORY_POSITION ="selectedCategoryPosition";
    public static String LAST_CLICKED_CATEGORY ="lastClickedCategory";
    public static String PLAYER_TYPE = "player_type";
    public static String customBookID = "customBookID";

    public static final String  RESPONCE_STRING= "ResponceString";
    public static final String  RESPONCE_FAIL= "failure";

    public static final String LAUNCHED_FROM_BROWSER = "launchedfrombrowser";
    public static final String FIRST_LAUNCH = "firstlaunch";
    public static final String FIRST_LAUNCH_FOR_DOWNLOAD = "firstlaunchdownload";
    public static final String HELPSCREEN_REQUIRED_READER = "bookplayerfirsttime";
    public static final String HELPSCREEN_REQUIRED_REVIEW = "reviewfirsttime";
    public static final String IS_FAVOURITE="isfavourite";
    public static final String APP_UPDATE_REQUIRED = "is_Required";

    public static final String DEFAULT_APP_LANGUAGE_POSITION= "position";
    public static final String DEFAULT_APP_LANGUAGE= "locale";

    public static final String SHELF_PREFS_NAME = "myPrefs";
    public static final String USER_FIRST_LAST_NAME = "firstLastName";
    public static final String IS_CLASS_ASSOCIATED = "isClassAsscocoated";
    public static final String BOOK_SHELF_LAUNCH_FIRST_TIME = "bookshelffirsttime";
    public static final String BOOK_PLAYER_LAUNCH_FIRST_TIME = "bookplayerfirsttime";
    public static final String HELPSCREEN_REQUIRED = "bookplayerfirsttime";
    public static final String PREF_USER_TOKEN = "token";
    public static final String PREF_USER_LOGIN_ID = "userLoginID";
    public static final String PREF_USER_ID = "userID";
    public static final String PREF_USER_APP_LOGO = "logoURL";
    public static final String PREF_USER_APP_COLOR = "colorCode";
    public static final String PREF_USER_ROLE_NAME = "roleName";
    public static final String PREF_USER_TYPE = "type";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_USER_FIRST_NAME = "firstName";
    public static final String PREF_USER_LAST_NAME = "lastName";
    public static final String PREF_LOGIN_STATUS = "loginStatus";
    public static final String PREF_LAUNCH_STATUS = "launchStatus";
    public static final String IS_PENTOOL = "isPentool";
    public static final String GOOGLE_DOCS = "https://docs.google.com/gview?embedded=true&url=";

    public static final String DEFAULT_USER_NAME = "prepacked";
    public static final String DEFAULT_USER_ID = "prepacked";

    public static final String DEFAULT_CATEGORY_NAME0 = "Downloadable eBooks";
    public static final String DEFAULT_CATEGORY_NAME1 = "Preloaded eBooks";
    public static final String DEFAULT_CATEGORY_NAME3 = "Recently Viewed";
    public static final String DEFAULT_CATEGORY_NAME4 = "Most Viewed";

    public static final String USER_DOWNLOADED_CATEGORY = "Recently Downloaded";

    public static final String SEARCH_ALL = "All";
    public static final String SEARCH_TITLE = "Title";
    public static final String SEARCH_AUTHOR = "Author";
    public static final String SEARCH_SUMMARY = "Summary";
    public static final String SEARCH_ISBN = "ISBN";
    public static final String SEARCH_BOOK = "Book";
    public static String  FROM_ACCESS_CODE = "fromaccesscode";

    public static final String PREF_CURRENT_USER_BOOKLIST = "currentuserbooklist";
    public static final String thumbsDir = "thumbnailcache";
    public static final String thumbsPreview = "preview";
    public static final String PREF_IS_LAUNCH_FIRST_TIME = "islaunchfirsttime";
    public static final String PREF_FONT_JSON_CHECKSUM = "fontjsonchecksum";
    public static final String PREF_THEME_JSON_CHECKSUM = "themejsonchecksum";
    public static final String UGC_METADATA_PARAM_SHARED_WITH = "sharedWith";

    public static String CUSTOME_URL_PRIVACY_POLICY = "";
    public static String CUSTOME_URL_TERMS_AND_CONDITIONS = "";

    public static final String ASSESSMENT_TYPE_PEN_TOOL = "3";
    public static final String ASSESSMENT_TYPE_FIB_TOOL = "5";

    public static final String PREFS_NAME = "UnzipBooks";
    public static String HURIX_ENCRYPTION_KEY = "hurix123!@#";
    public static String HURIX_ENCRYPTION_KEY_FOR_DB = "hurix01234567890";

    public static  String DATA ="";
    public static  String CAMDATA =Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    public static final String UTILSDATA =  "/"+Constants.getROOTFOLDER();
    //public static final String DATA1= Environment.
    public static String getOLDDATA() {
        return OLDDATA;
    }
    public static final String OLDDATA = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    // Search highlight Split xml Path
    public static final String SPLIT_XML_FILES_PATH = "assets/strip/split_";
    public static boolean IS_TEXT_HIGHLIGHT = true;
    public static int HIGHLIGHT_RECT = 0;
    public static int HIGHLIGHT_UNDERLINE = 1;

    public static String getROOTFOLDER() {
        return ROOTFOLDER;
    }

    // Root folder for all ebook assets
    public static String ROOTFOLDER = ".cloudreaderbooks/";

    public static String ROOTFOLDER_UNHIDDEN ="cloudreaderbooks/";
    //DB
    public static final String XML_ROOT_BIN = "assets/xml-bin/";
    public static  String ALLBOOKROOTPATH = DATA + ROOTFOLDER;
    public static final String PROFILE_PICS_FOLDER = "ProfilePics";
    public static final String PROFILE_PICS_BACKUP = "backup";
    public static final String SERVER_THEME_FONT_PICS_FOLDER = "ThemeAndFont";

    public static final String DB_NAME = "xml.sqlite";
    public static final String ALL_SQLITE_PATH = XML_ROOT_BIN + DB_NAME;
    public static final String AUDIO_TIMEINDEX ="time-index.json";

    //epub Db version code
    public static final int DATABASE_VERSION = 1;


    public static final String EPUB_PATH = "OPS/";
    public static final String EPUB_THUMBNAIL_PATH = "images/cover.png";
    public static final String EBOOK_PATH = "/" + XML_ROOT_BIN + "eBook.xml";
    // Pages thumbnail root path
    public static final String THUMBNAIL_PATH = "assets/images/thumbnails/";
    public static final String THUMBNAIL_CACHE_PATH = "assets/images/thumbnails/cache/";
    // public static final String IMG_PREVIEW_PATH = "assets/images/pages/";
    public static final String PAGE_PATH = "assets/pdf/chapter0/ebook.pdf";
    public static final String DUPLICATE_PAGE_PATH = "assets/pdf/chapter0/ebook2.pdf";
    // This constant is relative to the CHAPTER_PATH
    public static final String SEARCH_FILE_PATH = "assets/xml-bin/search/search.xml";
    // Constant used for bookshelf for front page icon
    public static final String FRONTPAGE = "assets/images/thumbnails/cover.png";
    // Constant used for bookshelf for front page alternate icon
    public static final String ALTERNATEFRONTPAGE = "/assets/images/thumbnails/page_1.png";
    // Temp path to read pos data for search words -sudhir
    public static final String SEARCH_WORD_POS = "xml_res_pos/Eng_split_11_word.xml";
    // Path to store book data
    public static final String APP_DATA_FOLDER = "app_book_data/";
    // Table of Contents
    public static final String TOC_FILE_PATH = "assets/xml-bin/toc.xml";
    public static final String GLOSSARY_FILE_PATH = "assets/xml-bin/glossary/glossary.xml";
    public static final String BOOKMARK_FILE_PATH = "assets/xml-bin/bookmark.xml";
    public static final String RESOURCE_FILE_PATH = "assets/xml-bin/resources.xml";
    public static final String INTERACTIVE_FILE_PATH = "assets/xml-bin/chapter0/pages.xml";
    public static final String PAGENOTE_FILE_PATH = XML_ROOT_BIN + APP_DATA_FOLDER + "pagesnotes.xml";
    public static final String STICKYNOTE_FILE_PATH = XML_ROOT_BIN + APP_DATA_FOLDER + "stickynotes.xml";
    public static final String PENTOOL_FILE_PATH = XML_ROOT_BIN + APP_DATA_FOLDER + "pentool.bmp";

    public static final int TOCButtonid = 1;
    public static final int bookmarkButtonid = 2;
    public static final int stickyButtonid = 3;
    public static final int resourceButtonid = 4;
    public static final int glossaryButtonid = 5;

    public static final int HIGHLIGHT = 1;
    public static final int HIGHLIGHT_STICKY_NOTE = 2;
    public static final int HIGHLIGHT_STICKY_NOTE_WITH_IMAGE = 3;
    public static final int HIGHLIGHT_STICKY_NOTE_WITH_AUDIO = 4;
    public static final int HIGHLIGHT_STICKY_NOTE_WITH_VIDEO = 5;
    // Layout Ids for the controls
    public static final int TOOL_NAVIGATION_ID = 100;
    public static final String HIGHLIGHT_FILE_PATH = XML_ROOT_BIN + APP_DATA_FOLDER + "highlight/";
    public static final float SEARCH_WINDOW_SCALE = (float) 0.4;
    public static final int THUMB_INSAMPLE_SIZE = 3;
    public static final int PREVIEW_INSAMPLE_SIZE = 1;

    public static final int HIGHLIGHT_COLORS_RED = 0x44FF0000;// Color.RED;
    public static final int HIGHLIGHT_COLORS_BLUE = 0x446698FF;// Color.BLUE;
    public static final int HIGHLIGHT_COLORS_YELLOW = 0x44FFFF00;// Color.YELLOW;
    public static final int HIGHLIGHT_COLORS_GREEN = 0x4400FF00;// Color.GREEN;
    public static final int HIGHLIGHT_COLORS_MAGENTA = 0x44EE00EE;// Color.MAJENTA;
    public static final int HIGHLIGHT_COLORS_BLACK = 0x44000000;// Color.BLACK;

    public static final int[] HIGHLIGHT_COLORS = {HIGHLIGHT_COLORS_RED, HIGHLIGHT_COLORS_BLUE, HIGHLIGHT_COLORS_YELLOW, HIGHLIGHT_COLORS_BLACK, HIGHLIGHT_COLORS_GREEN, HIGHLIGHT_COLORS_MAGENTA};
    public static String WEB_XML_PATH = "/assets/web.xml";
    public static final String IMAGE_CACHE_FOLDER = "onlineImagesCache";

    public static final int E_BOOK = 0;
    public static final int V_BOOK = 1;
    // public static final int THUMB_VIEW_HEIGHT = 180;//by the ref as default
    // density
    public static final int DEFAULT_DENSITY = 160;// mdpi

    public static final int HIGHLIGHT_NORMAL = 0x44FFFF00;
    public static final int HIGHLIGHT_IMPORTANT = 0x44FF0000;
    public static final int HIGHLIGHT_SEARCH_COLOR = 0x4B1f25aa;


    //for cross Platform Syncing
    public static final int HIGHLIGHT_NORMAL_FOR_SYNCING = 16777062;
    public static final int HIGHLIGHT_IMPORTANT_FOR_SYNCING = 16737894;

    public static final int HIGHLIGHT_READ_ONLY = 0X44479CC3;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final String UGC_RESOURCES = "UgcResources";
    // public static final String UGC_IMAGESFOLDER ="Images";
    // public static final String UGC_AUDIOSFOLDER = "Audios";
    // public static final String UGC_VIDEOSFOLDER = "Videos";
    public static final String UGC_IMAGEFORMAT = ".jpg";
    public static final String UGC_VIDEOFORMAT = ".mp4";
    public static final String UGC_AUDIOFORMAT = ".amr";
    public static String CLIENT_LOGO_PATH = "";
    public static String INSTITUTE_LOGO_PATH = "";
    public static String INSTITUTE_LOGO_PATH_PREFIX = "institute";
    public static String CLIENT_LOGO_PATH_PREFIX = "client";

    /* first time on opening the book if the last visted page is not 1 then
	  pager adapter navigates to respective page from page 1.Here page 1 goes
	  to last visited page stack.In this case we should not consider first page
	  as last visited */
    public static boolean Last_Visited_History_Enable = true;

    public static String DEFAULT_BOOK_CATEGORY = "All Books";
    public static String DOWNLOAD_BROADCAST_ACTION_TAG = ".Managers.DownloadManager.DownloadService";
    public static String DOWNLOAD_BROADCAST_START_OR_STOP_TAG = ".Managers.DownloadManager.StartOrStopDownload";

    public static String UNZIP_BROADCAST_ACTION_TAG = ".Managers.UnZipManager.UnzipService";
    public static String UNZIP_BROADCAST_START_OR_STOP_TAG = ".Managers.UnZipManager.StartOrStopUnzip";

    public static String BROADCAST_START_UGC_SYNC_TAG = ".com.hurix.kiitaboo.ugcsync.android_services";

    //these constants would be created dynamically by concatinating the package name
    public static String DOWNLOAD_BROADCAST_ACTION = "";
    public static String DOWNLOAD_BROADCAST_START_OR_STOP = "";

    public static String UNZIP_BROADCAST_ACTION = "";
    public static String UNZIP_BROADCAST_START_OR_STOP = "";

    public static String BROADCAST_START_UGC_SYNC = "";
    public static String UGC_SYNC_DB_FILE_NAME = "";
    public static String UGC_SYNC_DB_FILE_PATH = "";

    //Services time out constants  CTO= Connection Time Out  STO= Socket Time Out
    public static final int CTO_UGC_DATA_DOWNLOAD = 60 * 1000;//in milliseconds
    public static final int STO_UGC_DATA_DOWNLOAD = 60 * 1000;//in milliseconds

    //Data upload to server -  New Update Delete
    public static final String UGC_ITEM_NEW = "NEW";
    public static final String UGC_ITEM_UPDATE = "UPDATE";
    public static final String UGC_ITEM_DELETE = "DELETE";

    //UGC Type
    public static final String UGC_TYPE_NOTE = "1";
    public static final String UGC_TYPE_HIGHLIGHT = "2";
    public static final String UGC_TYPE_DRAWING = "3";
    public static final String UGC_TYPE_BOOKMARK = "4";
    public static final String UGC_TYPE_FFT = "5";
    public static final String UGC_PROTRACTOR_TYPE = "6";

    //UGC Params
    public static final String UGC_PARAM_BOOK_LIST = "ugcBookList";
    public static final String UGC_PARAM_LIST = "ugcList";
    public static final String UGC_PARAM_BOOK_ID = "bookID";
    public static final String UGC_PARAM_BOOK_VERSION = "bookVersion";

    public static final String UGC_PARAM_ID = "id";
    public static final String UGC_BOOK_ID = "bookID";
    public static final String UGC_PARAM_LOCAL_ID = "localId";
    public static final String UGC_PARAM_PAGE_ID = "pageId";
    public static final String UGC_PARAM_TYPE = "type";
    public static final String UGC_PARAM_UGC_DATA = "ugcData";
    public static final String UGC_PARAM_CREATED_ON = "createdOn";
    public static final String UGC_PARAM_STATUS = "status";
    public static final String UGC_PARAM_META_DATA = "metadata";
    public static final String UGC_PARAM_IMPORTANT = "important";
    public static final String UGC_PARAM_FOLIO_ID = "folioID";

    //UGC Metadata Params For Highlight
    public static final String UGC_METADATA_PARAM_START_WORD_INDEX = "StartWordIndex";
    public static final String UGC_METADATA_PARAM_HIGHLIGHTED_TEXT = "HighlightedText";
    public static final String UGC_METADATA_PARAM_END_WORD_INDEX = "EndWordIndex";
    public static final String UGC_METADATA_PARAM_IMPORTANT = "IsImportant";
    public static final String UGC_METADATA_PARAM_COLOR = "color";

    public static final String UGC_METADATA_PARAM_CHAPTER_ID = "ChapterId";
    public static final String UGC_METADATA_PARAM_CHAPTER_NAME = "ChapterName";    //Also used in Bookmark metadata
    public static final String UGC_METADATA_PARAM_NOTE_TEXT = "Text";
    public static final String UGC_METADATA_PARAM_COMMENTED_BY = "Comments";

    //UGC Metadata Params For Pentool
    public static final String UGC_METADATA_PARAM_X_POS = "x";
    public static final String UGC_METADATA_PARAM_Y_POS = "y";
    public static final String UGC_METADATA_PARAM_LINK_ID = "LinkID";
    public static final String UGC_METADATA_PARAM_REVIEW_MODE ="Review" ;

    public static final boolean UGC_BOOKMARK_SYNC_ENABLED = true;
    public static final boolean UGC_HIGHLIGHT_SYNC_ENABLED = true;
    public static final boolean UGC_NOTE_SYNC_ENABLED = true;
    public static final boolean UGC_PENTOOL_SYNC_ENABLED = true;


    public static final int EBOOK_PLAYER_ACTIVITY_CODE = 100;
    public static final int STICKYNOTE_ACTIVITY_CODE = 200;
    public static final int VIDEO_PLAYER_ACTIVITY_CODE = 300;

    public static final String UGC_ITEM_MODE_NEW = "N";
    //	public static final String UGC_ITEM_MODE_UPDATED = "U";
    public static final String UGC_ITEM_MODE_MODIFIED = "M";
    public static final String UGC_ITEM_MODE_DELETED = "D";
    //	public static final String UGC_ITEM_MODE_UPTODATE = "M"; //


    public static final String PENTOOL_COLOR_YELLOW = "fcb000";
    public static final String PENTOOL_COLOR_DEFAULT = "010002";
    public static final String PENTOOL_COLOR_BLUE = "01a7fc";


    public static final String PENTOOL_ASSESSMENTS_COLOR_RED = "ff0000";
    public static final String PENTOOL_ASSESSMENTS_COLOR_GREEN = "93c50c";

    public static final int PENTOOL_SIZE_BIG = 24;
    public static final int PENTOOL_SIZE_MEDIUM = 12;
    public static final int PENTOOL_SIZE_SMALL = 1;


    //User Json screen data
    public static final String USER_ID = "ID";
    public static final String USER_FIRST_NAME = "FirstName";
    public static final String USER_LAST_NAME = "LastName";
    public static final String USER_ROLE = "UserRole";
    public static final String USER_LOGIN_ID = "UserLoginID";
    public static final String USER_LOGIN_ACCESSCODE = "UserAccessCode";

    // Comment screen Json data key
    public static final String UGC_COMMENT_DATA = "Text";
    public static final String UGC_COMMENT_DATE = "Date";
    public static final String UGC_COMMENT_USER_ID = "UserID";
    public static final String UGC_COMMENT_DISPLAY_NAME = "DisplayName";

    public static String USER_TOKEN = "";

    public static final int HTTP_REQUEST_TIMEOUT_CODE = 777;

    public static final int ADD = 1;
    public static final int UPDATE = 2;

    public static final boolean UGC_ALL_DATA_SYNC_STATUS = true;

    public static String HOME_BROADCAST_ACTION = "android.intent.category.HOME";
    public static String HOME_BROADCAST_ACTION_TAG = ".com.hurix.bookreader.views.link.LinkAudioView.onChangeOrientation";

    public static final String REFRESH_DATA = "REFRESH_DATA";

    //service type 1= local 2 = remote
    public static final int SERVICE_TYPE = 1;

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
        APP_LANGUAGE_SAVE,
        HIT_LIKE_SERVICE_REQUEST,
        RECENTLY_ADDED_BOOKS_SERVICE_REQUEST,
        RECENTLY_VIWED_MORE_INFO_BOOKS_SERVICE_REQUEST,
        MARK_AS_VISITED_MORE_INFO,
        UNMARK_AS_VISITED_MORE_INFO,
        FETCH_MULTICATEGORIES_BOOKLIST_REQUEST,
        FETCH_CATEGORIES_BOOKLIST_REQUEST,
        FETCH_STUDENT_MARKUP_REPORT,
        PINCODE_REQUEST,
        CONTENT_SERVER_AUTH_REQUEST,
        TOC_TIME_INDEX_REQUEST,
        GET_JWTOKEN_REQUEST,
        ISBNCODE_REQUEST;

    }

    public static final int ALL_DATA = 100 ;
    public static final int SHARED_WITH_ME = 200;
    public static final int SHARED_WITH_ME_ACTION_TAKEN_YES = 201;
    public static final int SHARED_WITH_ME_ACTION_TAKEN_NO = 202;
    public static final int NOTES_DATA = 301;

    public static final String STUDENT = "LEARNER";
    public static final String TEACHER = "INSTRUCTOR";


    public static final String JSON_LINECOLOR = "LineColor";
    public static final String JSON_LINEWIDTH = "LineWidth";
    public static final String JSON_LINESTYLE = "LineStyle";
    public static final String JSON_PATHPOINTS = "PathPoints";

    public static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";


    public static final String HIGHLIGHT_CREATEDBY_ID_TAG = "id";
    public static final String HIGHLIGHT_CREATEDBY_FIRSTNAME_TAG = "firstName";
    public static final String HIGHLIGHT_CREATEDBY_LASTNAME_TAG = "lastName";
    public static final String HIGHLIGHT_CREATEDBY_USERNAME_TAG = "userName";

    public static final int SHARE_DATA_ACCEPTED = 1;
    public static final int SHARE_DATA_REJECTED = 0;

    public static final String ACCEPT_UGC_LIST_TAG = "acceptUgcList";
    public static final String UGCID_TAG = "ugcID";
    public static final String STATUS_TAG = "status";

    public static final String DEFAULT_TIME_STAMP = "1970-01-01 00:00:01";
    public static final long SEARCH_REMOVE_TIME = 30000;

    public static final String HIGHLIGHT_SHARED_BY_DATA_TAG = "collabData";
    public static final String HIGHLIGHT_SHARED_BY_DATA_USER_ID_TAG = "id";
    public static final String HIGHLIGHT_SHARED_BY_DATA_UGCID_TAG = "ugcID";
    public static final String HIGHLIGHT_SHARED_BY_DATA_RECEIVERS_TAG = "receivers";

    public static String FROM_CUSTUME_USER = "fromCustumeUser";

    public static String CUSTOME_URL_TOKEN = "";
    public static String CUSTOME_CLIENT_ID = "";
    public static String CUSTOME_BOOK_ID = "";
    public static final int FIRST_TIME_LAUNCH = 0;
    public static final int SECOND_TIME_LAUNCH = 1;

    public static final long PREPACKED_USERID = -1;
    public static final String PREPACKED_EMAILID = "";

    public static final int BOOKSHELF_REQUEST_CODE = 1001;
    public static final int BOOKPLAYER_REQUEST_CODE = 1002;
    public static final int UGCDATA_REQUEST_CODE = 1003;
    public static final int SCORM_REQUEST_CODE = 1011;

    public static final int BOOKPLAYER_OPEN_PDF_REQUESTCODE = 1003;
    public static final int BOOKPLAYER_PROCESS_LINKTYPE_REQUESTCODE = 1004;

    public static final int BOOKSHELF_OLD_REQUEST_CODE = 1005;

    public static final int BOOKSHELF_NEW_REQUEST_CODE = 1006;

    public static final int BOOKSHELF_VEDIO_REQUEST_CODE = 1006;

    public static final int BOOKPLAYER_ANALYTICS_TIMESPENT_UPPER_LIMIT = 600;

    public static final String COMMANDTYPE_NEW = "new";
    public static final String COMMANDTYPE_PAUSE = "pause";
    public static final String COMMANDTYPE_STOPALL = "stopall";


    //epub related constants
    public static final String CONTAINER = "container";
    public static final String CONTAINER_FULL_PATH = "full-path";
    public static final String CONTAINER_MEDIA_TYPE = "media-type";
    public static final String CONTAINER_ROOTFILE = "rootfile";
    public static final String SPINE = "spine";
    public static final String TOC = "toc";
    public static final String ITEM = "item";
    public static final String ID = "id";
    public static final String HREF = "href";
    static final String BOOKPLAYER_SECURE_URLTYPE_ = "Client Secured";


    public static final String META_INF = "META-INF";
    public static final String CONTAINER_XML = "container.xml";

    public static final String PAGE_TRACKING_RESOURCE_TAG = "resources";
    public static final String PAGE_TRACKING_RESOURCE_ID_TAG = "id";
    public static final String PAGE_TRACKING_RESOURCE_TYPE_TAG = "type";
    public static final String PAGE_TRACKING_RESOURCE_TITLE_TAG = "title";
    public static final String PAGE_TRACKING_RESOURCE_CATEGORY_TAG = "category";


    public static final int REQ_START_STANDALONE_PLAYER = 1;
    public static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    public static final int REQ_YOUTUBE_PLAYER = 3;


    public static final int SESTION_EXPIRED = 900;

    //1 for Kitaboo product
    //2 for anaya
    //3 for porto
    //4 for profil

    public static final int SERVER_ADAPTER = 1;
    public static final String ANAYA_OS_TYPE = "Android";

    //Client id
    public static final String PORTO = "porto";
    public static final String PORTO_CLIENT_ID = "UG9ydG9BZGFwdGVy";

    public static final int ERROR_CODE_CONNECTIONTIMEOUT = 1001;
    public static final int ERROR_CODE_SOCKETTIMEOUT = 1002;
    public static final int ERROR_CODE_UNKNOWN = 1003;
    public static final String NO_SEARCH_RESULT = "noSearchResult";

    public static final int CAMERA_REQUEST_CODE = 1004;
    public static final int GALLERY_REQUEST_CODE = 1005;
    public static final int CAMERA_GALLERY_REQUEST_CODE = 1006;
    public static final int CAMERA_GALLERY_ALL_REQUEST_CODE = 1009;
    public static final int CROP_REQUEST_CODE = 1007;
    public static final String CAMERA_DATA = "data";
    public static final String RETURN_CODE_STRING = "returnCodeString";
    public static final String PROFILE_IMAGE_EXTENSION = ".jpg";
    public static final String PROFILE_IMAGE_EXTENSION_OLD = ".png";

    public static final String BOOKLIST_CLASS_ARRAY_TAG = "classList";
    public static final String BOOKLIST_CLASS_ID_TAG = "classID";
    public static final String BOOKLIST_CLASS_ROLEID_TAG = "roleID";
    public static final String BOOKLIST_CLASS_ROLENAME_TAG = "roleName";
    public static final String BOOKLIST_CLASS_LASTPAGEFOLIOID_TAG = "lastPageFolio";
    public static final String BOOKLIST_CLASS_SUSPENDDATA_TAG = "suspendData";
    public static final String BOOKLIST_FORMAT_ARRAY_TAG = "formats";
    public static final String PROFILE_PIC_DATA = "profilePicData";
    public static final String PROFILE_PIC_MESSAGE = "profilePicMessage";
    public static final long FILESIZE = 1024;
    // ForgotPassword type = 1
    public static final int FORGOT_PASSWORD_TYPE = 1;
    //Comment Thread limit
    public static final int COMMENT_THREAD_LIMIT = 100;

    public static final String BOOK_TYPE_MODE_ONLINE = "Online";
    public static final String BOOK_TYPE_MODE_UPLOAD = "Upload";
    public static final String BOOK_TYPE_SOURCE_YOUTUBE = "Youtube";
    public static final String BOOK_TYPE_SOURCE_KALTURA = "Kaltura";
    public static final String BOOK_TYPE_SOURCE_VIMEO = "Vimeo";
    public static final int DICTIONARY_POP_UP_HEIGHT = 280;

    public static final int DICTIONARY_POP_UP_HEIGHT_ALLOWED = 20;

    public static final int READ_ALOUD_PLAY_STATE = 1;
    public static final int READ_ALOUD_PAUSE_STATE = 2;
    public static final int READ_ALOUD_STOP_STATE = 3;
    public static final int READ_ALOUD_START_MEDIA_PLAYER = 4;

    public static final String AUDIO_BOOK_TYPE = "Auto Read Aloud";

    public static final int AUDIO_TYPE_AUDIO_SYNC = 0;
    public static final int AUDIO_TYPE_READALOUD = 1;

    public static final String BOOKSHELF_COLLECTION_INNER_VIEW_CLICKED = "innerViewClicked";
    public static final String ENCRYPTION_MODE = "encryptionMode";
    public static final String TEACHER_SUPPORTED = "teacherExclusiveSupported";
    public static final String NOTIFICATION_ACTIONTAKEN_ACCEPTED = "Y";
    public static final String NOTIFICATION_ACTIONTAKEN_REJECTED = "R";
    public static final int MY_COLLECTION_ID = 999;

    public static final String SIGNOUT_OPTION ="signout";
    public static final String FILE_TYPE_ZIP = ".zip";
    public static final String FILE_TYPE_TAR = ".tar";

    public static final Integer SESSION_EXPIRE_ERRORCODE = 103;
    public static final String APP_VERSION = "app_version";
    public static final String CATEGORY_POSITION = "catPosition";
    public static final String SHARED_USER_LIST = "shared_user_list";
    public static final String PEN_COLLECTIONS = "pen_collections";


    public static final int REQUEST_EXTERNAL_STORAGE_DOWNLOAD = 1;
    public static final int REQUEST_EXTERNAL_STORAGE_OPEN = 2;
    public static final int REQUEST_EXTERNAL_STORAGE_DELETE = 3;
    public static final int REQUEST_PERMISSION_CAMERA = 4;
    public static final int REQUEST_ALERTWINDOW_STICKYNOTE = 5;
    public static final int REQUEST_ALERTWINDOW_HTMLWRAP = 6;
    public static final int REQUEST_HELPSCREEN = 7;

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static String[] PERMISSIONS_SYS_ALERT = {
            Manifest.permission.SYSTEM_ALERT_WINDOW
    };

    public static final String BOOK_ASSET_TYPE= "VIDEO";

    public static final String FLEXIBLE_ICON_URL_HITAREA = "flexibleicon.png";

    public static final String VIDEO_DECRIPTION_ERROR = "Error: Video";

    public static final String RESOURCES_ROLE = "Teacher";

    public static boolean ISACTIVITY_OPEN = true;

    public static boolean DATASYNCING = false;

    public static final String GET_BOOKLIST_SERVICE_FORSCROLLED_CATEGORY = "getBooklistServiceForScrolledCategory";

    public static final String GET_BOOKLIST_SERVICE_FORSCROLLED_COLL = "getBooklistServiceForScrolledCollection";

    public static final String IS_FETCH_BOOKLIST_REQUEST_CALLED = "isFetchBookListRequestCalled";

    public static final String GET_FIRST_BUNCHOFBOOKS_FOR_EACH_CAT_SERVICECALLED = "getFirstBunchOfBooksForEachCat";

    public static final String SET_SESSION_ERROR_OCCUR_WHILE_BOOKLIST_SERVICE = "sessionErrorOccurForBookListService";

    public static final String THEME_DATA = "theme_data";
    public static final String APP_UPDATE = "data_migration";

    //Tabs
    public static final String ALL_TAB = "All";
    public static final String DOWNLOADED_TAB = "Downloaded";
    public static final String RECENTLY_VIEWED_TAB = "Recently Viewed";
    public static final String FAVOURITE_BOOKS_TAB="My Books";
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String DISTRICTID = "districtid";

    public static final String FAVOURITES = "Favourites";
    //public static final String FAVORITES = "Favorites";

    //for client specific we have to change here for favourite tab.
    public static final String FAVORITES = "Favorites";

    //     5.0 reader
    public static final String RECENTLY_VIEWED_BY = "recentlyViewedBy";
    public static final String LASTVISITED_PAGE_FILE_NAME = "lastvisited_page.png";

    public static final String SESSION_HISTORY = "sessionHistory";
    public static final String COLLECTION_BOOK_ID = "collection_book_id";
    public static final String SAVE_SESSION_TIME = "time";
    public static final  String USERNAME="username";
    public static final String PASSWORD_VG = "password_vg";
    public static final  String USERID ="userid";

    public static final String TIME_INTERVAL = "time_interval";
    public static final String CLIENT_USER_TYPE = "client_user_type";

    public static final String ISSYNCED = "isSynced";

    public static final String SORT_BY = "sortBy";
    public static final String CATEGORY_SORT_BY = "Category_sortBy";

    public static boolean IS_READ_RESPONSEMSG = false;



    //MILLS AND BOONS
    public static final String RECENTLY_ADDED = "Trending";
    public static final String MY_BOOKS = "My Books";
    public static final String EXPLORE = "Explore";

    public static final String ARC_ABOUTUS_URL = "https://www.redcross.org/about-us.html";


}

