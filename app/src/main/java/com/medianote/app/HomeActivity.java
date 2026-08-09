package com.medianote.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    private TokenManager tokenManager;
    private TextView txtName, txtId, txtToken;
    private Button btnLogout;
    private ProgressBar progressBar;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tokenManager = new TokenManager(this);
        txtName = findViewById(R.id.txtName);
        txtId = findViewById(R.id.txtId);
        txtToken = findViewById(R.id.txtToken);
        btnLogout = findViewById(R.id.btnLogout);
        progressBar = findViewById(R.id.progressBarHome);
        client = new OkHttpClient();

        String savedName = tokenManager.getUserName();
        String savedId = tokenManager.getUserId();
        String token = tokenManager.getToken();

        if (savedName != null) {
            txtName.setText("الاسم: " + savedName);
        } else {
            txtName.setText("الاسم: يتم التحميل...");
        }

        if (savedId != null) {
            txtId.setText("ID: " + savedId);
        } else {
            txtId.setText("ID: يتم التحميل...");
        }

        if (token != null) {
            txtToken.setText("البيانات محفوظة للفتح القادم");
            fetchProfile(token);
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

    private void fetchProfile(String token) {
        progressBar.setVisibility(View.VISIBLE);
        Request request = new Request.Builder()
                .url("https://media-note.ct.ws/profile.php")
                .header("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (tokenManager.getUserName() == null) {
                        txtName.setText("الاسم: غير متاح بدون انترنت");
                        txtId.setText("ID: غير متاح بدون انترنت");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body() != null ? response.body().string() : "";
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                if (response.isSuccessful()) {
                    try {
                        JSONObject root = new JSONObject(body);
                        JSONObject data = root;
                        if (root.has("data") && root.get("data") instanceof JSONObject) {
                            data = root.getJSONObject("data");
                        }
                        String id = extractField(data, new String[]{"id", "user_id", "userId", "uid"});
                        String name = extractField(data, new String[]{"name", "username", "full_name", "user_name", "display_name"});
                        if (name == null) name = "مستخدم";
                        if (id == null) id = "غير معروف";
                        String finalName = name;
                        String finalId = id;
                        runOnUiThread(() -> {
                            txtName.setText("الاسم: " + finalName);
                            txtId.setText("ID: " + finalId);
                            txtToken.setText("تم حفظ الحساب، سيفتح تلقائيا المرة القادمة");
                            tokenManager.saveUserData(finalName, finalId);
                            tokenManager.saveAccount(token, finalName, finalId);
                        });
                    } catch (Exception ex) {
                        runOnUiThread(() -> Toast.makeText(HomeActivity.this, "شكل البيانات غير متوقع", Toast.LENGTH_SHORT).show());
                    }
                } else if (response.code() == 401) {
                    runOnUiThread(() -> {
                        Toast.makeText(HomeActivity.this, "التوكن منتهي، سجل دخول تاني", Toast.LENGTH_SHORT).show();
                        tokenManager.clearToken();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    });
                }
            }
        });
    }

    private String extractField(JSONObject obj, String[] keys) {
        for (String key : keys) {
            if (obj.has(key) && !obj.isNull(key)) {
                try {
                    return obj.getString(key);
                } catch (Exception e) {
                    try {
                        return String.valueOf(obj.getInt(key));
                    } catch (Exception ex) {}
                }
            }
        }
        return null;
    }
}
