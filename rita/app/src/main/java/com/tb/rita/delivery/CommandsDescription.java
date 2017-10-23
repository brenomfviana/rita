package com.tb.rita.delivery;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import com.tb.rita.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thales on 22/10/17.
 */

public class CommandsDescription extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content
        setContentView(R.layout.commands_descr);

        // Expansible List View
        ExpandableListView aliases = (ExpandableListView) findViewById(R.id.aliases);

        List aliasesGroups = new ArrayList<String>();
        aliasesGroups.add(getResources().getString(R.string.expandableGroup1));

        List<String> myAliases = new ArrayList<String>();
        myAliases.add(getResources().getString(R.string.alias1));
        myAliases.add(getResources().getString(R.string.alias2));
        myAliases.add(getResources().getString(R.string.alias3));

        HashMap groupAlias = new HashMap<List<String>, List<String>>();
        groupAlias.put(aliasesGroups, myAliases);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
