package stud.elka.umik_final.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import stud.elka.umik_final.R;

/**
 * Created by Łukasz on 19.12.2017.
 */

public class BluetoothDataReceiver extends BroadcastReceiver {

    private static final String TAG = "BluetoothDataReceiver";
    private static final String NOTIFICATION_CHANNEL = "Notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "In onReceive method");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_electronics)
                        .setContentTitle(context.getText(R.string.notification_title))
                        .setContentText(context.getText(R.string.notification_text));
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /*mNotificationManager.createNotificationChannel(new NotificationChannel(
                NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT));
        List<NotificationChannel> nChannels = mNotificationManager.getNotificationChannels();
        Log.d(TAG, "Size: " + nChannels.size());
        for (NotificationChannel n : nChannels) {
            Log.d(TAG, "Notification channel: " + n.getId());
        }*/
        int mNotificationID = 1;
        mNotificationManager.notify(mNotificationID, mBuilder.build());
        Log.d(TAG, "Notification should have shown");
    }
}
