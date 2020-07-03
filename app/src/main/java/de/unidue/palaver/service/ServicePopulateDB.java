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
import java.util.ArrayList;
import java.util.List;

import de.unidue.palaver.httpclient.IHttpClient;
import de.unidue.palaver.httpclient.JSONBuilder;
import de.unidue.palaver.httpclient.RetrofitHttpClient;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.model.User;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
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
        PalaverDB palaverDB = PalaverDB.getDatabase(getApplicationContext());
        PalaverDao palaverDao = palaverDB.palaverDao();
        new PopulateDBAsyncTask(palaverDao).execute(user);

        return  START_STICKY;
    }

    @SuppressLint("StaticFieldLeak")
    private static class PopulateDBAsyncTask extends AsyncTask<User, Void, Void> {
        private PalaverDao palaverDao;
        PopulateDBAsyncTask(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            IHttpClient retrofitHttpClient = new RetrofitHttpClient();
            Response<StackApiResponseList<String>> responseFetchFriends;
            Response<StackApiResponseList<Message>> responseFetchChat;
            try {
                responseFetchFriends = retrofitHttpClient.fetchAllFriend(users[0]);

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
                        responseFetchChat = retrofitHttpClient.getMessage(body);

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
