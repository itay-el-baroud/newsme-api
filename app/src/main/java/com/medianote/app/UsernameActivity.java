package com.medianote.app;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class UsernameActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        EditText et = findViewById(R.id.et_username);
        findViewById(R.id.btn_save_username).setOnClickListener(v -> {
            String name = et.getText().toString().trim();
            if(name.isEmpty()){
                Toast.makeText(this, "اكتب اسمك", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences p = getSharedPreferences("app_prefs", MODE_PRIVATE);
            p.edit().putString("username", name).apply();
            startActivity(new Intent(this, UsersActivity.class));
            finish();
        });
    }
}
