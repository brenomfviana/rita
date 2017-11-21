package domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thales on 04/11/17.
 */
@Entity(tableName = "command",
        indices = {@Index(value = "name", unique = true)})
public class Command implements Serializable {

    public static final int MAX_ALIAS_LENGTH = 15;
    public static final int MIN_ALIAS_LENGTH = 1;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_cmd")
    private int id_cmd;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "appliance")
    private int appliance;

    @Ignore
    private List<Alias> aliases;

    public Command(String name, int appliance) {
        this.name = name;
        this.appliance = appliance;
        aliases = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId_cmd() {
        return id_cmd;
    }

    public void  setId_cmd(int id_cmd) {
        this.id_cmd = id_cmd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(List<Alias> aliases) {
        this.aliases = aliases;
    }

    public int getAppliance() {
        return appliance;
    }

    public void setAppliance(int appliance) {
        this.appliance = appliance;
    }
}
