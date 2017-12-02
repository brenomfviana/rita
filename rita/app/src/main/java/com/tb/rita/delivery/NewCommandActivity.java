package com.tb.rita.delivery;

import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import domain.Alias;
import domain.Appliance;
import domain.Command;
import domain.CommandGrammar;
import domain.dao.AppDatabase;

/**
 * Created by thales on 04/11/17.
 */

public class NewCommandActivity extends AppCompatActivity {

    ImageButton tapToTalk;
    BluetoothService btService;
    private final int REQUEST_SPEECH_RECOG = 1;
    private final int REQUEST_BLUETOOTH_ON = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content
        setContentView(R.layout.new_command_screen);

        btService = new BluetoothService(this);
        tapToTalk = findViewById(R.id.ncmd_tap_talk);

        tapToTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTapToTalk(view);
            }
        });

        populateAppliances();
    }

    public void addCmd(final Context context) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Command newCmd = getNewCmd();
                if(verifyCmd(newCmd, new ArrayList<Command>()))
                    AppDatabase.getDatabase(context).commandDao().insertAll(getNewCmd());
            }
        };

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onAddCmdPressed(View view) {
        // Creates the new command
        addCmd(this);
        // Send the new list of commands to the other activity
        Intent intent = new Intent(this, CommandsListActivity.class);
        startActivity(intent);
    }

    public void onBackNAliasPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        startActivity(toCmdList);
    }

    private void populateAppliances() {
        RadioGroup radios = findViewById(R.id.ncmd_appliances);

        // States of the radio
        int[][] colorStates = new int[][] {
                new int[]{-R.attr.state_enabled},
                new int[]{R.attr.state_enabled}
        };

        // Colors of the radios
        int[] colors = new int[] {
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
        };

        Appliance appliances[] = Appliance.values();
        for(int i = 0; i < appliances.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(appliances[i].toString());
            rb.setId(i);
            rb.setButtonTintList(new ColorStateList(colorStates, colors));
            radios.addView(rb);
        }
    }

    private Command getNewCmd() {
        EditText cmdNameInput = findViewById(R.id.ncmd_input_text);
        RadioGroup applianceRadios = findViewById(R.id.ncmd_appliances);
        if(cmdNameInput.getText() != null &&
                applianceRadios.getCheckedRadioButtonId() != - 1) {
            String cmdName = cmdNameInput.getText().toString();

            int selectedRadio = applianceRadios.getCheckedRadioButtonId();
            Appliance appliances[] = Appliance.values();
            Appliance cmdAppliance = appliances[selectedRadio];

            return new Command(cmdName, cmdAppliance.ordinal());
        } else {
            return null;
        }
    }

    private boolean verifyCmd(Command cmd, List<Command> commands) {
        boolean isValid = true;
        if(cmd == null) {
            isValid = false;
        } else if(cmd.getName() == null) {
            isValid = false;
        } else if(cmd.getName().length() < Command.MIN_CMD_LENGTH) {
            isValid = false;
        } else if(cmd.getName().length() > Command.MAX_CMD_LENGTH) {
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
                // Checks if there is an alias with the same name
                for(Alias alias : cmd.getAliases()) {
                    if(alias.getName().compareToIgnoreCase(aux) == 0) {
                        isValid = false;
                        break;
                    }
                }
            }
        }
        return isValid;
    }

    // ================================== Speech  Recognition Functions
    public void onTapToTalk(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt_title));
        try {
            startActivityForResult(intent, REQUEST_SPEECH_RECOG);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.unsuportted_speech),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBluetooth(String msg) {
        btService = new BluetoothService(this);
        if(!btService.isBluetoothEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_BLUETOOTH_ON);
        } else {
            // Bluetooth já está ativado
            onSpeechFound(msg);
        }
    }

    private void onSpeechFound(String text) {
        List<Command> cmds = new ArrayList<>();
        CommandGrammar cmdGrammar = new CommandGrammar(cmds);
        String cmd = cmdGrammar.getValidCmdFromText(text);
        btService.connectAndSend(cmd);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String msg = "";
        switch (requestCode) {
            case REQUEST_SPEECH_RECOG: {
                if(resultCode == RESULT_OK) {
                    ArrayList<String> mySpeech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(mySpeech != null) {
                        msg = mySpeech.get(0);
                        checkBluetooth(msg);
                    }
                }
                break;
            }
            case REQUEST_BLUETOOTH_ON: {
                if(resultCode == RESULT_OK) {
                    Toast.makeText(this, "Bluetooh ativado com sucesso!", Toast.LENGTH_LONG).show();
                    onSpeechFound(msg);
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível ativar o bluetooth", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

}
