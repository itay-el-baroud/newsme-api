package com.medianote.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            String token = prefs.getString("auth_token", null);
            String role = prefs.getString("user_role", null);
            String username = prefs.getString("username", null);

            Intent intent;

            if (token == null) {
                // مفيش تسجيل دخول خالص
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            } else if (role == null) {
                // سجل دخول بس لسه مختارش نوع الخدمة
                intent = new Intent(SplashActivity.this, RoleSelectionActivity.class);
            } else if (username == null) {
                // اختار النوع بس لسه محطش اسمه
                intent = new Intent(SplashActivity.this, UsernameActivity.class);
            } else {
                // كل حاجة كاملة يدخل على قايمة المستخدمين
                intent = new Intent(SplashActivity.this, UsersActivity.class);
            }

            startActivity(intent);
            finish();

        }, 1200); // ثانية واحدة انتظار
    }
}
