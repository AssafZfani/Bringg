package zfani.assaf.bringg.viewmodels;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zfani.assaf.bringg.Constants;
import zfani.assaf.bringg.R;
import zfani.assaf.bringg.models.Presence;
import zfani.assaf.bringg.models.PresenceRepository;
import zfani.assaf.bringg.services.GeoFenceTransitionsIntentService;

public class MainViewModel extends AndroidViewModel {

    private final SharedPreferences sharedPreferences;
    private MutableLiveData<Location> workLocation;
    private final PresenceRepository presenceRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        presenceRepository = new PresenceRepository(application);
    }

    public MutableLiveData<Location> getWorkLocation() {
        if (workLocation == null) {
            Location location = new Location("");
            location.setLatitude(Double.longBitsToDouble(sharedPreferences.getLong(Constants.KEY_WORK_ADDRESS_LAT, 0)));
            location.setLongitude(Double.longBitsToDouble(sharedPreferences.getLong(Constants.KEY_WORK_ADDRESS_LON, 0)));
            workLocation = new MutableLiveData<>(location);
        }
        return workLocation;
    }

    public LiveData<List<Presence>> getPresenceList() {
        return presenceRepository.getAllPresences();
    }

    public void configureWorkLocation(String workAddress) {
        try {
            if (workAddress.isEmpty()) {
                showAddressErrorMessage();
                return;
            }
            List<Address> addressList = new Geocoder(getApplication()).getFromLocationName(workAddress, 1);
            if (addressList == null || addressList.isEmpty()) {
                showAddressErrorMessage();
            } else {
                saveWorkLocation(addressList.get(0));
            }
        } catch (IOException e) {
            showAddressErrorMessage();
        }
    }

    private void showAddressErrorMessage() {
        Toast.makeText(getApplication(), getApplication().getString(R.string.work_address_field_error), Toast.LENGTH_SHORT).show();
    }

    private void saveWorkLocation(Address address) {
        double lat = address.getLatitude(), lon = address.getLongitude();
        sharedPreferences.edit().putLong(Constants.KEY_WORK_ADDRESS_LAT, Double.doubleToRawLongBits(lat))
                .putLong(Constants.KEY_WORK_ADDRESS_LON, Double.doubleToRawLongBits(lon)).apply();
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lon);
        workLocation.setValue(location);
    }

    private List<Geofence> getGeoFenceList(Location location) {
        List<Geofence> geoFenceList = new ArrayList<>();
        geoFenceList.add(new Geofence.Builder()
                .setRequestId(String.valueOf(System.currentTimeMillis()))
                .setCircularRegion(location.getLatitude(), location.getLongitude(), Constants.GEO_FENCE_RADIUS_IN_METERS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setLoiteringDelay(Constants.GEO_FENCE_DWELL_IN_MILLISECONDS)
                .build());
        return geoFenceList;
    }

    public GeofencingRequest getGeoFencingRequest(Location location) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(getGeoFenceList(location));
        return builder.build();
    }

    public PendingIntent getGeoFencePendingIntent() {
        return PendingIntent.getService(getApplication(), 0, new Intent(getApplication(), GeoFenceTransitionsIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
