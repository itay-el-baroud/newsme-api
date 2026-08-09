package com.medianote.app.core;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import androidx.appcompat.app.AlertDialog;
import com.medianote.app.R;
public class LoadingManager {
    private Dialog dialog;
    public void show(Context c) {
        if (dialog != null && dialog.isShowing()) return;
        try {
            android.view.View v = LayoutInflater.from(c).inflate(R.layout.dialog_loading, null);
            dialog = new AlertDialog.Builder(c).setView(v).setCancelable(false).create();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        } catch (Exception ignored) {}
    }
    public void hide() { try { if (dialog != null) dialog.dismiss(); } catch (Exception ignored) {} }
}
