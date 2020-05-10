package de.unidue.palaver.activity;


import android.app.AlertDialog;
import android.content.Context;

import de.unidue.palaver.model.StringValue;

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
