package de.unidue.palaver;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import de.unidue.palaver.ui.ChatManagerActivity;

public class UIController {


    public UIController() {
    }

    public void showErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setNegativeButton("Close",
                (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    public void openChatManagerActivity(Context context) {
        ChatManagerActivity.startIntent(context);
    }
}
