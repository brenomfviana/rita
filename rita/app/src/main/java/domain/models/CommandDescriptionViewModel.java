package domain.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import domain.Alias;
import domain.Command;
import domain.dao.AppDatabase;

/**
 * Created by thalesaguiar on 20/11/2017.
 */

public class CommandDescriptionViewModel extends AndroidViewModel {

    public LiveData<List<Alias>> aliases;
    public LiveData<Command> command;
    private AppDatabase mDb;

    CommandDescriptionViewModel(Application application) {
        super(application);
        createDb();
        aliases = new LiveData<List<Alias>>() {
            @Override
            protected void setValue(List<Alias> value) {
                super.setValue(new ArrayList<Alias>());
            }
        };

        command = new LiveData<Command>() {
            @Override
            protected void setValue(Command value) {
                super.setValue(new Command("ERROR", 1));
            }
        };
    }

    public void init(int cmd_id) {
        getCmd(cmd_id);
        findByCmd(cmd_id);
    }

    private void getCmd(final int id) {
        createDb();
        command = mDb.commandDao().findCommandById(id);
    }

    private void findByCmd(int id) {
        createDb();
        aliases = mDb.aliasDao().getByCmd(id);
    }


    public void createDb() {
        mDb = AppDatabase.getDatabase(this.getApplication());
    }
}
