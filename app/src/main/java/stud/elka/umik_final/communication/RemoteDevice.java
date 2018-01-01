package stud.elka.umik_final.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.LinkedList;
import java.util.UUID;

import stud.elka.umik_final.db.DatabaseHelper;
import stud.elka.umik_final.receivers.BluetoothDataReceiver;

/**
 * Klasa reprezentująca urządzenie zdalne, umożliwiająca połączenie i rozłączenie
 * @author mateuszwojciechowski
 * @version 1
 */

public class RemoteDevice {

    private static final UUID SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    // Adres modułu z inżynierki
    private static final String DEVICE_ADDRESS = "88:4A:EA:8B:8B:CD";
    private static final String TAG = "RemoteDevice";

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattService mBluetoothGattService;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic;
    private Context mContext;
    private LinkedList<Data> data;
    private boolean connected = false;

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            switch(newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    connected = true;
                    mBluetoothGatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    connected = false;
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "onServicesDiscovered");
            mBluetoothGattService = gatt.getService(SERVICE_UUID);
            mBluetoothGattCharacteristic = mBluetoothGattService.getCharacteristic(CHARACTERISTIC_UUID);
            mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristic, true);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d(TAG, "onCharacteristicRead");
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(TAG, "onCharacteristicWrite");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d(TAG, "onCharacteristicChanged");

            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            long id = dbHelper.getSensor(mBluetoothDevice.getAddress()).getId();
            dbHelper.close();
            data.add(new Data(id, characteristic.getStringValue(0)));
            if (data.size() >= 100) {
                data.removeFirst();
            }

            Intent dataIntent = new Intent("stud.elka.umik_final.PushNotification");
            dataIntent.putExtra("data", getLastData());
            mContext.sendBroadcast(dataIntent);
        }
    };

    /**
     * Konstruktor klasy RemoteDevice
     * @param context kontekst aplikacji
     * @param manager instancja klasy BluetoothManager
     * @param macAddress adres MAC urządzenia BLE
     */
    public RemoteDevice(Context context, BluetoothManager manager, String macAddress) {
        mBluetoothManager = manager;
        mBluetoothAdapter = manager.getAdapter();
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(macAddress);
        mContext = context;
        data = new LinkedList<>();
    }

    /**
     * Funkcja nawiązująca połączenie z urządzeniem zdalnym
     */
    public void connect() {
        if (mBluetoothGatt == null) {
            mBluetoothGatt = mBluetoothDevice.connectGatt(mContext, true, gattCallback);
            SystemClock.sleep(2000);
            if(connected) {
                Log.d(TAG, "Device connected: " + mBluetoothDevice.getAddress());
            } else {
                Log.d(TAG, "Failed to connect to the device: " + mBluetoothDevice.getAddress());
            }
        }
    }

    /**
     * Funkcja rozłączająca połączenie z urządzeniem zdalnym
     */
    public void disconnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            Log.d(TAG, "Device disconnected: " + mBluetoothDevice.getAddress());
        }
    }

    public boolean sendConfig(ConfigData data) {
        if(connected) {
            mBluetoothGattCharacteristic.setValue(data.createMessage());
            return mBluetoothGatt.writeCharacteristic(mBluetoothGattCharacteristic);
        } else {
            return false;
        }
    }

    /**
     * Funkcja zwracająca listę otrzymanych odczytów z czujnika
     * @return lista odczytów
     */
    public LinkedList<Data> getData() {
        return data;
    }

    /**
     * Funkcja zwracająca ostatni otrzymany odczyt z czujnika
     * @return ostatni odczyt
     */
    public Data getLastData() {
        return data.getLast();
    }
}
