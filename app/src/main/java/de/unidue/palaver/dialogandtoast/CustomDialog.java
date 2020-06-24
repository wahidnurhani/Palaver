package de.unidue.palaver.dialogandtoast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class CustomDialog {
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private View view;
    private Context applicationContext;
    private Activity activity;

    public CustomDialog(Context applicationContext, Activity activity) {
        this.applicationContext = applicationContext;
        this.activity = activity;
    }

    public void init(int layout){
        builder = new AlertDialog.Builder(activity);
        inflater = (activity).getLayoutInflater();
        view = inflater.inflate(layout, null);
        builder.setView(view);
        builder.setCancelable(false);
    }

    public void show(){
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Activity getActivity() {
        return activity;
    }

    public View getView() {
        return view;
    }

    public void dismiss(){
        alertDialog.dismiss();
    }
}
