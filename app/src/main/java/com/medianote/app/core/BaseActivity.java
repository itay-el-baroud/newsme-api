package com.medianote.app.core;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
public class BaseActivity extends AppCompatActivity {
    public LoadingManager loading = new LoadingManager();
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkMonitor.isConnected.observe(this, connected -> {
            if (connected != null && !connected) ErrorHandler.show(getRoot(), "لا يوجد اتصال بالانترنت");
        });
    }
    public View getRoot() { return findViewById(android.R.id.content); }
    public void showLoading() { loading.show(this); }
    public void hideLoading() { loading.hide(); }
    public void showError(String m) { ErrorHandler.show(getRoot(), m); }
    public void showSuccess(String m) { SuccessManager.show(getRoot(), m); }
}
