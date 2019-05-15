package zfani.assaf.bringg.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PresenceDao {

    @Query("Select * from presence_table")
    LiveData<List<Presence>> getAllPresences();

    @Insert(onConflict = REPLACE)
    void insertPresence(Presence presence);

    @Query("Update presence_table set endTs = :endTs where endTs = 0")
    void updatePresence(long endTs);
}
