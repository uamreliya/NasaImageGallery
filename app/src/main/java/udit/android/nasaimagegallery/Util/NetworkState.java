package udit.android.nasaimagegallery.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static udit.android.nasaimagegallery.GalleryView.MainActivity.networkError;

public class NetworkState extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (getConnectionStatus(context)) {
                networkError(true);
            } else {
                networkError(false);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean getConnectionStatus(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            return (networkInfo != null && networkInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
