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
import java.util.List;

import domain.Alias;
import domain.Command;
import domain.dao.AliasDao;
import domain.dao.AppDatabase;
import domain.dao.CommandDao;

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
        final CommandDao cmdDao = AppDatabase.getINSTANCE(this).commandDao();
        final AliasDao aliasDao = AppDatabase.getINSTANCE(this).aliasDao();

        if(intent.getSerializableExtra())

        new Runnable() {
            @Override
            public void run() {
                commands = new ArrayList<>(cmdDao.getAll());

                for(Command cmd : commands) {
                    List<Alias> cmdAlias = aliasDao.getByCmd(cmd.getId_cmd());
                    if(cmdAlias != null)
                        cmd.setAliases(cmdAlias);
                }
            }
        };
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
