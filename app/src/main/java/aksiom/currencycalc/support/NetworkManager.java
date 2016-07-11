package aksiom.currencycalc.support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Aksiom on 6/30/2016.
 */
public class NetworkManager {

    Context context;

    public NetworkManager(Context context) {
        this.context = context;
    }

    public boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

}
