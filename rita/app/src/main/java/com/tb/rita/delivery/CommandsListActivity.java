package com.tb.rita.delivery;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import domain.Command;
import domain.CommandGrammar;
import domain.models.CommandListViewModel;

public class CommandsListActivity extends AppCompatActivity {

    ImageButton tapToTalk;
    BluetoothService btService;
    private CommandListViewModel cmdModel;
    private final int REQUEST_SPEECH_RECOG = 1;
    private final int REQUEST_BLUETOOTH_ON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_list_screen);

        btService = new BluetoothService(this);
        tapToTalk = findViewById(R.id.lst_tap_talk);

        tapToTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTapToTalk(view);
            }
        });

        cmdModel = ViewModelProviders.of(this).get(CommandListViewModel.class);
        cmdModel.createDb();
        subscribeCommands();
    }

    private void subscribeCommands() {
        cmdModel.commands.observe(this, new Observer<List<Command>>() {
            @Override
            public void onChanged(@Nullable List<Command> commands) {
                showCmds(commands);
            }
        });
    }

    private void showCmds(final List<Command> commands) {
        ListView cmdList = findViewById(R.id.lst_cmds_list);
        ArrayAdapter<Command> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, commands);
        cmdList.setAdapter(adapter);

        // Add listener to list item
        cmdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onCmdPressed(position, view, commands);
            }
        });
    }

    /** Transition functions **/

    public void onBackButtonPressed(View view) {
        Intent toHome = new Intent(this, MainActivity.class);
        startActivity(toHome);
    }

    public void onAddButtonPressed(View view) {
        Intent toNewCmd = new Intent(this, NewCommandActivity.class);
        startActivity(toNewCmd);
    }

    public void onCmdPressed(int position, View view, List<Command> commands) {
        Intent toCmdDescr = new Intent(this, CommandDescriptionActivity.class);
        /* Pass the cmd name to command description
           Change later to pass an instance of the command */
        toCmdDescr.putExtra(CommandDescriptionActivity.CMD_ID, commands.get(position).getId_cmd());
        startActivity(toCmdDescr);
    }

    // ============================== Speech Recognition Functions
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
