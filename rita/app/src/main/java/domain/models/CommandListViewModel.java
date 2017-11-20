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
        commands = new LiveData<List<Command>>() {};
        Thread worker = new Thread() {
            @Override
            public void run() {
                commands = mDb.commandDao().getAll();
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
        mDb = AppDatabase.getINSTANCE(this.getApplication());
    }
}
