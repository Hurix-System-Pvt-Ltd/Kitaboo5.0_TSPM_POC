package com.hurix.kitaboocloud.firebase

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.hurix.kitaboocloud.R

object FirebaseAnalyticsEvents {

    private lateinit var mContext: Context
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics


    @SuppressLint("MissingPermission")
    fun initializeFirebaseObject(context: Context) {
        mContext = context
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext)
    }

    fun getFirebaseObject(): FirebaseAnalytics = mFirebaseAnalytics

    fun sendFirebaseEvents(eventName: String, firebaseBundle: Bundle = Bundle()) {

        Log.e("Event", "" + eventName)
       if(this::mFirebaseAnalytics.isInitialized){
           firebaseBundle.putString("client_name", mContext.getString(R.string.app_name))
           mFirebaseAnalytics.logEvent(eventName, firebaseBundle)
       }



    }

    fun sendFireBaseNAEvents(eventName: String) {

        Log.e("Event", "" + eventName)
        if(this::mFirebaseAnalytics.isInitialized) {
            val firebaseBundle = Bundle()
            firebaseBundle.putString("client_name", mContext.getString(R.string.app_name))
            sendFirebaseEvents(eventName, firebaseBundle)
        }
    }


}