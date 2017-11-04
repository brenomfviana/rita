package com.tb.rita.delivery;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;

public class CommandsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_list_screen);
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onBackButtonPressed(View view) {
        Intent toHome = new Intent(this, MainActivity.class);
        startActivity(toHome);
    }

    public void onAddButtonPressed(View view) {
        Intent toNewCmd = new Intent(this, NewCommandActivity.class);
        startActivity(toNewCmd);
    }

    public void onCmdPressed(View view) {
        Intent toCmdDescr = new Intent(this, CommandDescriptionActivity.class);
        /* Pass the cmd name to command description
           Change later to pass an instance of the command*/
        Button pressedBtn = (Button) view;
        String cmd_name = pressedBtn.getText().toString();
        toCmdDescr.putExtra(CommandDescriptionActivity.CMD_NAME, cmd_name);
        startActivity(toCmdDescr);
    }


}
