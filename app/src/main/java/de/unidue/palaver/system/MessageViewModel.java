package de.unidue.palaver.system;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.model.ListLiveData;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.IChat;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.resource.MessageType;

public class MessageViewModel extends AndroidViewModel implements Comparable<MessageViewModel>, IChat, Serializable {

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


    @Override
    public void sort(){
        //Collections.sort(messageList);
    }

    @Override
    public Message getLatestMessage() {
        sort();
        //return messageList.get(messageList.size()-1);
        return null;
    }

    @Override
    public int compareTo(MessageViewModel o) {
        return this.getLatestMessage().compareTo(o.getLatestMessage());
    }

    @Override
    public boolean setAllMessageToRead() {
        //TODO
        return false;
    }

    @Override
    public void refreshView(){
        //TODO
    }


    private void fetchChat(){
        FectchChatFromDB fectchChatFromDB = new FectchChatFromDB();
        fectchChatFromDB.execute();
    }

    public void addMessage(String text) {
         Message message = new Message(
                 user.getUserData().getUserName(),
                 friend.getUsername(),
                 MessageType.OUT,
                 text,
                 "true",
                 new Date());
         messageListLiveData.add(message);
    }

    @SuppressLint("StaticFieldLeak")
    private class FectchChatFromDB extends AsyncTask<Void, Void, List<Message>> {

        @Override
        protected List<Message> doInBackground(Void... voids) {
            PalaverDB palaverDB = Palaver.getInstance().getPalaverDB();
            return new ArrayList<>(palaverDB.getAllChatData(friend));
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            messageListLiveData.override(messages);
        }
    }
}
