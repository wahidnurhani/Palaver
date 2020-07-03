package de.unidue.palaver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.unidue.palaver.repository.Repository;
import de.unidue.palaver.sessionmanager.SessionManager;
import de.unidue.palaver.model.Chat;
import de.unidue.palaver.repository.ChatRepository;

public class ChatsViewModel extends AndroidViewModel {

    private Repository chatRepository;
    private SessionManager sessionManager;
    private LiveData<List<Chat>> chats;
    private LiveData<Boolean> loginStatus;

    public ChatsViewModel(Application application) {
        super(application);
        this.chatRepository = new ChatRepository(application);
        this.sessionManager = SessionManager.getSessionManagerInstance(application);
        this.chats = chatRepository.getLiveData();
        this.loginStatus = sessionManager.getLoginStatus();
    }

    public LiveData<Boolean> getLoginStatus() {
        return loginStatus;
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

    public void handleLogoutRequest() {
        sessionManager.endSession();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
