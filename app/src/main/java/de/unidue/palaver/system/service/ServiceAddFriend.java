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

import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.model.CommunicatorResult;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.engine.communicator.Communicator;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;

public class ServiceAddFriend extends Service {
    private static final String TAG= ServiceAddFriend.class.getSimpleName();
    private Palaver palaver;
    private SessionManager sessionManager;
    private Communicator communicator;
    private PalaverDB palaverDB;

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
        palaver = Palaver.getInstance();
        sessionManager = SessionManager.getSessionManagerInstance(getApplicationContext());
        communicator = Palaver.getInstance().getPalaverEngine().getCommunicator();
        palaverDB = Palaver.getInstance().getPalaverDB();
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
    private class FetchAddFriend extends AsyncTask<Friend, Void, CommunicatorResult<Friend>> {

        @Override
        protected CommunicatorResult<Friend> doInBackground(Friend... friends) {
            User user = sessionManager.getUser();
            CommunicatorResult<Friend> resultValue = communicator.addFriend(user, friends[0].getUsername());
            if(resultValue.getResponseValue()==1){
                palaverDB.insertFriend(friends[0]);
                Intent intent = new Intent(StringValue.IntentAction.BROADCAST_FRIENDADDED);
                LocalBroadcastManager.getInstance(ServiceAddFriend.this).sendBroadcast(intent);
            }
            return resultValue;
        }

        @Override
        protected void onPostExecute(CommunicatorResult<Friend> s) {
            super.onPostExecute(s);
            palaver.getUiController().showToast(getApplicationContext(), s.getMessage());
        }
    }
}
