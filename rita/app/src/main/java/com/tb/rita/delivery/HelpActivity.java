package com.tb.rita.delivery;

import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import domain.Command;
import domain.CommandGrammar;

/**
 * Created by thales on 02/11/17.
 */

public class HelpActivity extends AppCompatActivity {

    private ImageButton tapToTalk;
    private BluetoothService btService;
    private final int REQUEST_SPEECH_RECOG = 1;
    private final int REQUEST_BLUETOOTH_ON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content
        setContentView(R.layout.help_screen);
        btService = new BluetoothService(this);
        tapToTalk = findViewById(R.id.help_rita_icon);

        tapToTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTapToTalk(view);
            }
        });
    }

    public void OnBackPressed(View view) {
        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
    }

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
