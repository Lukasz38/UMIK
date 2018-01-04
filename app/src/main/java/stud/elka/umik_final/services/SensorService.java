package stud.elka.umik_final.services;

import android.app.Service;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import stud.elka.umik_final.communication.RemoteDevice;
import stud.elka.umik_final.db.DatabaseHelper;
import stud.elka.umik_final.entities.Sensor;

/** 
 * Service for listening for the data incoming from the BLE device.
 * Enables to send data to the BLE device as well.
 */
public class SensorService extends Service {

    private static final String TAG = "SensorService";
    
    // Number of currently running services.
    private static int runningServices = 0;

    private IBinder binder = new MyLocalBinder();
    private List<RemoteDevice> remoteDevices = new ArrayList<>();

    public SensorService() { Log.d(TAG,"Created."); }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand");
        addSensors();
        connectSensors();
        startTimer();
        runningServices++;
        Log.i(TAG, "Service launched. Running services: " + runningServices);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent("stud.elka.umik_final.RestartSensor");
        sendBroadcast(broadcastIntent);
        disconnectSensors();
        stopTimer();
        runningServices--;
        Log.i(TAG, "Service destroyed. Running services: " + runningServices);
    }

    public void addSensors()
    {
        BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        if(mBluetoothManager.getAdapter() != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            List<Sensor> sensors = dbHelper.getAllSensors();
            dbHelper.close();
            Log.d(TAG, "Adding remote devices...");
            for(Sensor sensor : sensors) {
                RemoteDevice mRemoteDevice = new RemoteDevice(getApplicationContext(), mBluetoothManager, sensor.getMacAddress());
                remoteDevices.add(mRemoteDevice);
                Log.d(TAG, "Remote device added: " + sensor.getMacAddress());
            }
        } else {
            Log.d(TAG, "No bluetooth adapter!");
        }
    }

    public void connectSensors()
    {
        Log.d(TAG, "Connecting sensors... Number of remote devices: " + remoteDevices.size());
        for(RemoteDevice mRemoteDevice : remoteDevices) {
            mRemoteDevice.connect();
            if(mRemoteDevice.isConnected()) {
                Log.d(TAG, "Sensor connected.");
            }
        }
    }

    public void disconnectSensors()
    {
        for(RemoteDevice mRemoteDevice : remoteDevices) {
            mRemoteDevice.disconnect();
        }
    }

    public RemoteDevice getRemoteDevice(String mac) {
        for(RemoteDevice rd : remoteDevices) {
            if(rd.getMacAddress().equals(mac)) {
                return rd;
            }
        }
        return null;
    }

    public List<RemoteDevice> getRemoteDevices() {
        return remoteDevices;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // Only for testing purposes
    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();

        timer.schedule(timerTask, 1000, 3000);
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                RemoteDevice rd = getRemoteDevice(RemoteDevice.DEFAULT_DEVICE_ADDRESS);
                if(!rd.isConnected()) {
                    rd.connect()
                }
                Log.d("Timer", "Is RD connected?: " + rd.isConnected());
            }
        };
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public class MyLocalBinder extends Binder {
        public SensorService getService() {
            return SensorService.this;
        }
    }
}
