package de.unidue.palaver.dialogandtoast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import de.unidue.palaver.LocationTrackerService;
import de.unidue.palaver.R;
import de.unidue.palaver.activity.ChatRoomActivity;
import de.unidue.palaver.model.PalaverLocation;
import de.unidue.palaver.model.StringValue;

public class ExtrasDialog extends CustomDialog{

    @SuppressLint("StaticFieldLeak")
    private static ExtrasDialog extrasDialogInstance;
    private ImageView fileImageView;
    private ImageView locationImageView;


    public static final int FILE_REQUEST_CODE = 2012;
    public static final int LOCATION_REQUEST_CODE = 2013;


    public ExtrasDialog(Application application, Activity activity) {
        super(application, activity);
    }

    public static void startDialog(Application application, ChatRoomActivity activity) {
        extrasDialogInstance = new ExtrasDialog(application, activity);
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
            dismiss();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            LocationTrackerService locationTrackerService = new LocationTrackerService(getApplication());

            double lat = locationTrackerService.getLatitude();
            double lon = locationTrackerService.getLongitude();

            PalaverLocation palaverLocation = new PalaverLocation(lat, lon);

            bundle.putSerializable(StringValue.IntentKeyName.LOCATION, palaverLocation);
            intent.putExtras(bundle);
            getActivity().startActivityForResult(intent, LOCATION_REQUEST_CODE);

        });

        show();
    }
}
