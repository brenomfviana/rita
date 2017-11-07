package domain;

/**
 * Created by thales on 04/11/17.
 */

public enum Appliance {
    FAN,
    HEATER,
    TV;

    @Override
    public String toString() {
        String str = "";
        if(this == FAN) {
            str = "FAN";
        } else if(this == HEATER) {
            str = "HEATER";
        } else if(this == TV) {
            str = "TV";
        }
        return str;
    }
}
