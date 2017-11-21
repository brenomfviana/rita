package domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by thales on 17/11/17.
 */

@Entity(tableName = "alias",
        foreignKeys = @ForeignKey(entity = Command.class,
                                           parentColumns = "id_cmd",
                                           childColumns = "id_cmd"),
        indices = {@Index(value = "name", unique = true)})
public class Alias implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_alias")
    private int id_alias;

    @ColumnInfo(name = "id_cmd")
    private int id_cmd;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "is_synonym")
    private boolean isSynonym;

    public Alias(int id_cmd, String name, boolean isSynonym) {
        this.id_cmd = id_cmd;
        this.name = name;
        this.isSynonym = isSynonym;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getId_alias() {
        return id_alias;
    }

    public void setId_alias(int id_alias) {
        this.id_alias = id_alias;
    }

    public int getId_cmd() {
        return id_cmd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSynonym() {
        return isSynonym;
    }

    public void setSynonym(boolean synonym) {
        isSynonym = synonym;
    }

}
