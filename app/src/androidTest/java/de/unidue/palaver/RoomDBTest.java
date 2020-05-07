package de.unidue.palaver;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.Date;
import java.util.List;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.resource.MessageType;
import de.unidue.palaver.system.roomdatabase.PalaverDao;
import de.unidue.palaver.system.roomdatabase.PalaverRoomDatabase;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RoomDBTest {

    private PalaverDao palaverDao;
    private PalaverRoomDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, PalaverRoomDatabase.class).build();
        palaverDao = db.palaverDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() {
        Friend friend = new Friend("haloo");
        palaverDao.insert(friend);
        List<Friend> friends = palaverDao.loadAllFriend();
        assertNotNull(friends);

        Message message = new Message("wahid", "jimmy", MessageType.INCOMMING, "hallo", "true", "2016-02-12 17:02:38.663");
        palaverDao.insert(message);

        assertNotNull(palaverDao.loadChat("wahid").get(0));
    }
}
