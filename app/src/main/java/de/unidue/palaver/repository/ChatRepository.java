package de.unidue.palaver.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;

public class ChatRepository implements Repository{

    private PalaverDao palaverDao;
    private LiveData chats;

    public ChatRepository(Application application) {
        PalaverDB palaverDB = PalaverDB.getDatabase(application);
        this.palaverDao = palaverDB.palaverDao();
        this.chats = palaverDao.getAllChat();
    }

    @Override
    public LiveData getLiveData() {
        return chats;
    }

    @Override
    public void add(Object o) {
    }

    @Override
    public void delete(Object o) {
    }


}
