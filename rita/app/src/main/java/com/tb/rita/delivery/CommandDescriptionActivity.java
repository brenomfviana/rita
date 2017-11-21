package com.tb.rita.delivery;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;

import domain.Alias;
import domain.Command;
import domain.models.CommandDescriptionViewModel;

/**
 * Created by thales on 04/11/17.
 */

public class CommandDescriptionActivity extends AppCompatActivity {


    // Views
    private ListView alias_list;
    private int id_cmd;
    private CommandDescriptionViewModel cmdDescrModel;
    public static final String CMD_ID = "ID OF THE SELECTED COMMAND";
    public static final String ALIAS_ID = "ID OF THE SELECTED ALIAS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_descr_screen);
        // Initialize class properties
        Intent intent = getIntent();
        id_cmd = intent.getIntExtra(CMD_ID, -1);
        alias_list = findViewById(R.id.descr_alias_list);

        cmdDescrModel = ViewModelProviders.of(this).get(CommandDescriptionViewModel.class);
        cmdDescrModel.createDb();
        cmdDescrModel.init(id_cmd);
        subscribeData();
    }

    private void subscribeData() {
        cmdDescrModel.command.observe(this, new Observer<Command>() {
            @Override
            public void onChanged(@Nullable Command command) {
                populateCmdName(command);
            }
        });

        cmdDescrModel.aliases.observe(this, new Observer<List<Alias>>() {
            @Override
            public void onChanged(@Nullable List<Alias> aliases) {
                populateAliasList(aliases);
            }
        });
    }

    private void populateAliasList(List<Alias> aliases) {
        if(aliases == null)
            aliases = new ArrayList<>();
        final ArrayAdapter<Alias> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, aliases);
        alias_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onAliasPressed(adapter.getItem(i).getId_alias(), view);
            }
        });
        alias_list.setAdapter(adapter);
    }

    private void populateCmdName(Command cmd) {
        if(cmd == null)
            cmd = new Command("", 1);
        TextView cmdNameView = findViewById(R.id.descr_cmd_name);
        cmdNameView.setText(cmd.getName());
    }

    public void onNewAliasButtonPressed(View view) {
        Intent toNewAlias = new Intent(this, NewAliasActivity.class);
        toNewAlias.putExtra(CMD_ID, id_cmd);
        startActivity(toNewAlias);
    }

    public void OnBackButtonPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        startActivity(toCmdList);
    }

    private void onAliasPressed(int alias_id, View view) {
        Intent intent = new Intent(this, NewAliasActivity.class);
        intent.putExtra(CMD_ID, id_cmd);
        intent.putExtra(NewAliasActivity.IS_EDIT, true);
        intent.putExtra(ALIAS_ID, alias_id);
        startActivity(intent);
    }
}
