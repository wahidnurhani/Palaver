package de.unidue.palaver.system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.ListLiveData;
import de.unidue.palaver.ui.FriendManagerActivity;

public class FriendModelView extends MutableLiveData<Friend> {

    private ListLiveData<Friend> friendsLiveData;
    private PalaverEngine palaverEngine;

    public FriendModelView() {
        this.palaverEngine = Palaver.getInstance().getPalaverEngine();
        this.friendsLiveData = new ListLiveData<>();
        this.friendsLiveData.setValue(new ArrayList<>());
        this.fetchFriends();
    }

    public ListLiveData<Friend> getFriendsLiveData() {
        return friendsLiveData;
    }

    public void search(String string){
        //TODO
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

    public void openChat(Activity activity, Friend friend) {
        palaverEngine.handleClickOnFriend(activity, friend);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchFriendFromDB extends AsyncTask<Void, Void, List<Friend>> {

        @Override
        protected List<Friend> doInBackground(Void... voids) {
            PalaverDB palaverDB = Palaver.getInstance().getPalaverDB();
            return new ArrayList<>(palaverDB.getAllFriends());
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            friendsLiveData.override(friends);
        }
    }
}