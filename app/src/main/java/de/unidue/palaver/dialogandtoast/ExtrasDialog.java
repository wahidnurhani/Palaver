package de.unidue.palaver.dialogandtoast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.ResultReceiver;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import de.unidue.palaver.R;
import de.unidue.palaver.activity.ChatRoomActivity;
import de.unidue.palaver.viewmodel.MessageViewModel;

public class ExtrasDialog extends CustomDialog{
    public static final String TAG = ExtrasDialog.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private static ExtrasDialog extrasDialogInstance;
    private ImageView fileImageView;
    private ImageView locationImageView;
    private Context applicationContext;
    private MessageViewModel messageViewModel;
    private ResultReceiver locationReceiver;


    public static final int FILE_REQUEST_CODE = 2012;
    public static final int LOCATION_REQUEST_CODE = 2013;


    public ExtrasDialog(Context applicationContext, Activity activity, MessageViewModel messageViewModel, ResultReceiver locationReceiver) {
        super((Application) applicationContext, activity);
        this.applicationContext = applicationContext;
        this.messageViewModel = messageViewModel;
        this.locationReceiver = locationReceiver;
    }

    public static void startDialog(Context applicationContext, ChatRoomActivity activity, MessageViewModel messageViewModel, ResultReceiver locationReceiver) {
        extrasDialogInstance = new ExtrasDialog(applicationContext, activity, messageViewModel, locationReceiver);
        extrasDialogInstance.initAndShowDialog();
    }

    @Override
    public void initAndShowDialog(){
        inflateLayout(R.layout.dialog_extras);

        fileImageView = getView().findViewById(R.id.file_extras);
        locationImageView = getView().findViewById(R.id.location_extras);

        fileImageView.setOnClickListener(v->{
            dismiss();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            getActivity().startActivityForResult(intent, FILE_REQUEST_CODE);
        });

        locationImageView.setOnClickListener(v->{

            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                dismiss();
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ExtrasDialog.LOCATION_REQUEST_CODE);
            } else {
                dismiss();
                messageViewModel.fetchLocation(locationReceiver);
            }
        });
        show();
    }
}
