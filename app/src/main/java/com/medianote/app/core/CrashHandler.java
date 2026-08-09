package com.medianote.app.core;
import android.content.Context;
import java.io.File;
import java.io.PrintWriter;
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Context ctx;
    public static void init(Context c) { Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(c)); }
    public CrashHandler(Context c) { ctx = c; }
    @Override public void uncaughtException(Thread t, Throwable e) {
        try {
            File f = new File(ctx.getExternalFilesDir(null), "crash_log.txt");
            PrintWriter pw = new PrintWriter(f);
            e.printStackTrace(pw);
            pw.close();
        } catch (Exception ignored) {}
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
