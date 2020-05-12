package de.unidue.palaver.repository;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.service.ServiceSendMessage;

public class MessageRepository {
    private PalaverDao palaverDao;
    private Friend friend;

    public MessageRepository(Application application){
        PalaverDB palaverDB = PalaverDB.getDatabase(application);
        palaverDao = palaverDB.palaverDao();
    }

    public LiveData<List<Message>> getAllMessage(Friend friend){
        this.friend = friend;
        return palaverDao.getMessages(friend.getUsername());
    }

    public void sendMessageService(Activity activity, Message message){
        ServiceSendMessage.startIntent(activity, message);
    }

    public Friend getFriend() {
        return friend;
    }

    public void update(Message message){
        new UpdateAsynctask(palaverDao).execute(message);
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
