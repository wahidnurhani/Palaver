package de.unidue.palaver.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.User;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.serviceandworker.ServiceFetchMessageOffset;
import de.unidue.palaver.serviceandworker.ServiceSendMessage;

public class MessageRepository implements Repository{
    private PalaverDao palaverDao;
    private Friend friend;
    private Activity activity;
    private LiveData messages;

    public MessageRepository(Context applicationContext, Activity activity, Friend friend){
        this.friend = friend;
        PalaverDB palaverDB = PalaverDB.getDatabase(applicationContext);
        this.activity = activity;
        this.palaverDao = palaverDB.palaverDao();
        this.messages =  palaverDao.getMessages(friend.getUsername());
    }

    public MessageRepository(Context applicationContext, Friend friend) {
        this.friend = friend;
        PalaverDB palaverDB = PalaverDB.getDatabase(applicationContext);
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

    public void fetchMessageOffset(Context applicationContext, User user, Friend friend){
        new GetOffsetAsynctask(applicationContext, user, friend, palaverDao).execute();
    }

    public Friend getFriend() {
        return friend;
    }


    @SuppressLint("StaticFieldLeak")
    private static class GetOffsetAsynctask extends AsyncTask<Void, Void, String>{
        Context applicationContext;
        User user;
        Friend friend;
        PalaverDao palaverDao;

        GetOffsetAsynctask(Context applicationContext, User user, Friend friend, PalaverDao palaverDao) {
            this.applicationContext = applicationContext;
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
            ServiceFetchMessageOffset.startIntent(applicationContext, user, friend, offset);
        }
    }
}
