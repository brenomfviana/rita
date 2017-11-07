package com.tb.rita.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;

import domain.Appliance;
import domain.Command;

/**
 * Created by thales on 04/11/17.
 */

public class NewCommandActivity extends AppCompatActivity {

    ArrayList<Command> commands;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content
        setContentView(R.layout.new_command_screen);

        Intent intent = getIntent();
        commands = (ArrayList<Command>) intent.getSerializableExtra(CommandsListActivity.CMD_LIST);
        populateAppliances();
    }

    public void onAddCmdPressed(View view) {
        // Creates the new command
        Command cmd = getNewCmd();
        if(verifyCmd(cmd)) {
            commands.add(cmd);
        }
        // Send the new list of commands to the other activity
        Intent intent = new Intent(this, CommandsListActivity.class);
        intent.putExtra(CommandsListActivity.CMD_LIST, commands);
        startActivity(intent);
    }

    public void onBackNAliasPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        toCmdList.putExtra(CommandsListActivity.CMD_LIST, commands);
        startActivity(toCmdList);
    }

    private void populateAppliances() {
        RadioGroup radios = (RadioGroup) findViewById(R.id.ncmd_appliances);
        Appliance appliances[] = Appliance.values();
        for(int i = 0; i < appliances.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(appliances[i].toString());
            rb.setId(i);
            radios.addView(rb);
        }
    }

    private Command getNewCmd() {
        EditText cmdNameInput = (EditText) findViewById(R.id.ncmd_input_text);
        RadioGroup applianceRadios = (RadioGroup) findViewById(R.id.ncmd_appliances);
        if(cmdNameInput.getText() != null &&
                applianceRadios.getCheckedRadioButtonId() != - 1) {
            String cmdName = cmdNameInput.getText().toString();

            int selectedRadio = applianceRadios.getCheckedRadioButtonId();
            Appliance appliances[] = Appliance.values();
            Appliance cmdAppliance = appliances[selectedRadio];

            return new Command(cmdName, cmdAppliance);
        } else {
            return null;
        }
    }

    private boolean verifyCmd(Command cmd) {
        boolean isValid = true;
        if(cmd == null) {
            isValid = false;
        } else if(cmd.getName() == null) {
            isValid = false;
        } else if(cmd.getName().length() < 2) {
            isValid = false;
        } else if(cmd.getName().length() > 15) {
            isValid = false;
        } else if(isValid) {
            String aux = cmd.getName().trim().toUpperCase();
            for(Command cmd_ : commands) {
                String auxCmd_ = cmd_.getName().trim().toUpperCase();
                // Checks if there is another cmd with the same name
                if(auxCmd_.equals(aux)) {
                    isValid = false;
                    break;
                }
                // Checks if there is another cmd connected to the same appliance
                if(cmd_.getAppliance() == cmd.getAppliance()) {
                    isValid = false;
                    break;
                }
            }
        }

        return isValid;
    }

}
