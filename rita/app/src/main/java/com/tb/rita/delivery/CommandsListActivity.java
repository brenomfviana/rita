package com.tb.rita.delivery;

import android.arch.lifecycle.LiveData;
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

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;

import domain.Command;
import domain.dao.AppDatabase;
import domain.models.CommandListViewModel;

public class CommandsListActivity extends AppCompatActivity {

    private CommandListViewModel cmdModel;

    public static final String CMD_SELECTED = "SELECTED COMMAND POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_list_screen);
        cmdModel = ViewModelProviders.of(this).get(CommandListViewModel.class);
        subscribeCommands();
        cmdModel.createDb();
        if(cmdModel.commands.getValue() == null)
            showCmds(new ArrayList<Command>());
        else
            showCmds(cmdModel.commands.getValue());
    }

    private void subscribeCommands() {
        if(cmdModel.commands == null)
            cmdModel.commands = new LiveData<List<Command>>() {};
        cmdModel.commands.observe(this, new Observer<List<Command>>() {
            @Override
            public void onChanged(@Nullable List<Command> commands) {
                showCmds(commands);
            }
        });
    }

    private void showCmds(final List<Command> commands) {
        ListView cmdList = findViewById(R.id.lst_cmds_list);
        ArrayAdapter<Command> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, commands);
        cmdList.setAdapter(adapter);

        // Add listener to list item
        cmdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onCmdPressed(position, view, commands);
            }
        });
    }

    /** Transition functions **/

    public void onBackButtonPressed(View view) {
        Intent toHome = new Intent(this, MainActivity.class);
        startActivity(toHome);
    }

    public void onAddButtonPressed(View view) {
        Intent toNewCmd = new Intent(this, NewCommandActivity.class);
        startActivity(toNewCmd);
    }

    public void onCmdPressed(int position, View view, List<Command> commands) {
        Intent toCmdDescr = new Intent(this, CommandDescriptionActivity.class);
        /* Pass the cmd name to command description
           Change later to pass an instance of the command */
        toCmdDescr.putExtra(CMD_SELECTED, commands.get(position).getId_cmd());
        startActivity(toCmdDescr);
    }

}
