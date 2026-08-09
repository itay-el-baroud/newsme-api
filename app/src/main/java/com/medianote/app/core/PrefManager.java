package com.medianote.app.core;
import android.content.Context;
import android.content.SharedPreferences;
public class PrefManager {
    private static SharedPreferences prefs;
    public static void init(Context c) { prefs = c.getSharedPreferences("app_prefs", Context.MODE_PRIVATE); }
    public static void put(String key, String value) { prefs.edit().putString(key, value).apply(); }
    public static String get(String key) { return prefs.getString(key, null); }
    public static void remove(String key) { prefs.edit().remove(key).apply(); }
    public static void clear() { prefs.edit().clear().apply(); }
}
