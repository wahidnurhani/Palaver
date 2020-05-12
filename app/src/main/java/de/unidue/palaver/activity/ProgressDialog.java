package de.unidue.palaver.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import de.unidue.palaver.R;

class ProgressDialog {

    private Activity context;
    private AlertDialog alertDialog;

    ProgressDialog(Activity context) {
        this.context = context;
    }

    @SuppressLint("InflateParams")
    void startDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.ProgressDialog);
        LayoutInflater inflater = context.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        MaxProgressAction maxProgressAction = new MaxProgressAction();
        maxProgressAction.start();
    }

    private class MaxProgressAction extends Thread{
        public void run(){
            try{
                int TimeOut = 5;
                sleep(1000 * TimeOut);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            dismissDialog();
        }
    }

    void dismissDialog(){
        if(alertDialog!=null){
            alertDialog.dismiss();
        }
    }
}
