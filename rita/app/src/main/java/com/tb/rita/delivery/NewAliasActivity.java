package com.tb.rita.delivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tb.rita.R;

import org.w3c.dom.Text;

import domain.Command;

public class NewAliasActivity extends AppCompatActivity {

    public static final String NEW_ALIAS_NAME = "INPUT FROM TEXT FIELD";

    private String cmdName;
    private Command command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alias_screen);
        Intent intent = getIntent();
        command = (Command) intent.getSerializableExtra(CommandsListActivity.CMD_SELECTED);

        populateCmdName();
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
        Intent intent = new Intent(this, CommandDescriptionActivity.class);
        EditText newAlias = (EditText) findViewById(R.id.nalias_alias_input);

        if(newAlias.getText() != null && validateAlias(newAlias.getText().toString())) {
            command.getAliases().add(newAlias.getText().toString());
        }
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
