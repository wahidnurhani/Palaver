package de.unidue.palaver.system.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;

public class FriendRepository {

    private PalaverDao palaverDao;
    private LiveData<List<Friend>> friends;

    public FriendRepository(Application application) {
        PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(application);
        palaverDao = palaverRoomDatabase.palaverDao();
        friends = palaverDao.getAllFriend();
    }

    public LiveData<List<Friend>> getAllFriends(){
        return friends;
    }

    public void insert(Friend friend){
        new InsertAsyntask(palaverDao).execute(friend);
    }

    public void remove(Friend friend){
        new DeleteAsyntask(palaverDao).execute(friend);
    }

    public void update(Friend friend){
        new UpdateAsynctask(palaverDao).execute(friend);
    }

    public void removeAll(){
        new RemoveAllAsynctask(palaverDao).execute();
    }

    public static class InsertAsyntask extends AsyncTask<Friend, Void, Void>{
        private PalaverDao palaverDao;

        public InsertAsyntask(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param friends The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Friend... friends) {
            palaverDao.insert(friends[0]);
            return null;
        }
    }

    public static class RemoveAllAsynctask extends AsyncTask<Void, Void, Void>{

        private PalaverDao palaverDao;
        public RemoveAllAsynctask(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param voids The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Void... voids) {
            palaverDao.deleteAllFriend();
            return null;
        }
    }
    public static class DeleteAsyntask extends AsyncTask<Friend, Void, Void>{
        private PalaverDao palaverDao;

        public DeleteAsyntask(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param friends The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Friend... friends) {
            palaverDao.delete(friends[0]);
            return null;
        }
    }
    public static class UpdateAsynctask extends AsyncTask<Friend, Void, Void>{
        private PalaverDao palaverDao;

        public UpdateAsynctask(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param friends The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Friend... friends) {
            palaverDao.update(friends[0]);
            return null;
        }
    }

}
