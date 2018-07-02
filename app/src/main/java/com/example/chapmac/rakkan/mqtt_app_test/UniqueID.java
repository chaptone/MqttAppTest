package com.example.chapmac.rakkan.mqtt_app_test;

import android.content.Context;
import android.provider.Settings;

public class UniqueID {

    public static String getId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
    }
}
