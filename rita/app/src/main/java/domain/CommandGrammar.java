package domain;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tb.rita.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thalesaguiar on 28/11/2017.
 */

import com.tb.rita.*;

public class CommandGrammar {

    private Context ctx;

    private String cmdText;
    private Map<String, List<String>> prefixAppliance; // LIGAR -> TV
    private Map<String, String> applianceSufix; // TV -> CH 6

    public CommandGrammar(String cmdText, Context ctx) {
        this.cmdText = cmdText;
        prefixAppliance = new HashMap<>();
        applianceSufix = new HashMap<>();
        this.ctx = ctx;
        populateTree();
    }

    private void populateTree() {
        String[] prefixes = ctx.getResources().getStringArray(R.array.cmd_prefix);
        String[] appliances = ctx.getResources().getStringArray(R.array.cmd_appliance);
        String[] sufix = ctx.getResources().getStringArray(R.array.appliance_sufix);

        prefixAppliance.put(prefixes[0], Arrays.asList(appliances));
    }

    public String getCmd() {
        boolean valid = false;
        String cmd = "";
        if(cmdText != null) {
            String[] cmdTxtTmp = cmdText.split(" ");
            List<String> cmdComponents = new ArrayList();
            // Configure the prompt
            for(int i = 0; i < cmdTxtTmp.length; i++) {
                cmdComponents.add(cmdTxtTmp[i].trim().toUpperCase());
            }
            // Search for the first occurence of a prefix
            for(int i=0; i<cmdComponents.size(); i++) {
                if(prefixAppliance.containsKey(cmdComponents.get(i))) {
                    List<String> myAppliances = prefixAppliance.get(cmdComponents.get(i));
                    for(int j=0; j<myAppliances.size(); j++) {
                        if(cmdComponents.get(i+1).compareTo(myAppliances.get(j)) == 0) {
                            cmd = cmdComponents.get(i) + "_" + myAppliances.get(j);
                            valid = true;
                            if(applianceSufix.containsKey(myAppliances.get(j))) {
                                Toast.makeText(ctx, "SUFIX", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    break;
                }
            }
        }




        return cmd;
    }

    public String getCmdText() {
        return this.cmdText;
    }

    public void setCmdText(String cmdText) {
        this.cmdText = cmdText;
    }
}
