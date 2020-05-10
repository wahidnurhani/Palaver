package de.unidue.palaver.system.repository;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.io.Serializable;
import java.util.List;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;
import de.unidue.palaver.system.service.ServiceSendMessage;

public class MessageRepository implements Serializable {
    private PalaverDao palaverDao;
    private Friend friend;

    public MessageRepository(Application application){
        PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(application);
        palaverDao = palaverRoomDatabase.palaverDao();
    }

    public LiveData<List<Message>> getAllMessage(Friend friend){
        this.friend = friend;
        return palaverDao.getMessages(friend.getUsername());
    }

    public void sendMessageService(Activity activity, Message message){
        ServiceSendMessage.startIntent(activity, message);
    }

    public void insert(Message message){
        new InsertAsyntask(palaverDao).execute(message);
    }

    public Friend getFriend() {
        return friend;
    }

    public void remove(Message message){
        new DeleteAsyntask(palaverDao).execute(message);
    }

    public void update(Message message){
        new UpdateAsynctask(palaverDao).execute(message);
    }

    public void removeAll(){
        new RemoveAllAsynctask(palaverDao).execute();
    }

    public static class InsertAsyntask extends AsyncTask<Message, Void, Void> {
        private PalaverDao palaverDao;

        InsertAsyntask(PalaverDao palaverDao) {
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
         * @param messages The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Message... messages) {
            return null;
        }
    }

    public static class RemoveAllAsynctask extends AsyncTask<Void, Void, Void>{

        private PalaverDao palaverDao;
        RemoveAllAsynctask(PalaverDao palaverDao) {
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
            palaverDao.deleteAllChat();
            return null;
        }
    }
    public static class DeleteAsyntask extends AsyncTask<Message, Void, Void>{
        private PalaverDao palaverDao;

        DeleteAsyntask(PalaverDao palaverDao) {
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
         * @param messages The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Message... messages) {
            palaverDao.delete(messages[0]);
            return null;
        }
    }
    public static class UpdateAsynctask extends AsyncTask<Message, Void, Void>{
        private PalaverDao palaverDao;

        UpdateAsynctask(PalaverDao palaverDao) {
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
         * @param messages The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Message... messages) {
            palaverDao.update(messages[0]);
            return null;
        }
    }
}
