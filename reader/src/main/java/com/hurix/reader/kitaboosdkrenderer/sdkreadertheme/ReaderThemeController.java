package com.hurix.reader.kitaboosdkrenderer.sdkreadertheme;

import android.content.Context;

import com.google.gson.Gson;

import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.io.IOException;
import java.io.InputStream;

public class ReaderThemeController {

    private static ReaderThemeController mSingleInstance;
    private final Context context;
    private ReaderThemeSettingVo readerThemeSettingVo;


    private ReaderThemeController(Context context) {
        this.context = context;
    }

    public static ReaderThemeController getInstance(Context context) {
        if (mSingleInstance == null) {
            mSingleInstance = new ReaderThemeController(context);
        }

        return mSingleInstance;
    }

    public void parseInsightReaderTheme() {
        Gson gson = new Gson();
        String jsonStr = getAssetsJSON(context.getString(R.string.reader_theme_json_5_0));
        readerThemeSettingVo = gson.fromJson(jsonStr, ReaderThemeSettingVo.class);
        //String canvasBG = readerThemeSettingVo.getReader().getDayMode().getMain().getCanvasBackground();
    }

    public ReaderThemeSettingVo getReaderThemeUserSettingVo() {
        return readerThemeSettingVo;
    }

    public String getAssetsJSON(String fileName) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}