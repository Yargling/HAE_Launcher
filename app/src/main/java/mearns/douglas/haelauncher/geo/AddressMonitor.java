package mearns.douglas.haelauncher.geo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AddressMonitor implements LocationListener {
    public AddressMonitor(Context context) {
        geocoder = new Geocoder(context);
    }

    public void setReceiver(AddressReceiver recv) {
        myReceiver.set(recv);
    }

    public interface AddressReceiver {
        void processAddress(Address address);
        void processNoAddress();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            final List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
            final AddressReceiver receiverSnapshot = myReceiver.get();
            if (receiverSnapshot != null) {
                if (addresses.isEmpty()) {
                    receiverSnapshot.processNoAddress();
                } else {
                    receiverSnapshot.processAddress(addresses.get(0));
                }
            }
        } catch (IOException e) {
            Log.e("HAE", e.getMessage());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private final AtomicReference<AddressReceiver> myReceiver = new AtomicReference<>();
    private final Geocoder geocoder;
}
