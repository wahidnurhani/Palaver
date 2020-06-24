package de.unidue.palaver.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.User;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.service.ServiceFetchMessageOffset;
import de.unidue.palaver.service.ServiceSendMessage;

public class MessageRepository {
    private PalaverDao palaverDao;
    private Friend friend;
    private LiveData<List<Message>> messages;

    public MessageRepository(Application application){
        PalaverDB palaverDB = PalaverDB.getDatabase(application);
        palaverDao = palaverDB.palaverDao();
    }

    public LiveData<List<Message>> getAllMessage(Friend friend){
        this.friend = friend;
        this.messages = palaverDao.getMessages(friend.getUsername());
        return messages;
    }

    public void sendMessageService(Activity activity, Message message){
        ServiceSendMessage.startIntent(activity, message);
    }

    public void fetchMessageOffset(Application application, User user, Friend friend){
        new GetOffsetAsynctask(application, user, friend, palaverDao).execute();
    }

    public Friend getFriend() {
        return friend;
    }

    public void update(Message message){
        new UpdateAsynctask(palaverDao).execute(message);
    }

    public static class UpdateAsynctask extends AsyncTask<Message, Void, Void>{
        private PalaverDao palaverDao;

        UpdateAsynctask(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            palaverDao.update(messages[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetOffsetAsynctask extends AsyncTask<Void, Void, String>{
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
