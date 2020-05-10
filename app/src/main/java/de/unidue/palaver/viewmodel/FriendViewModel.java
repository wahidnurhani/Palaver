package de.unidue.palaver.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.unidue.palaver.model.Friend;
import de.unidue.palaver.repository.FriendRepository;

public class FriendViewModel extends AndroidViewModel {

    private FriendRepository friendRepository;
    private LiveData<List<Friend>> friends;

    public FriendViewModel(Application application) {
        super(application);

        friendRepository = new FriendRepository(application);
        friends = friendRepository.getAllFriends();
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

    public void insert(Friend friend){
        friendRepository.insert(friend);
    }

    public void remove(Activity activity, Friend friend){
        friendRepository.remove(activity, friend);
    }

    public void update(Friend friend){
        friendRepository.update(friend);
    }

    public void removeAll(){
        friendRepository.removeAll();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}