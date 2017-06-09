package com.teralser.weatherapp.utils;

import android.util.Log;

import com.teralser.weatherapp.BuildConfig;

public class Logger {

    public static void logd(String tag, String str) {
        if (!BuildConfig.DEBUG)
            return;
        if (str.length() > 4000) {
            Log.d(tag, str.substring(0, 4000));
            logd(tag, str.substring(4000));
        } else {
            Log.d(tag, str);
        }
    }

    public static void loge(String tag, String str) {
        if (!BuildConfig.DEBUG)
            return;
        if (str.length() > 4000) {
            Log.e(tag, str.substring(0, 4000));
            loge(tag, str.substring(4000));
        } else {
            Log.e(tag, str);
        }
    }
}
