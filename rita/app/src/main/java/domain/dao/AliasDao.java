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

    @Query("SELECT * FROM alias WHERE id_cmd = :idCmd")
    LiveData<List<Alias>> getByCmd(int idCmd);

    @Query("SELECT * FROM alias WHERE id_alias = :idAlias")
    LiveData<Alias> getById(int idAlias);

    @Query("SELECT * FROM alias")
    LiveData<List<Alias>> getAll();

    @Query("UPDATE alias SET name = :newName WHERE id_alias = :idAlias")
    void updateAux(String newName, int idAlias);

    @Insert(onConflict = REPLACE)
    void insertAll(Alias...alias);

    @Delete
    void delete(Alias alias);

    @Update
    void update(Alias alias);
}
