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

public class MessageViewModel extends AndroidViewModel implements Comparable<MessageViewModel>, Serializable {

    private MessageRepository messageRepository;
    private Friend friend;
    private LiveData<List<Message>> messages;

    public MessageViewModel(Application application, Activity activity, Friend friend) {
        super(application);
        this.friend = friend;
        this.messageRepository = new MessageRepository(getApplication(), activity, friend);
        this.messages = messageRepository.getLiveData();
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

    public void sendMessage(Message message) {
         messageRepository.add(message);
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
