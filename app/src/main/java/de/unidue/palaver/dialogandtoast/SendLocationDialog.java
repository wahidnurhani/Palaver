package de.unidue.palaver.dialogandtoast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import de.unidue.palaver.R;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.PalaverLocation;
import de.unidue.palaver.viewmodel.MessageViewModel;

public class SendLocationDialog extends CustomDialog {
    private MessageViewModel messageViewModel;
    private PalaverLocation palaverLocation;
    @SuppressLint("StaticFieldLeak")
    private static SendLocationDialog sendLocationDialogInstance;

    private TextView streetAndHouseNumber;
    private TextView posCodeAndCity;
    private TextView coordinate;

    public static void startDialog(Application application, Activity activity, PalaverLocation palaverLocation, MessageViewModel messageViewModel){
        sendLocationDialogInstance = new SendLocationDialog(application, activity, palaverLocation, messageViewModel);
        sendLocationDialogInstance.initAndShowDialog();
    }

    public SendLocationDialog(Application application, Activity activity, PalaverLocation palaverLocation, MessageViewModel messageViewModel) {
        super(application, activity);
        this.messageViewModel = messageViewModel;
        this.palaverLocation = palaverLocation;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initAndShowDialog() {
        inflateLayout(R.layout.dialog_send_location);

        streetAndHouseNumber = getView().findViewById(R.id.streetAndHouseNumber_textView);
        posCodeAndCity = getView().findViewById(R.id.posCodeAndCityAndCountry_textView);
        coordinate = getView().findViewById(R.id.coordinat_textView);

        streetAndHouseNumber.setText(palaverLocation.getAddress().getAddressLine(0));
        posCodeAndCity.setText(palaverLocation.getAddress().getPostalCode()
                +" "+palaverLocation.getAddress().getLocality()
                +", "+ palaverLocation.getAddress().getAdminArea());

        coordinate.setText("Coordinate : "+palaverLocation.getLatitude()+", "+palaverLocation.getLongitude());

        Button cancelButton = getView().findViewById(R.id.cancel_Button);
        Button sendButton = getView().findViewById(R.id.sendLocation_Button);

        sendButton.setOnClickListener(v -> {
            Message message = new Message(
                    messageViewModel.getFriend().getUsername(),
                    messageViewModel.getUser().getUserName(),
                    messageViewModel.getFriend().getUsername(),
                    palaverLocation.getLocationUrlString(),
                    new Date()

            );
            messageViewModel.sendMessage(message);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
        show();
    }
}
