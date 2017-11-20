package domain.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import domain.Command;
import domain.dao.AppDatabase;

/**
 * Created by thalesaguiar on 20/11/2017.
 */

public class CommandViewModel extends AndroidViewModel {

    public LiveData<List<Command>> commands;
    private AppDatabase mDb;

    CommandViewModel(Application application) {
        super(application);
        createDb();
        new Thread() {
            @Override
            public void run() {
                commands = mDb.commandDao().getAll();
            }
        }.start();
    }

    public void createDb() {
        mDb = AppDatabase.getINSTANCE(this.getApplication());
    }
}
