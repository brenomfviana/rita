package com.tb.rita.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    public static final String CMD_NAME = "The name of the selected cmd";

    // Views
    private ListView alias_list;
    private Command command;

    private List<String> aliases;
    private String cmdName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_descr_screen);
        // Initialize class properties
        Intent intent = getIntent();

        command = (Command) intent.getSerializableExtra(CommandsListActivity.CMD_SELECTED);

        aliases = command.getAliases();

        alias_list = (ListView) findViewById(R.id.descr_alias_list);
        populateAliasList();
        populateCmdName();
    }

    private void populateAliasList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, aliases);
        alias_list.setAdapter(adapter);
    }

    private void populateCmdName() {
        TextView cmdNameView = (TextView) findViewById(R.id.descr_cmd_name);
        cmdNameView.setText(command.getName());
    }

    public void onNewAliasButtonPressed(View view) {
        Intent toNewAlias = new Intent(this, NewAliasActivity.class);
        toNewAlias.putExtra(CommandsListActivity.CMD_SELECTED, command);
        startActivity(toNewAlias);
    }

    public void OnBackButtonPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        toCmdList.putExtra(CommandsListActivity.CMD_SELECTED, command);
        startActivity(toCmdList);
    }
}
