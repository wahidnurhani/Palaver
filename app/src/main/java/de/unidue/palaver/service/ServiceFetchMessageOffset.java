package de.unidue.palaver.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.Objects;

import de.unidue.palaver.httpclient.IHttpClient;
import de.unidue.palaver.httpclient.JSONBuilder;
import de.unidue.palaver.httpclient.RetrofitHttpClient;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.model.User;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
import retrofit2.Response;

public class ServiceFetchMessageOffset extends Service {
    private static final String TAG= ServiceFetchMessageOffset.class.getSimpleName();

    public static void startIntent(Context context, User user, Friend friend, String offset) {
        Intent intent = new Intent(context, ServiceFetchMessageOffset.class);
        intent.putExtra(StringValue.IntentKeyName.OFFSET, offset);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringValue.IntentKeyName.USER, user);
        bundle.putSerializable(StringValue.IntentKeyName.FRIEND, friend);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PalaverDao palaverDao = PalaverDB.getDatabase(getApplicationContext()).palaverDao();
        User user = (User) Objects.requireNonNull(intent.getExtras()).getSerializable(StringValue.IntentKeyName.USER);
        Friend friend = (Friend) intent.getExtras().getSerializable(StringValue.IntentKeyName.FRIEND);
        String offset = (String) intent.getCharSequenceExtra(StringValue.IntentKeyName.OFFSET);
        new FetchMessageOffset(palaverDao, user, offset).execute(friend);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, StringValue.LogMessage.SERVICE_DESTROYED);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchMessageOffset extends AsyncTask<Friend, Void, Void> {
        PalaverDao palaverDao;
        User user;
        String offset;

        FetchMessageOffset(PalaverDao palaverDao, User user, String offset) {
            this.palaverDao = palaverDao;
            this.user = user;
            this.offset = offset;
        }

        @Override
        protected Void doInBackground(Friend... friends) {
            IHttpClient retrofitHttpClient = new RetrofitHttpClient();
            Response<StackApiResponseList<Message>> response;
            JSONBuilder.GetMessageOffset body = new JSONBuilder.GetMessageOffset(user, friends[0], offset);
            try {
                response = retrofitHttpClient.getMessageOffset(body);
                assert response != null;
                if(response.body().getMessageType()==1){
                    for(Message message : response.body().getDatas()){
                        message.setFriendUserName(friends[0].getUsername());
                        palaverDao.insert(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            onDestroy();
            return null;
        }
    }
}
