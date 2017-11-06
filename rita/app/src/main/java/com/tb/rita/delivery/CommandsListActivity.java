package com.tb.rita.delivery;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;

import domain.Appliance;
import domain.Command;

public class CommandsListActivity extends AppCompatActivity {

    private ListView cmdList;
    private List<Command> commands;

    public static final String CMD_SELECTED = "The command selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_list_screen);

        commands = new ArrayList<>();
        populateCommands();
        checkCommandsUpdate();

        cmdList = (ListView) findViewById(R.id.lst_cmds_list);
        populateCmdView();
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void populateCommands() {
        commands.add(new Command("Ligar ventilador", Appliance.FAN));
        commands.add(new Command("Ligar Televisão", Appliance.TV));
    }

    private void checkCommandsUpdate() {
        Intent intent = getIntent();

        if(intent.getSerializableExtra(CMD_SELECTED) != null) {
            Command newCmd = (Command) intent.getSerializableExtra(CMD_SELECTED);

            for(int i = 0; i < commands.size(); i++) {
                String newCmdName = newCmd.getName().trim().toUpperCase();
                String cmdName = commands.get(i).getName().trim().toUpperCase();

                if(cmdName.equals(newCmdName)) {
                    commands.set(i, newCmd);
                }
            }
        }
    }

    private void populateCmdView() {
        ArrayAdapter<Command> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, commands);
        cmdList.setAdapter(adapter);

        // Add listener to list item
        cmdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onCmdPressed(position, view);
            }
        });
    }

    public void onBackButtonPressed(View view) {
        Intent toHome = new Intent(this, MainActivity.class);
        startActivity(toHome);
    }

    public void onAddButtonPressed(View view) {
        Intent toNewCmd = new Intent(this, NewCommandActivity.class);
        startActivity(toNewCmd);
    }

    public void onCmdPressed(int position, View view) {
        Intent toCmdDescr = new Intent(this, CommandDescriptionActivity.class);
        /* Pass the cmd name to command description
           Change later to pass an instance of the command*/
        TextView pressedBtn = (TextView) view;
        String cmd_name = pressedBtn.getText().toString();
        toCmdDescr.putExtra(CommandDescriptionActivity.CMD_NAME, cmd_name);
        toCmdDescr.putExtra(CMD_SELECTED, commands.get(position));
        startActivity(toCmdDescr);
    }

}
