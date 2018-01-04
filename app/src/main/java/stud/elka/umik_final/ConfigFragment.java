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
 * View that enables configuration of the remote device.
 */

public class ConfigFragment extends Fragment {

    private static final String TAG = "ConfigFragment";
    
    private SensorService sensorService;
    private boolean isBound = false;

    private EditText freqEditText;
    private Button sendFreqButton;
    private EditText smallLeakRangeEditText;
    private EditText largeLeakRangeEditText;
    private Button sendLeakRangeButton;
    private Button resetButton;
    private Button getInfoButton;
    
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

        freqEditText = (EditText) getActivity().findViewById(R.id.freq_edit);
        sendFreqButton = (Button) getActivity().findViewById(R.id.send_freq_button);
        smallLeakRangeEditText = (EditText) getActivity().findViewById(R.id.small_leak_edit);
        largeLeakRangeEditText = (EditText) getActivity().findViewById(R.id.large_leak_edit);
        sendLeakRangeButton = (Button) getActivity().findViewById(R.id.send_leak_range_button);
        resetButton = (Button) getActivity().findViewById(R.id.reset_button);
        getInfoButton = (Button) getActivity().findViewById(R.id.get_info_button);

        getActivity().bindService(new Intent(getActivity(), SensorService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);

        sendFreqButton.setOnClickListener(sendFreqButtonHandler);
        sendLeakRangeButton.setOnClickListener(sendLeakRangeButtonHandler);
        resetButton.setOnClickListener(resetButtonHandler);
        getInfoButton.setOnClickListener(getInfoButtonHandler);
    }

    View.OnClickListener sendFreqButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String freq = freqEditText.getText().toString();
            String message = ConfigData.createMessage(ConfigData.MESSAGE_PUT, ConfigData.FREQ_CODE, new String[] { freq });
            sendMessage(String message);
        }
    };
        
        View.OnClickListener sendLeakRangeButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String smallLeakRange = smallLeakRangeEditText.getText().toString();
            String largeLeakRange = largeLeakRangeEditText.getText().toString();
            String message = ConfigData.createMessage(ConfigData.MESSAGE_PUT, ConfigData.LEAK_RANGE_CODE , 
                                                      new String[] { smallLeakRange, largeLeakRange });
            sendMessage(String message);
        }
    };
        
        View.OnClickListener resetButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String message = ConfigData.createMessage(ConfigData.MESSAGE_PUT, ConfigData.RESET_CODE, null);
            sendMessage(String message);
        }
    };
        
        View.OnClickListener getInfoButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String message = ConfigData.createMessage(ConfigData.MESSAGE_GET, ConfigData.INFO_CODE, null);
            sendMessage(String message);
        }
    };

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
