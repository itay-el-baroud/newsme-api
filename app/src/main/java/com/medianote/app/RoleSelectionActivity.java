package com.medianote.app;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
public class RoleSelectionActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);
        findViewById(R.id.card_client).setOnClickListener(v -> save("client"));
        findViewById(R.id.card_driver).setOnClickListener(v -> save("driver"));
    }
    private void save(String role){
        getSharedPreferences("app_prefs", MODE_PRIVATE).edit().putString("user_role", role).apply();
        startActivity(new Intent(this, UsernameActivity.class));
        finish();
    }
}
