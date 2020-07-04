package de.unidue.palaver.dialogandtoast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.ImageView;

import de.unidue.palaver.R;
import de.unidue.palaver.activity.ChatRoomActivity;
import de.unidue.palaver.viewmodel.MessageViewModel;

public class ExtrasDialog extends CustomDialog{

    private MessageViewModel messageViewModel;
    @SuppressLint("StaticFieldLeak")
    private static ExtrasDialog extrasDialogInstance;
    private ImageView fileImageView;
    private ImageView locationImageView;


    public static int REQUEST_CODE = 2011;

    public ExtrasDialog(Application application, Activity activity, MessageViewModel messageViewModel) {
        super(application, activity);
        this.messageViewModel = messageViewModel;
    }

    public static void startDialog(Application application, ChatRoomActivity activity, MessageViewModel messageViewModel) {
        extrasDialogInstance = new ExtrasDialog(application, activity, messageViewModel);
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
            getActivity().startActivityForResult(intent, REQUEST_CODE);
        });

        locationImageView.setOnClickListener(v->{
            //TODO
        });

        show();
    }
}
