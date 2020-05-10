package de.unidue.palaver.system.roomdatabase;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.List;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;

import static org.junit.Assert.*;

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
    public void writeAndReadInList() {
        Friend friend = new Friend("haloo");
        palaverDao.insert(friend);
        List<Friend> friends = palaverDao.getAllFriendList();
        assertNotNull(friends);

        Message message = new Message("wahid", "jimmy", "hallo", "2016-02-12T17:02:38.663");
        message.setFriendUserName("jimmy");
        palaverDao.insert(message);

        assertNotNull(palaverDao.getMessagesList("wahid"));
    }

    @Test
    public void deleteFriend(){

        Friend friend = new Friend("haloo");

        palaverDao.insert(friend);
        palaverDao.delete(friend);

        assertEquals(1, palaverDao.insert(friend));
        assertEquals(1, palaverDao.delete(friend));
    }

    @Test
    public void deleteMessage(){
        Message message = new Message("wahid", "jimmy", "hallo", "2016-02-12T17:02:38.663");
        message.setFriendUserName("jimmy");
        assertEquals(1, palaverDao.insert(message));
        assertEquals(1, palaverDao.delete(message));
    }

}
