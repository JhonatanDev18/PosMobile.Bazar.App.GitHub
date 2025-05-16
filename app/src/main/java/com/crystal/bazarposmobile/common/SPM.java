package com.crystal.bazarposmobile.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

//Guardar variable de sesion de la aplicacion
public class SPM {
    private static final String APP_SETTINGS_FILE = "BAZAR_POS_MOBILE_CRYSTAL";

    private SPM() {}

    private static SharedPreferences getSharedPreferences() {
        return MyApp.getContext()
                .getSharedPreferences(APP_SETTINGS_FILE, Context.MODE_PRIVATE);
    }

    @SuppressLint({"ApplySharedPref"})
    public static void setObject(String key, Object obj) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        Gson gson = new Gson();
        String json = gson.toJson(obj);

        editor.putString(key, json);
        editor.apply();
    }

    public static Object getObject(String key, Class<?> clazz) {
        String json = getSharedPreferences().getString(key, null);

        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static void setString(String dataLabel, String dataValue) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(dataLabel, dataValue);
        editor.commit();
    }

    public static String getString(String dataLabel) {
        return getSharedPreferences().getString(dataLabel, null);
    }

    public static void setBoolean(String dataLabel, boolean dataValue) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(dataLabel, dataValue);
        editor.commit();
    }

    public static boolean getBoolean(String dataLabel) {
        return getSharedPreferences().getBoolean(dataLabel,false);
    }

    public static void setInt(String dataLabel, int dataValue) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(dataLabel, dataValue);
        editor.commit();
    }

    public static int getInt(String dataLabel) {
        return getSharedPreferences().getInt(dataLabel,0);
    }
}
