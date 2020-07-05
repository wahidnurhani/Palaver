package de.unidue.palaver.activity.resultreceiver;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import de.unidue.palaver.dialogandtoast.SendLocationDialog;
import de.unidue.palaver.model.PalaverLocation;
import de.unidue.palaver.serviceandworker.locationservice.LocationServiceConstant;
import de.unidue.palaver.viewmodel.MessageViewModel;

public class GetLocationResultReceiver extends ResultReceiver {
    private static final String TAG = GetLocationResultReceiver.class.getSimpleName();

    PalaverLocation palaverLocation;
    private Application application;
    private Activity activity;
    private MessageViewModel messageViewModel;



    public GetLocationResultReceiver(Application application, Activity activity, MessageViewModel messageViewModel, Handler handler) {
        super(handler);
        this.application = application;
        this.activity = activity;
        this.messageViewModel = messageViewModel;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        Log.i(TAG, "receiver active");

        if(resultCode == LocationServiceConstant.SUCCESS_RESULT){
            palaverLocation = (PalaverLocation) resultData.getSerializable(LocationServiceConstant.RESULT_DATA_LOCATION_KEY);
            Log.i(TAG, palaverLocation.toString());
            SendLocationDialog.startDialog(application,
                    activity, palaverLocation, messageViewModel);
        }
    }
}