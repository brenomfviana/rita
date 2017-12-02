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


/**
 *  This class will verify if a given command is valid.
 *  Commands are composed of a prefix, and appliance, and sufix. The prefix is
 *  the action to perform, fo instance, TURN ON or TURN OFF. The Appliance is
 *  the object to perform the action, and the sufix is an optinal text that
 *  some appliances may have like a channel for a tv.
 *  COMMAND: PREFIX | APPLIANCE | SUFIX
 *  COMMAND: TURN ON | TV | ON CHANNEL 3
 */
public class CommandGrammar {

    private Context ctx;

    private String cmdText;
    private List<Command> commands;
    private List<String> prefix;
    private List<String> appliance;
    private List<String> sufix;
    private Map<String, String> prefixMap; // LIGAR -> TV
    private Map<String, String> applianceMap; // TV -> CH 6
    private Map<String, String> sufixMap; // TV -> CH 6

    public CommandGrammar(List<Command> commands) {
        if(commands != null)
            this.commands = commands;
        else
            this.commands = new ArrayList<>();
        prefix = new ArrayList<>();
        appliance = new ArrayList<>();
        sufix = new ArrayList<>();
        prefixMap = new HashMap<>();
        applianceMap = new HashMap<>();
        sufixMap = new HashMap<>();
        populateLists();
    }

    private void populateLists() {
        populatePrefix();
        populateAppliance();
        populateSufix();
    }

    private void populatePrefix() {
        String on = "LIGAR";
        prefixMap.put("LIGAR", on);
        prefixMap.put("ATIVAR", on);
        prefixMap.put("ACENDER", on);
        prefixMap.put("ACIONAR", on);
        String off = "DESLIGAR";
        prefixMap.put("DESLIGAR", off);
        prefixMap.put("DESATIVAR", off);
        prefixMap.put("APAGAR", off);

        prefix.add("LIGAR");
        prefix.add("DESLIGAR");
        prefix.add("ATIVAR");
        prefix.add("DESATIVAR");
    }

    private void populateAppliance() {
        // Ventilador
        applianceMap.put("VENTILADOR", Appliance.FAN.toString());
        applianceMap.put("VENTOINHA", Appliance.FAN.toString());
        // Luz
        applianceMap.put("LUZ", Appliance.LIGHT.toString());
        applianceMap.put("LUZES", Appliance.LIGHT.toString());
        applianceMap.put("LÂMPADA", Appliance.LIGHT.toString());
        applianceMap.put("LAMPADA", Appliance.LIGHT.toString());
        // Televisão
        applianceMap.put("TV", Appliance.TV.toString());
        applianceMap.put("TELEVISÃO", Appliance.TV.toString());
        applianceMap.put("TELEVISAO", Appliance.TV.toString());
        applianceMap.put("TELEVISIVO", Appliance.TV.toString());

        appliance.add(Appliance.FAN.toString());
        appliance.add(Appliance.LIGHT.toString());
        appliance.add(Appliance.TV.toString());
    }

    private void populateSufix() {
        sufixMap.put("CANAL", "C");
        sufixMap.put("UM", "1");
        sufixMap.put("DOIS", "2");
        sufixMap.put("TRES", "3");
        sufixMap.put("TRÊS", "3");
        sufixMap.put("QUATRO", "4");
        sufixMap.put("CINCO", "5");
        sufixMap.put("1", "1");
        sufixMap.put("2", "2");
        sufixMap.put("3", "3");
        sufixMap.put("4", "4");
        sufixMap.put("5", "5");
        sufixMap.put("6", "6");



        sufix.add("C1");
        sufix.add("C2");
        sufix.add("C3");
        sufix.add("C4");
        sufix.add("C5");
        sufix.add("C6");
    }

    /**
     * Returns a valid string to send to arduino, if the text contains
     * a valid command. Otherwise, returns a empty string
     * @param command The command to be verifyed
     * @return A valid command to send, or an empty String
     */
    public String getValidCmdFromText(Command command) {
        String validCmd = "";
        if(command != null) {
            validCmd = getValidCmdFromText(command.getName());
        }

        return validCmd;
    }

    /**
     * Returns a valid string to send to arduino, if the text contains
     * a valid command. Otherwise, returns a empty string
     * @param text_ The text to be verified
     * @return A valid command to send, or an empty String
     */
    public String getValidCmdFromText(String text_) {

        StringBuilder validCmd = new StringBuilder("");
        String[] text = text_.trim().toUpperCase().split(" ");
        if(text != null) {
            for(int i=0; i<text.length; i++){
                if(prefixMap.containsKey(text[i])){
                    validCmd.append(prefixMap.get(text[i]));
                } else if(applianceMap.containsKey(text[i])) {
                    validCmd.append("_" + applianceMap.get(text[i]));
                } else if(sufixMap.containsKey(text[i])) {
                    String value = sufixMap.get(text[i]);
                    if(value.compareToIgnoreCase("C") != 0) {
                        validCmd.append(value);
                    } else {
                        validCmd.append("_" + value);
                    }
                }
            }
        }

        return validCmd.toString();
    }

    public String getCmd() {
        return "";
    }

    public String getCmdText() {
        return this.cmdText;
    }

    public void setCmdText(String cmdText) {
        this.cmdText = cmdText;
    }
}
