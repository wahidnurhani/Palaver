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

public class ServiceFetchFriend extends Service {
    private static final String TAG= ServiceFetchFriend.class.getSimpleName();
    private Palaver palaver = Palaver.getInstance();
    private Communicator communicator = palaver.getPalaverEngine().getCommunicator();
    @Nullable
    @Override
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
        Log.i(TAG, "service destroyed");
    }

    private class FetchAllFriendFromServer extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String[] resultValue = communicator.fetchFriend(palaver.getUser());
            if(resultValue[0].equals("1")){
                Intent intent = new Intent("friendfetched_broadcast");
                LocalBroadcastManager.getInstance(ServiceFetchFriend.this).sendBroadcast(intent);
            }
            onDestroy();
            return null;
        }
    }
}
