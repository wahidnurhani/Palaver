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

import java.util.List;

import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.engine.Communicator;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.engine.UIController;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.engine.CommunicatorResult;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.values.StringValue;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;

public class ServiceFetchAllChat extends Service {
    private static final String TAG= ServiceFetchAllChat.class.getSimpleName();

    private Communicator communicator;
    private UIController uiController;
    private SessionManager sessionManager;
    private PalaverDao palaverDao;

    public static void startIntent(Context applicationContext, Activity activity) {
        Intent intent = new Intent(applicationContext, ServiceFetchAllChat.class);
        activity.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(getApplicationContext());
        palaverDao = palaverRoomDatabase.palaverDao();
        sessionManager = SessionManager.getSessionManagerInstance(getApplicationContext());
        communicator = PalaverEngine.getPalaverEngineInstance().getCommunicator();
        uiController = PalaverEngine.getPalaverEngineInstance().getUiController();
        FetchAllChat fetchAllChat = new FetchAllChat();
        fetchAllChat.execute("all");
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, StringValue.LogMessage.SERVICE_DESTROYED);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchAllChat extends AsyncTask<String, Void, CommunicatorResult<Message>> {

        @Override
        protected CommunicatorResult<Message> doInBackground(String... strings) {
            CommunicatorResult<Message> result = null;
            User user = sessionManager.getUser();
            List<Friend> friends = palaverDao.loadAllFriend();
            for(Friend friend : friends){
                result = communicator.getMessage(user, friend);
                if(result.getResponseValue()==1){
                    for (Message message : result.getData()){
                        palaverDao.insert(message);
                    }
                }
            }

            Intent intent = new Intent(StringValue.IntentAction.BROADCAST_ALL_CHAT_FETCHED);
            LocalBroadcastManager.getInstance(ServiceFetchAllChat.this).sendBroadcast(intent);
            return result;
        }

        @Override
        protected void onPostExecute(CommunicatorResult<Message> s) {
            super.onPostExecute(s);
            uiController.showToast(getApplicationContext(), s.getMessage());
            onDestroy();
        }
    }
}
