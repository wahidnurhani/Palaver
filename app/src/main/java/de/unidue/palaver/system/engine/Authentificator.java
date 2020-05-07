package de.unidue.palaver.system.engine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.ui.ProgressDialog;

public class Authentificator {
    private static final String TAG= Authentificator.class.getSimpleName();

    private Context applicationContext;
    private Activity activity;
    private PalaverEngine palaverEngine;

    private int method;
    private ProgressDialog progressDialog;

    public Authentificator() {

    }

    public void register(Context applicationContext, Activity activity, String userName, String password) {

        this.method = 2;
        this.activity = activity;
        this.applicationContext = applicationContext;
        User user = new User(userName, password);
        String cmd = StringValue.APICmd.REGISTER;
        MyParam myParam = new MyParam(user, cmd);
        FetchAuthentification fetchAuthentification = new FetchAuthentification();
        fetchAuthentification.execute(myParam);
        progressDialog = new ProgressDialog(activity);
        progressDialog.startDialog();

        palaverEngine= Palaver.getInstance().getPalaverEngine();
    }

    public void authentificate(Context applicationContext, Activity activity, String userName, String password) {

        this.method = 1;
        this.applicationContext = applicationContext;
        this.activity = activity;
        User user = new User(userName, password);
        String cmd = StringValue.APICmd.VALIDATE;
        MyParam myParam = new MyParam(user, cmd);
        FetchAuthentification fetchAuthentification = new FetchAuthentification();
        fetchAuthentification.execute(myParam);
        progressDialog = new ProgressDialog(activity);
        progressDialog.startDialog();

        palaverEngine= Palaver.getInstance().getPalaverEngine();
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

            Log.i(TAG, "check applicationContext :"+ (applicationContext!=null));
            Log.i(TAG, "chieck palavarEngine FetchAuthentification:"+ (palaverEngine!=null));

            String[] returnValue=new String[]{};
            User user = myParams[0].getUser();
            Communicator communicator = Palaver.getInstance().getPalaverEngine().getCommunicator();
            try {
                returnValue= communicator.registerAndValidate(myParams[0].getUser(),
                        myParams[0].getCmd());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(returnValue[0].equals("1")){
                if(myParams[0].getCmd().equals(StringValue.APICmd.VALIDATE)){
                    palaverEngine.handleStartSessionRequest(applicationContext, user);
                    palaverEngine.handleSendLocalBroadCastRequest(applicationContext,
                            StringValue.IntentAction.BROADCAST_AUTHENTIFICATED);
                    try {
                        palaverEngine.handleFetchAllFriendRequestWithNoService(applicationContext,myParams[0].getUser());
                        palaverEngine.handleFetchAllChatRequestWithNoService(applicationContext);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    palaverEngine.handleSendLocalBroadCastRequest(applicationContext,
                            StringValue.IntentAction.BROADCAST_USER_REGISTERED);

                    palaverEngine.handleSendLocalBroadCastRequest(applicationContext,
                            StringValue.IntentAction.BROADCAST_USER_REGISTERED);
                }
            }
            return returnValue;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(String[] objects) {
            Log.i(TAG, "chieck palavarEngine FetchAuthentification onPost:"+ (palaverEngine!=null));

            progressDialog.dismissDialog();
            if (!objects[0].equals("1")) {
                palaverEngine.handleShowToastRequest(applicationContext, objects[1]);
            } else {
                if(method==1){
                    palaverEngine.hadleOpenSplashScreenActivityRequest(activity);
                    activity.overridePendingTransition(0,0);
                } else {
                    palaverEngine.handleShowToastRequest(applicationContext, objects[1]);
                    palaverEngine.handleOpenLoginActivityRequest(activity);
                    activity.overridePendingTransition(0,0);
                }

            }
        }
    }
}
