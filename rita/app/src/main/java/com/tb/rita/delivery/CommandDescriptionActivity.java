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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thales on 04/11/17.
 */

public class CommandDescriptionActivity extends AppCompatActivity {

    public static final String CMD_NAME = "NAME OF CMD DESCRIPTED";
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
        checkNewAlias();

        alias_list = (ListView) findViewById(R.id.descr_alias_list);
        populateAliasList();
        populateCmdName();

    }

    private void populateAliasList() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, aliases);
        alias_list.setAdapter(adapter);
    }

    private void populateCmdName() {
        Intent intent = getIntent();
        // Get the previous context
        cmdName = intent.getStringExtra(CMD_NAME);
        TextView cmdNameView = (TextView) findViewById(R.id.descr_cmd_name);
        cmdNameView.setText(cmdName);
    }

    public void onNewAliasButtonPressed(View view) {
        Intent toNewAlias = new Intent(this, NewAliasActivity.class);
        toNewAlias.putExtra(CMD_NAME, cmdName);
        startActivity(toNewAlias);
    }

    public void OnBackButtonPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        startActivity(toCmdList);
    }

    private void checkNewAlias() {
        Intent intent = getIntent();
        String newAlias = intent.getStringExtra(NewAliasActivity.NEW_ALIAS_NAME);
        if(newAlias != null && newAlias.length() > 1 && newAlias.length() < 15) {
            aliases.add(newAlias);
        }
    }
}
