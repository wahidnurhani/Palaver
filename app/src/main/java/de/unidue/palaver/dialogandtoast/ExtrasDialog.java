package de.unidue.palaver.dialogandtoast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import de.unidue.palaver.R;
import de.unidue.palaver.activity.ChatRoomActivity;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.PalaverLocation;
import de.unidue.palaver.model.StringValue;

public class ExtrasDialog extends CustomDialog{
    public static final String TAG = ExtrasDialog.class.getSimpleName();
    private PalaverLocation palaverLocation;

    @SuppressLint("StaticFieldLeak")
    private static ExtrasDialog extrasDialogInstance;
    private ImageView fileImageView;
    private ImageView locationImageView;
    private Context applicationContext;


    public static final int FILE_REQUEST_CODE = 2012;
    public static final int LOCATION_REQUEST_CODE = 2013;


    public ExtrasDialog(Context applicationContext, Activity activity, Friend friend) {
        super((Application) applicationContext, activity);
        this.applicationContext = applicationContext;
    }

    public static void startDialog(Context applicationContext, ChatRoomActivity activity, Friend friend) {
        extrasDialogInstance = new ExtrasDialog(applicationContext, activity, friend);
        extrasDialogInstance.startDialog();
    }

    private void startDialog(){
        init(R.layout.dialog_extras);

        fileImageView = getView().findViewById(R.id.file_extras);
        locationImageView = getView().findViewById(R.id.location_extras);

        fileImageView.setOnClickListener(v->{
            dismiss();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            getActivity().startActivityForResult(intent, FILE_REQUEST_CODE);
        });

        locationImageView.setOnClickListener(v->{

            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ExtrasDialog.LOCATION_REQUEST_CODE);
            } else {
                Intent intent = new Intent(StringValue.IntentAction.LOCATION_PERMITION);
                intent.putExtra(StringValue.IntentKeyName.LOCATION_PERMITION_VALUE, 1);
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
            }
            dismiss();
        });
        show();
    }


}
