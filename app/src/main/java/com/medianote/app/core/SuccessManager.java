package com.medianote.app.core;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;
public class SuccessManager {
    public static void show(View root, String msg) {
        if (root == null) return;
        try {
            Snackbar sb = Snackbar.make(root, msg, Snackbar.LENGTH_SHORT);
            sb.setBackgroundTint(0xFF4CAF50);
            sb.setTextColor(0xFF000000);
            sb.show();
        } catch (Exception ignored) {}
    }
}
