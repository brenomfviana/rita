package com.tb.rita.delivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tb.rita.R;

import org.w3c.dom.Text;

public class NewAliasActivity extends AppCompatActivity {

    public static final String NEW_ALIAS_NAME = "INPUT FROM TEXT FIELD";

    private String cmdName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alias_screen);
        populateCmdName();
    }

    private void populateCmdName() {
        Intent intent = getIntent();
        cmdName = intent.getStringExtra(CommandDescriptionActivity.CMD_NAME);
        TextView cmdNameView = (TextView) findViewById(R.id.nalias_cmd_name);
        cmdNameView.setText(cmdName);
    }

    /* Transition functions */

    public void onBackButtonPressed(View view) {
        Intent intent = new Intent(this, CommandDescriptionActivity.class);
        intent.putExtra(CommandDescriptionActivity.CMD_NAME, cmdName);
        startActivity(intent);
    }

    public void onAddButtonPressed(View view) {
        Intent intent = new Intent(this, CommandsListActivity.class);

        EditText newAlias = (EditText) findViewById(R.id.nalias_alias_input);

        intent.putExtra(CommandDescriptionActivity.CMD_NAME, cmdName);
        intent.putExtra(NEW_ALIAS_NAME, newAlias.getText());
        startActivity(intent);
    }
}
