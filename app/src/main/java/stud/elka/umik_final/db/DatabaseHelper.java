package stud.elka.umik_final.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import stud.elka.umik_final.communication.Data;
import stud.elka.umik_final.entities.Sensor;

/**
 * Created by Łukasz on 30.12.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "LEAK_DETECTOR";

    // Table names
    private static final String TABLE_SENSOR = "sensor";
    private static final String TABLE_DATA = "data";
    private static final String TABLE_SENSOR_DATA = "sensor_data";

    // Common column names
    private static final String COLUMN_ID = "_id";

    // SENSOR table - column names
    private static final String COLUMN_NAME = "_name";
    private static final String COLUMN_LOCATION = "_location";
    private static final String COLUMN_MAC = "mac_address";

    // DATA table - column names
    private static final String COLUMN_RECEIVAL_TIME = "_received";
    private static final String COLUMN_CODE = "_code";

    // SENSOR_DATA table - column names
    private static final String KEY_SENSOR = "sensor_id";
    private static final String KEY_DATA = "data_id";

    // CREATE statements
    private static final String CREATE_TABLE_SENSOR = "CREATE TABLE " + TABLE_SENSOR + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_MAC + " TEXT NOT NULL, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_LOCATION + " TEXT" +
            ");";

    private static final String CREATE_TABLE_DATA = "CREATE TABLE " + TABLE_DATA + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_RECEIVAL_TIME + " TEXT NOT NULL, " +
            COLUMN_CODE + " INTEGER NOT NULL" +
            ");";

    private static final String CREATE_TABLE_SENSOR_DATA = "CREATE TABLE " + TABLE_SENSOR_DATA + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            KEY_SENSOR + " INTEGER NOT NULL, " +
            KEY_DATA + " INTEGER NOT NULL" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        db.execSQL(CREATE_TABLE_SENSOR);
        db.execSQL(CREATE_TABLE_DATA);
        db.execSQL(CREATE_TABLE_SENSOR_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR_DATA);
        onCreate(db);
    }

    public long addSensor(Sensor sensor) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, sensor.getName());
        values.put(COLUMN_LOCATION, sensor.getLocation());
        values.put(COLUMN_MAC, sensor.getMacAddress());

        SQLiteDatabase db = getWritableDatabase();
        long sensor_id = db.insert(TABLE_SENSOR, null, values);
        Log.d(TAG, "Sensor added with id: " + sensor_id);

        return sensor_id;
    }

    public long addData(Data data) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECEIVAL_TIME, data.getTimestampString());
        values.put(COLUMN_CODE, data.getCode());

        SQLiteDatabase db = getWritableDatabase();
        long dataID = db.insert(TABLE_DATA, null, values);
        Log.d(TAG, "Data added with id: " + dataID);
        addSensorDataRelation(data.getSensorID(), dataID);

        return dataID;
    }

    public long addSensorDataRelation(long sensor_id, long data_id) {
        ContentValues values = new ContentValues();
        values.put(KEY_SENSOR, sensor_id);
        values.put(KEY_DATA, data_id);

        SQLiteDatabase db = getWritableDatabase();
        long sensorDataId = db.insert(TABLE_SENSOR_DATA, null, values);
        Log.d(TAG, "Sensor - data relation added with id: " + sensorDataId);

        return sensorDataId;
    }

    public Sensor getSensor(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SENSOR + " " +
                "WHERE " + COLUMN_ID + " = " + id + ";";

        Cursor c = db.rawQuery(query, null);

        int iColumnId = c.getColumnIndex(COLUMN_ID);
        int iColumnMac = c.getColumnIndex(COLUMN_MAC);
        int iColumnName= c.getColumnIndex(COLUMN_NAME);
        int iColumnLocation = c.getColumnIndex(COLUMN_LOCATION);

        if(c.isAfterLast()) {
            return null;
        }
        c.moveToFirst();
        long sensorId = c.getLong(iColumnId);
        String mac = c.getString(iColumnMac);
        String name = c.getString(iColumnName);
        String location = c.getString(iColumnLocation);
        Log.d(TAG, "Fetched sensor with id: " + sensorId);

        return new Sensor(sensorId, mac, name, location);
    }

    public Sensor getSensor(String macAddress) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SENSOR + " " +
                "WHERE " + COLUMN_MAC + " = '" + macAddress + "';";

        Cursor c = db.rawQuery(query, null);

        int iColumnId = c.getColumnIndex(COLUMN_ID);
        int iColumnMac = c.getColumnIndex(COLUMN_MAC);
        int iColumnName= c.getColumnIndex(COLUMN_NAME);
        int iColumnLocation = c.getColumnIndex(COLUMN_LOCATION);

        c.moveToFirst();
        long id = c.getLong(iColumnId);
        String mac = c.getString(iColumnMac);
        String name = c.getString(iColumnName);
        String location = c.getString(iColumnLocation);

        Log.d(TAG, "Fetched sensor with mac: " + mac);
        return new Sensor(id, mac, name, location);
    }

    public List<Sensor> getAllSensors() {
        SQLiteDatabase db = getReadableDatabase();
        List<Sensor> sensors = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_SENSOR + ";";
        Cursor c = db.rawQuery(query, null);

        int iColumnId = c.getColumnIndex(COLUMN_ID);
        int iColumnMac = c.getColumnIndex(COLUMN_MAC);
        int iColumnName= c.getColumnIndex(COLUMN_NAME);
        int iColumnLocation = c.getColumnIndex(COLUMN_LOCATION);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            long id = c.getLong(iColumnId);
            String mac = c.getString(iColumnMac);
            String name = c.getString(iColumnName);
            String location = c.getString(iColumnLocation);
            sensors.add(new Sensor(id, mac, name, location));
        }

        Log.d(TAG, "Fetched all sensors. Length: " + sensors.size());
        return sensors;
    }

    public List<Data> getAllData(long sensorId) {
        SQLiteDatabase db = getReadableDatabase();
        List<Data> dataList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_DATA + " d " +
                "INNER JOIN " + TABLE_SENSOR_DATA + " ds " +
                "ON d." + COLUMN_ID + " = " + KEY_DATA + " " +
                "WHERE " + KEY_SENSOR + " = " + sensorId + ";";
        Cursor c = db.rawQuery(query, null);

        int iColumnId = c.getColumnIndex(COLUMN_ID);
        int iColumnCode = c.getColumnIndex(COLUMN_CODE);
        int iColumnDate = c.getColumnIndex(COLUMN_RECEIVAL_TIME);
        int iColumnSensorId = c.getColumnIndex(KEY_SENSOR);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            long id = c.getLong(iColumnId);
            int code = c.getInt(iColumnCode);
            String date = c.getString(iColumnDate);
            dataList.add(new Data(id, sensorId, code, date));
        }

        Log.d(TAG, "Fetched all data. Length: " + dataList.size());
        return dataList;
    }
}
