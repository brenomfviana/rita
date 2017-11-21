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
import domain.dao.AppDatabase;
import domain.dao.CommandDao;
import domain.models.CommandDescriptionViewModel;
import domain.models.CommandListViewModel;

/**
 * Created by thales on 04/11/17.
 */

public class CommandDescriptionActivity extends AppCompatActivity {


    // Views
    private ListView alias_list;
    private int pos;
    private CommandDescriptionViewModel cmdDescrModel;
    private List<Command> cmds;
    public static final String CMD_ID = "ID OF THE SELECTED COMMAND";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_descr_screen);
        // Initialize class properties
        Intent intent = getIntent();
        pos = intent.getIntExtra(CMD_ID, -1);
        alias_list = findViewById(R.id.descr_alias_list);

        cmdDescrModel = ViewModelProviders.of(this).get(CommandDescriptionViewModel.class);
        cmdDescrModel.createDb();
        cmdDescrModel.init(pos);
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
        ArrayAdapter<Alias> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, aliases);
        alias_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onAliasPressed(i, view);
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
        toNewAlias.putExtra(CommandsListActivity.CMD_SELECTED, pos);
        startActivity(toNewAlias);
    }

    public void OnBackButtonPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        startActivity(toCmdList);
    }

    private void onAliasPressed(int position, View view) {
        Intent intent = new Intent(this, NewAliasActivity.class);
        intent.putExtra(CommandsListActivity.CMD_SELECTED, pos);
        intent.putExtra(NewAliasActivity.IS_EDIT, true);
        intent.putExtra(NewAliasActivity.ALIAS_POSITION, position);
        startActivity(intent);
    }
}
