package com.tb.rita.delivery;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.RecognizerResultsIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import domain.Command;
import domain.CommandGrammar;

/**
 * This class is responsible by the main activity.
 * @author Breno Viana
 * @version 2017/10/19
 */
public class MainActivity extends AppCompatActivity {

    ImageButton tapToTalk;
    TextView speechTextView;
    BluetoothService btService;
    TextToSpeech speacher;

    private final int REQUEST_SPEECH_RECOG = 1;
    private final int REQUEST_BLUETOOTH_ON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content
        setContentView(R.layout.home_screen);

        btService = new BluetoothService(this);

        speechTextView = findViewById(R.id.speech_text_view);
        speechTextView.setVisibility(View.GONE);

        tapToTalk = findViewById(R.id.press_to_talk);
        tapToTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTapToTalk(view);
            }
        });
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

    public void OnHelpButtonPressed(View view) {
        Intent toHelp = new Intent(this, HelpActivity.class);
        startActivity(toHelp);
    }

    public void OnCommandsButtonPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        startActivity(toCmdList);
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

    public void onSettingPressed(View view) {
    }

    private void onSpeechFound(String text) {
        List<Command> cmds = new ArrayList<>();
        CommandGrammar cmdGrammar = new CommandGrammar(cmds);
        String cmd = cmdGrammar.getValidCmdFromText(text);
        btService.connectAndSend(cmd);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String msg = "";
        switch (requestCode) {
            case REQUEST_SPEECH_RECOG: {
                if(resultCode == RESULT_OK) {
                    ArrayList<String> mySpeech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(mySpeech != null) {
                        speechTextView.setText(mySpeech.get(0));
                        speechTextView.setVisibility(View.VISIBLE);
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
