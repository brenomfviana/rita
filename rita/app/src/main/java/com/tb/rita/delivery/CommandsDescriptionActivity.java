package com.tb.rita.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thales on 22/10/17.
 */

public class CommandsDescriptionActivity extends AppCompatActivity {

    private List<String> commands;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content
        setContentView(R.layout.commands_descr);
        init();
    }

    private void init() {
        commands = new ArrayList();
    }

    public void OnBackPressed(View view) {
        Intent toHome = new Intent(this, MainActivity.class);
        startActivity(toHome);
    }

    public void OnAddPressed() {

    }
}
