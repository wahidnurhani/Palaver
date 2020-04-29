package de.unidue.palaver.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.engine.Communicator;

public class ServiceAddFriend extends Service {
    private static final String TAG= ServiceAddFriend.class.getSimpleName();
    private Palaver palaver = Palaver.getInstance();
    private Communicator communicator = palaver.getPalaverEngine().getCommunicator();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FetchAddFriend fetchAddFriend = new FetchAddFriend();
        fetchAddFriend.execute("initiate friend");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "service destroyed");
    }

    private class FetchAddFriend extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String[] resultValue = communicator.fetchFriend(palaver.getUser());
            if(resultValue[0].equals("1")){
                Intent intent = new Intent("friendadded_broadcast");
                LocalBroadcastManager.getInstance(ServiceAddFriend.this).sendBroadcast(intent);
            }
            onDestroy();
            return null;
        }
    }
}
