package com.hurix.kitaboocloud.kitaboosdkrenderer.sdkUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.hurix.kitaboocloud.kitaboosdkrenderer.AudioBookActivity;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private AppCompatActivity appCompatActivity;

    public NetworkChangeReceiver(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

                if (isOnline(context) ) {
                    if(!isInitialStickyBroadcast()){
                        if (appCompatActivity instanceof AudioBookActivity)
                            ((AudioBookActivity) appCompatActivity).onConnectivityChange(true);
                    }
                    Log.e("keshav", "Online Connect Intenet ");
                } else {
                    ((AudioBookActivity) appCompatActivity).onConnectivityChange(false);
                    Log.e("keshav", "Conectivity Failure !!! ");
                }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}