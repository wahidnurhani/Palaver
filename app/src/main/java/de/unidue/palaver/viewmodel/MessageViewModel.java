package de.unidue.palaver.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.Serializable;
import java.util.List;

import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.repository.MessageRepository;
import de.unidue.palaver.sessionmanager.SessionManager;

public class MessageViewModel extends AndroidViewModel implements Comparable<MessageViewModel>, Serializable {

    private MessageRepository messageRepository;
    private Friend friend;
    private SessionManager sessionManager;
    private LiveData<List<Message>> messages;

    public MessageViewModel(Application application) {
        super(application);
        this.messageRepository = new MessageRepository(getApplication());
        this.sessionManager = SessionManager.getSessionManagerInstance(application);
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
        this.messages = messageRepository.getAllMessage(friend);
    }

    public Friend getFriend() {
        return friend;
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public boolean setAllMessageToRead() {
        //TODO
        return false;
    }

    public void sendMessage(Activity activity, Message message) {
         messageRepository.sendMessageService(activity, message);
    }

    @Override
    public int compareTo(MessageViewModel o) {
        return 0;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
