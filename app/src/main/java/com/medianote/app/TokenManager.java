package com.medianote.app;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "medianote_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public boolean hasToken() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }

    public void clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply();
    }
}
