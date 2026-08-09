package com.medianote.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private View btnBack;
    private LinearLayout errorLayout;
    private ImageView imgErrorIcon;
    private TextView txtErrorTitle;
    private TextView txtErrorMessage;
    private Button btnRetry;

    private final String LOGIN_URL = "https://media-note.ct.ws/login.php";
    private final String SUCCESS_SCHEME = "myapp://auth-success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = prefs.getString("auth_token", null);
        String role = prefs.getString("user_role", null);
        String username = prefs.getString("username", null);

        if (token != null) {
            Intent i;
            if (username != null) i = new Intent(this, UsersActivity.class);
            else if (role != null) i = new Intent(this, UsernameActivity.class);
            else i = new Intent(this, RoleSelectionActivity.class);
            startActivity(i);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        errorLayout = findViewById(R.id.errorLayout);
        imgErrorIcon = findViewById(R.id.imgErrorIcon);
        txtErrorTitle = findViewById(R.id.txtErrorTitle);
        txtErrorMessage = findViewById(R.id.txtErrorMessage);
        btnRetry = findViewById(R.id.btnRetry);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith(SUCCESS_SCHEME)) {
                    String myToken = Uri.parse(url).getQueryParameter("token");
                    if (myToken != null) {
                        prefs.edit().putString("auth_token", myToken).apply();
                        startActivity(new Intent(LoginActivity.this, RoleSelectionActivity.class));
                        finish();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                // لما الصفحة تحمل بنجاح اخفي صفحة الخطأ
                if (errorLayout.getVisibility() == View.VISIBLE) {
                    errorLayout.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // ده التعديل المهم: متظهرش صفحة مفيش انترنت الا لو الخطأ في الصفحة الرئيسية نفسها
                if (request.isForMainFrame()) {
                    showError();
                }
            }
        });

        btnRetry.setOnClickListener(v -> loadLogin());
        loadLogin();
    }

    private void loadLogin() {
        if (!isNetworkAvailable()) {
            showError();
            return;
        }
        errorLayout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(LOGIN_URL);
    }

    private void showError() {
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
