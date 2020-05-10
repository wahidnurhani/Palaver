package de.unidue.palaver.system.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.unidue.palaver.system.model.Chat;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;

public class ChatRepository implements ControlRepository{
    private PalaverDao palaverDao;

    public ChatRepository(Application application) {
        PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(application);
        palaverDao = palaverRoomDatabase.palaverDao();
    }

    public LiveData<List<Chat>> getChats() {
        return palaverDao.getAllChat();
    }


    @Override
    public void populateData() {

    }

    @Override
    public void cleanData() {
        new CleanDatabase(palaverDao).execute();
    }

    private static class CleanDatabase extends AsyncTask<Void, Void, Void> {
        PalaverDao palaverDao;

        CleanDatabase(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            palaverDao.deleteAllChat();
            palaverDao.deleteAllFriend();
            return null;
        }
    }
}
