package de.unidue.palaver.dialogandtoast;


import android.app.AlertDialog;
import android.app.Application;

import de.unidue.palaver.model.StringValue;

public class ErrorDialog {

    public static void show(Application application, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(application);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setNegativeButton(StringValue.TextAndLabel.CLOSE,
                (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
