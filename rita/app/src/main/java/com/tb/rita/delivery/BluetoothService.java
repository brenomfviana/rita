package com.tb.rita.delivery;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by thalesaguiar on 29/11/2017.
 */

public class BluetoothService {

    private final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private final String centralMac = "20:15:11:30:32:41";
    private Context context;
    private BluetoothDevice centralBt;
    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;

    private ConnectThread connectThread;

    // Requests
    private final int REQUEST_ENABLE = 1;

    public BluetoothService(Context context) {
        this.context = context;
    }

    /**
     *  Checks if the phone has a bluetooth adapter
     * @return
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
     * @return
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
        }
        return true;
    }

    public void close() {
        try {
            if(btSocket != null)
                btSocket.close();
        } catch (IOException e) {
            Toast.makeText(context, "FAILED TO CLOSE CONNECTION", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


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
            } catch (IOException e) {
                Toast.makeText(context, "FAILED TO CONNECT TO CENTRAL", Toast.LENGTH_LONG).show();
                try {
                    btSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
//            Toast.makeText(context, "CONNECTED TO CENTRAL", Toast.LENGTH_LONG).show();
        }

        public void cancel() {
            try {
                btSocket.close();
            } catch (IOException e) {
                Toast.makeText(context, "FAILED TO CLOSE CONNECTION", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

}
