package de.unidue.palaver.ui;


import android.app.AlertDialog;
import android.content.Context;

import de.unidue.palaver.system.model.StringValue;

public class ErrorDialog {

    public static void show(Context context, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setNegativeButton(StringValue.TextAndLabel.CLOSE,
                (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
