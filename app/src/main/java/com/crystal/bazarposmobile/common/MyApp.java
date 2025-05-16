package com.crystal.bazarposmobile.common;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class MyApp extends Application {
    private static MyApp instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
