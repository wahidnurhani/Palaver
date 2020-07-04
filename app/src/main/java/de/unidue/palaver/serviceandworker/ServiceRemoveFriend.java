package de.unidue.palaver.serviceandworker;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

import de.unidue.palaver.httpclient.IHttpClient;
import de.unidue.palaver.httpclient.RetrofitHttpClient;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.model.User;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.sessionmanager.SessionManager;
import de.unidue.palaver.dialogandtoast.CustomToast;
import retrofit2.Response;

@SuppressLint("Registered")
public class ServiceRemoveFriend extends Service {

    private static final String TAG= ServiceRemoveFriend.class.getSimpleName();

    public static void startIntent(Context applicationContext, Friend friend) {
        Intent intent = new Intent(applicationContext, ServiceRemoveFriend.class);
        intent.putExtra(StringValue.IntentKeyName.FRIEND, friend.getUsername().trim());
        applicationContext.startService(intent);
    }

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
            SessionManager sessionManager = SessionManager.getSessionManagerInstance(getApplication());
            User user = sessionManager.getUser();
            PalaverDB palaverDB = PalaverDB.getDatabase(getApplication());
            PalaverDao palaverDao = palaverDB.palaverDao();
            Response<StackApiResponseList<String>> response= null;

            IHttpClient retrofitHttpClient = new RetrofitHttpClient();
            try {
                response = retrofitHttpClient.removeFriend(user, friends[0]);
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
