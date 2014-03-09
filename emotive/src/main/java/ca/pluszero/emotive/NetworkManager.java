package ca.pluszero.emotive;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chronos on 2014-03-08.
 */
public class NetworkManager {

    public static boolean isConnected(Context ctx) {
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
