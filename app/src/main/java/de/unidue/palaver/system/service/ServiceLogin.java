package de.unidue.palaver.system.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.engine.JSONBuilder;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.retrofit.DataServerResponseList;
import de.unidue.palaver.system.retrofit.NewCommunicator;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;
import de.unidue.palaver.system.values.StringValue;
import retrofit2.Response;

public class ServiceLogin extends Service {
    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     *
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    public static void startIntent(Context applicationContext, Activity activity, String username, String password) {
        Intent intent = new Intent(applicationContext, ServiceLogin.class);
        intent.putExtra(StringValue.IntentKeyName.USERNAME, username.trim());
        intent.putExtra(StringValue.IntentKeyName.PASSWORD, password.trim());
        activity.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String username = intent.getCharSequenceExtra(StringValue.IntentKeyName.USERNAME).toString();
        String password = intent.getCharSequenceExtra(StringValue.IntentKeyName.PASSWORD).toString();
        LoginProcessor loginProcessor = new LoginProcessor();
        loginProcessor.execute(new User(username, password));
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginProcessor extends AsyncTask<User, Void, Response<DataServerResponseList<String>>> {
        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param users The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Response<DataServerResponseList<String>> doInBackground(User... users) {

            NewCommunicator newCommunicator = new NewCommunicator();

            Response<DataServerResponseList<String>> responseAuthenticate = null;
            Response<DataServerResponseList<String>> responseFetchFriends;
            Response<DataServerResponseList<Message>> responseFetchChat = null;


            SessionManager sessionManager = SessionManager.getSessionManagerInstance(getApplicationContext());

            PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(getApplicationContext());
            PalaverDao palaverDao = palaverRoomDatabase.palaverDao();

            try {

                responseAuthenticate = newCommunicator.authenticate(users[0]);

                assert responseAuthenticate.body() != null;
                if(responseAuthenticate.body().getMessageType()==1){


                    responseFetchFriends =newCommunicator.fetchAllFriend(users[0]);

                    assert responseFetchFriends.body() != null;
                    if (responseFetchFriends.body().getMessageType()==1){
                        List<Friend> friends = new ArrayList<>();
                        for(String username : responseFetchFriends.body().getDatas()){
                            Friend friend = new Friend(username);
                            friends.add(friend);
                            palaverDao.insert(friend);
                        }

                        for (Friend friend: friends){
                            JSONBuilder.UserAndRecipient body = new JSONBuilder.UserAndRecipient(users[0], friend);
                            responseFetchChat = newCommunicator.getMessage(body);

                            if(responseFetchChat!=null){
                                for (Message message : responseFetchChat.body().getDatas()){
                                    palaverDao.insert(message);
                                }
                            }
                        }
                        if(responseFetchChat!=null){
                            if(responseFetchChat.body().getMessageType()==1){
                                sessionManager.startSession(users[0].getUserName(), users[0].getPassword());
                                Intent intent = new Intent(StringValue.IntentAction.BROADCAST_LOGINRESULT);
                                LocalBroadcastManager.getInstance(ServiceLogin.this).sendBroadcast(intent);
                            }
                        }
                    } else {
                        return responseFetchFriends;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseAuthenticate;
        }

        @Override
        protected void onPostExecute(Response<DataServerResponseList<String>> dataServerResponseResponse) {
            if(dataServerResponseResponse!= null){
                PalaverEngine palaverEngine = PalaverEngine.getPalaverEngineInstance();
                assert dataServerResponseResponse.body() != null;
                if(dataServerResponseResponse.body().getMessageType()!=1){
                    palaverEngine.handleShowToastRequest(getApplicationContext(),
                            dataServerResponseResponse.body().getInfo());
                }
            }
            onDestroy();
        }
    }
}
