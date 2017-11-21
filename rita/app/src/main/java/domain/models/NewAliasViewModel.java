package domain.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import domain.Alias;
import domain.Command;
import domain.dao.AppDatabase;

/**
 * Created by thalesaguiar on 21/11/2017.
 */

public class NewAliasViewModel extends AndroidViewModel {

    public LiveData<Command> cmd;
    public LiveData<Alias> alias;
    private AppDatabase mDb;

    public NewAliasViewModel(@NonNull Application application) {
        super(application);
        createDb();

        cmd = new LiveData<Command>() {
            @Override
            protected void setValue(Command value) {
                super.setValue(new Command("", 1));
            }
        };
        alias = new LiveData<Alias>() {
            @Override
            protected void setValue(Alias value) {
                super.setValue(new Alias(0, "ERROR", false));
            }
        };
    }

    public void getCmd(final int id) {
        createDb();
        cmd = mDb.commandDao().findCommandById(id);
    }

    public void getAlias(int id) {
        createDb();
        alias = mDb.aliasDao().getById(id);
    }

    public void addAlias(final Alias alias) {
        Thread worker = new Thread() {
            @Override
            public void run() {
                super.run();
                mDb.aliasDao().insertAll(alias);
            }
        };
        worker.start();
        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateAlias(final Alias alias) {
        Thread worker = new Thread() {
            @Override
            public void run() {
                super.run();
                if(alias != null)
                    mDb.aliasDao().updateAux(alias.getName(), alias.getId_alias());
            }
        };

        worker.start();
        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createDb() {
        mDb = AppDatabase.getDatabase(this.getApplication());
    }
}
