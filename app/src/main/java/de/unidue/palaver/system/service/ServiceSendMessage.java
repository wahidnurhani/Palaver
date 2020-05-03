package de.unidue.palaver.system.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.engine.Communicator;
import de.unidue.palaver.system.model.CommunicatorResult;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.resource.StringValue;


public class ServiceSendMessage extends Service {
    private static final String TAG= ServiceAddFriend.class.getSimpleName();

    private User user;
    private Message message;
    private Friend friend;
    private Communicator communicator;
    private PalaverDB palaverDB;

    public static void startIntent(Context applicationContext, Activity activity, Friend friend, Message message) {
        Intent intent = new Intent(applicationContext, ServiceSendMessage.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringValue.IntentKeyName.MESSAGE, message);
        bundle.putSerializable(StringValue.IntentKeyName.FRIEND, friend);
        intent.putExtras(bundle);
        activity.startService(intent);
        System.out.println("send message service intent");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("send message service intent start");
        message = (Message) Objects.requireNonNull(intent.getExtras()).getSerializable(StringValue.IntentKeyName.MESSAGE);
        friend = (Friend) intent.getExtras().getSerializable(StringValue.IntentKeyName.FRIEND);
        user = SessionManager.getSessionManagerInstance(getApplicationContext()).getUser();
        communicator = Palaver.getInstance().getPalaverEngine().getCommunicator();
        palaverDB = Palaver.getInstance().getPalaverDB();

        SendMessage sendMessage = new SendMessage();
        sendMessage.execute();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, StringValue.LogMessage.SERVICE_DESTROYED);
    }

    @SuppressLint("StaticFieldLeak")
    public class SendMessage extends AsyncTask<Void, Void, CommunicatorResult<Date>> {

        @Override
        protected CommunicatorResult<Date> doInBackground(Void... myParams) {
            CommunicatorResult<Date> resultValue = communicator.sendMessage(user, friend, message);
            if(resultValue.getResponseValue()!=1){
                palaverDB.deleteMessage(message);
            }
            return resultValue;
        }


        @Override
        protected void onPostExecute(CommunicatorResult<Date> s) {
            super.onPostExecute(s);
            if(s.getResponseValue()!=1){
                Palaver.getInstance().getUiManager().showToast(getApplicationContext(), "failed to send message");
            }


        }
    }

}
