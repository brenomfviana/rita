package domain.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import domain.Alias;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by thales on 17/11/17.
 */

@Dao
public interface AliasDao {

    @Query("SELECT * FROM alias WHERE id_cmd = :id_cmd")
    LiveData<List<Alias>> getByCmd(int id_cmd);

    @Query("SELECT * FROM alias")
    LiveData<List<Alias>> getAll();

    @Insert(onConflict = REPLACE)
    void insertAll(Alias...alias);

    @Delete
    void delete(Alias alias);
}
