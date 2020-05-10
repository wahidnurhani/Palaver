package de.unidue.palaver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.unidue.palaver.sessionmanager.SessionManager;
import de.unidue.palaver.model.Chat;
import de.unidue.palaver.repository.ChatRepository;

public class ChatsViewModel extends AndroidViewModel {

    private ChatRepository chatRepository;
    private LiveData<List<Chat>> chats;

    public ChatsViewModel(Application application) {
        super(application);
        chatRepository = new ChatRepository(application);
        this.chats = chatRepository.getChats();
    }

    public LiveData<List<Chat>> getChats() {
        return chats;
    }

    public List<Chat> search(String newText) {
        List<Chat> searched = new ArrayList<>();
        for (Chat chat: Objects.requireNonNull(chats.getValue())){
            if(chat.getFk_friend().contains(newText)){
                searched.add(chat);
            }
        }
        if(newText.equals("")){
            searched = chats.getValue();
        }
        return searched;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void handleLogoutRequest() {
        SessionManager.getSessionManagerInstance(getApplication()).endSession();
        chatRepository.cleanData();
    }
}
