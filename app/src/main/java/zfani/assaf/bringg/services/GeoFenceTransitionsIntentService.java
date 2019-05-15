package zfani.assaf.bringg.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import zfani.assaf.bringg.models.PresenceRepository;

public class GeoFenceTransitionsIntentService extends IntentService {

    public GeoFenceTransitionsIntentService() {
        super("GeoFenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent != null) {
            int geoFencingTransition = geofencingEvent.getGeofenceTransition();
            PresenceRepository presenceRepository = new PresenceRepository(getApplicationContext());
            long time = System.currentTimeMillis();
            if (geoFencingTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                presenceRepository.insert(time);
            } else if (geoFencingTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                presenceRepository.update(time);
            }
        }
    }
}
