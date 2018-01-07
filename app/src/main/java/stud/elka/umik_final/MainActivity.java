package stud.elka.umik_final;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import stud.elka.umik_final.communication.InfoData;
import stud.elka.umik_final.db.DatabaseHelper;
import stud.elka.umik_final.entities.Sensor;
import stud.elka.umik_final.services.SensorService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    /** Default sensor to be selected at the app start. */
    private static final long DEFAULT_SENSOR_ID = 1;

    /** Currently selected sensor. */
    private static Sensor selectedSensor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        selectedSensor = dbHelper.getSensor(DEFAULT_SENSOR_ID);
        dbHelper.close();

        // Set initial view (fragment)
        Fragment fragment;
        if(selectedSensor == null) {
            fragment = new SensorList();
        } else {
            fragment = new ConfigFragment();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fragment);
        fragmentTransaction.commit();

        // Navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // BLE service
        Intent serviceIntent = new Intent(this, SensorService.class);
        if(!isServiceRunning(SensorService.class)) {
            Log.d(TAG, "Service not running. Launching service.");
            startService(serviceIntent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Displays screen (fragment) depending on the selected
       option in the navigation menu.
       @param id selected option id
     */
    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_devices:
                fragment = new SensorList();
                break;
            case R.id.nav_chart:
                Toast.makeText(this, "Coming soon...", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_config:
                fragment = new ConfigFragment();
                break;
        }
        if(fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    @Override
    protected void onDestroy() {
        Intent serviceIntent = new Intent(this, SensorService.class);
        stopService(serviceIntent);
        super.onDestroy();
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d ("isServiceRunning?", true + "");
                return true;
            }
        }
        Log.d ("isServiceRunning?", false + "");
        return false;
    }

    public static Sensor getSelectedSensor() {
        return selectedSensor;
    }

    public static void setSelectedSensor(Sensor selectedSensor) {
        MainActivity.selectedSensor = selectedSensor;
    }
}
