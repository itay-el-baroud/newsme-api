package com.medianote.app;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;

public class TokenManager {
    private static final String PREF_NAME = "medianote_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ACCOUNTS = "accounts_list";
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
        prefs.edit().remove(KEY_TOKEN).remove(KEY_USER_NAME).remove(KEY_USER_ID).apply();
    }

    public void saveUserData(String name, String id) {
        prefs.edit().putString(KEY_USER_NAME, name).putString(KEY_USER_ID, id).apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public void saveAccount(String token, String name, String id) {
        try {
            String json = prefs.getString(KEY_ACCOUNTS, "[]");
            JSONArray array = new JSONArray(json);
            boolean found = false;
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.optString("token").equals(token)) {
                    obj.put("name", name);
                    obj.put("id", id);
                    found = true;
                    break;
                }
            }
            if (!found) {
                JSONObject newObj = new JSONObject();
                newObj.put("token", token);
                newObj.put("name", name);
                newObj.put("id", id);
                array.put(newObj);
            }
            prefs.edit().putString(KEY_ACCOUNTS, array.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getAccounts() {
        try {
            String json = prefs.getString(KEY_ACCOUNTS, "[]");
            return new JSONArray(json);
        } catch (Exception e) {
            return new JSONArray();
        }
    }
}
