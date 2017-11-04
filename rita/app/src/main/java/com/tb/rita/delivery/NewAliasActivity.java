package com.tb.rita.delivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tb.rita.R;

public class NewAliasActivity extends AppCompatActivity {

    public static final String NEW_ALIAS_NAME = "INPUT FROM TEXT FIELD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alias_screen);
    }


    /* Transition functions */
    public void onBackButtonPressed(View view) {
        Intent toCmdDescr = new Intent(this, NewAliasActivity.class);
        startActivity(toCmdDescr);
    }
}
