package stud.elka.umik_final;

import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import stud.elka.umik_final.communication.RemoteDevice;

import stud.elka.umik_final.db.DatabaseHelper;
import stud.elka.umik_final.entities.Sensor;

public class SensorList extends Fragment {

    private static final String TAG = "SensorList";

    private AlertDialog dialog;
    private ArrayAdapter<Sensor> adapter;
    private List<Sensor> sensors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_sensor_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.sensor_list_title);

        // ListView
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        sensors = dbHelper.getAllSensors();
        dbHelper.close();
        Log.d(TAG, "Sensory: " + sensors.toString());

        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, sensors);

        ListView listView = (ListView) getActivity().findViewById(R.id.sensor_list);
        TextView emptyView = (TextView) getActivity().findViewById(R.id.empty);
        emptyView.setVisibility(View.INVISIBLE);

        if (sensors.size() == 0) {
            Log.d(TAG, "Sensor list length: " + sensors.size());
            listView.setEmptyView(emptyView);
        } else {
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        //Floating button
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button clicked!");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.add_sensor_dialog, null);

                dialogBuilder.setView(dialogView);
                dialog = dialogBuilder.create();
                dialog.show();

                Button submitButton = (Button) dialog.findViewById(R.id.submit_add_sensor);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText macText = (EditText) dialog.findViewById(R.id.mac_address_text);
                        EditText nameText = (EditText) dialog.findViewById(R.id.name_text);
                        EditText locationText = (EditText) dialog.findViewById(R.id.location_text);

                        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                        dbHelper.addSensor(new Sensor(macText.getText().toString(),
                                nameText.getText().toString(),
                                locationText.getText().toString()));
                        dbHelper.close();

                        Toast.makeText(getActivity(), R.string.confirm_sensor_add, Toast.LENGTH_SHORT).show();
                        updateAdapter();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void updateAdapter() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        sensors.clear();
        sensors.addAll(dbHelper.getAllSensors());
        dbHelper.close();
    }
}