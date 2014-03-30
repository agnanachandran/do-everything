package ca.pluszero.emotive.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by chronos on 2014-03-08.
 */
public class NetworkManager {

    public static boolean isConnected(Context ctx) {
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();
        if (!isConnected) {
            Toast.makeText(ctx, "Make sure you are connected to the internet.", Toast.LENGTH_LONG).show();
        }
        return isConnected;
    }
}
