package de.unidue.palaver.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import de.unidue.palaver.R;

public class ProgressDialog {

    Activity context;
    AlertDialog alertDialog;

    public ProgressDialog(Activity context) {
        this.context = context;
    }

    public void startDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.ProgressDialog);
        LayoutInflater inflater = context.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissDialog(){
        alertDialog.dismiss();
    }
}
