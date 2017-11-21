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

public class CommandListViewModel extends AndroidViewModel {

    public LiveData<List<Command>> commands;
    private AppDatabase mDb;

    CommandListViewModel(Application application) {
        super(application);
        createDb();
        commands = mDb.commandDao().getAll();
    }

    public void createDb() {
        mDb = AppDatabase.getDatabase(this.getApplication());
    }
}
