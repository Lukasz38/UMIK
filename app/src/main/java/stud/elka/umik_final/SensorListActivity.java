package stud.elka.umik_final;

import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import stud.elka.umik_final.communication.RemoteDevice;

import stud.elka.umik_final.entities.Sensor;

public class SensorListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);

        //ActionBar
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setDisplayShowCustomEnabled(true);
        //getSupportActionBar().setCustomView(R.layout.custom_toolbar);

        //View toolbarView = getSupportActionBar().getCustomView();
        //TextView toolbarTextView = toolbarView.findViewById(R.id.toolbarTextView);
        //toolbarTextView.setText(R.string.sensor_list_activity_bar_string);

        //AppCompatImageButton hamburgerMenuButton = (AppCompatImageButton) toolbarView.findViewById(R.id.hamburger_menu_btn);
        /*hamburgerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("Hamburger clicked");
            }
        }); */

        //ListView
        Sensor[] sensors = {};/*
                new Sensor(Long.valueOf(1), "toilet_sensor", "3rd floor, 5th cabin"),
                new Sensor(Long.valueOf(2), "outside_sensor", "2nd floor, 4th cabin")
        };*/
        ArrayAdapter<Sensor> adapter = new ArrayAdapter<Sensor>(this,
                android.R.layout.simple_list_item_1, sensors);

        ListView listView = (ListView) this.findViewById(R.id.sensor_list);
        TextView emptyView = (TextView) this.findViewById(R.id.empty);
        emptyView.setVisibility(View.INVISIBLE);

        if(sensors.length == 0) {
            listView.setEmptyView(emptyView);
        } else {
            listView.setAdapter(adapter);
        }

        //Floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SensorListActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.add_sensor_dialog, null);

                dialogBuilder.setView(dialogView);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
       // BluetoothManager bm = BluetoothManager.
        //RemoteDevice remote = new RemoteDevice(SensorListActivity.this, )
    }
}