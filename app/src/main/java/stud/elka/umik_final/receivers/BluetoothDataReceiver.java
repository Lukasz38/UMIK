package stud.elka.umik_final.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import stud.elka.umik_final.R;
import stud.elka.umik_final.communication.Data;

/**
 * Handles the data received from BLE device.
 */

public class BluetoothDataReceiver extends BroadcastReceiver {

    private static final String TAG = "BluetoothDataReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
    
        Data data = (Data) intent.getSerializableExtra("data");
        if(data != null) {
            Log.d(TAG, "Data received: " + data);
            
            NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_electronics)
                        .setContentTitle(data.getName() + "[" + data.getLocation() + "]");
                        .setContentText(data + "");
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            
            int mNotificationID = data.getSensorId();
            mNotificationManager.notify(mNotificationID, mBuilder.build());
            Log.d(TAG, "Notification should have shown");
            return;
        } 
        
        InfoData infoData = (InfoData) intent.getSerializableExtra("data");
        if(infoData != null) {
            //TODO show toast
        }
    }
}
