package de.unidue.palaver.repository;

import android.app.Application;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.unidue.palaver.model.Friend;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class FriendRepositoryTest {
    private FriendRepository friendRepository;
    private Context context;

    @Test
    public void createRepository(){
        context = ApplicationProvider.getApplicationContext();
        friendRepository = new FriendRepository((Application) context);
        assertNotNull(friendRepository);
    }

    @Test
    public void getAllFriends() {
        context = ApplicationProvider.getApplicationContext();
        friendRepository = new FriendRepository((Application) context);
        assertNotNull(friendRepository.getLiveData());
    }

    @Test
    public void insertFriend(){
        context = ApplicationProvider.getApplicationContext();
        friendRepository = new FriendRepository((Application) context);
        Friend friend = new Friend("Hallo");
        assertEquals(1, friendRepository.insert(friend));
    }
}