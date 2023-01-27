package com.jayaraj.CrazyChat7;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

public class BluetoothController {
    public static final String STATE_NONE = "none";
    public static final String STATE_LISTEN = "listen";
    public static final String STATE_CONNECTING = "connecting";
    public static final String STATE_CONNECTED = "connected";

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    private String state = BluetoothController.STATE_NONE;

    private static BluetoothController instance;

    public static synchronized BluetoothController getInstance() {
        if (BluetoothController.instance == null) {
            BluetoothController.instance = new BluetoothController();
        }

        return BluetoothController.instance;
    }

    public synchronized void start(final BluetoothConnect bluetoothConnect, final BluetoothConnect.BluetoothConnectionListener listener, final String tag, final UUID uuid, final BluetoothAdapter bluetoothAdapter) {
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }

        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }

        if (this.acceptThread != null) {
            this.acceptThread.cancel();
            this.acceptThread = null;
        }

        this.acceptThread = new AcceptThread(bluetoothConnect, listener, tag, uuid, bluetoothAdapter);
        this.acceptThread.start();
    }

    public synchronized void connect(final BluetoothDevice device, final BluetoothConnect bluetoothConnect, final BluetoothConnect.BluetoothConnectionListener listener, final String tag, final UUID uuid, final BluetoothAdapter bluetoothAdapter) {
        if (this.state.equals(BluetoothController.STATE_CONNECTING)) {
            if (this.connectThread != null) {
                this.connectThread.cancel();
                this.connectThread = null;
            }
        }

        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }

        this.connectThread = new ConnectThread(device, bluetoothConnect, listener, tag, uuid, bluetoothAdapter);
        this.connectThread.start();
    }

    public synchronized void connected(final BluetoothSocket socket, BluetoothDevice device, final BluetoothConnect bluetoothConnect, BluetoothConnect.BluetoothConnectionListener listener, String tag) {
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }

        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }

        if (this.acceptThread != null) {
            this.acceptThread.cancel();
            this.acceptThread = null;
        }

        this.connectedThread = new ConnectedThread(socket, bluetoothConnect, listener, tag);
        this.connectedThread.start();

        bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final HashMap<String, Object> deviceMap = new HashMap<>();
                deviceMap.put("name", device.getName());
                deviceMap.put("address", device.getAddress());

                listener.onConnected(tag, deviceMap);
            }
        });
    }

    public synchronized void stop(final BluetoothConnect bluetoothConnect, BluetoothConnect.BluetoothConnectionListener listener, String tag) {
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }

        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }

        if (this.acceptThread != null) {
            this.acceptThread.cancel();
            this.acceptThread = null;
        }

        this.state = BluetoothController.STATE_NONE;

        bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onConnectionStopped(tag);
            }
        });
    }

    public void write(final byte[] out) {
        final ConnectedThread r;

        synchronized (this) {
            if (!this.state.equals(BluetoothController.STATE_CONNECTED)) return;
            r = this.connectedThread;
        }

        r.write(out);
    }

    public void connectionFailed(final BluetoothConnect bluetoothConnect, BluetoothConnect.BluetoothConnectionListener listener, String tag, String message) {
        this.state = BluetoothController.STATE_NONE;

        bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onConnectionError(tag, BluetoothController.this.state, message);
            }
        });
    }

    public void connectionLost(final BluetoothConnect bluetoothConnect, BluetoothConnect.BluetoothConnectionListener listener, String tag) {
        this.state = BluetoothController.STATE_NONE;

        bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onConnectionError(tag, BluetoothController.this.state, "Bluetooth connection is disconnected");
            }
        });
    }

    public String getState() {
        return this.state;
    }

    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;

        private final BluetoothConnect bluetoothConnect;
        private final BluetoothConnect.BluetoothConnectionListener listener;
        private final String tag;

        public AcceptThread(final BluetoothConnect bluetoothConnect, final BluetoothConnect.BluetoothConnectionListener listener, final String tag, final UUID uuid, final BluetoothAdapter bluetoothAdapter) {
            this.bluetoothConnect = bluetoothConnect;
            this.listener = listener;
            this.tag = tag;

            try {
                this.serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(tag, uuid);
            } catch (final Exception e) {
                e.printStackTrace();
            }

            BluetoothController.this.state = BluetoothController.STATE_LISTEN;
        }

        @Override
        public void run() {
            BluetoothSocket bluetoothSocket;

            while (!BluetoothController.this.state.equals(BluetoothController.STATE_CONNECTED)) {
                try {
                    bluetoothSocket = this.serverSocket.accept();
                } catch (final Exception e) {
                    e.printStackTrace();
                    break;
                }

                if (bluetoothSocket != null) {
                    synchronized (BluetoothController.this) {
                        switch (BluetoothController.this.state) {
                            case BluetoothController.STATE_LISTEN:
                            case BluetoothController.STATE_CONNECTING:
                                BluetoothController.this.connected(bluetoothSocket, bluetoothSocket.getRemoteDevice(), this.bluetoothConnect, this.listener, this.tag);
                                break;
                            case BluetoothController.STATE_NONE:
                            case BluetoothController.STATE_CONNECTED:
                                try {
                                    bluetoothSocket.close();
                                } catch (final Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                this.serverSocket.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothDevice device;
        private BluetoothSocket socket;

        private final BluetoothConnect bluetoothConnect;
        private final BluetoothConnect.BluetoothConnectionListener listener;
        private final String tag;
        private final BluetoothAdapter bluetoothAdapter;

        public ConnectThread(final BluetoothDevice device, final BluetoothConnect bluetoothConnect, final BluetoothConnect.BluetoothConnectionListener listener, final String tag, final UUID uuid, final BluetoothAdapter bluetoothAdapter) {
            this.device = device;
            this.bluetoothConnect = bluetoothConnect;
            this.listener = listener;
            this.tag = tag;
            this.bluetoothAdapter = bluetoothAdapter;

            try {
                this.socket = device.createRfcommSocketToServiceRecord(uuid);
            } catch (final Exception e) {
                e.printStackTrace();
            }

            BluetoothController.this.state = BluetoothController.STATE_CONNECTING;
        }

        @Override
        public void run() {
            this.bluetoothAdapter.cancelDiscovery();

            try {
                this.socket.connect();
            } catch (final Exception e) {
                try {
                    this.socket.close();
                } catch (final Exception e2) {
                    e2.printStackTrace();
                }
                BluetoothController.this.connectionFailed(this.bluetoothConnect, this.listener, this.tag, e.getMessage());
                return;
            }

            synchronized (BluetoothController.this) {
                BluetoothController.this.connectThread = null;
            }

            BluetoothController.this.connected(this.socket, this.device, this.bluetoothConnect, this.listener, this.tag);
        }

        public void cancel() {
            try {
                this.socket.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        private final BluetoothConnect bluetoothConnect;
        private final BluetoothConnect.BluetoothConnectionListener listener;
        private final String tag;

        public ConnectedThread(final BluetoothSocket socket, final BluetoothConnect bluetoothConnect, final BluetoothConnect.BluetoothConnectionListener listener, final String tag) {
            this.bluetoothConnect = bluetoothConnect;
            this.listener = listener;
            this.tag = tag;

            this.socket = socket;

            try {
                this.inputStream = socket.getInputStream();
                this.outputStream = socket.getOutputStream();
            } catch (final Exception e) {
                e.printStackTrace();
            }

            BluetoothController.this.state = BluetoothController.STATE_CONNECTED;
        }

        public void run() {
            while (BluetoothController.this.state.equals(BluetoothController.STATE_CONNECTED)) {
                try {
                    byte[] buffer = new byte[1024];
                    int bytes = this.inputStream.read(buffer);

                    this.bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ConnectedThread.this.listener.onDataReceived(ConnectedThread.this.tag, buffer, bytes);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    BluetoothController.this.connectionLost(this.bluetoothConnect, this.listener, this.tag);
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                this.outputStream.write(buffer);

                this.bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConnectedThread.this.listener.onDataSent(ConnectedThread.this.tag, buffer);
                    }
                });
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                this.socket.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}