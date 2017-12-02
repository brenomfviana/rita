package com.tb.rita.delivery;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

/**
 * Created by thalesaguiar on 29/11/2017.
 */

public class BluetoothService {

    private final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private final String centralMac = "20:15:11:30:32:41";
    private Context context;
    private String msg;
    private boolean success;

    private BluetoothDevice centralBt;
    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;

    private ConnectThread connectThread;
    private ConnectedDevice connectedDevice;

    public BluetoothService(Context context) {
        this.context = context;
    }

    /**
     *  Checks if the phone has a bluetooth adapter
     * @return True if the device has support
     */
    private boolean hasBluetoothSupport() {
        boolean hasSupport;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        hasSupport = btAdapter != null;
        if(!hasSupport) {
            Toast.makeText(context, "NO BLUETOOTH DEVICE FOUND", Toast.LENGTH_LONG).show();
        }
        return hasSupport;
    }

    /**
     *  CHecks if the blueetooth adapter is enabled, otherwise request a connection
     * @return true if the bluetooth is enabled
     */
    public boolean isBluetoothEnabled() {
        return hasBluetoothSupport() && btAdapter.isEnabled();
    }

    private boolean isPairedToCentral() {
        boolean isPaired = false;
        if(btAdapter != null) {
            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
            for(BluetoothDevice device : pairedDevices) {
                if(device.getAddress().compareToIgnoreCase(centralMac) == 0) {
                    isPaired = true;
                    break;
                }
            }
        }
        return isPaired;
    }

    public boolean connect() {
        if(btAdapter == null || !hasBluetoothSupport()) {
            // Does nothing, no support to Bluetooth connections
            Toast.makeText(context, "BLUETOOTH UNSUPPORTED", Toast.LENGTH_LONG).show();
        } else if(!isPairedToCentral()) {
            // start discovery
            Toast.makeText(context, "PREPARE BOUND TO CENTRAL", Toast.LENGTH_LONG).show();
        } else {
            // Get a reference to central bluetooth adapter
            Toast.makeText(context, "STARTING CONNECTION", Toast.LENGTH_LONG).show();
            centralBt = btAdapter.getRemoteDevice(centralMac);
            connectThread = new ConnectThread(centralBt);
            connectThread.start();
            try {
                connectThread.join();
                if(success) {
                    connectedDevice = new ConnectedDevice(btSocket);
                    connectedDevice.setMessage(msg);
                    connectedDevice.start();
                    connectedDevice.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean connectAndSend(String msg) {
        if(msg != null) {
            this.msg = msg;
            connect();
        }
        return true;
    }

    public void close() {
        try {
            if(btSocket != null)
                btSocket.close();
            if(connectThread != null)
                connectThread.cancel();
        } catch (IOException e) {
            Toast.makeText(context, "FAILED TO CLOSE CONNECTION", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // ============================== Connection thread
    private class ConnectThread extends Thread {

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            centralBt = device;
            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(myUUID);
            } catch (IOException e) {
                Toast.makeText(context, "FAILED TO CREATE SOCKET", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            btSocket = tmp;
        }

        @Override
        public void run() {
            btAdapter.cancelDiscovery();
            try {
                btSocket.connect();
                success = true;
                Log.d("BLUETOOTH", "CONEECTION SUCCESS!");
            } catch (IOException e) {
                success = false;
                Log.d("BLUETOOTH", "CONEECTION FAILED!");
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                btSocket.close();
            } catch (IOException e) {
                Log.d("BLUETOOTH", "FAILED TO CLOSE SOCKET!");
                e.printStackTrace();
            } finally {
                success = false;
            }
        }
    }

    // ============================== Courrier thread
    private class ConnectedDevice extends Thread {

        private BluetoothSocket connSocket;
        private OutputStream outStream;
        private String message;

        public ConnectedDevice(BluetoothSocket socket) {
            connSocket = socket;
            OutputStream tmp = null;
            try {
                tmp = connSocket.getOutputStream();
                Log.d("BLUETOOTH", "RETRIEVED OUTPUTSTREAM!");
            } catch (IOException e) {
                Log.d("BLUETOOTH", "FAILED TO RETRIEVE OUTPUTSTREAM!");
                e.printStackTrace();
            }
            outStream = tmp;
        }

        @Override
        public void run() {
            try {
                Log.d("BLUETOOTH", "WRITING: " + msg.toString());
                outStream.write(message.getBytes());
                Log.d("BLUETOOTH", "MESSAGE SENT, CLOSING SOCKET!");
                sleep(500);
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        public void write(byte[] msg) {
            try {
                if(outStream != null) {
                    Log.d("BLUETOOTH", "WRITING: " + msg.toString());
                    outStream.write(msg);
                    Log.d("BLUETOOTH", "MESSAGE SENT, CLOSING SOCKET!");
                    btSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void write(String msg) {
            if(msg != null) {
                byte[] bytesMsg = msg.getBytes();
                write(bytesMsg);
            }
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
