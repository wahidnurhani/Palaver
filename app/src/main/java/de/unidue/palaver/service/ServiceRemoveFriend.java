package de.unidue.palaver.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

import de.unidue.palaver.httpclient.Retrofit;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.model.User;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.roomdatabase.PalaverRoomDatabase;
import de.unidue.palaver.sessionmanager.SessionManager;
import de.unidue.palaver.activity.CustomToast;
import retrofit2.Response;

@SuppressLint("Registered")
public class ServiceRemoveFriend extends Service {


    private static final String TAG= ServiceRemoveFriend.class.getSimpleName();

    public static void startIntent(Context applicationContext, Friend friend) {
        Intent intent = new Intent(applicationContext, ServiceRemoveFriend.class);
        intent.putExtra(StringValue.IntentKeyName.FRIEND, friend.getUsername().trim());
        applicationContext.startService(intent);
    }
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
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String friendUsername = intent.getCharSequenceExtra(StringValue.IntentKeyName.FRIEND).toString();
        Friend friend = new Friend(friendUsername);
        new RemoveFriendAsyncTask().execute(friend);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, StringValue.LogMessage.SERVICE_DESTROYED);
    }

    @SuppressLint("StaticFieldLeak")
    private class RemoveFriendAsyncTask extends AsyncTask<Friend, Void, Response<StackApiResponseList<String>>> {

        @Override
        protected Response<StackApiResponseList<String>> doInBackground(Friend... friends) {
            SessionManager sessionManager = SessionManager.getSessionManagerInstance(getApplicationContext());
            User user = sessionManager.getUser();
            PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(getApplicationContext());
            PalaverDao palaverDao = palaverRoomDatabase.palaverDao();
            Response<StackApiResponseList<String>> response= null;

            Retrofit retrofit = new Retrofit();
            try {
                response = retrofit.removeFriend(user, friends[0]);
                assert response.body() != null;
                if(response.body().getMessageType()==1){
                    palaverDao.delete(friends[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response<StackApiResponseList<String>> s) {
            super.onPostExecute(s);
            if(s!=null){
                assert s.body() != null;
                CustomToast.makeText(getApplicationContext(), s.body().getInfo());
            }
            onDestroy();
        }
    }
}