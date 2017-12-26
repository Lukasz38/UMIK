package stud.elka.umik_final.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import stud.elka.umik_final.services.SensorService;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SensorRestarter";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "SensorService stops!");
        context.startService(new Intent(context, SensorService.class));
        Log.d(TAG, "SensorService restarted.");
    }

}
