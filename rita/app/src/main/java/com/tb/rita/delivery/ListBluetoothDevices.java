package com.tb.rita.delivery;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by thalesaguiar on 28/11/2017.
 */

public class ListBluetoothDevices extends ListActivity {

    private BluetoothAdapter bluetoothAdapter;
    static String MAC_ADRESS = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> devicesArray = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

        for(BluetoothDevice device : devices) {
            devicesArray.add(device.getName() + "\n" + device.getAddress());
        }

        setListAdapter(devicesArray);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String deviceInfo = ((TextView) v).getText().toString();
        String macAdress = deviceInfo.substring(deviceInfo.length() - 17); // Get MAC ADRESS
        Intent returnMac =  new Intent(this, CommandDescriptionActivity.class);
        returnMac.putExtra(MAC_ADRESS, macAdress);
        setResult(RESULT_OK, returnMac);
        finish();
    }
}
