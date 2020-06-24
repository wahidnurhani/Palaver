package de.unidue.palaver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.User;
import de.unidue.palaver.repository.FriendRepository;
import de.unidue.palaver.sessionmanager.SessionManager;

public class FriendViewModel extends AndroidViewModel {

    private FriendRepository friendRepository;
    private LiveData<List<Friend>> friends;
    private SessionManager sessionManager;
    private User user;

    public FriendViewModel(Application application) {
        super(application);

        friendRepository = new FriendRepository(application);
        sessionManager = SessionManager.getSessionManagerInstance(application);
        friends = friendRepository.getAllFriends();
        user = sessionManager.getUser();
    }

    public User getUser() {
        return user;
    }

    public LiveData<List<Friend>> getFriends() {
        return friends;
    }

    public List<Friend> search(String string){
        List<Friend> searched = new ArrayList<>();
        for (Friend friend: Objects.requireNonNull(friends.getValue())){
            if(friend.getUsername().contains(string)){
                searched.add(friend);
            }
        }
        if(string.equals("")){
            searched = friends.getValue();
        }
        return searched;
    }

    public void remove(Friend friend){
        friendRepository.remove( friend);
    }

    public void removeAll(){
        friendRepository.removeAll();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void addFriend(String username) {
        friendRepository.addFriend(username);
    }

}