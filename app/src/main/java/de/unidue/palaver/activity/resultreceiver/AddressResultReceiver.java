package de.unidue.palaver.activity.resultreceiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import de.unidue.palaver.serviceandworker.locationservice.LocationServiceConstant;

public class AddressResultReceiver extends ResultReceiver {
    String address;

    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);

        if(resultCode == LocationServiceConstant.SUCCESS_RESULT){
            address = resultData.getString(LocationServiceConstant.RESULT_DATA_ADDRESS_KEY);
        }
    }
}
