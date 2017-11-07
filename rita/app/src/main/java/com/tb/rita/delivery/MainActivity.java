package com.tb.rita.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
        commands = (ArrayList<Command>) intent.getSerializableExtra(CommandsListActivity.CMD_LIST);
    }

    public void OnHelpButtonPressed(View view) {
        Intent toHelp = new Intent(this, HelpActivity.class);
        startActivity(toHelp);
    }

    public void OnCommandsButtonPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        toCmdList.putExtra(CommandsListActivity.CMD_LIST, commands);
        startActivity(toCmdList);
    }
}
