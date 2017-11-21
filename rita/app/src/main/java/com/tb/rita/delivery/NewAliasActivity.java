package com.tb.rita.delivery;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tb.rita.R;

import java.util.List;

import domain.Alias;
import domain.Command;
import domain.dao.AliasDao;
import domain.dao.AppDatabase;
import domain.dao.CommandDao;
import domain.models.NewAliasViewModel;

public class NewAliasActivity extends AppCompatActivity {

    public static final String IS_EDIT = "TO EDIT ALIAS";

    NewAliasViewModel newAliasModel;

    private boolean isEdit;
    private int cmd_id;
    private int alias_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alias_screen);

        Intent intent = getIntent();
        cmd_id = intent.getIntExtra(CommandDescriptionActivity.CMD_ID, -1);
        alias_id = intent.getIntExtra(CommandDescriptionActivity.ALIAS_ID, -1);
        isEdit = intent.getBooleanExtra(IS_EDIT, false);

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
        if(newAlias.getText() != null)
            aliasTyped = newAlias.getText().toString();
        if(cmd != null && validateAlias(aliasTyped)) {

            Alias alias = new Alias(cmd_id, aliasTyped, false);
            if(!isEdit){
                newAliasModel.addAlias(alias);
            } else {
                alias.setId_alias(alias_id);
                newAliasModel.updateAlias(alias);
            }

            Intent intent = new Intent(this, CommandDescriptionActivity.class);
            intent.putExtra(CommandDescriptionActivity.CMD_ID, cmd_id);
            startActivity(intent);
        }
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

//        for(Alias alias_ : commands.get(cmd_id).getAliases()) {
//            String aux = alias_.getName().trim().toUpperCase();
//            String aliasAux = alias.trim().toUpperCase();
//
//            if(aux.equals(aliasAux)) {
//                isValid = false;
//            }
//        }

        return isValid;
    }
}
