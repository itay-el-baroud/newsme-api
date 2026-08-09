package com.medianote.app.core;
import android.app.Activity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class PermissionManager {
    public static boolean has(Activity a, String perm) { return ContextCompat.checkSelfPermission(a, perm) == 0; }
    public static void request(Activity a, String perm, int code) { ActivityCompat.requestPermissions(a, new String[]{perm}, code); }
}
