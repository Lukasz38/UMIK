package stud.elka.umik_final;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import stud.elka.umik_final.communication.RemoteDevice;
import stud.elka.umik_final.entities.Sensor;
import stud.elka.umik_final.services.SensorService;

/**
 * Created by Łukasz on 26.12.2017.
 */

public class ConfigFragment extends Fragment {

    private static final String TAG = "ConfigFragment";

    private EditText mEditText;
    private Button mButton;
    private SensorService sensorService;
    private boolean isBound = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.config_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Sensor sensor = MainActivity.getSelectedSensor();
        if (sensor == null) {
            Toast.makeText(getActivity(), R.string.no_sensor_toast, Toast.LENGTH_LONG);
            Fragment fragment = new SensorList();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment);
            fragmentTransaction.commit();
        } else {
            getActivity().setTitle(MainActivity.getSelectedSensor().getName());
        }

        mEditText = (EditText) getActivity().findViewById(R.id.config_value);
        mButton = (Button) getActivity().findViewById(R.id.send_button);

        getActivity().bindService(new Intent(getActivity(), SensorService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isBound) {
                    if (sensorService.getRemoteDevices().size() == 0) {
                        Toast.makeText(getActivity(),
                                "No remote devices!", Toast.LENGTH_SHORT).show();

                    } else {
                        RemoteDevice remoteDevice = sensorService.getRemoteDevice(
                                MainActivity.getSelectedSensor().getMacAddress());
                        Log.d(TAG, "remoteDevice mac: " + remoteDevice.getMacAddress());
                        if (remoteDevice == null) {
                            Log.e(TAG, "No remote device found.");
                            return;
                        }
                        if (!remoteDevice.isConnected()) {
                            remoteDevice.connect();
                            SystemClock.sleep(2000);
                            if (!remoteDevice.isConnected()) {
                                Log.d(TAG, "Remote device not connected");
                                Toast.makeText(getActivity(), "Remote device not connected. Probably out of reach.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        //TODO
                        boolean result = remoteDevice.sendConfig("PUT:600:200");

                        // boolean result = remoteDevice.sendConfig("GET:100");
                        //new ConfigData(Integer.valueOf(mEditText.getText().toString())));
                        String message = "Data sent with result: " + result;
                        Toast.makeText(getActivity(),
                                message, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    String message = "Service not bound";
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button freqButton = (Button) getActivity().findViewById(R.id.freq);
        Button resetButton = (Button) getActivity().findViewById(R.id.reset);

        freqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isBound) {
                    if (sensorService.getRemoteDevices().size() == 0) {
                        Toast.makeText(getActivity(),
                                "No remote devices!", Toast.LENGTH_SHORT).show();

                    } else {
                        RemoteDevice remoteDevice = sensorService.getRemoteDevice(
                                MainActivity.getSelectedSensor().getMacAddress());
                        Log.d(TAG, "remoteDevice mac: " + remoteDevice.getMacAddress());
                        if (remoteDevice == null) {
                            Log.e(TAG, "No remote device found.");
                            return;
                        }
                        if (!remoteDevice.isConnected()) {
                            remoteDevice.connect();
                            SystemClock.sleep(2000);
                            if (!remoteDevice.isConnected()) {
                                Log.d(TAG, "Remote device not connected");
                                Toast.makeText(getActivity(), "Remote device not connected. Probably out of reach.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        //TODO
                        boolean result = remoteDevice.sendConfig("GET:100");
                        //new ConfigData(Integer.valueOf(mEditText.getText().toString())));
                        String message = "Data sent with result: " + result;
                        Toast.makeText(getActivity(),
                                message, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    String message = "Service not bound";
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isBound) {
                    if (sensorService.getRemoteDevices().size() == 0) {
                        Toast.makeText(getActivity(),
                                "No remote devices!", Toast.LENGTH_SHORT).show();

                    } else {
                        RemoteDevice remoteDevice = sensorService.getRemoteDevice(
                                MainActivity.getSelectedSensor().getMacAddress());
                        Log.d(TAG, "remoteDevice mac: " + remoteDevice.getMacAddress());
                        if (remoteDevice == null) {
                            Log.e(TAG, "No remote device found.");
                            return;
                        }
                        if (!remoteDevice.isConnected()) {
                            remoteDevice.connect();
                            SystemClock.sleep(2000);
                            if (!remoteDevice.isConnected()) {
                                Log.d(TAG, "Remote device not connected");
                                Toast.makeText(getActivity(), "Remote device not connected. Probably out of reach.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        //TODO
                        boolean result = remoteDevice.sendConfig("PUT:900");
                        //new ConfigData(Integer.valueOf(mEditText.getText().toString())));
                        String message = "Data sent with result: " + result;
                        Toast.makeText(getActivity(),
                                message, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    String message = "Service not bound";
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SensorService.MyLocalBinder myLocalBinder = (SensorService.MyLocalBinder) iBinder;
            sensorService = myLocalBinder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };
}
