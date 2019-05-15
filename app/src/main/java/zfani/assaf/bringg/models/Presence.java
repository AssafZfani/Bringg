package zfani.assaf.bringg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "presence_table")
public class Presence {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final long startTs;
    private long endTs;

    Presence(long startTs) {
        this.startTs = startTs;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public long getStartTs() {
        return startTs;
    }

    public long getEndTs() {
        return endTs;
    }

    void setEndTs(long endTs) {
        this.endTs = endTs;
    }
}
