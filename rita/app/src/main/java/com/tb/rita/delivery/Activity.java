package com.tb.rita.delivery;

/**
 * Created by thales on 04/11/17.
 */

public enum Activity {
    COMMAND_DESCRIPTION,
    COMMAND_LIST,
    NEW_ALIAS,
    HELP,
    MAIN,
    NEW_COMMAND,
    SETTINGS;


    @Override
    public String toString() {
        String str = "";
        if(this == Activity.COMMAND_DESCRIPTION ){
            str = "COMMAND_DESCRIPTION";
        } else if(this == Activity.COMMAND_LIST) {
            str = "COMMAND_LIST";
        } else if(this == Activity.NEW_ALIAS) {
            str = "NEW_ALIAS";
        } else if(this == Activity.HELP) {
            str = "HELP";
        } else if(this == Activity.MAIN) {
            str = "MAIN";
        } else if(this == NEW_COMMAND) {
            str = "NEW_COMMAND";
        } else if(this == Activity.SETTINGS) {
            str = "SETTINGS";
        } else {
            str = "[ERROR]";
        }

        return str;
    }
}
