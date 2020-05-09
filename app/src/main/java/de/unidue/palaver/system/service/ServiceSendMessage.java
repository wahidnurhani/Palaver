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

import java.io.IOException;
import java.util.Objects;

import de.unidue.palaver.system.engine.SessionManager;

import de.unidue.palaver.system.httpclient.JSONBuilder;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.model.DataServerResponse;
import de.unidue.palaver.system.httpclient.NewCommunicator;
import de.unidue.palaver.system.model.StringValue;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;
import retrofit2.Response;


public class ServiceSendMessage extends Service {
    private static final String TAG= ServiceAddFriend.class.getSimpleName();

    private User user;
    private Message message;
    private Friend friend;
    private PalaverRoomDatabase palaverRoomDatabase;
    private PalaverDao palaverDao;

    public static void startIntent(Context applicationContext, Activity activity, Friend friend, Message message) {
        Intent intent = new Intent(applicationContext, ServiceSendMessage.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringValue.IntentKeyName.MESSAGE, message);
        bundle.putSerializable(StringValue.IntentKeyName.FRIEND, friend);
        intent.putExtras(bundle);
        activity.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        message = (Message) Objects.requireNonNull(intent.getExtras()).getSerializable(StringValue.IntentKeyName.MESSAGE);
        friend = (Friend) intent.getExtras().getSerializable(StringValue.IntentKeyName.FRIEND);
        user = SessionManager.getSessionManagerInstance(getApplicationContext()).getUser();

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
    public class SendMessage extends AsyncTask<Void, Void, Response<DataServerResponse>> {

        @Override
        protected Response<DataServerResponse> doInBackground(Void... myParams) {
            palaverRoomDatabase = PalaverRoomDatabase.getDatabase(getApplicationContext());
            palaverDao = palaverRoomDatabase.palaverDao();
            NewCommunicator newCommunicator = new NewCommunicator();
            Response<DataServerResponse> response = null;
            JSONBuilder.SendMessageBody body = new JSONBuilder.SendMessageBody(user, friend, message);
            try {
                response = newCommunicator.sendMessage(body);
                assert response.body() != null;
                if(response.body().getMessageType()==1){
                    message.setDate(response.body().getDataDateTime().getDateTime());
                    palaverDao.insert(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }


        @Override
        protected void onPostExecute(Response<DataServerResponse> s) {
            super.onPostExecute(s);
            if(s!=null){
                if(s.body().getMessageType()!=1){
                    PalaverEngine.getPalaverEngineInstance().getUiController().showToast(getApplicationContext(),
                            "failed to send message");
                }
            }
            onDestroy();
        }
    }

}
