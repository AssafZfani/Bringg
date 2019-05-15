package zfani.assaf.bringg.models;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PresenceRepository {

    private final PresenceDao presenceDao;

    public PresenceRepository(Context context) {
        presenceDao = PresenceDataBase.getDatabase(context).getPresenceDAO();
    }

    public LiveData<List<Presence>> getAllPresences() {
        return presenceDao.getAllPresences();
    }

    public void insert(long time) {
        new InsertPresenceTask(presenceDao).execute(time);
    }

    public void update(long time) {
        new UpdatePresenceTask(presenceDao).execute(time);
    }

    private static class InsertPresenceTask extends AsyncTask<Long, Void, Void> {

        private final PresenceDao presenceDao;

        InsertPresenceTask(PresenceDao presenceDao) {
            this.presenceDao = presenceDao;
        }

        @Override
        protected Void doInBackground(Long... times) {
            presenceDao.insertPresence(new Presence(times[0]));
            return null;
        }
    }

    private static class UpdatePresenceTask extends AsyncTask<Long, Void, Void> {

        private final PresenceDao presenceDao;

        UpdatePresenceTask(PresenceDao presenceDao) {
            this.presenceDao = presenceDao;
        }

        @Override
        protected Void doInBackground(Long... times) {
            presenceDao.updatePresence(times[0]);
            return null;
        }
    }
}
