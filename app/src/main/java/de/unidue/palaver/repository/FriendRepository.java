package de.unidue.palaver.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.unidue.palaver.model.Friend;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.service.ServiceAddFriend;
import de.unidue.palaver.service.ServiceRemoveFriend;

public class FriendRepository {

    private PalaverDao palaverDao;
    private LiveData<List<Friend>> friends;
    private Application application;

    public FriendRepository(Application application) {
        this.application = application;
        PalaverDB palaverDB = PalaverDB.getDatabase(application);
        palaverDao = palaverDB.palaverDao();
        friends = palaverDao.getAllFriend();
    }

    public LiveData<List<Friend>> getAllFriends(){
        return friends;
    }

    public void remove(Friend friend){
        ServiceRemoveFriend.startIntent(application, friend);
    }

    public void removeAll(){
        new RemoveAllAsynctask(palaverDao).execute();
    }

    public void addFriend(String username) {
        ServiceAddFriend.startIntent(application, username);
    }

    int insert(Friend friend){
        InsertAsyncTask insertAsyncTask = new InsertAsyncTask(palaverDao);
        insertAsyncTask.execute(friend);
        return insertAsyncTask.getReturnValue();
    }

    public static class InsertAsyncTask extends AsyncTask<Friend, Void, Integer> {
        int returnValue;

        private PalaverDao palaverDao;
        InsertAsyncTask(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
            this.returnValue = 1;
        }

        int getReturnValue() {
            return returnValue;
        }

        @Override
        protected Integer doInBackground(Friend... friends) {
            returnValue = (int) palaverDao.insert(friends[0]);
            return returnValue;
        }
    }

    public static class RemoveAllAsynctask extends AsyncTask<Void, Void, Void>{

        private PalaverDao palaverDao;
        RemoveAllAsynctask(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            palaverDao.deleteAllFriend();
            return null;
        }
    }

}
