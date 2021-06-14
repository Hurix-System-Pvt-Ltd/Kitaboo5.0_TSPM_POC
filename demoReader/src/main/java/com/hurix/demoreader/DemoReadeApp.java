package com.hurix.demoreader;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.hurix.bookreader.views.audiobook.theme.AudioVideoBookThemeController;
import com.hurix.commons.KitabooSDK;
import com.hurix.kitaboo.constants.Constants;
import com.hurix.kitaboocloud.firebase.FirebaseAnalyticsEvents;
import com.hurix.kitaboocloud.sdkreadertheme.InsightReaderThemeController;

public class DemoReadeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Default SDK initialization
        MultiDex.install(this);
        KitabooSDK.setRootFolder(Constants.ROOTFOLDER);
        KitabooSDK.initializeSDK(this, getString(R.string.clientid));
        KitabooSDK.setTheme(this.getResources()
                .getString(R.string.kitaboo_theme_file_name));
        KitabooSDK.logEnable(true);
        KitabooSDK.setServiceUrl(getString(R.string.server_pointing));
        KitabooSDK.setApplicationDetails(getString(R.string.app_name),
                getString(R.string.app_version_name));

        try {
            //FirebaseAnalyticsEvents.INSTANCE.initializeFirebaseObject(this);
            InsightReaderThemeController.getInstance(getApplicationContext()).parseInsightReaderTheme();
            AudioVideoBookThemeController.getInstance(getApplicationContext()).parseAudioVideoBookTheme(getString(com.hurix.kitaboocloud.R.string.reader_theme_json_5_0));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
