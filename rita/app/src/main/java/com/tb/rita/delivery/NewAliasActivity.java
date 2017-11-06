package com.tb.rita.delivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tb.rita.R;

import org.w3c.dom.Text;

import domain.Command;

public class NewAliasActivity extends AppCompatActivity {

    public static final String IS_EDIT = "TO EDIT ALIAS";
    public static final String ALIAS_POSITION = "POSITION OF ALIAS TO MODIFY";

    private Command command;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alias_screen);
        Intent intent = getIntent();
        command = (Command) intent.getSerializableExtra(CommandsListActivity.CMD_SELECTED);
        isEdit = intent.getBooleanExtra(IS_EDIT, false);

        if(isEdit) setUpEdit(intent);

        populateCmdName();
    }

    private void setUpEdit(Intent intent) {
        int position = intent.getIntExtra(ALIAS_POSITION, -1);
        if(position >= 0) {
            Button btn = (Button) findViewById(R.id.nalias_confirm_btn);
            btn.setText(R.string.button_confirm);

            EditText edit = (EditText) findViewById(R.id.nalias_alias_input);
            edit.setText(command.getAliases().get(position));
        }
    }

    private void populateCmdName() {
        TextView cmd_name = (TextView) findViewById(R.id.nalias_cmd_name);
        cmd_name.setText(command.getName());
    }

    /* Transition functions */

    public void onBackButtonPressed(View view) {
        Intent intent = new Intent(this, CommandDescriptionActivity.class);
        intent.putExtra(CommandsListActivity.CMD_SELECTED, command);
        startActivity(intent);
    }

    public void onAddButtonPressed(View view) {
        Intent incoming = getIntent();
        EditText newAlias = (EditText) findViewById(R.id.nalias_alias_input);

        if(newAlias.getText() != null && validateAlias(newAlias.getText().toString())) {
            if(isEdit) {
                if(incoming.getIntExtra(ALIAS_POSITION, -1) != -1) {
                    int position = incoming.getIntExtra(ALIAS_POSITION, -1);
                    command.getAliases().set(position, newAlias.getText().toString());
                }
            } else {
                command.getAliases().add(newAlias.getText().toString());
            }
        }
        Intent intent = new Intent(this, CommandDescriptionActivity.class);
        intent.putExtra(CommandsListActivity.CMD_SELECTED, command);
        startActivity(intent);
    }

    private boolean validateAlias(String alias){
        boolean isValid = true;
        if(alias == null) {
            isValid = false;
        } else if(alias.length() < Command.MIN_ALIAS_LENGTH) {
            isValid = false;
        } else if(alias.length() > Command.MAX_ALIAS_LENGTH) {
            isValid = false;
        }

        for(String alias_ : command.getAliases()) {
            String aux = alias_.trim().toUpperCase();
            String aliasAux = alias.trim().toUpperCase();

            if(aux.equals(aliasAux)) {
                isValid = false;
            }
        }

        return isValid;
    }
}
