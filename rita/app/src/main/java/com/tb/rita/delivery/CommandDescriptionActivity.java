package com.tb.rita.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thales on 04/11/17.
 */

public class CommandDescriptionActivity extends AppCompatActivity {

    // Views
    private ListView alias_list;
    private TextView cmdNameView;

    private List<String> aliases;
    private String cmdName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_descr_screen);
        // Initialize class properties
        aliases = new ArrayList<>();
        createAliases();

        alias_list = (ListView) findViewById(R.id.descr_alias_list);
        populateAliasList();
        populateCmdName();
    }

    private void createAliases() {
        aliases.add("ALIAS 1");
        aliases.add("ALIAS 2");
        aliases.add("ALIAS 3");
        aliases.add("ALIAS 4");
        aliases.add("ALIAS 5");
        aliases.add("ALIAS 6");
    }

    private void populateAliasList() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, aliases);
        alias_list.setAdapter(adapter);
    }

    private void populateCmdName() {
        Intent fromDescr = getIntent();
        cmdName = fromDescr.getStringExtra(CommandsListActivity.CMD_SELECTED);
        cmdNameView = (TextView) findViewById(R.id.descr_cmd_name);
        cmdNameView.setText(cmdName);
    }

    public void OnBackButtonPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        startActivity(toCmdList);
    }

    public void addAlias(String alias) {
        if(alias != null && alias.length() > 1) {
            aliases.add(alias);
        }
    }
}
