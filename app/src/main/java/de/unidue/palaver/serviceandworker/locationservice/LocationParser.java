package de.unidue.palaver.serviceandworker.locationservice;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class LocationParser {
    private static final String TAG = LocationParser.class.getSimpleName();

    private Address address;
    private Context applicationContext;

    public LocationParser(Context applicationContext, String googleMapUrl) {
        this.applicationContext = applicationContext;
        String latAndLong = googleMapUrl.split("=")[1];
        String latitudeString = latAndLong.split(",")[0];
        String longitudeString = latAndLong.split(",")[1];
        double latitude = Double.parseDouble(latitudeString);
        double longitude = Double.parseDouble(longitudeString);
        this.address = fetchAddress(latitude, longitude);
        Log.i(TAG, address.toString());
    }

    private Address fetchAddress(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(applicationContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (Exception e){
            e.printStackTrace();
        }
        if(addresses != null || !addresses.isEmpty()){
            return addresses.get(0);

        }
        return null;
    }

    public Address getAddress() {
        return address;
    }
}
