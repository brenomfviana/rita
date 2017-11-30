package domain;

import com.tb.rita.R;

/**
 * Created by thales on 04/11/17.
 */

public enum Appliance {
    FAN,
    LIGHT,
    TV;

    @Override
    public String toString() {
        String str = "";
        if(this == FAN) {
            str = "VENTILADOR";
        } else if(this == LIGHT) {
            str = "LUZ";
        } else if(this == TV) {
            str = "TELEVISAO";
        }
        return str;
    }
}
