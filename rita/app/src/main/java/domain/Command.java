package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thales on 04/11/17.
 */

public class Command implements Serializable {

    public static final int MAX_ALIAS_LENGTH = 15;
    public static final int MIN_ALIAS_LENGTH = 1;

    private String name;
    private List<String> aliases;
    private List<String> sinonyms;
    private Appliance appliance;

    public Command(String name, Appliance appliance) {
        this.name = name;
        this.appliance = appliance;
        aliases = new ArrayList<>();
        sinonyms = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<String> getSinonyms() {
        return sinonyms;
    }

    public void setSinonyms(List<String> sinonyms) {
        this.sinonyms = sinonyms;
    }

    public Appliance getAppliance() {
        return appliance;
    }

    public void setAppliance(Appliance appliance) {
        this.appliance = appliance;
    }
}
