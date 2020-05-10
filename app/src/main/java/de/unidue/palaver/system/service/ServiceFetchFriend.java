package de.unidue.palaver.system.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import de.unidue.palaver.system.model.StringValue;

public class ServiceFetchFriend extends Service {
    private static final String TAG= ServiceFetchFriend.class.getSimpleName();

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FetchAllFriendFromServer fetchAllFriendFromServer=new FetchAllFriendFromServer();
        fetchAllFriendFromServer.execute("initiate friend");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, StringValue.LogMessage.SERVICE_DESTROYED);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchAllFriendFromServer extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            onDestroy();
            return null;
        }
    }
}
