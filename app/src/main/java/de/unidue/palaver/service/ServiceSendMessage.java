package de.unidue.palaver.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Objects;

import de.unidue.palaver.sessionmanager.SessionManager;

import de.unidue.palaver.httpclient.JSONBuilder;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.StackApiResponseDate;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.User;
import de.unidue.palaver.httpclient.Retrofit;
import de.unidue.palaver.model.StringValue;

import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.dialogandtoast.CustomToast;
import retrofit2.Response;


public class ServiceSendMessage extends Service {
    private static final String TAG= ServiceAddFriend.class.getSimpleName();

    public static void startIntent(Activity activity, Message message) {
        Intent intent = new Intent(activity, ServiceSendMessage.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringValue.IntentKeyName.MESSAGE, message);
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
        Message message = (Message) Objects.requireNonNull(intent.getExtras()).
                getSerializable(StringValue.IntentKeyName.MESSAGE);
        User user = SessionManager.getSessionManagerInstance(getApplicationContext()).getUser();
        new SendMessageAsyncTask(user).execute(message);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, StringValue.LogMessage.SERVICE_DESTROYED);
    }


    @SuppressLint("StaticFieldLeak")
    public class SendMessageAsyncTask extends AsyncTask<Message, Void, Response<StackApiResponseDate>> {
        User user;

        SendMessageAsyncTask(User user){
            this.user = user;
        }

        @Override
        protected Response<StackApiResponseDate> doInBackground(Message... messages) {
            PalaverDB palaverDB = PalaverDB.getDatabase(getApplicationContext());
            PalaverDao palaverDao = palaverDB.palaverDao();
            Friend friend = new Friend(messages[0].getFriendUserName());
            Retrofit retrofit = new Retrofit();
            Response<StackApiResponseDate> response = null;
            JSONBuilder.SendMessageBody body = new JSONBuilder.SendMessageBody(user,
                    friend, messages[0]);
            try {
                response = retrofit.sendMessage(body);
                assert response.body() != null;
                if(response.body().getMessageType()==1){
                    messages[0].setDate(response.body().getDateTime().getDateTime());
                    messages[0].setFriendUserName(friend.getUsername());
                    palaverDao.insert(messages[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }


        @Override
        protected void onPostExecute(Response<StackApiResponseDate> s) {
            super.onPostExecute(s);
            if(s!=null){
                assert s.body() != null;
                if(s.body().getMessageType()!=1){
                    CustomToast.makeText(getApplicationContext(),
                            "failed to send message");
                }
            }
        }
    }

}
