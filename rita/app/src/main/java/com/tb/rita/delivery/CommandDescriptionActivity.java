package com.tb.rita.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tb.rita.R;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import domain.Command;

/**
 * Created by thales on 04/11/17.
 */

public class CommandDescriptionActivity extends AppCompatActivity {


    // Views
    private ListView alias_list;

    private int pos;
    private List<String> aliases;
    private ArrayList<Command> commands;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_descr_screen);
        // Initialize class properties
        Intent intent = getIntent();

        commands = (ArrayList<Command>) intent.getSerializableExtra(CommandsListActivity.CMD_LIST);

        pos = intent.getIntExtra(CommandsListActivity.CMD_SELECTED, -1);
        if(pos >= 0) {
            aliases = commands.get(pos).getAliases();
        } else {
            aliases = new ArrayList<>();
        }

        alias_list = (ListView) findViewById(R.id.descr_alias_list);

        populateAliasList();
        populateCmdName();
    }

    private void populateAliasList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, aliases);
        alias_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onAliasPressed(i, view);
            }
        });
        alias_list.setAdapter(adapter);
    }

    private void populateCmdName() {
        TextView cmdNameView = (TextView) findViewById(R.id.descr_cmd_name);
        cmdNameView.setText(commands.get(pos).getName());
    }

    public void onNewAliasButtonPressed(View view) {
        Intent toNewAlias = new Intent(this, NewAliasActivity.class);
        toNewAlias.putExtra(CommandsListActivity.CMD_LIST, commands);
        toNewAlias.putExtra(CommandsListActivity.CMD_SELECTED, pos);
        startActivity(toNewAlias);
    }

    public void OnBackButtonPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        toCmdList.putExtra(CommandsListActivity.CMD_LIST, commands);
        startActivity(toCmdList);
    }

    private void onAliasPressed(int position, View view) {
        Intent intent = new Intent(this, NewAliasActivity.class);
        intent.putExtra(CommandsListActivity.CMD_LIST, commands);
        intent.putExtra(CommandsListActivity.CMD_SELECTED, pos);
        intent.putExtra(NewAliasActivity.IS_EDIT, true);
        intent.putExtra(NewAliasActivity.ALIAS_POSITION, position);
        startActivity(intent);
    }
}
