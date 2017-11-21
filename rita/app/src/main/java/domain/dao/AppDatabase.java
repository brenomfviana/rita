package domain.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import domain.Alias;
import domain.Command;

/**
 * Created by thales on 17/11/17.
 */
@Database(entities = {Command.class, Alias.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase database;

    public abstract CommandDao commandDao();
    public abstract AliasDao aliasDao();

    public static AppDatabase getDatabase(Context context) {
        if(database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                                                    AppDatabase.class, "AppDb").build();
        }
        return database;
    }
}
