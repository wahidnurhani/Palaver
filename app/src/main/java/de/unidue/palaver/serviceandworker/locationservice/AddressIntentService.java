package de.unidue.palaver.serviceandworker.locationservice;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.unidue.palaver.model.PalaverLocation;

public class AddressIntentService extends IntentService {

    private ResultReceiver resultReceiver;

    public AddressIntentService() {
        super("AddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            String err="";
            resultReceiver = intent.getParcelableExtra(LocationServiceConstant.RECEIVER_KEY);
            PalaverLocation palaverLocation = intent.getParcelableExtra(LocationServiceConstant.LOCATION_DATA_EXTRA_KEY);

            if(palaverLocation!=null){
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;

                try {
                    addresses = geocoder.getFromLocation(palaverLocation.getLatitude(), palaverLocation.getLongitude(), 1);
                } catch (Exception e){
                    err = e.getMessage();
                }
                if(addresses == null || addresses.isEmpty()){
                    deliverToReceiver(LocationServiceConstant.FAILURE_RESULT, err);
                } else {
                    Address address = addresses.get(0);
                    List<String> addressesFragment = new ArrayList<>();
                    for(int i=0 ; i<address.getMaxAddressLineIndex(); i++){
                        addressesFragment.add(address.getAddressLine(i));
                    }
                    deliverToReceiver(
                            LocationServiceConstant.SUCCESS_RESULT,
                            TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")),
                                    addressesFragment));
                }
            }
        }
    }

    private void deliverToReceiver(int resultCode, String address){
        Bundle bundle = new Bundle();
        bundle.putString(LocationServiceConstant.RESULT_DATA_ADDRESS_KEY, address);
        resultReceiver.send(resultCode, bundle);
    }
}
