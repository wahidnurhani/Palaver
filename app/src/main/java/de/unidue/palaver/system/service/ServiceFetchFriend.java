package de.unidue.palaver.system.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.engine.CommunicatorResult;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.values.StringValue;
import de.unidue.palaver.system.engine.Communicator;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;

public class ServiceFetchFriend extends Service {
    private static final String TAG= ServiceFetchFriend.class.getSimpleName();
    private Communicator communicator;
    private PalaverDao palaverDao;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(getApplicationContext());
        palaverDao = palaverRoomDatabase.palaverDao();
        FetchAllFriendFromServer fetchAllFriendFromServer=new FetchAllFriendFromServer();
        fetchAllFriendFromServer.execute("initiate friend");
        communicator = PalaverEngine.getPalaverEngineInstance().getCommunicator();
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
                if(palaverDao.deleteFriend()==1){
                    for(Friend friend : communicatorResult.getData()) {
                        palaverDao.insert(friend);
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
