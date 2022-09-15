package com.hl.sun;

import android.app.Application;
import android.content.Context;

import com.hl.weblib.PackageManager;

/**
 * Function:
 * Date:2022/9/15
 * Author: sunHL
 */
public class SunApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        PackageManager.getInstance().init(context);
    }
}
