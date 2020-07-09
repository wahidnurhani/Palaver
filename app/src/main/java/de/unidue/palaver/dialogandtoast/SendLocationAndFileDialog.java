package de.unidue.palaver.dialogandtoast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import de.unidue.palaver.R;
import de.unidue.palaver.model.PalaverLocation;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.viewmodel.MessageViewModel;

public class SendLocationAndFileDialog extends CustomDialog {
    private MessageViewModel messageViewModel;

    private PalaverLocation palaverLocation;
    private File file;

    @SuppressLint("StaticFieldLeak")
    private static SendLocationAndFileDialog sendLocationAndFileDialogInstance;
    private String CONTENT_TYPE;

    private TextView line1;
    private TextView line2;
    private TextView line3;

    public static void startDialog(Application application, Activity activity, PalaverLocation palaverLocation, MessageViewModel messageViewModel, String CONTENT_TYPE){
        sendLocationAndFileDialogInstance = new SendLocationAndFileDialog(application, activity, palaverLocation, messageViewModel, CONTENT_TYPE);
        sendLocationAndFileDialogInstance.initAndShowDialog();
    }

    public static void startDialog(Application application, Activity activity, File file, MessageViewModel messageViewModel, String CONTENT_TYPE){
        sendLocationAndFileDialogInstance = new SendLocationAndFileDialog(application, activity, messageViewModel, file, CONTENT_TYPE);
        sendLocationAndFileDialogInstance.initAndShowDialog();
    }

    public SendLocationAndFileDialog(Application application, Activity activity, PalaverLocation palaverLocation, MessageViewModel messageViewModel, String CONTENT_TYPE) {
        super(application, activity);
        this.messageViewModel = messageViewModel;
        this.palaverLocation = palaverLocation;
        this.CONTENT_TYPE = CONTENT_TYPE;
    }

    public SendLocationAndFileDialog(Application application, Activity activity, MessageViewModel messageViewModel, File file, String CONTENT_TYPE){
        super(application, activity);
        this.file = file;
        this.messageViewModel = messageViewModel;
        this.CONTENT_TYPE = CONTENT_TYPE;
    }

    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    protected void initAndShowDialog() {
        inflateLayout(R.layout.dialog_send_location_and_file);

        Button cancelButton = getView().findViewById(R.id.cancel_Button);
        Button sendButton = getView().findViewById(R.id.send_Button);

        line1 = getView().findViewById(R.id.line1);
        line2 = getView().findViewById(R.id.line2);
        line3 = getView().findViewById(R.id.line3);


        if(CONTENT_TYPE.equals(StringValue.ContenType.FILE)){
            line1.setText(file.getName());

            sendButton.setOnClickListener(v -> {
                dismiss();
                messageViewModel.sendFile(file);});

        } else if (CONTENT_TYPE.equals(StringValue.ContenType.LOCATION)){


            line1.setText(palaverLocation.getAddress().getAddressLine(0));
            line2.setText(palaverLocation.getAddress().getPostalCode()
                    +" "+palaverLocation.getAddress().getLocality()
                    +", "+ palaverLocation.getAddress().getAdminArea());

            line3.setText("Coordinate : "+palaverLocation.getLatitude()+", "+palaverLocation.getLongitude());

            sendButton.setOnClickListener(v -> {
                dismiss();
                messageViewModel.sendLocation(palaverLocation);
            });
        }

        cancelButton.setOnClickListener(v -> dismiss());
        show();
    }
}
