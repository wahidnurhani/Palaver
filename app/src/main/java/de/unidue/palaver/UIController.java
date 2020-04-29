package de.unidue.palaver;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class UIController {

    private Palaver palaver;

    public UIController(Palaver palaver) {
        this.palaver = palaver;
    }

    public void showErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
