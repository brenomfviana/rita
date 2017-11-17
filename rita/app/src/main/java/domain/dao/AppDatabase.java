package domain.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import domain.Alias;
import domain.Command;

/**
 * Created by thales on 17/11/17.
 */
@Database(entities = {Command.class, Alias.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CommandDao commandDao();
    public abstract AliasDao aliasDao();
}
