package stud.elka.umik_final.receivers;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import stud.elka.umik_final.R;
import stud.elka.umik_final.communication.Data;
import stud.elka.umik_final.communication.InfoData;
import stud.elka.umik_final.db.DatabaseHelper;
import stud.elka.umik_final.entities.Sensor;

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

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Sensor sensor = dbHelper.getSensor(data.getSensorID());
            dbHelper.close();

            NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_electronics)
                        .setContentTitle(sensor.getName() + "[" + sensor.getLocation() + "]")
                        .setContentText(data + "");
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            
            int mNotificationID = (int) data.getSensorID();
            mNotificationManager.notify(mNotificationID, mBuilder.build());
            Log.d(TAG, "Notification should have shown");
            return;
        }
    }
}
