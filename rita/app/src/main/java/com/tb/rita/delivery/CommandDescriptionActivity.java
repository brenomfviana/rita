package com.tb.rita.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tb.rita.R;

/**
 * Created by thales on 04/11/17.
 */

public class CommandDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commands_descr_screen);
    }

    public void OnBackButtonPressed(View view) {
        Intent toCmdList = new Intent(this, CommandsListActivity.class);
        startActivity(toCmdList);
    }
}
