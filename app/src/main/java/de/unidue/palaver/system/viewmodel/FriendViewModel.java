package de.unidue.palaver.system.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;

public class FriendViewModel extends AndroidViewModel {

    private ListLiveData<Friend> friendsLiveData;
    private PalaverEngine palaverEngine;
    private PalaverDao palaverDao;

    public FriendViewModel(Application application) {
        super(application);

        PalaverRoomDatabase palaverDB = PalaverRoomDatabase.getDatabase(getApplication());
        palaverDao = palaverDB.palaverDao();
        friendsLiveData = new ListLiveData<>();
        friendsLiveData.setValue(new ArrayList<>());
        fetchFriends();
        this.palaverEngine = Palaver.getInstance().getPalaverEngine();
    }

    public ListLiveData<Friend> getFriendsLiveData() {
        return friendsLiveData;
    }

    public List<Friend> search(String string){
        List<Friend> searched = new ArrayList<>();
        for (Friend friend: Objects.requireNonNull(friendsLiveData.getValue())){
            if(friend.getUsername().contains(string)){
                searched.add(friend);
            }
        }
        if(string.equals("")){
            searched = friendsLiveData.getValue();
        }
        return searched;
    }

    public void fetchFriends() {

        FetchFriendFromDB fetchFriendFromDB = new FetchFriendFromDB();
        fetchFriendFromDB.execute();
    }

    public void OpenChatManagerActivity(Activity activity) {
        palaverEngine.handleOpenChatManagerActivityRequest(activity);
    }

    public void openAddFriendDialog(Context applicationContext, Activity activity) {
        palaverEngine.handleOpenAddFriendDialogRequest(applicationContext, activity);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchFriendFromDB extends AsyncTask<Void, Void, List<Friend>> {

        @Override
        protected List<Friend> doInBackground(Void... voids) {
            List<Friend> sorted = palaverDao.loadAllFriend();
            Collections.sort(sorted);
            return new ArrayList<>(sorted);
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            friendsLiveData.override(friends);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}