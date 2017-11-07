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

import java.util.ArrayList;

import domain.Command;

public class NewAliasActivity extends AppCompatActivity {

    public static final String IS_EDIT = "TO EDIT ALIAS";
    public static final String ALIAS_POSITION = "POSITION OF ALIAS TO MODIFY";

    private Command command;
    private boolean isEdit;

    private int pos;
    private ArrayList<Command> commands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alias_screen);
        Intent intent = getIntent();
        commands = (ArrayList<Command>) intent.getSerializableExtra(CommandsListActivity.CMD_LIST);
        pos = intent.getIntExtra(CommandsListActivity.CMD_SELECTED, -1);

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
            if(pos >= 0)
                edit.setText(commands.get(pos).getAliases().get(position));
        }
    }

    private void populateCmdName() {
        TextView cmd_name = (TextView) findViewById(R.id.nalias_cmd_name);
        if(pos >= 0)
            cmd_name.setText(commands.get(pos).getName());
    }

    /* Transition functions */

    public void onBackButtonPressed(View view) {
        Intent intent = new Intent(this, CommandDescriptionActivity.class);
        intent.putExtra(CommandsListActivity.CMD_LIST, commands);
        intent.putExtra(CommandsListActivity.CMD_SELECTED, pos);
        startActivity(intent);
    }

    public void onAddButtonPressed(View view) {
        Intent incoming = getIntent();
        EditText newAlias = (EditText) findViewById(R.id.nalias_alias_input);

        if(newAlias.getText() != null
                && validateAlias(newAlias.getText().toString())
                && pos >= 0) {
            if(isEdit) {
                if(incoming.getIntExtra(ALIAS_POSITION, -1) != -1) {
                    int position = incoming.getIntExtra(ALIAS_POSITION, -1);
                    commands.get(pos).getAliases().set(position, newAlias.getText().toString());
                }
            } else {
                commands.get(pos).getAliases().add(newAlias.getText().toString());
            }
        }
        Intent intent = new Intent(this, CommandDescriptionActivity.class);
        intent.putExtra(CommandsListActivity.CMD_LIST, commands);
        intent.putExtra(CommandsListActivity.CMD_SELECTED, pos);
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

        for(String alias_ : commands.get(pos).getAliases()) {
            String aux = alias_.trim().toUpperCase();
            String aliasAux = alias.trim().toUpperCase();

            if(aux.equals(aliasAux)) {
                isValid = false;
            }
        }

        return isValid;
    }
}
