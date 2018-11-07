package equipbid.armentum.com.equip_bid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class UnitechReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("sfdfdff", "unitech.scanservice.data!");
        if  ("unitech.scanservice.data" .equals(intent.getAction()))
        {
            Log.e("sfdfdff", "unitech.scanservice.data!");
            System.out.println("unitech.scanservice.data!");
            Bundle bundle = intent.getExtras();
            if  (bundle != null ) {
                String text = bundle.getString("text" );
                Log.e("text", text);
                New_Lot_Entry inst = New_Lot_Entry.instance();
                if(inst != null)
                    inst.setViewText(text);
            }
        }
    }
}
