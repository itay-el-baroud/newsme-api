package com.medianote.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private TokenManager tokenManager;
    private TextView txtToken;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tokenManager = new TokenManager(this);
        txtToken = findViewById(R.id.txtToken);
        btnLogout = findViewById(R.id.btnLogout);
        String token = tokenManager.getToken();
        if (token != null) {
            txtToken.setText("التوكن:\n" + token);
        } else {
            txtToken.setText("لا يوجد توكن");
        }
        btnLogout.setOnClickListener(v -> {
            tokenManager.clearToken();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
