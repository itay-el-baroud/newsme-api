package com.medianote.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private ImageButton btnBack;
    private LinearLayout errorLayout;
    private ImageView imgErrorIcon;
    private TextView txtErrorTitle;
    private TextView txtErrorMessage;
    private Button btnRetry;
    private TokenManager tokenManager;
    private String lastUrl = "https://media-note.ct.ws/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tokenManager = new TokenManager(this);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        errorLayout = findViewById(R.id.errorLayout);
        imgErrorIcon = findViewById(R.id.imgErrorIcon);
        txtErrorTitle = findViewById(R.id.txtErrorTitle);
        txtErrorMessage = findViewById(R.id.txtErrorMessage);
        btnRetry = findViewById(R.id.btnRetry);
        setupWebView();
        setupBackButton();
        setupRetry();
        webView.loadUrl(lastUrl);
    }

    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("myapp://auth-success")) {
                    handleAuthSuccess(url);
                    return true;
                }
                lastUrl = url;
                return false;
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("myapp://auth-success")) {
                    handleAuthSuccess(url);
                    return true;
                }
                lastUrl = url;
                return false;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                lastUrl = url;
                updateBackButton();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                updateBackButton();
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    int code = error.getErrorCode();
                    if (code == WebViewClient.ERROR_HOST_LOOKUP || code == WebViewClient.ERROR_CONNECT || code == WebViewClient.ERROR_TIMEOUT) {
                        showCustomError(0);
                    } else {
                        showCustomError(3);
                    }
                }
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                lastUrl = failingUrl;
                if (errorCode == WebViewClient.ERROR_HOST_LOOKUP || errorCode == WebViewClient.ERROR_CONNECT || errorCode == WebViewClient.ERROR_TIMEOUT) {
                    showCustomError(0);
                } else {
                    showCustomError(3);
                }
            }
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                if (request.isForMainFrame()) {
                    int status = errorResponse.getStatusCode();
                    if (status == 404) {
                        showCustomError(1);
                    } else if (status >= 500) {
                        showCustomError(2);
                    } else {
                        showCustomError(3);
                    }
                }
            }
        });
    }

    private void showCustomError(int type) {
        runOnUiThread(() -> {
            webView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            if (type == 0) {
                imgErrorIcon.setImageResource(R.drawable.ic_no_internet);
                txtErrorTitle.setText("لا يوجد اتصال بالانترنت");
                txtErrorMessage.setText("تأكد ان الموبايل متوصل بالانترنت وحاول مرة تانية");
            } else if (type == 1) {
                imgErrorIcon.setImageResource(R.drawable.ic_error);
                txtErrorTitle.setText("الصفحة غير متاحة");
                txtErrorMessage.setText("الرابط اللي بتحاول تفتحه مش موجود حاليا");
            } else if (type == 2) {
                imgErrorIcon.setImageResource(R.drawable.ic_error);
                txtErrorTitle.setText("خطأ في الخادم");
                txtErrorMessage.setText("السيرفر فيه مشكلة مؤقتة، جرب كمان شوية");
            } else {
                imgErrorIcon.setImageResource(R.drawable.ic_error);
                txtErrorTitle.setText("حدث خطأ غير متوقع");
                txtErrorMessage.setText("حصلت مشكلة اثناء تحميل الصفحة، دوس اعادة محاولة");
            }
        });
    }

    private void hideError() {
        errorLayout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
    }

    private void handleAuthSuccess(String url) {
        try {
            Uri uri = Uri.parse(url);
            String token = uri.getQueryParameter("token");
            if (token != null && !token.isEmpty()) {
                tokenManager.saveToken(token);
                Toast.makeText(this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            showCustomError(3);
        }
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        });
    }

    private void setupRetry() {
        btnRetry.setOnClickListener(v -> {
            hideError();
            webView.loadUrl(lastUrl);
        });
    }

    private void updateBackButton() {
        if (webView.canGoBack()) {
            btnBack.setVisibility(View.VISIBLE);
        } else {
            btnBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            hideError();
            webView.loadUrl(lastUrl);
            return;
        }
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
                }
