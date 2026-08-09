package com.medianote.app.core;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import com.medianote.app.R;
public class NotificationHelper {
    public static void show(Context c, String title, String msg) {
        try {
            NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel ch = new NotificationChannel("main_channel", "Main", NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(ch);
            android.app.Notification notif = new NotificationCompat.Builder(c, "main_channel")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .build();
            nm.notify(1, notif);
        } catch (Exception ignored) {}
    }
}
