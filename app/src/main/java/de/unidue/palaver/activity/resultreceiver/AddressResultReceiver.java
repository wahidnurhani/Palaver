package de.unidue.palaver.activity.resultreceiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import de.unidue.palaver.serviceandworker.locationservice.LocationAndFileServiceConstant;

public class AddressResultReceiver extends ResultReceiver {
    String address;

    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);

        if(resultCode == LocationAndFileServiceConstant.SUCCESS_RESULT){
            address = resultData.getString(LocationAndFileServiceConstant.RESULT_DATA_ADDRESS_KEY);
        }
    }
}
