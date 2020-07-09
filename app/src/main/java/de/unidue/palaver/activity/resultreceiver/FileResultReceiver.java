package de.unidue.palaver.activity.resultreceiver;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.File;

import de.unidue.palaver.dialogandtoast.SendLocationAndFileDialog;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.serviceandworker.locationservice.LocationAndFileServiceConstant;
import de.unidue.palaver.viewmodel.MessageViewModel;

public class FileResultReceiver extends ResultReceiver {
    private static final String TAG = FileResultReceiver.class.getSimpleName();

    private File file;
    private Application application;
    private Activity activity;
    private MessageViewModel messageViewModel;


    public FileResultReceiver(Application application, Activity activity, MessageViewModel messageViewModel, Handler handler) {
        super(handler);
        this.application = application;
        this.activity = activity;
        this.messageViewModel = messageViewModel;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);

        if(resultCode == LocationAndFileServiceConstant.SUCCESS_RESULT){
            file = (File) resultData.getSerializable("RESULT_DATA_FILE_KEY");
            Log.i(TAG, file.getName());
            SendLocationAndFileDialog.startDialog(application,
                    activity, file, messageViewModel, StringValue.ContenType.FILE);
        }

    }
}
