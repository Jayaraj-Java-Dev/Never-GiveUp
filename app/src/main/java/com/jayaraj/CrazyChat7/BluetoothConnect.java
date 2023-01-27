package com.jayaraj.CrazyChat7;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public abstract class BluetoothConnect extends Context {
    private static final String DEFAULT_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    private final Activity activity;

    private final BluetoothAdapter bluetoothAdapter;

    public BluetoothConnect(final Activity activity) {
        this.activity = activity;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothEnabled() {
        return this.bluetoothAdapter != null;
    }

    public boolean isBluetoothActivated() {
        if (this.bluetoothAdapter == null) return false;

        return this.bluetoothAdapter.isEnabled();
    }

    public void activateBluetooth() {
        final Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.activity.startActivity(intent);
    }

    public String getRandomUUID() {
        return String.valueOf(UUID.randomUUID());
    }

    public void getPairedDevices(final ArrayList<HashMap<String, Object>> results) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final Set<BluetoothDevice> pairedDevices = this.bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (final BluetoothDevice device : pairedDevices) {
                final HashMap<String, Object> result = new HashMap<>();
                result.put("name", device.getName());
                result.put("address", device.getAddress());

                results.add(result);
            }
        }
    }

    public void readyConnection(final BluetoothConnectionListener listener, final String tag) {
        if (BluetoothController.getInstance().getState().equals(BluetoothController.STATE_NONE)) {
            BluetoothController.getInstance().start(this, listener, tag, UUID.fromString(BluetoothConnect.DEFAULT_UUID), this.bluetoothAdapter);
        }
    }

    public void readyConnection(final BluetoothConnectionListener listener, final String uuid, final String tag) {
        if (BluetoothController.getInstance().getState().equals(BluetoothController.STATE_NONE)) {
            BluetoothController.getInstance().start(this, listener, tag, UUID.fromString(uuid), this.bluetoothAdapter);
        }
    }


    public void startConnection(final BluetoothConnectionListener listener, final String address, final String tag) {
        final BluetoothDevice device = this.bluetoothAdapter.getRemoteDevice(address);

        BluetoothController.getInstance().connect(device, this, listener, tag, UUID.fromString(BluetoothConnect.DEFAULT_UUID), this.bluetoothAdapter);
    }

    public void startConnection(final BluetoothConnectionListener listener, final String uuid, final String address, final String tag) {
        final BluetoothDevice device = this.bluetoothAdapter.getRemoteDevice(address);

        BluetoothController.getInstance().connect(device, this, listener, tag, UUID.fromString(uuid), this.bluetoothAdapter);
    }

    public void stopConnection(final BluetoothConnectionListener listener, final String tag) {
        BluetoothController.getInstance().stop(this, listener, tag);
    }

    public void sendData(final BluetoothConnectionListener listener, final String data, final String tag) {
        final String state = BluetoothController.getInstance().getState();

        if (!state.equals(BluetoothController.STATE_CONNECTED)) {
            listener.onConnectionError(tag, state, "Bluetooth is not connected yet");
            return;
        }

        BluetoothController.getInstance().write(data.getBytes());
    }

    public Activity getActivity() {
        return this.activity;
    }

    public interface BluetoothConnectionListener {
        void onConnected(String tag, HashMap<String, Object> deviceData);

        void onDataReceived(String tag, byte[] data, int bytes);

        void onDataSent(String tag, byte[] data);

        void onConnectionError(String tag, String connectionState, String message);

        void onConnectionStopped(String tag);
    }
}