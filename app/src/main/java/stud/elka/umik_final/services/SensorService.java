package stud.elka.umik_final.services;

import android.app.Service;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import stud.elka.umik_final.communication.RemoteDevice;
import stud.elka.umik_final.entities.Sensor;

public class SensorService extends Service {

    private static final String TAG = "SensorService";
    private static int runningServices = 0;

    private IBinder binder = new MyLocalBinder();
    private List<RemoteDevice> remoteDevices = new ArrayList<>();

    public SensorService()
    {
        Log.d(TAG,"Created.");
    }

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
        //TODO add multiple sensors
        String macAddress = "88:4A:EA:8B:8B:CD";

        BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        if(mBluetoothManager.getAdapter() != null) {
            Log.d(TAG, "Adding remote device...");
            RemoteDevice mRemoteDevice = new RemoteDevice(getApplicationContext(), mBluetoothManager, macAddress);
            remoteDevices.add(mRemoteDevice);
            Log.d(TAG, "Sensor added.");
        } else {
            Log.d(TAG, "No bluetooth adapter!");
        }
    }

    public void connectSensors()
    {
        Log.d(TAG, "Connect sensors, remoteDevices number: " + remoteDevices.size());
        for(RemoteDevice mRemoteDevice : remoteDevices) {
            mRemoteDevice.connect();
            Log.d(TAG, "Sensor connected.");
        }
    }

    public void disconnectSensors()
    {
        for(RemoteDevice mRemoteDevice : remoteDevices) {
            mRemoteDevice.disconnect();
        }
    }

    public RemoteDevice getRemoteDevice(int id) {
        //TODO
        return remoteDevices.get(0);
    }

    public List<RemoteDevice> getRemoteDevices() {
        return remoteDevices;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //ONLY FOR TESTING PURPOSES
    //TODO delete
    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();

        timer.schedule(timerTask, 1000, 2000);
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.d("Timer", "I'm alive!");
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