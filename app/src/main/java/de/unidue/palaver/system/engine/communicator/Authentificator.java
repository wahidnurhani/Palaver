package de.unidue.palaver.system.communicator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.uicontroller.UIController;
import de.unidue.palaver.ui.LoginActivity;
import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.model.UserData;
import de.unidue.palaver.ui.ProgressDialog;

public class Authentificator {
    private static final String TAG= LoginActivity.class.getSimpleName();

    private Palaver palaver;
    private UIController uiController;
    private Context applicationContext;
    private Activity activity;
    private int method;
    private ProgressDialog progressDialog;

    public Authentificator() {
        this.palaver = Palaver.getInstance();
        uiController = palaver.getUiController();
    }

    public void register(Context applicationContext, Activity activity, String userName, String password) {
        this.method = 2;
        this.activity = activity;
        this.applicationContext = applicationContext;
        User user = new User(new UserData(userName, password));
        String cmd = StringValue.APICmd.REGISTER;
        MyParam myParam = new MyParam(user, cmd);
        FetchAuthentification fetchAuthentification = new FetchAuthentification();
        fetchAuthentification.execute(myParam);
        progressDialog = new ProgressDialog(activity);
        progressDialog.startDialog();
    }

    public void authentificate(Context applicationContext, Activity activity, String userName, String password) {
        this.method = 1;
        this.applicationContext = applicationContext;
        this.activity = activity;
        User user = new User(new UserData(userName, password));
        String cmd = StringValue.APICmd.VALIDATE;
        MyParam myParam = new MyParam(user, cmd);
        FetchAuthentification fetchAuthentification = new FetchAuthentification();
        fetchAuthentification.execute(myParam);
        progressDialog = new ProgressDialog(activity);
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
                    SessionManager.getSessionManagerInstance(applicationContext).setUser(user);
                    SessionManager.getSessionManagerInstance(applicationContext).startSession(user.getUserData().getUserName(),
                            user.getUserData().getPassword());
                    Intent intent = new Intent(StringValue.IntentAction.BROADCAST_AUTHENTIFICATED);
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
                    try {

                        Palaver.getInstance().getPalaverEngine().handleFetchAllFriendRequestWithNoService(myParams[0].getUser());

                        Palaver.getInstance().getPalaverEngine().handleFetchAllChatRequestWithNoService(applicationContext);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(StringValue.IntentAction.BROADCAST_USER_REGISTERED);
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
                }
            }
            return returnValue;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(String[] objects) {
            progressDialog.dismissDialog();
            if (!objects[0].equals("1")) {
                Palaver.getInstance().getUiController().showToast(applicationContext, objects[1]);
            } else {
                if(method==1){
                    Palaver.getInstance().getUiController().openSplashScreenActivity(activity);
                    activity.overridePendingTransition(0,0);
                } else {
                    Palaver.getInstance().getUiController().showToast(applicationContext, objects[1]);
                    Palaver.getInstance().getUiController().openLoginActivity(activity);
                    activity.overridePendingTransition(0,0);
                }

            }
        }
    }
}
