package de.unidue.palaver.system.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.engine.Parser;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.resource.MessageType;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;

public class MessageViewModel extends AndroidViewModel implements Comparable<MessageViewModel>, Serializable {

    private final Friend friend;
    private final User user;
    private final ListLiveData<Message> messageListLiveData;

    public MessageViewModel(Application application, Friend friend) {
        super(application);
        this.friend = friend;
        this.user = SessionManager.getSessionManagerInstance(getApplication()).getUser();
        this.messageListLiveData = new ListLiveData<>();
        this.messageListLiveData.setValue(new ArrayList<>());
        fetchChat();
    }

    public Friend getFriend() {
        return friend;
    }

    public ListLiveData<Message> getMessageList() {
        return messageListLiveData;
    }

    public boolean setAllMessageToRead() {
        //TODO
        return false;
    }

    private void fetchChat(){
        FectchChatFromDB fectchChatFromDB = new FectchChatFromDB();
        fectchChatFromDB.execute();
    }

    public void addMessage(Activity activity, String text) {
        Parser parser = new Parser();
         Message message = new Message(
                 user.getUserName(),
                 friend.getUsername(),
                 MessageType.OUT,
                 text,
                 "true",
                 parser.dateToString(new Date()));
        PalaverEngine.getPalaverEngineInstance().handleSendMessage(
                getApplication(),
                activity,
                friend,
                message.getMessage());
         messageListLiveData.add(message);
    }

    @Override
    public int compareTo(MessageViewModel o) {
        return 0;
    }

    @SuppressLint("StaticFieldLeak")
    private class FectchChatFromDB extends AsyncTask<Void, Void, List<Message>> {

        @Override
        protected List<Message> doInBackground(Void... voids) {
            PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(getApplication());
            PalaverDao palaverDao = palaverRoomDatabase.palaverDao();

            List<Message> messages;
            messages = palaverDao.loadChat(friend.getUsername());
            return messages;
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            Collections.sort(messages);
            messageListLiveData.addAll(messages);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
