package de.unidue.palaver.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import de.unidue.palaver.model.Friend;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.serviceandworker.ServiceAddFriend;
import de.unidue.palaver.serviceandworker.ServiceRemoveFriend;

public class FriendRepository implements Repository{

    private PalaverDao palaverDao;
    private LiveData friends;
    private Application application;

    public FriendRepository(Application application) {
        this.application = application;
        PalaverDB palaverDB = PalaverDB.getDatabase(application);
        this.palaverDao = palaverDB.palaverDao();
        this.friends = palaverDao.getAllFriend();
    }

    @Override
    public LiveData getLiveData() {
        return friends;
    }

    @Override
    public void add(Object o) {
        if(o instanceof Friend){
            ServiceAddFriend.startIntent(application, ((Friend) o).getUsername());
        }
    }

    @Override
    public void delete(Object o) {
        if(o instanceof Friend){
            ServiceRemoveFriend.startIntent(application, (Friend) o);
        }
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
}
