package equipbid.armentum.com.equip_bid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Darshan on 12/14/2017.
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Receiver", "Localdata");
        if  ("Localdata".equals(intent.getAction()))
        {
            Log.e("Receiver", "Localdata");
        }
    }
}
