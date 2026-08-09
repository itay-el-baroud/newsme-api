package com.medianote.app;
import android.app.Application;
import com.medianote.app.core.CrashHandler;
import com.medianote.app.core.NetworkMonitor;
import com.medianote.app.core.PrefManager;
public class MediaNoteApp extends Application {
    @Override public void onCreate() {
        super.onCreate();
        PrefManager.init(this);
        CrashHandler.init(this);
        NetworkMonitor.init(this);
    }
}
