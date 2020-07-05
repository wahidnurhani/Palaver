package de.unidue.palaver.serviceandworker.locationservice;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import de.unidue.palaver.model.PalaverLocation;

public class LocationProviderService extends Service {

    private static final String TAG = LocationProviderService.class.getSimpleName();
    private PalaverLocation latestLocation;
    private ResultReceiver resultReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        resultReceiver = intent.getParcelableExtra(LocationServiceConstant.RECEIVER_KEY);
        fetchLocation();
        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    private void fetchLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(getApplication())
                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(getApplication())
                                    .removeLocationUpdates(this);

                            if(locationResult!= null && locationResult.getLocations().size()>0){
                                int latestLocationIndex = locationResult.getLocations().size() -1;
                                double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                                latestLocation = new PalaverLocation(latitude, longitude);

                                if(latestLocation!=null){
                                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    List<Address> addresses = null;
                                    try {
                                        addresses = geocoder.getFromLocation(
                                                latestLocation.getLatitude(),
                                                latestLocation.getLongitude(),
                                                1);
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    if(addresses != null || !addresses.isEmpty()){
                                        Address address = addresses.get(0);
                                        latestLocation.setAddress(address);
                                    }
                                }
                                Log.i(TAG, latestLocation.getLatitude()+" "+ latestLocation.getLongitude());
                                deliverToReceiver(latestLocation);
                            }
                        }
                    }, Looper.getMainLooper());

    }

    private void deliverToReceiver(PalaverLocation palaverLocation){
        if(palaverLocation != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(LocationServiceConstant.RESULT_DATA_LOCATION_KEY, palaverLocation);
            resultReceiver.send(LocationServiceConstant.SUCCESS_RESULT, bundle);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
