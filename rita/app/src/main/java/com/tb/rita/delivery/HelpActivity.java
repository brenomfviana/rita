package com.tb.rita.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tb.rita.R;

/**
 * Created by thales on 02/11/17.
 */

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content
        setContentView(R.layout.help_screen);

        Intent intentFrom = getIntent();
    }


    public void OnBackPressed(View view) {
        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
    }
}
