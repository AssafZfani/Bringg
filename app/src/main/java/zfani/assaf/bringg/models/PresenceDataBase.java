package zfani.assaf.bringg.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Presence.class}, version = 1, exportSchema = false)
abstract class PresenceDataBase extends RoomDatabase {

    private static PresenceDataBase INSTANCE;

    static PresenceDataBase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, PresenceDataBase.class, "presence.db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    abstract PresenceDao getPresenceDAO();
}
