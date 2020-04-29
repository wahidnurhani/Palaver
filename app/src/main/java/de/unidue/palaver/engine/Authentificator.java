package de.unidue.palaver.engine;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import de.unidue.palaver.ChatManagerActivity;
import de.unidue.palaver.LoginActivity;
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

    public void authentificate(Context context, String userName, String password) {
        this.context = context;
        User user = new User(new UserData(userName, password));
        FetchAuthentification fetchAuthentification = new FetchAuthentification();
        fetchAuthentification.execute(user);
    }

    private class FetchAuthentification extends AsyncTask<User, Void, String[]> {
        @Override
        protected String[] doInBackground(User... userData) {
            String[] returnValue=new String[]{};
            try {
                returnValue= palaver.getPalaverEngine().getCommunicator().registerAndValidate(userData[0],"/api/user/validate");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(returnValue[0].equals("1")){
                Intent intent = new Intent("palaver_authentification_broadcast");
                //LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                try {
                    palaver.getPalaverEngine().handleAddFriendRequest(userData[0]);
                } catch (Exception e) {
                    e.printStackTrace();
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
            if (objects[0].equals("1")) {
                ChatManagerActivity.startIntent(context);
            }else palaver.getUiController().showErrorDialog(context, objects[1]);
        }
    }
}
