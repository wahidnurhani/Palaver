package de.unidue.palaver.system.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.model.ListLiveData;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.resource.MessageType;

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
         Message message = new Message(
                 user.getUserName(),
                 friend.getUsername(),
                 MessageType.OUT,
                 text,
                 "true",
                 new Date());
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
            PalaverDB palaverDB = Palaver.getInstance().getPalaverDB();

            List<Message> messages;
            messages = palaverDB.getAllChatData(friend);
            return messages;
        }

        @Override
        protected void onPostExecute(List<Message> messages) {

            messageListLiveData.addAll(messages);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
