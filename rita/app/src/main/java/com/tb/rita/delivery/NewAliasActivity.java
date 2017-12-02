package com.tb.rita.delivery;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import domain.Alias;
import domain.Command;
import domain.CommandGrammar;
import domain.custom.MyDialogBox;
import domain.models.NewAliasViewModel;

public class NewAliasActivity extends AppCompatActivity {

    public static final String IS_EDIT = "TO EDIT ALIAS";
    NewAliasViewModel newAliasModel;
    private boolean isEdit;
    private int cmd_id;
    private int alias_id;
    private ImageButton tapToTalk;
    private BluetoothService btService;
    private final int REQUEST_SPEECH_RECOG = 1;
    private final int REQUEST_BLUETOOTH_ON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alias_screen);

        Intent intent = getIntent();
        cmd_id = intent.getIntExtra(CommandDescriptionActivity.CMD_ID, -1);
        alias_id = intent.getIntExtra(CommandDescriptionActivity.ALIAS_ID, -1);
        isEdit = intent.getBooleanExtra(IS_EDIT, false);

        btService = new BluetoothService(this);
        tapToTalk = findViewById(R.id.nalias_tap_talk);

        tapToTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTapToTalk(view);
            }
        });

        newAliasModel = ViewModelProviders.of(this).get(NewAliasViewModel.class);
        newAliasModel.createDb();
        subscribeData();
        if(isEdit)
            subscribeAlias();
    }

    private void subscribeAlias() {
        newAliasModel.getAlias(alias_id);
        newAliasModel.alias.observe(this, new Observer<Alias>() {
            @Override
            public void onChanged(@Nullable Alias alias) {
                setUpEdit(alias);
            }
        });
    }

    private void subscribeData() {
        newAliasModel.getCmd(cmd_id);
        newAliasModel.cmd.observe(this, new Observer<Command>() {
            @Override
            public void onChanged(@Nullable Command command) {
                populateCmdName(command);
            }
        });
    }

    private void setUpEdit(Alias alias) {
        if(alias_id >= 0) {
            EditText edit = findViewById(R.id.nalias_alias_input);
            if(cmd_id >= 0 && alias != null)
                edit.setText(alias.getName());
        }
    }

    private void populateCmdName(Command command) {
        TextView cmd_name = findViewById(R.id.nalias_cmd_name);
        if(cmd_id >= 0 && command != null)
            cmd_name.setText(command.getName());
    }

    private void showInvalidAliasDialog(String msg) {
        MyDialogBox invalidAlias = new MyDialogBox();
        invalidAlias.setDialogMessage(msg);
        invalidAlias.setBuilder(new AlertDialog.Builder(this));
        invalidAlias.getBuilder().setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Confirmed
            }
        });
        invalidAlias.show(this.getFragmentManager(), "tututu");
    }

    private void showConfirmEditDialog(String msg, final Alias alias){
        MyDialogBox confirmEdit = new MyDialogBox();
        confirmEdit.setDialogMessage(msg);
        confirmEdit.setBuilder(new AlertDialog.Builder(this));
        confirmEdit.getBuilder().setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                goToDescr(alias);
            }
        });
        confirmEdit.getBuilder().setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        confirmEdit.show(this.getFragmentManager(), "tatata");
    }

    /* Transition functions */
    public void onBackButtonPressed(View view) {
        Intent intent = new Intent(this, CommandDescriptionActivity.class);
        intent.putExtra(CommandDescriptionActivity.CMD_ID, cmd_id);
        startActivity(intent);
    }

    public void onAddButtonPressed(View view) {
        EditText newAlias = findViewById(R.id.nalias_alias_input);
        Command cmd = newAliasModel.cmd.getValue();
        String aliasTyped = "";
        if(newAlias.getText() != null) {
            aliasTyped = newAlias.getText().toString();
        }

        if(cmd != null && validateAlias(aliasTyped)) {
            Alias alias = new Alias(cmd_id, aliasTyped, false);
            if(!isEdit){
                newAliasModel.addAlias(alias);
                goToDescr(alias);
            } else {
                alias.setId_alias(alias_id);
                showConfirmEditDialog(getString(R.string.confirm_edit_text) + " \"" + alias.getName() + "\"",
                        alias);
            }
        } else {
            showInvalidAliasDialog(getString(R.string.invalid_alias));
        }
    }

    private void goToDescr(Alias alias) {
        if(isEdit) {
            newAliasModel.updateAlias(alias);
        } else {
            newAliasModel.addAlias(alias);
        }

        Intent intent = new Intent(this, CommandDescriptionActivity.class);
        intent.putExtra(CommandDescriptionActivity.CMD_ID, cmd_id);
        startActivity(intent);

    }

    private boolean validateAlias(String alias){
        boolean isValid = true;
        if(alias == null) {
            isValid = false;
        } else if(alias.length() < Alias.MIN_ALIAS_LENGTH) {
            isValid = false;
        } else if(alias.length() > Alias.MAX_ALIAS_LENGTH) {
            isValid = false;
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
