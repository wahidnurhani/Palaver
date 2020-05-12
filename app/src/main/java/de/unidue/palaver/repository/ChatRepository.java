package de.unidue.palaver.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.unidue.palaver.model.Chat;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.roomdatabase.PalaverRoomDatabase;

public class ChatRepository{
    private PalaverDao palaverDao;

    public ChatRepository(Application application) {
        PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(application);
        palaverDao = palaverRoomDatabase.palaverDao();
    }

    public LiveData<List<Chat>> getChats() {
        return palaverDao.getAllChat();
    }
}
