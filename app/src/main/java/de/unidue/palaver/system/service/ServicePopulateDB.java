package de.unidue.palaver.system.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.unidue.palaver.system.httpclient.JSONBuilder;
import de.unidue.palaver.system.httpclient.Retrofit;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.StackApiResponseList;
import de.unidue.palaver.system.model.StringValue;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;
import retrofit2.Response;

@SuppressLint("Registered")
public class ServicePopulateDB extends Service {
    private static String TAG = ServicePopulateDB.class.getSimpleName();

    public static void startIntent(Context context, User user) {
        Intent intent = new Intent(context, ServicePopulateDB.class);
        intent.putExtra(StringValue.IntentKeyName.USERNAME, user.getUserName().trim());
        intent.putExtra(StringValue.IntentKeyName.PASSWORD, user.getPassword().trim());
        context.startService(intent);
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
        Log.i(TAG, "on start commend");
        String username = intent.getCharSequenceExtra(StringValue.IntentKeyName.USERNAME).toString();
        String password = intent.getCharSequenceExtra(StringValue.IntentKeyName.PASSWORD).toString();
        User user = new User(username, password);
        PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(getApplicationContext());
        PalaverDao palaverDao = palaverRoomDatabase.palaverDao();
        new PopulateDBAsyncTask(palaverDao).execute(user);

        return  START_STICKY;
    }

    @SuppressLint("StaticFieldLeak")
    private static class PopulateDBAsyncTask extends AsyncTask<User, Void, Void> {
        private PalaverDao palaverDao;
        PopulateDBAsyncTask(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

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
        protected Void doInBackground(User... users) {
            Retrofit retrofit = new Retrofit();
            Response<StackApiResponseList<String>> responseFetchFriends;
            Response<StackApiResponseList<Message>> responseFetchChat;
            try {
                responseFetchFriends = retrofit.fetchAllFriend(users[0]);

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
                        responseFetchChat = retrofit.getMessage(body);

                        assert responseFetchChat.body() != null;
                        if(responseFetchChat.body().getDatas() != null){
                            for (Message message : responseFetchChat.body().getDatas()){
                                message.setFriendUserName(friend.getUsername());
                                palaverDao.insert(message);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
