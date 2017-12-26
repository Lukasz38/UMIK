package stud.elka.umik_final;

import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import stud.elka.umik_final.communication.RemoteDevice;

import stud.elka.umik_final.entities.Sensor;

public class SensorList extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_sensor_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.sensor_list_title);
        //ListView
        Sensor[] sensors = {};
             /*   new Sensor(Long.valueOf(1), "toilet_sensor", "3rd floor, 5th cabin"),
                new Sensor(Long.valueOf(2), "outside_sensor", "2nd floor, 4th cabin")
        };*/
        ArrayAdapter<Sensor> adapter = new ArrayAdapter<Sensor>(getActivity(),
                android.R.layout.simple_list_item_1, sensors);

        ListView listView = (ListView) getActivity().findViewById(R.id.sensor_list);
        TextView emptyView = (TextView) getActivity().findViewById(R.id.empty);
        emptyView.setVisibility(View.INVISIBLE);

        if (sensors.length == 0) {
            listView.setEmptyView(emptyView);
        } else {
            listView.setAdapter(adapter);
        }

        //Floating button
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.add_sensor_dialog, null);

                dialogBuilder.setView(dialogView);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }


}



       // BluetoothManager bm = BluetoothManager.
        //RemoteDevice remote = new RemoteDevice(SensorList.this, )
