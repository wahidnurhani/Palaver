package de.unidue.palaver.system.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.repository.FriendRepository;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;

public class FriendViewModel extends AndroidViewModel {

    private FriendRepository friendRepository;
    private LiveData<List<Friend>> friends;
//    private PalaverEngine palaverEngine;

    public FriendViewModel(Application application) {
        super(application);

        friendRepository = new FriendRepository(application);
        friends = friendRepository.getAllFriends();
//        this.palaverEngine = PalaverEngine.getPalaverEngineInstance();
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

    public void remove(Friend friend){
        friendRepository.remove(friend);
    }

    public void update(Friend friend){
        friendRepository.update(friend);
    }

    public void removeAll(){
        friendRepository.removeAll();
    }

//    public void fetchFriends() {
////
////        FetchFriendFromDB fetchFriendFromDB = new FetchFriendFromDB();
////        fetchFriendFromDB.execute();
//    }

    public void openAddFriendDialog(Context applicationContext, Activity activity) {
        //palaverEngine.handleOpenAddFriendDialogRequest(applicationContext, activity);
    }

//    @SuppressLint("StaticFieldLeak")
//    private class FetchFriendFromDB extends AsyncTask<Void, Void, List<Friend>> {
//
//        @Override
//        protected List<Friend> doInBackground(Void... voids) {
//            List<Friend> sorted = palaverDao.getAllFriend();
//            Collections.sort(sorted);
//            return new ArrayList<>(sorted);
//        }
//
//        @Override
//        protected void onPostExecute(List<Friend> friends) {
//            FriendViewModel.this.friends.override(friends);
//        }
//    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}