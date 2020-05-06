package de.unidue.palaver.system.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;

import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.ListLiveData;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;

public class FriendViewModel extends AndroidViewModel {

    private ListLiveData<Friend> friendsLiveData;
    private PalaverEngine palaverEngine;
    private PalaverDao palaverDao;
    private PalaverRoomDatabase palaverDB;

    public FriendViewModel(Application application) {
        super(application);

        palaverDB = PalaverRoomDatabase.getDatabase(getApplication());
        palaverDao = palaverDB.friendsDao();

        this.palaverEngine = Palaver.getInstance().getPalaverEngine();
        this.friendsLiveData = new ListLiveData<>();
        this.friendsLiveData.setValue(new ArrayList<>());
        this.fetchFriends();
    }

    public void insert(Friend friend){
        new InsertAsyncTask(palaverDao).execute(friend);
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

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private class InsertAsyncTask extends AsyncTask<Friend, Void, Void>{

        PalaverDao palaverDao;

        InsertAsyncTask(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

        @Override
        protected Void doInBackground(Friend... friends) {
            palaverDao.insert(friends[0]);
            return null;
        }
    }
}