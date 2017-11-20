package domain.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import domain.Alias;
import domain.dao.AppDatabase;

/**
 * Created by thalesaguiar on 20/11/2017.
 */

public class AliasViewModel extends AndroidViewModel {

    public LiveData<List<Alias>> aliases;
    private AppDatabase mDb;

    AliasViewModel(Application application) {
        super(application);
        createDb();
        new Thread() {
            @Override
            public void run() {
                super.run();
                aliases = mDb.aliasDao().getAll();
            }
        }.start();
    }

    AliasViewModel(Application application, int cmdId) {
        super(application);
        createDb();
        final int tmpId = cmdId;
        new Thread() {
            @Override
            public void run() {
                super.run();
                aliases = mDb.aliasDao().getByCmd(tmpId);
            }
        }.start();
    }


    private void createDb() {
        mDb = AppDatabase.getINSTANCE(this.getApplication());
    }
}
