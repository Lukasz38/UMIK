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

import java.util.UUID;

import stud.elka.umik_final.db.DatabaseHelper;

/**
 * Klasa reprezentująca urządzenie zdalne, umożliwia połączenie i rozłączenie,
 * a także odbieranie i wysyłanie danych.
 * @author mateuszwojciechowski
 * @version 1
 */

public class RemoteDevice {

    private static final UUID SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    // Adres modułu z inżynierki
    public static final String DEFAULT_DEVICE_ADDRESS = "04:A3:16:A7:0F:6C";
    
    private static final String TAG = "RemoteDevice";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattService mBluetoothGattService;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic;
    private Context mContext;
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
            mBluetoothGattService = gatt.getService(SERVICE_UUID);
            mBluetoothGattCharacteristic = mBluetoothGattService.getCharacteristic(CHARACTERISTIC_UUID);
            mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristic, true);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d(TAG, "Received characteristic: " + characteristic.getStringValue(0));

            Object obj = parseMessage(characteristic.getStringValue(0));
            if(obj == null) {
                return;
            }

            if(obj instanceof Data) {
                Data data = (Data) obj;
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                dbHelper.addData(data);
                Log.d(TAG, "Data added to DB: " + data);
                dbHelper.close();

                Intent dataIntent = new Intent("stud.elka.umik_final.PushNotification");
                dataIntent.putExtra("data", data);
                mContext.sendBroadcast(dataIntent);
            }
            else if(obj instanceof InfoData) {
                InfoData infoData = (InfoData) obj;

                Intent infoDataIntent = new Intent("stud.elka.umik_final.PushConfig");
                infoDataIntent.putExtra("infoData", infoData);
                mContext.sendBroadcast(infoDataIntent);
            }
        }
    };

    /**
     * Konstruktor klasy RemoteDevice.
     * @param context kontekst aplikacji
     * @param manager instancja klasy BluetoothManager
     * @param macAddress adres MAC urządzenia BLE
     */
    public RemoteDevice(Context context, BluetoothManager manager, String macAddress) {
        mBluetoothAdapter = manager.getAdapter();
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(macAddress);
        mContext = context;
    }

    /**
     * Funkcja nawiązująca połączenie z urządzeniem zdalnym.
     */
    public void connect() {
        if (mBluetoothGatt == null) {
            mBluetoothGatt = mBluetoothDevice.connectGatt(mContext, true, gattCallback);
            //SystemClock.sleep(2000);
            if(connected) {
                Log.d(TAG, "Device connected: " + mBluetoothDevice.getAddress());
            } else {
                Log.d(TAG, "Failed to connect to the device: " + mBluetoothDevice.getAddress());
            }
        }
    }

    /**
     * Funkcja rozłączająca połączenie z urządzeniem zdalnym.
     */
    public void disconnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            Log.d(TAG, "Device disconnected: " + mBluetoothDevice.getAddress());
        }
    }

    /**
     * Metoda wysyłająca wiadomość do urządzenia BLE.
     * @param message wiadomość do wysłania
     * @return true - jeśli wysłano wiadomość
     */
    public boolean sendConfig(String message) {
        if(connected) {
            mBluetoothGattCharacteristic.setValue(message);
            return mBluetoothGatt.writeCharacteristic(mBluetoothGattCharacteristic);
        } else {
            return false;
        }
    }

    public String getMacAddress() {
        return mBluetoothDevice.getAddress();
    }

    public boolean isConnected() {
        return connected;
    }

    /** Parses given message and returns Data or InfoData object depending on the message. */
    private Object parseMessage(String message) {
        String[] splittedMessage = message.split(":");
        String messageType = splittedMessage[0];
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        long sensorId = 0;

        switch (messageType) {
            case "LEAK":
                sensorId = dbHelper.getSensor(getMacAddress()).getId();
                dbHelper.close();
                return new Data(sensorId, splittedMessage[1]);
            case "INFO":
                sensorId = dbHelper.getSensor(getMacAddress()).getId();
                dbHelper.close();
                int freqency = Integer.parseInt(splittedMessage[1]);
                int smallLeakRange = Integer.parseInt(splittedMessage[2]);
                int largeLeakRange = Integer.parseInt(splittedMessage[3]);
                return new InfoData(sensorId, freqency, smallLeakRange, largeLeakRange);
            default:
                Log.d(TAG, "Unknown message type.");
                return null;
        }
    }
}
