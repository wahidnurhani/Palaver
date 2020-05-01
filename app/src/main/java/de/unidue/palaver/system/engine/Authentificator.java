package de.unidue.palaver.system.engine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.UIManager;
import de.unidue.palaver.ui.LoginActivity;
import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.model.UserData;
import de.unidue.palaver.ui.ProgressDialog;

class Authentificator {
    private static final String TAG= LoginActivity.class.getSimpleName();

    private Palaver palaver;
    private UIManager uiManager;
    private Context context;
    private int method;
    private ProgressDialog progressDialog;

    Authentificator() {
        this.palaver = Palaver.getInstance();
        uiManager = palaver.getUiManager();
    }

    void register(Context context, String userName, String password) {
        this.method = 2;
        this.context = context;
        User user = new User(new UserData(userName, password));
        String cmd = StringValue.APICmd.REGISTER;
        MyParam myParam = new MyParam(user, cmd);
        FetchAuthentification fetchAuthentification = new FetchAuthentification();
        fetchAuthentification.execute(myParam);
        progressDialog = new ProgressDialog((Activity) context);
        progressDialog.startDialog();
    }

    void authentificate(Context context, String userName, String password) {
        this.method = 1;
        this.context = context;
        User user = new User(new UserData(userName, password));
        String cmd = StringValue.APICmd.VALIDATE;
        MyParam myParam = new MyParam(user, cmd);
        FetchAuthentification fetchAuthentification = new FetchAuthentification();
        fetchAuthentification.execute(myParam);
        progressDialog = new ProgressDialog((Activity) context);
        progressDialog.startDialog();
    }

    private static class MyParam{
        private User user;
        private String cmd;

        MyParam(User user, String cmd) {
            this.user=user;
            this.cmd=cmd;
        }

        public User getUser() {
            return user;
        }

        String getCmd() {
            return cmd;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchAuthentification extends AsyncTask<MyParam, Void, String[]> {
        @Override
        protected String[] doInBackground(MyParam... myParams) {
            String[] returnValue=new String[]{};
            User user = myParams[0].getUser();
            Communicator communicator = palaver.getPalaverEngine().getCommunicator();
            try {
                returnValue= communicator.registerAndValidate(myParams[0].getUser(),
                        myParams[0].getCmd());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(returnValue[0].equals("1")){
                if(myParams[0].getCmd().equals(StringValue.APICmd.VALIDATE)){
                    SessionManager.getSessionManagerInstance(context).setUser(user);
                    SessionManager.getSessionManagerInstance(context).startSession(user.getUserData().getUserName(),
                            user.getUserData().getPassword());
                    Intent intent = new Intent(StringValue.IntentAction.BROADCAST_AUTHENTIFICATED);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    try {
                        palaver.getPalaverEngine().handleFetchAllFriendRequest(myParams[0].getUser());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(StringValue.IntentAction.BROADCAST_USER_REGISTERED);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
            return returnValue;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //showDialog("Loging in ...");
        }

        @Override
        protected void onPostExecute(String[] objects) {
            progressDialog.dismissDialog();
            if (!objects[0].equals("1")) {
                palaver.getUiManager().showToast(context, objects[1]);
            } else {
                if(method==1){
                    Palaver.getInstance().getUiManager().openSplashScreenActivity(context);
                    ((Activity)context).overridePendingTransition(0,0);
                } else {
                    uiManager.showToast(context, objects[1]);
                    uiManager.openLoginActivity(context);
                    ((Activity)context).overridePendingTransition(0,0);
                }

            }
        }
    }
}
