package com.hurix.kitaboo.constants.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;

import com.hurix.kitaboo.constants.Constants;
import com.hurix.kitaboo.constants.R;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Utils {

    private final static String MY_PREFERENCES = "kitabooPrefs";
    private final static String TYPE_FACE = "typeface";
    public static final int PERMISSION_REQUEST_CODE = 1;
    public static int mCount=0;
    public static File lastModifiedFile=null;
    private static int statusBarHeight = 0;

    public  static  boolean isLangChangedByUser=false;

    public static String escapeString(String strplain) {
        String escapedString = "";
        try {
            escapedString = URLEncoder.encode(strplain, "UTF-8").replaceAll(
                    "\\+", "%20");
        } catch (Exception e) {
        }
        return escapedString;
    }

    public static int dpToPx(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }


    public static boolean isMobile(Context context)
    {
        WebView ua = new WebView(context);
        String settiing  = ua.getSettings().getUserAgentString();
        return settiing.contains("Mobile");
    }

    public static boolean isBelowofAndroidM()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static String unescapeString(String strdecoded) {
        String unescapedString = "";
        try {
            unescapedString = URLDecoder.decode(strdecoded, "UTF-8");
        } catch (Exception e) {
        }
        return unescapedString;
    }
    public static boolean checkPermission(Activity nameofactivity) {
        int result = ContextCompat.checkSelfPermission(nameofactivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    public static String getMessageForError(Context context, int errorcode) {
        if (errorcode == 200) {
            return context.getResources().getString(R.string.server_sucess_msg);
        } else if (errorcode == 400) {
            return context.getResources().getString(R.string.server_fail_msg);
        } else if (errorcode == 100) {
            return context.getResources().getString(
                    R.string.server_empty_field_msg);
        } else if (errorcode == 101) {
            return context.getResources().getString(
                    R.string.server_invalid_field_msg);
        } else if (errorcode == 102) {
            return context.getResources().getString(
                    R.string.server_invalid_type_msg);
        } else if (errorcode == 103) {
            return context.getResources().getString(
                    R.string.server_token_expired_msg);
        } else if (errorcode == 104) {
            return context.getResources().getString(
                    R.string.server_multiple_client_msg);
        } else if (errorcode == 105) {
            return context.getResources().getString(
                    R.string.server_multiple_institutes_msg);
        } else if (errorcode == 106) {
            return context.getResources().getString(
                    R.string.server_duplicate_emailid_msg);
        }else if (errorcode == 401) {
            return context.getResources().getString(
                    R.string.not_purchased);
        }
        else if (errorcode == 403) {
            return context.getResources().getString(
                    R.string.not_purchased);
        }
        else if (errorcode == 500) {
            Spanned errorMsg = Html.fromHtml(context.getResources().getString(
                    R.string.error_on_file_access_content));
            return errorMsg.toString();
        }
        else if (errorcode == 601) {
            return context.getResources().getString(
                    R.string.server_accesscode_invalid_msg);
        } else if (errorcode == 602) {
            return context.getResources().getString(
                    R.string.server_accesscode_invalid_with_other_user_msg);
        } else if (errorcode == 603) {
            return context.getResources().getString(
                    R.string.server_accesscode_invalid_with_same_user_msg);
        } else if (errorcode == 604) {
            return context.getResources().getString(
                    R.string.server_accesscode_invalid_with_other_institute);
        } else if (errorcode == 605) {
            return context.getResources().getString(
                    R.string.server_accesscode_invalid_distribustion_fail_msg);
        } else if (errorcode == 700) {
            return context.getResources().getString(
                    R.string.server_client_invalid_msg);
        } else if (errorcode == 800) {
            if(context.getResources().getBoolean(R.bool.is_Navneet_Client)) {
                return context.getResources().getString(
                        R.string.navneet_server_book_licence_limit_reached);
            }
            else{
                return context.getResources().getString(
                        R.string.server_book_licence_limit_reached);
            }
        } else if (errorcode == 801) {
            return context.getResources().getString(
                    R.string.server_book_licence_expired);
        } else if (errorcode == 802) {
            if(context.getResources().getBoolean(R.bool.is_Navneet_Client)) {
                return context.getResources().getString(
                        R.string.navneet_server_book_not_available);
            }
            else{
                return context.getResources().getString(
                        R.string.server_book_not_available);
            }
        } else if (errorcode == 300) {
            return context.getResources().getString(
                    R.string.server_client_trial_redirect);
        } else if (errorcode == 107) {
            return context.getResources().getString(
                    R.string.server_account_inactive);
        } else if (errorcode == Constants.ERROR_CODE_CONNECTIONTIMEOUT
                || errorcode == Constants.ERROR_CODE_SOCKETTIMEOUT) {
            return context.getResources().getString(
                    R.string.server_time_out_erros);
        } else if (errorcode == Constants.ERROR_CODE_UNKNOWN) {
            return context.getResources().getString( R.string.server_unknown_error);
        }else if (errorcode ==1) {
            return context.getResources().getString(
                    R.string.ERROR_BOOK_UNAVAILABLE);
        }

        return "Not found";
    }

    public static int Donullcheck(String string_Data) {
        int int_Value;
        if (string_Data.equals("")) {
            int_Value = 0;
        } else {
            int_Value = Integer.parseInt(string_Data);
        }
        return int_Value;
    }

    public static String clientIDCheck(Context context){
        String clientName = "";
        String clientID = context.getResources().getString(R.string.clientid);
        if(clientID.equals(Constants.PORTO_CLIENT_ID)){
            clientName = Constants.PORTO;
        }
        return clientName;
    }

    public static BitmapDrawable getRepeatableBitmap(Context context, int resid, int alpha) {
        final int version = Build.VERSION.SDK_INT;
        SVG svg = SVGParser.getSVGFromResource(context.getResources(), resid);
        Bitmap bitmap = Bitmap.createBitmap(svg.createPictureDrawable().getIntrinsicWidth(),
                svg.createPictureDrawable().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(svg.createPictureDrawable().getPicture());
        Bitmap currentBitmap = bitmap;
        BitmapDrawable Tileimage = new BitmapDrawable(currentBitmap);
        Tileimage.setAlpha(alpha);
        Tileimage.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        return Tileimage;
    }

    public static String EnCryptPassword(String password, String key)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        String original = android.util.Base64.encodeToString(
                password.getBytes(), android.util.Base64.DEFAULT);
        return original;
    }

    public static String DeCryptPassword(String password, String key)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] original = android.util.Base64.decode(password.getBytes(),
                android.util.Base64.DEFAULT);
        return new String(original).trim();
    }

    /**
     * ***********************************************************************
     * Method Name : getDateTime Description : To Get the Current System Date
     * Input : Output : Date in String format (yyyy-MM-dd HH:mm:ss.SSS)
     * ***********************************************************************
     */
    public static String getDateTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = df.format(currentLocalTime);
        return date;
    }


    public static String getStartDate (){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String startdate = df.format(currentLocalTime);
        return startdate;
    }

    public static String getNext30days(String dateAndTime) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, 30);
        //cal.add(Calendar.MINUTE,2);
        String currentDatePlusOne = df.format(cal.getTime());
        return currentDatePlusOne;
    }

    /**
     * ***********************************************************************
     * Method Name : getDateInLocalFormat Description : To Get the Date in
     * device Timezone
     * ***********************************************************************
     */
    public static String getDateInLocalFormat(String date, String outformat) {
        String result = date; // Default Value
        SimpleDateFormat outputFormat;
        if (TextUtils.isEmpty(outformat)) {
            outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            outputFormat.setTimeZone(TimeZone.getDefault());
        } else {
            outputFormat = new SimpleDateFormat(outformat);
            outputFormat.setTimeZone(TimeZone.getDefault());
        }
        SimpleDateFormat sourceFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date parsed = sourceFormat.parse(date);
            result = outputFormat.format(parsed);
        } catch (ParseException e) {
        }
        return getDateFormat(result);
    }

    public static  String getDateFormat(String date){
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
            date = date.replace("am", "AM").replace("pm", "PM");
            return date;
        }else {
            return date;
        }
    }

    public static boolean checkPhysicalZipFile(String isbn, String bookID) {
        boolean isBookzip = false;
        try {
            File zipFile = new File(
                    com.hurix.kitaboo.constants.utils.Utils
                            .getBookFolderPathCompat(bookID, isbn) + ".zip");

            File tarFile = new File(
                    com.hurix.kitaboo.constants.utils.Utils
                            .getBookFolderPathCompat(bookID, isbn) + ".tar");

            if (zipFile.exists() || tarFile.exists()) {
                isBookzip = true;
            }
        } catch (Exception ex) {
        }

        return isBookzip;
    }

    public static Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }
        return doc;
    }

    public static boolean getTeacherExclusiveSupported(String bookXML){
        boolean isTeacherExclusive = false;
        Document doc = null;
        doc = com.hurix.kitaboo.constants.utils.Utils.createDocumentFromFilePath(bookXML,doc);
        if (doc != null) {
            NodeList flowList = doc.getElementsByTagName(Constants.TEACHER_SUPPORTED);
            if(flowList != null && flowList.getLength()>0){
                Element el = (Element) flowList.item(0);
                if (el.getFirstChild() != null) {
                    if(el.getFirstChild().getNodeValue().equalsIgnoreCase("yes")){
                        isTeacherExclusive = true;
                    }
                }
            }
        }
        return isTeacherExclusive;
    }

    public static String getTextValue(Element ele, String tagName) {
        String textVal = "";
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            if (el.getFirstChild() != null) {
                textVal = el.getFirstChild().getNodeValue();
            }
        }
        return textVal;
    }

    public static String getValue(Element item, String tagName) {
        String str = "";
        try {
            NodeList n = item.getElementsByTagName(tagName);
            str = getCharacterDataFromElement(n.item(0));
        } catch (Exception e) {
            str = "";
        }
        return str;
    }

    public static String getCharacterDataFromElement(Node node) {
        String inputText = node.getTextContent();
        // inputText = escapeHtml(inputText);
        String outputText = null;
        if (inputText == null || inputText.equalsIgnoreCase("null")) {
            inputText = null;
        } else {
            outputText = "" + inputText;
        }
        String cdataPrefix = "<![CDATA[";
        String cdataPostfix = "]]>";
        if (inputText != null && inputText.startsWith(cdataPrefix)
                && inputText.endsWith(cdataPostfix)) {
            int lenOfInput = inputText.length();
            outputText = inputText.substring(cdataPrefix.length(), lenOfInput
                    - cdataPostfix.length());
        }
        if (outputText == null) {
            outputText = "";
        }
        return outputText;
    }

    /**
     * Backwards compatible method that will clear all activities in the stack.
     */
    public static void startLauncherActivity(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context
                .getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.putExtra("isSessionExpired", true);
        context.startActivity(mainIntent);
    }


    public static void removeFragements(Context context,
                                        boolean isAllFragmentRemove, ArrayList<String> tagNametoBeRemoved) {
        try {
            List<Fragment> al = ((FragmentActivity) context)
                    .getSupportFragmentManager().getFragments();
            // You might have to access all the fragments by their tag,
            // in which case just follow the line below to remove the fragment
            if (al != null) {
                // code that handles no existing fragments
                if (isAllFragmentRemove) {
                    for (Fragment frag : al) {
                        if (frag != null && frag.getTag() != null) {
                            ((FragmentActivity) context)
                                    .getSupportFragmentManager()
                                    .beginTransaction().remove(frag).commit();
                        }
                    }
                } else {
                    for (Fragment frag : al) {
                        if (frag != null && frag.getTag() != null) {
                            for (String str : tagNametoBeRemoved) {
                                if (frag.getTag().equalsIgnoreCase(str)) {
                                    ((FragmentActivity) context)
                                            .getSupportFragmentManager()
                                            .beginTransaction().remove(frag)
                                            .commit();
                                }

                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
        }

    }

    // @ method returns unique deviceId of an device ,
    // this deviceId will be changed once device is factory reset.
    public static String getDeviceId(Context contex) {
        String deviceid = "";
        try {
            deviceid = Secure.getString(contex.getContentResolver(),
                    Secure.ANDROID_ID);
        } catch (Exception e) {
        }
        return deviceid;
    }

    public static String getProfilePicFolderPathCompat() {
        File file = new File(Constants.ALLBOOKROOTPATH + Constants.PROFILE_PICS_FOLDER);
        if (file.exists() == false) {
            file.mkdirs();
        }
        return Constants.ALLBOOKROOTPATH + Constants.PROFILE_PICS_FOLDER + File.separator;
    }

    public static String getProfilePicFolderPathCompat(Context mContext) {

        File file = new File( getAppDataFolder(mContext) + Constants.PROFILE_PICS_FOLDER);
        if (file.exists() == false) {
            file.mkdirs();
        }

        return  getAppDataFolder(mContext) + Constants.PROFILE_PICS_FOLDER + File.separator;
    }


    public static String getAppDataFolder(Context context)
    {
        return  Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data"+ "/"+context.getPackageName()+"/"+"files/"+ Constants.ROOTFOLDER;
    }
    public static String getThemeAndFontFolderPathCompat() {
        File file = new File(Constants.ALLBOOKROOTPATH + Constants.SERVER_THEME_FONT_PICS_FOLDER);
        if (file.exists() == false) {
            file.mkdirs();
        }
        return Constants.ALLBOOKROOTPATH + Constants.SERVER_THEME_FONT_PICS_FOLDER + File.separator;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    public static String getBookFolderPathCompat(String bookID, String bookISBN) {
        File file = new File(Constants.ALLBOOKROOTPATH + bookISBN);
        if (file.exists()) {
            return Constants.ALLBOOKROOTPATH + bookISBN;
        }
        return Constants.ALLBOOKROOTPATH + bookID;
    }


    public static boolean isPhysicalPackageMissing(long bookID, String isbn,
                                                   boolean isEpub , boolean isVideo, boolean isAudio) {
        boolean isBookPackageMissing = true;
        if (isEpub) {
            try {
                File file = new File(getBookFolderPathCompat(bookID + "", isbn)
                        + File.separator + Constants.META_INF + File.separator
                        + Constants.CONTAINER + ".xml");
                if (file.exists()) {
                    isBookPackageMissing = false;
                }
            } catch (Exception ex) {
            }
        } else {
            try {
                if(isVideo) {
                    File folder = new File(getBookFolderPathCompat(bookID + "", isbn));
                    File[] listOfFiles = folder.listFiles();
                    for (File file : listOfFiles) {
                        if (file.isFile()) {
                            String[] filename = file.getName().split("\\.(?=[^\\.]+$)");
                            if(filename[1].equalsIgnoreCase("mp4") ||filename[1].equalsIgnoreCase("mp3") )
                            {
                                isBookPackageMissing = false;
                            }
                        }
                    }
                }else if(isAudio){
                    File file = new File(getBookFolderPathCompat(bookID + "", isbn)
                            + File.separator + Constants.AUDIO_TIMEINDEX);
                    if (file.exists()) {
                        isBookPackageMissing = false;
                    }

                }

                else {
                    File file = new File(getBookFolderPathCompat(bookID + "", isbn)
                            + File.separator + Constants.ALL_SQLITE_PATH);
                    if (file.exists()) {
                        isBookPackageMissing = false;
                    }
                }
            } catch (Exception ex) {
            }
        }

        return isBookPackageMissing;
    }

    public static String generateCryptString(String license, String device_id) {
        try {
            String retVal = license + device_id;

            for (int i = 0; i < 10; i++) {
                retVal = hash(retVal);
            }
            return retVal;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String hash(String str) throws NoSuchAlgorithmException {
        MessageDigest md;
        String pvKey = "*lt+m_(4%4)_m+tl*";
        md = MessageDigest.getInstance("SHA-256");
        md.update((pvKey + str).getBytes());
        StringBuffer sb = new StringBuffer();
        byte[] mdbytes = md.digest();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return sb.toString();
    }

    public static long getTimeSpentInSecond(String StartDateTime,String EndTimeDateTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            Date startDate = df.parse(StartDateTime);
            Date endTime = df.parse(EndTimeDateTime);
            long timespent = (endTime.getTime() - startDate.getTime()) / 1000;
            return timespent;
        } catch (Exception e) {
            return 0L;
        }
    }

    public static String getAppVersionName(Context context) {
        String versionName = "1.0";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
        }

        return versionName;
    }

    public static boolean IsDeviceTypeMobile(Context context) {
        String ua = new WebView(context).getSettings().getUserAgentString();
        return ua.contains("Mobile");
    }

    public static int getDeviceOrientation(Context context) {
        Display getOrient = ((Activity) context).getWindowManager().getDefaultDisplay();
        int orientation = context.getResources().getConfiguration().orientation;
        // Sometimes you may get undefined orientation Value is 0
        // simple logic solves the problem compare the screen
        // X,Y Co-ordinates and determine the Orientation in such cases
        if (orientation == Configuration.ORIENTATION_UNDEFINED) {
            Configuration config = context.getResources().getConfiguration();
            orientation = config.orientation;
            if (orientation == Configuration.ORIENTATION_UNDEFINED) {
                if (getOrient.getWidth() < getOrient.getHeight()) {
                    orientation = Configuration.ORIENTATION_PORTRAIT;
                } else { // if it is not any of the above it will defineitly be landscape
                    orientation = Configuration.ORIENTATION_LANDSCAPE;
                }
            }
        }
        return orientation; // return value 1 is portrait and 2 is Landscape Mode
    }

    //this methos is used to hide keyboard and clear focus from View -----@Sandeep
    public static void hideKeyboard(Context context) {
        // Check if no view has focus:
        View view;
        try {
            view = ((Activity) context).getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                view.clearFocus();
            }
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }

    }

    public static String getJsonString(String fileName, Context context,String path) {
        String tContents = "";
        try {
            File f = new File(path);
            if(f.exists()) {
                FileInputStream finput = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(finput));
                StringBuilder strBl = new StringBuilder();
                StringBuilder sb = new StringBuilder();
                String line = null;
                try {
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try{
                        br.close();
                    } catch (IOException e){
                        if(Constants.IS_DEBUG_ENABLED){
                            e.printStackTrace();
                        }
                    }
                }
                tContents = br.toString();
            }else {
                InputStream stream = context.getAssets().open(fileName);
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                tContents = new String(buffer);
            }
        } catch (IOException e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return tContents;
    }

    public static boolean hasValue(JSONObject obj, String type){
        boolean returnResult = false;
        try {
            returnResult = obj.has(type) && !obj.getString(type).isEmpty();
        }catch (Exception e){
            returnResult = false;
        }
        return returnResult;
    }

    public static String getValueFromJsonObj(JSONObject obj, String type) {
        String returnValue = "";
        try {
            returnValue = obj.getString(type);
        } catch (Exception e) {
        }
        return returnValue;
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int generateRandomNumber()
    {
        int rnd = 0;
        Random rand=new Random();
        int[] randNo = new int[8];
        ArrayList numbers = new ArrayList();
        for (int k=0 ; k< 8;k++)
        {
            rnd = rand.nextInt(8) + 1;
            if(k==0)
            {
                randNo[0] = rnd;
                numbers.add(randNo[0]);
            }
            else
            {
                while(numbers.contains(new Integer(rnd)))
                {
                    rnd = rand.nextInt(8) + 1;
                }
                randNo[k] = rnd;
                numbers.add(randNo[k]);
            }
        }
        return  rnd;
    }
    public static void hideKeyboard(Context context,View view)
    {
        InputMethodManager imm= (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static void showKeyboard(Context context,View view)
    {
        InputMethodManager imm= (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null && view != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static GradientDrawable getRectAngleDrawable(int solidColor,float[] _radius,int strokeWidth,int strokeColor)
    {
        GradientDrawable gradientDrawable= new GradientDrawable();
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(_radius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }
    public static GradientDrawable getCircleDrawableWithStroke(int solidColor,int strokeColor,int strokeWidth)
    {
        GradientDrawable gradientDrawable= new GradientDrawable();
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

    public static GradientDrawable getProgressDrawable(int strokeColor,int strokeWidth)
    {
        GradientDrawable gradientDrawable= new GradientDrawable();
        gradientDrawable.setColor(Color.TRANSPARENT);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setGradientRadius((float)2.5);


        return gradientDrawable;
    }


    public static String escapeHtml(CharSequence text) {
        StringBuilder out = new StringBuilder();
        withinStyle(out, text, 0, text.length());
        return out.toString();
    }

    private static void withinStyle(StringBuilder out, CharSequence text,int start, int end) {
        for (int i = start; i < end; i++) {
            char c = text.charAt(i);
            if (c == '<') {
                out.append("&lt;");
            } else if (c == '>') {
                out.append("&gt;");
            } else if (c == '&') {
                out.append("&amp;");
            } else if (c >= 0xD800 && c <= 0xDFFF) {
                if (c < 0xDC00 && i + 1 < end) {
                    char d = text.charAt(i + 1);
                    if (d >= 0xDC00 && d <= 0xDFFF) {
                        i++;
                        int codepoint = 0x010000 | (int) c - 0xD800 << 10 | (int) d - 0xDC00;
                        out.append("&#").append(codepoint).append(";");
                    }
                }
            } else if (c > 0x7E || c < ' ') {
                out.append("&#").append((int) c).append(";");
            } else if (c == ' ') {
                while (i + 1 < end && text.charAt(i + 1) == ' ') {
                    out.append("&nbsp;");
                    i++;
                }

                out.append(' ');
            } else {
                out.append(c);
            }
        }
    }

    public static Document createDocumentFromFilePath(String ebookXMLPath, Document document) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            document = builder.parse(new FileInputStream(ebookXMLPath));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static boolean isMobileData(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (cm.getNetworkInfo(0) != null) {
            NetworkInfo.State mobile = cm.getNetworkInfo(0).getState();
            return mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING;
        }
        return false;
    }

    public static String getEncaptiveName(String name){
        char firstLetter = Character.toUpperCase(name.charAt(0));
        name = firstLetter+name.substring(1,name.length()-1);
        return name;
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static boolean canResolveIntent(Intent intent,Context context) {
        List<ResolveInfo> resolveInfo = context.getPackageManager()
                .queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    /**
     * Insert Boolean Value
     * @param context context
     * @param key Key
     * @param value Boolean value
     * @return success
     */
    public static boolean insertSharedPreferenceBooleanValue(Context context, String key, boolean value){
        if(context != null)
        {
            try {
                SharedPreferences pref = getSharedPreferences(context,Constants.PREFS_NAME, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(key, value);
                editor.commit();
                return  true;
            }catch (Exception e){
                if(Constants.IS_DEBUG_ENABLED) {
                    e.printStackTrace();
                }
            }
        }
        return  false;
    }

    /**
     * Get boolean value
     * @param context context
     * @param key key
     * @param defaultValue default value
     * @return value
     */
    public static boolean getSharedPreferenceBooleanValue(Context context, String key, boolean defaultValue){
        try {
            SharedPreferences pref = getSharedPreferences(context, Constants.PREFS_NAME, 0);
            boolean value = pref.getBoolean(key, defaultValue);
            if (value == defaultValue) {
                // the key does not exist
                return defaultValue;
            } else {
                // handle the value
                return value;
            }

        }catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return  defaultValue;
    }

    public static boolean insertSharedPrefernceStringValues(Context context,String sharedPrefName,String key,String values) {
        try {
            SharedPreferences pref = getSharedPreferences(context,sharedPrefName, 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, values);
            editor.commit();
            return  true;
        }catch (Exception e){
            if(Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return  false;
    }

    public static String getSharedPreferenceStringValue(Context context,String sharedPrefName,String key,String defaultvalue) {
        try {
            SharedPreferences pref = getSharedPreferences(context, sharedPrefName, 0);
            String value = pref.getString(key, defaultvalue);
            if (value.equals(defaultvalue)) {
                // the key does not exist
                return defaultvalue;
            } else {
                // handle the value
                return value;
            }

        }catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return  defaultvalue;
    }


    private static SharedPreferences getSharedPreferences(Context context, String shelfPrefsName, int mode) {
        return context.getSharedPreferences(shelfPrefsName, mode);
    }

    public static double getDownloadedZipFileSize(String bookId, String bookIsbn, String ext) {
        double fileSize = 0;
        final String folderPath = Utils.getBookFolderPathCompat(bookId
                + "", bookIsbn);
        File file = new File(folderPath + ext);
        if (file.exists() && file.isFile()) {
            fileSize = file.length();
            fileSize = Utils.round(fileSize / (Constants.FILESIZE * Constants.FILESIZE * 1.0), 2);
        }else {
            file = new File(folderPath + ".zip");
            if (file.exists() && file.isFile()) {
                fileSize = file.length();
                fileSize = Utils.round(fileSize / (Constants.FILESIZE * Constants.FILESIZE * 1.0), 2);
            }
        }
        return fileSize;
    }

    public static boolean insertSharedPrefernceIntValues(Context context, String sharedPrefName, String key, int values) {
        try {
            SharedPreferences pref = getSharedPreferences(context, sharedPrefName, 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(key, values);
            editor.commit();

            return true;
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int getSharedPreferenceIntValue(Context context, String sharedPrefName, String key, int defaultvalue) {
        try {
            SharedPreferences pref = getSharedPreferences(context, sharedPrefName, 0);
            int value = pref.getInt(key, defaultvalue);
            if (value == defaultvalue) {
                // the key does not exist
                return defaultvalue;
            } else {
                // handle the value
                return value;
            }

        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return defaultvalue;
    }


    public static boolean insertSharedPrefernceLongValues(Context context, String sharedPrefName, String key, long values) {
        try {
            SharedPreferences pref = getSharedPreferences(context, sharedPrefName, 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong(key, values);
            editor.commit();

            return true;
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return false;
    }



    public static long getSharedPreferenceLongValue(Context context, String sharedPrefName, String key, long defaultvalue) {
        try {
            SharedPreferences pref = getSharedPreferences(context, sharedPrefName, 0);
            long value = pref.getLong(key, defaultvalue);
            if (value == defaultvalue) {
                // the key does not exist
                return defaultvalue;
            } else {
                // handle the value
                return value;
            }

        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return defaultvalue;
    }


    public static String getDirectory() {
        return Constants.DATA + File.separator;
    }


/*    public static Bitmap drawBitmap(String text){
        Bitmap b = Bitmap.createBitmap(20, 20, Bitmap.Config.ALPHA_8);
        Canvas c = new Canvas(b);
        c.drawText(text, 0, 20 / 2, new Paint());
        return b;
    }

    public static byte[] getPixels(Bitmap b) {
        ByteBuffer buffer = ByteBuffer.allocate(b.getByteCount());
        b.copyPixelsToBuffer(buffer);
        return buffer.array();
    }
    //method is used to check corresponding icon is missing in fontFile
    public static boolean isIconMissingInFont(String ch) {
        String missingCharacter = "\u0978";
        byte[] b1 = getPixels(drawBitmap(ch));
        byte[] b2 = getPixels(drawBitmap(missingCharacter));
        return Arrays.equals(b1, b2);
    }

    public static Typeface getTypface(String text, Context context) {
    Typeface typface = Typefaces.get(context, context.getResources().getString(R.string.client_font_file_name));
    TextView tv = new TextView(context);
    tv.setTypeface(typface);
    tv.setText(text);
    boolean isIconMissing = isIconMissingInFont(tv.getText().toString().trim());
    if (isIconMissing) {
        return Typefaces.get(context, context.getResources().getString(R.string.kitaboo_font_file_name));
    } else {
        return typface;
    }
    }*/

    public static String getHashMD5String(String s) {

        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }


    public static String decryptBlowfish(String to_decrypt, String strkey) {
        try {
            SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(to_decrypt.getBytes());
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String convertBlowFishToBaseString(String blowFishString , String strkey)
    {
        try {
            //String strkey = "hurix!@#";
            SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(android.util.Base64.decode(blowFishString.getBytes("UTF-8"),Base64.DEFAULT));

            return new String(decrypted);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static String encryptBlowfish(String to_encrypt, String strkey) {
        try {
           /* SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new String(cipher.doFinal(to_encrypt.getBytes("UTF-8")));*/
            byte[] keyData = strkey.trim().getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] hash = cipher.doFinal(to_encrypt.trim().getBytes());

            //System.out.println(new BASE64Encoder().encode(hasil));
            return android.util.Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (Exception e) { return null; }
    }

    public static long getCurrentUTCEpocTime(){
        long time = 0 ;
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            Date currentLocalTime = cal.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(df.format(currentLocalTime));
            time = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time ;
    }



    public static File getLatestFilefromDir(String dirPath){
        //Integer count = countFiles(new File(dirPath), 0);
        File dir = new File(dirPath);
        // getTotalFiles(dir);
        File lastModifiedFile=null;
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }


        for (int n = 0; n <files.length ; n++) {
            lastModifiedFile = files[n];
            if(lastModifiedFile.isDirectory()){
                File[]  fileInside=lastModifiedFile.listFiles();
                for (int i = 0; i < fileInside.length-1; i++) {
                    if (lastModifiedFile.lastModified() < fileInside[i].lastModified()) {
                        if(!lastModifiedFile.getPath().equalsIgnoreCase("/storage/emulated/0/Android/data/com.hurix.kitaboo.cloudreader/files/.cloudreaderbooks/348927193/lastvisited_page.png"))
                            lastModifiedFile = files[i];
                    }
                }
            }else {
                if (lastModifiedFile.lastModified() < files[n].lastModified()) {
                    if(lastModifiedFile.getPath().equalsIgnoreCase("/storage/emulated/0/Android/data/com.hurix.kitaboo.cloudreader/files/.cloudreaderbooks/348927193/lastvisited_page.png"))
                        lastModifiedFile = files[n];
                }
            }
        }

        return lastModifiedFile;
    }

    public static File getLastModifiedFile(File directory) {
        File[] files = directory.listFiles();
        if (files.length == 0) return null;
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File o1, File o2) {
                return new Long(o2.lastModified()).compareTo(o1.lastModified());
            }});
        return files[0];
    }


    public static Integer countFiles(File folder, Integer count) {
        File[] files = folder.listFiles();
        for (File file: files) {
            if (file.isFile()) {
                count++;
            } else {
                countFiles(file, count);
            }
        }

        return count;
    }
    public static File getTotalFiles(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getTotalFiles(file);
            } else {
                if(lastModifiedFile==null && !file.getPath().contains("lastvisited_page.png") ){
                    lastModifiedFile = file;
                } else if ( file !=null && !file.getPath().contains("lastvisited_page.png"))
                    if (lastModifiedFile.lastModified() < file.lastModified()) {
                        lastModifiedFile = file;
                    }
                mCount++;
            }
        }
        return lastModifiedFile;
    }

    public static int getDeviceHeight(Context context) {

        /*TODO RAVI, This is temporary solution, Needs to look in future   */
        if(context!=null){
            if (Build.MODEL.equalsIgnoreCase("SM-T835") || Build.MODEL.equalsIgnoreCase("SM-T595")) {
                int resource = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                if (resource > 0) {
                    statusBarHeight = context.getResources().getDimensionPixelSize(resource);
                }
            }
            return context.getResources().getDisplayMetrics().heightPixels + statusBarHeight;
        }else
            return 0;

    }

    public static int getDeviceWidth(Context context) {

        if(context!=null){
            return context.getResources().getDisplayMetrics().widthPixels ;
        }else
            return 0;

    }

    public static void adjustFontScale(Context context, Configuration configuration) {
        if (configuration.fontScale != 1) {
            configuration.fontScale = 1;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
        }
    }

    public static File getFileIfExists(String strFileName) {
        File returnFile = null;
        try {
            File directory = new File(Utils.getProfilePicFolderPathCompat());
            File[] fileList = directory.listFiles();

            if (fileList.length > 0) {
                for (File file : fileList) {
                    if (file.isFile()) {
                        int dotIndex = file.getName().lastIndexOf(".");
                        String fileName = file.getName().substring(0, dotIndex);
                        if (fileName.equalsIgnoreCase(strFileName)) {
                            returnFile = file;
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return returnFile;
    }

    /**
     * @param bitmap
     * @return bitmap (bitmap to string)
     */

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        return android.util.Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap StringToBitMap(String encodedString){
        try {
            return BitmapFactory.decodeByteArray(Base64.decode(encodedString,Base64.DEFAULT), 0, Base64.decode(encodedString,Base64.DEFAULT).length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static  Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
