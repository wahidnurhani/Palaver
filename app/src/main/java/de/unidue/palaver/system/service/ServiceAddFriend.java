package de.unidue.palaver.system.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;

import de.unidue.palaver.system.engine.SessionManager;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.engine.UIController;
import de.unidue.palaver.system.model.StackApiResponseList;
import de.unidue.palaver.system.httpclient.Retrofit;
import de.unidue.palaver.system.model.StringValue;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;
import retrofit2.Response;

public class ServiceAddFriend extends Service {
    private static final String TAG= ServiceAddFriend.class.getSimpleName();
    private SessionManager sessionManager;
    private UIController uiController;

    public static void startIntent(Context applicationContext, Activity activity, String username) {
        Intent intent = new Intent(applicationContext, ServiceAddFriend.class);
        intent.putExtra(StringValue.IntentKeyName.FRIEND, username.trim());
        activity.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sessionManager = SessionManager.getSessionManagerInstance(getApplicationContext());
        uiController = PalaverEngine.getPalaverEngineInstance().getUiController();

        String friendUsername = intent.getCharSequenceExtra(StringValue.IntentKeyName.FRIEND).toString();
        FetchAddFriend fetchAddFriend= new FetchAddFriend();
        fetchAddFriend.execute(new Friend(friendUsername));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, StringValue.LogMessage.SERVICE_DESTROYED);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchAddFriend extends AsyncTask<Friend, Void, Response<StackApiResponseList<String>>> {

        @Override
        protected Response<StackApiResponseList<String>> doInBackground(Friend... friends) {
            sessionManager = SessionManager.getSessionManagerInstance(getApplicationContext());
            User user = sessionManager.getUser();
            PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(getApplicationContext());
            PalaverDao palaverDao = palaverRoomDatabase.palaverDao();
            Response<StackApiResponseList<String>> response= null;

            Retrofit retrofit = new Retrofit();
            try {
                response = retrofit.addFriend(user, friends[0]);
                assert response.body() != null;
                if(response.body().getMessageType()==1){
                    palaverDao.insert(friends[0]);
                    Intent intent = new Intent(StringValue.IntentAction.BROADCAST_FRIENDADDED);
                    LocalBroadcastManager.getInstance(ServiceAddFriend.this).sendBroadcast(intent);
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
                uiController.showToast(getApplicationContext(), s.body().getInfo());
            }
            onDestroy();
        }
    }
}
