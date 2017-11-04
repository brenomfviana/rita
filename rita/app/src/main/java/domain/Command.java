package domain;

import java.util.List;

/**
 * Created by thales on 04/11/17.
 */

public class Command {

    private String name;
    private List<String> aliases;
    private List<String> sinonyms;
    private Appliance appliance;

    public Command(String name, Appliance appliance) {
        this.name = name;
        this.appliance = appliance;
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
