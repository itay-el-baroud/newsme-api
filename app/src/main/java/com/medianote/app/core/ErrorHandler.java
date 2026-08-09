package com.medianote.app.core;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;
public class ErrorHandler {
    public static void show(View root, String msg) {
        if (root == null) return;
        try {
            Snackbar sb = Snackbar.make(root, msg, Snackbar.LENGTH_LONG);
            sb.setBackgroundTint(0xFF121212);
            sb.setTextColor(0xFFFFFFFF);
            sb.show();
        } catch (Exception ignored) {}
    }
}
