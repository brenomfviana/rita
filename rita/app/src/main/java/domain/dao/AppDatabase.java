package domain.dao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import domain.Alias;
import domain.Command;

/**
 * Created by thales on 17/11/17.
 */
@Database(entities = {Command.class, Alias.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract CommandDao commandDao();
    public abstract AliasDao aliasDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //
        }
    };

    public static AppDatabase getINSTANCE(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                            AppDatabase.class,
                                            "myDb").addMigrations(MIGRATION_1_2).build();
        }
        return INSTANCE;
    }
}
