package com.tb.rita.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

import com.tb.rita.R;

import java.util.ArrayList;

import domain.Command;

/**
 * This class is responsible by the main activity.
 * @author Breno Viana
 * @version 2017/10/19
 */
public class MainActivity extends AppCompatActivity {

    ArrayList<Command> commands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content
        setContentView(R.layout.home_screen);
        Intent intent = getIntent();
    }

    public void OnHelpButtonPressed(View view) {
        Intent toHelp = new Intent(this, HelpActivity.class);
        startActivity(toHelp);
    }

    public void OnCommandsButtonPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        startActivity(toCmdList);
    }
}
