package mearns.douglas.haelauncher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mearns.douglas.haelauncher.widgets.LocationInformation;
import mearns.douglas.haelauncher.geo.AddressMonitor;

public class AppLauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        monitor = new AddressMonitor(this);
        startGPS();
    }

    public void showApps(View v){
        Intent i = new Intent(this, LauncherActivity.class);
        startActivity(i);
    }


    private void startGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }

        final LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        monitor.setReceiver((LocationInformation)findViewById(R.id.location));
        locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(), true), 0,
                0, monitor);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        for (final int result :  grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                // Permission denied on something so leave deactivated for now.
                return;
            }
        }
        startGPS();
    }

    private AddressMonitor monitor = null;
}
