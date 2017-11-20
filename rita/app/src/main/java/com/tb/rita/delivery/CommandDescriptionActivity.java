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

import java.util.List;

import domain.Alias;
import domain.Command;
import domain.models.AliasViewModel;
import domain.models.CommandListViewModel;

/**
 * Created by thales on 04/11/17.
 */

public class CommandDescriptionActivity extends AppCompatActivity {


    // Views
    private ListView alias_list;

    private int pos;
    private List<Alias> aliases;
    private CommandListViewModel cmdModel;
    private AliasViewModel aliasViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_descr_screen);
        // Initialize class properties
        Intent intent = getIntent();
        aliasViewModel = ViewModelProviders.of(this).get(AliasViewModel.class);
        pos = intent.getIntExtra(CommandsListActivity.CMD_SELECTED, -1);
        alias_list = findViewById(R.id.descr_alias_list);

        subscribeAliases();
        subscribeCommand();
    }

    private void subscribeCommand() {
        cmdModel.commands.observe(this, new Observer<List<Command>>() {
            @Override
            public void onChanged(@Nullable List<Command> commands) {
                populateCmdName(commands.get(pos));
            }
        });
    }

    private void subscribeAliases() {
        aliasViewModel.aliases.observe(this, new Observer<List<Alias>>() {
            @Override
            public void onChanged(@Nullable List<Alias> aliases) {
                populateAliasList(aliases);
            }
        });
    }

    private void populateAliasList(List<Alias> aliases) {
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
