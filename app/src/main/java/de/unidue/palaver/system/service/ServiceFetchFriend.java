package de.unidue.palaver.system.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.engine.CommunicatorResult;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.engine.Communicator;

public class ServiceFetchFriend extends Service {
    private static final String TAG= ServiceFetchFriend.class.getSimpleName();
    private PalaverDB palaverDB;
    private Communicator communicator;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FetchAllFriendFromServer fetchAllFriendFromServer=new FetchAllFriendFromServer();
        fetchAllFriendFromServer.execute("initiate friend");
        communicator = Palaver.getInstance().getPalaverEngine().getCommunicator();
        palaverDB = Palaver.getInstance().getPalaverDB();
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
            User user = SessionManager.getSessionManagerInstance(getApplicationContext()).getUser();
            CommunicatorResult<Friend> communicatorResult = communicator.fetchFriends(user);

            if(communicatorResult.getData().size()>0){
                if(palaverDB.deleteAllContact()){
                    for(Friend friend : communicatorResult.getData()) {
                        palaverDB.insertFriend(friend);
                    }
                }
                Intent intent = new Intent(StringValue.IntentAction.BROADCAST_ALL_FRIENDS_FETCHED);
                LocalBroadcastManager.getInstance(ServiceFetchFriend.this).sendBroadcast(intent);
            }
            onDestroy();
            return null;
        }
    }
}
