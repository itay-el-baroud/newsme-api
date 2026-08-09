package com.medianote.app.core;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import androidx.lifecycle.MutableLiveData;
public class NetworkMonitor {
    public static MutableLiveData<Boolean> isConnected = new MutableLiveData<>(true);
    public static void init(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override public void onAvailable(Network n) { isConnected.postValue(true); }
            @Override public void onLost(Network n) { isConnected.postValue(false); }
        });
    }
}
