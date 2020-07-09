package de.unidue.palaver.activity.resultreceiver;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import de.unidue.palaver.dialogandtoast.SendLocationAndFileDialog;
import de.unidue.palaver.model.PalaverLocation;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.serviceandworker.locationservice.LocationAndFileServiceConstant;
import de.unidue.palaver.viewmodel.MessageViewModel;

public class LocationResultReceiver extends ResultReceiver {
    private static final String TAG = LocationResultReceiver.class.getSimpleName();

    private PalaverLocation palaverLocation;
    private Application application;
    private Activity activity;
    private MessageViewModel messageViewModel;


    public LocationResultReceiver(Application application, Activity activity, MessageViewModel messageViewModel, Handler handler) {
        super(handler);
        this.application = application;
        this.activity = activity;
        this.messageViewModel = messageViewModel;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        Log.i(TAG, "receiver active");

        if(resultCode == LocationAndFileServiceConstant.SUCCESS_RESULT){
            palaverLocation = (PalaverLocation) resultData.getSerializable(LocationAndFileServiceConstant.RESULT_DATA_LOCATION_KEY);
            Log.i(TAG, palaverLocation.toString());
            SendLocationAndFileDialog.startDialog(application,
                    activity, palaverLocation, messageViewModel, StringValue.ContenType.LOCATION);
        }
    }
}