package de.unidue.palaver.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.unidue.palaver.model.Chat;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;

public class ChatRepository{
    private PalaverDao palaverDao;

    public ChatRepository(Application application) {
        PalaverDB palaverDB = PalaverDB.getDatabase(application);
        palaverDao = palaverDB.palaverDao();
    }

    public LiveData<List<Chat>> getChats() {
        return palaverDao.getAllChat();
    }
}
