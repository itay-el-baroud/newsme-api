package com.medianote.app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private final String VERSION_URL = "https://media-note.ct.ws/version.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            checkForUpdate();
        }, 1200);
    }

    private void checkForUpdate() {
        new Thread(() -> {
            try {
                URL url = new URL(VERSION_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    JSONObject json = new JSONObject(sb.toString());
                    int latestCode = json.getInt("latest_version_code");
                    boolean force = json.getBoolean("force_update");
                    String updateUrl = json.getString("update_url");
                    String message = json.optString("message", "يلزم تحديث البرنامج");

                    int currentCode = getCurrentVersionCode();

                    if (latestCode > currentCode) {
                        runOnUiThread(() -> showForceUpdateDialog(message, updateUrl, force));
                        return; // موقفش ومتكملش دخول
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // لو حصل اي خطأ في النت او السيرفر كمل عادي بدون ما يعلق
            }
            runOnUiThread(this::goNext);
        }).start();
    }

    private int getCurrentVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    private void showForceUpdateDialog(String msg, String updateUrl, boolean force) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_force_update, null);
        TextView tvMsg = view.findViewById(R.id.tv_update_message);
        Button btnUpdate = view.findViewById(R.id.btn_update_now);
        tvMsg.setText(msg);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(!force)
                .create();

        if (force) dialog.setCanceledOnTouchOutside(false);

        btnUpdate.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
                startActivity(intent);
            } catch (Exception ignored) {}
            if (!force) {
                dialog.dismiss();
                goNext();
            }
        });

        dialog.show();
        if (force) {
            // لو تحديث اجباري متخلهوش يكمل
            return;
        }
    }

    private void goNext() {
        android.content.SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = prefs.getString("auth_token", null);
        String role = prefs.getString("user_role", null);
        String username = prefs.getString("username", null);

        Intent intent;
        if (token == null) intent = new Intent(this, LoginActivity.class);
        else if (role == null) intent = new Intent(this, RoleSelectionActivity.class);
        else if (username == null) intent = new Intent(this, UsernameActivity.class);
        else intent = new Intent(this, UsersActivity.class);

        startActivity(intent);
        finish();
    }
            }
