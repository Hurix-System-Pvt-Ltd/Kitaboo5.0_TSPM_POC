package com.hurix.reader.kitaboosdkrenderer.utils;

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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.constants.Constants;


import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class Utils {

    private final static String MY_PREFERENCES = "kitabooPrefs";
    private final static String TYPE_FACE = "typeface";
    public static final int PERMISSION_REQUEST_CODE = 1;
    public static int mCount = 0;
    public static File lastModifiedFile = null;
    private static int statusBarHeight = 0;

    public static String escapeString(String strplain) {
        String escapedString = "";
        try {
            escapedString = URLEncoder.encode(strplain, "UTF-8").replaceAll(
                    "\\+", "%20");
        } catch (Exception e) {
        }
        return escapedString;
    }

    public static boolean isMobile(Context context) {
        WebView ua = new WebView(context);
        String settiing = ua.getSettings().getUserAgentString();
        return settiing.contains("Mobile");
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
        } else if (errorcode == 401) {
            return context.getResources().getString(
                    R.string.not_purchased);
        } else if (errorcode == 403) {
            return context.getResources().getString(
                    R.string.not_purchased);
        } else if (errorcode == 500) {
            Spanned errorMsg = Html.fromHtml(context.getResources().getString(
                    R.string.error_on_file_access_content));
            return errorMsg.toString();
        } else if (errorcode == 601) {
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
            return context.getResources().getString(
                    R.string.server_book_licence_limit_reached);
        } else if (errorcode == 801) {
            return context.getResources().getString(
                    R.string.server_book_licence_expired);
        } else if (errorcode == 802) {
            return context.getResources().getString(
                    R.string.server_book_not_available);
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
            return context.getResources().getString(R.string.server_unknown_error);
        } else if (errorcode == 1) {
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


    public static String getStartDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String startdate = df.format(currentLocalTime);
        return startdate;
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


    public static String getProfilePicFolderPathCompat() {
        File file = new File(Constants.ALLBOOKROOTPATH + Constants.PROFILE_PICS_FOLDER);
        if (file.exists() == false) {
            file.mkdirs();
        }
        return Constants.ALLBOOKROOTPATH + Constants.PROFILE_PICS_FOLDER + File.separator;
    }

    public static String getProfilePicFolderPathCompat(Context mContext) {

        File file = new File(getAppDataFolder(mContext) + Constants.PROFILE_PICS_FOLDER);
        if (file.exists() == false) {
            file.mkdirs();
        }

        return getAppDataFolder(mContext) + Constants.PROFILE_PICS_FOLDER + File.separator;
    }


    public static String getAppDataFolder(Context context) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data" + "/" + context.getPackageName() + "/" + "files/" + Constants.ROOTFOLDER;
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


    public static long getTimeSpentInSecond(String StartDateTime, String EndTimeDateTime) {
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


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static GradientDrawable getRectAngleDrawable(int solidColor, float[] _radius, int strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(_radius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

    public static GradientDrawable getCircleDrawableWithStroke(int solidColor, int strokeColor, int strokeWidth) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

    public static GradientDrawable getProgressDrawable(int strokeColor, int strokeWidth) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.TRANSPARENT);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setGradientRadius((float) 2.5);


        return gradientDrawable;
    }


    private static void withinStyle(StringBuilder out, CharSequence text, int start, int end) {
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


    public static boolean canResolveIntent(Intent intent, Context context) {
        List<ResolveInfo> resolveInfo = context.getPackageManager()
                .queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    /**
     * Insert Boolean Value
     *
     * @param context context
     * @param key     Key
     * @param value   Boolean value
     * @return success
     */
    public static boolean insertSharedPreferenceBooleanValue(Context context, String key, boolean value) {
        if (context != null) {
            try {
                SharedPreferences pref = getSharedPreferences(context, Constants.PREFS_NAME, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(key, value);
                editor.commit();
                return true;
            } catch (Exception e) {
                if (Constants.IS_DEBUG_ENABLED) {
                    e.printStackTrace();
                }
            }
        }
        return false;
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

    /**
     * Get boolean value
     *
     * @param context      context
     * @param key          key
     * @param defaultValue default value
     * @return value
     */
    public static boolean getSharedPreferenceBooleanValue(Context context, String key, boolean defaultValue) {
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

        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public static boolean insertSharedPrefernceStringValues(Context context, String sharedPrefName, String key, String values) {
        try {
            SharedPreferences pref = getSharedPreferences(context, sharedPrefName, 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, values);
            editor.commit();
            return true;
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String getSharedPreferenceStringValue(Context context, String sharedPrefName, String key, String defaultvalue) {
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

        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
        return defaultvalue;
    }


    private static SharedPreferences getSharedPreferences(Context context, String shelfPrefsName, int mode) {
        return context.getSharedPreferences(shelfPrefsName, mode);
    }


    public static int getDeviceHeight(Context context) {

        /*TODO RAVI, This is temporary solution, Needs to look in future   */
        if (context != null) {
            if (Build.MODEL.equalsIgnoreCase("SM-T835") || Build.MODEL.equalsIgnoreCase("SM-T595")) {
                int resource = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                if (resource > 0) {
                    statusBarHeight = context.getResources().getDimensionPixelSize(resource);
                }
            }
            return context.getResources().getDisplayMetrics().heightPixels + statusBarHeight;
        } else
            return 0;

    }
}
