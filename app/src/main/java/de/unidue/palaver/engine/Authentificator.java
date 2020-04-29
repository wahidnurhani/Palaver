package de.unidue.palaver.engine;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import de.unidue.palaver.ui.LoginActivity;
import de.unidue.palaver.Palaver;
import de.unidue.palaver.model.User;
import de.unidue.palaver.model.UserData;

public class Authentificator {
    private static final String TAG= LoginActivity.class.getSimpleName();

    private Palaver palaver;
    private Context context;

    public Authentificator(Palaver palaver) {
        this.palaver = palaver;
    }

    public void register(Context context, String userName, String password) {
        this.context = context;
        User user = new User(new UserData(userName, password));
        String cmd = "/api/user/register";
        MyParam myParam = new MyParam(user, cmd);
        FetchAuthentification fetchAuthentification = new FetchAuthentification();
        fetchAuthentification.execute(myParam);
    }


    public void authentificate(Context context, String userName, String password) {
        this.context = context;
        User user = new User(new UserData(userName, password));
        String cmd = "/api/user/validate";
        MyParam myParam = new MyParam(user, cmd);
        FetchAuthentification fetchAuthentification = new FetchAuthentification();
        fetchAuthentification.execute(myParam);
    }

    private class MyParam{
        private User user;
        private String cmd;

        public MyParam(User user, String cmd) {
            this.user=user;
            this.cmd=cmd;
        }

        public User getUser() {
            return user;
        }

        public String getCmd() {
            return cmd;
        }
    }

    private class FetchAuthentification extends AsyncTask<MyParam, Void, String[]> {
        @Override
        protected String[] doInBackground(MyParam... myParams) {
            String[] returnValue=new String[]{};
            Communicator communicator = palaver.getPalaverEngine().getCommunicator();
            try {
                returnValue= communicator.registerAndValidate(myParams[0].getUser(),
                        myParams[0].getCmd());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(returnValue[0].equals("1")){
                if(myParams[0].getCmd().equals("/api/user/validate")){
                    palaver.getSessionManager().setUser((myParams[0].getUser()));
                    palaver.getSessionManager().startSession();
                    Intent intent = new Intent("authentificated_broadcast");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    try {
                        palaver.getPalaverEngine().handleAddFriendRequest(myParams[0].getUser());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent("registered_broadcast");
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
            if (!objects[0].equals("1")) {
                palaver.getUiController().showToast(context, objects[1]);
            }
        }
    }
}
