package de.unidue.palaver.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.SessionManager;
import de.unidue.palaver.engine.Communicator;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.User;

public class ServiceAddFriend extends Service {
    private static final String TAG= ServiceAddFriend.class.getSimpleName();
    private Palaver palaver = Palaver.getInstance();
    private SessionManager sessionManager;
    private Communicator communicator = palaver.getPalaverEngine().getCommunicator();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sessionManager = SessionManager.getSessionManagerInstance(getApplicationContext());
        String friendUsername = intent.getCharSequenceExtra("INTENT_FRIEND_USERNAME").toString();
        FetchAddFriend fetchAddFriend= new FetchAddFriend();
        fetchAddFriend.execute(new Friend(friendUsername));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "service destroyed");
    }

    private class FetchAddFriend extends AsyncTask<Friend, Void, Void> {

        @Override
        protected Void doInBackground(Friend... friends) {
            User user = sessionManager.getUser();
            String[] resultValue = communicator.addContact(user, friends[0].getUsername());
            if(resultValue[0].equals("1")){
                Intent intent = new Intent("friendadded_broadcast");
                intent.putExtra("INTENT_ADDFRIEND_RESULT",resultValue[1]);
                LocalBroadcastManager.getInstance(ServiceAddFriend.this).sendBroadcast(intent);
            }
            onDestroy();
            return null;
        }
    }
}
