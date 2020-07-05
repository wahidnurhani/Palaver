package de.unidue.palaver.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.ResultReceiver;

import androidx.lifecycle.LiveData;

import de.unidue.palaver.activity.ChatRoomActivity;
import de.unidue.palaver.serviceandworker.locationservice.LocationProviderService;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.User;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.serviceandworker.ServiceFetchMessageOffset;
import de.unidue.palaver.serviceandworker.ServiceSendMessage;
import de.unidue.palaver.serviceandworker.locationservice.LocationServiceConstant;

public class MessageRepository implements Repository{
    private PalaverDao palaverDao;
    private Friend friend;
    private Activity activity;
    private LiveData messages;

    public MessageRepository(Application application, Activity activity, Friend friend){
        this.friend = friend;
        PalaverDB palaverDB = PalaverDB.getDatabase(application);
        this.activity = activity;
        this.palaverDao = palaverDB.palaverDao();
        this.messages =  palaverDao.getMessages(friend.getUsername());
    }

    public MessageRepository(Application application, Friend friend) {
        this.friend = friend;
        PalaverDB palaverDB = PalaverDB.getDatabase(application);
        this.palaverDao = palaverDB.palaverDao();
        this.messages = palaverDao.getMessages(friend.getUsername());
    }

    @Override
    public LiveData getLiveData() {
        return messages;
    }

    @Override
    public void add(Object o) {
        if(o instanceof Message){
            ServiceSendMessage.startIntent(activity, (Message) o);
        }
    }

    @Override
    public void delete(Object o) {
    }

    public void fetchMessageOffset(Application application, User user, Friend friend){
        new GetOffsetAsynctask(application, user, friend, palaverDao).execute();
    }

    public Friend getFriend() {
        return friend;
    }

    public void fetchLocation(ResultReceiver locationResultReceiver) {
        Intent intent = new Intent(activity, LocationProviderService.class);
        intent.putExtra(LocationServiceConstant.RECEIVER_KEY, locationResultReceiver);
        activity.startService(intent);
    }


    @SuppressLint("StaticFieldLeak")
    private static class GetOffsetAsynctask extends AsyncTask<Void, Void, String>{
        Application application;
        User user;
        Friend friend;
        PalaverDao palaverDao;

        GetOffsetAsynctask(Application application, User user, Friend friend, PalaverDao palaverDao) {
            this.application = application;
            this.user = user;
            this.friend = friend;
            this.palaverDao = palaverDao;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String offsetDb = palaverDao.getOffset(friend.getUsername());
            String[] offset = offsetDb.split("\\+");
            return offset[0];
        }

        @Override
        protected void onPostExecute(String offset) {
            ServiceFetchMessageOffset.startIntent(application, user, friend, offset);
        }
    }
}
