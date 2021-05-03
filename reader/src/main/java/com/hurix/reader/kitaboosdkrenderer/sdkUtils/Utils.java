package com.hurix.reader.kitaboosdkrenderer.sdkUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hurix.downloadbook.controller.UserController;
import com.hurix.reader.kitaboosdkrenderer.constants.Constants;

import java.io.File;


/**
 * Created by lopamudra.mohanty on 3/2/2016.
 */
public class Utils {
    private static String mFileExtension, mFilePath;
    static Bitmap mBitmap = null;



    public static boolean checkInternetConnection(Context context) {
        boolean result = false;
        boolean checkWifi = checkWIFI(context);
        boolean checkGPRS = checkGPRS(context);
        result = checkWifi || checkGPRS;
        return result;
    }



    public static void deleteProfilePic(Context context, boolean backUp) {
        String profilePicPath = com.hurix.reader.kitaboosdkrenderer.utils.Utils.getProfilePicFolderPathCompat(context);
        File file = null;
        if (backUp) {
            file = new File(profilePicPath + UserController.getInstance(context).getUserVO()
                    .getUserID() + "_" + Constants.PROFILE_PICS_BACKUP + Constants.PROFILE_IMAGE_EXTENSION);
        } else {
            file = new File(profilePicPath + UserController.getInstance(context).getUserVO()
                    .getUserID() + Constants.PROFILE_IMAGE_EXTENSION);
        }
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public static void restorePreviousProfilePic(Context context) {
        String profilePicPath = com.hurix.reader.kitaboosdkrenderer.utils.Utils.getProfilePicFolderPathCompat(context);
        File backupFile = new File(profilePicPath + UserController.getInstance(context).getUserVO()
                .getUserID() + "_" + Constants.PROFILE_PICS_BACKUP + Constants.PROFILE_IMAGE_EXTENSION);
        File file = new File(profilePicPath + UserController.getInstance(context).getUserVO()
                .getUserID() + Constants.PROFILE_IMAGE_EXTENSION);
        if (backupFile.exists()) {
            backupFile.renameTo(file);
        }
    }



    private static boolean checkWIFI(Context con) {
        WifiManager wifi = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
        wifi.getConnectionInfo();
        ConnectivityManager conMan = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifiState = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        boolean connected = (wifiState == NetworkInfo.State.CONNECTED);
        return connected;
    }

    private static boolean checkGPRS(Context context) {
        try {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo.State wifiState = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            boolean connected = wifiState == NetworkInfo.State.CONNECTED;
            return connected;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }






    public static int dpToPx(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }



    private static SharedPreferences getSharedPreferences(Context context, String shelfPrefsName, int mode) {
        return context.getSharedPreferences(shelfPrefsName, mode);
    }
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
}
