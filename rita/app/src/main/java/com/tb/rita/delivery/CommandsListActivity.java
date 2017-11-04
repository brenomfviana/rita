package com.tb.rita.delivery;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.List;

public class CommandsListActivity extends AppCompatActivity {

    private List<String> commands;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_list_screen);
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        commands = new ArrayList();
    }


    protected boolean validateCommand(String cmd) {
        return false;
    }

    public void onBackButtonPressed(View view) {
        Intent toHome = new Intent(this, MainActivity.class);
        startActivity(toHome);
    }

    public void onAddButtonPressed(View view) {
        Intent toNewCmd = new Intent(this, NewCommandActivity.class);
        startActivity(toNewCmd);
    }


}
