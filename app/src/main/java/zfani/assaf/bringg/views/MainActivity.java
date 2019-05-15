package zfani.assaf.bringg.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.LocationServices;

import butterknife.ButterKnife;
import zfani.assaf.bringg.Constants;
import zfani.assaf.bringg.R;
import zfani.assaf.bringg.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGeoFencing(mainViewModel.getWorkLocation().getValue());
            }
        }
    }

    private void initView() {
        mainViewModel.getWorkLocation().observe(this, workLocation -> {
            if (workLocation.getLatitude() == 0 && workLocation.getLongitude() == 0) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .add(R.id.clContainer, new WorkAddressFragment()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.clContainer, new StatisticsFragment()).commit();
                initGeoFencing(workLocation);
            }
        });
    }

    private void initGeoFencing(Location location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_REQUEST);
            return;
        }
        LocationServices.getGeofencingClient(this).addGeofences(mainViewModel.getGeoFencingRequest(location), mainViewModel.getGeoFencePendingIntent());
    }
}
