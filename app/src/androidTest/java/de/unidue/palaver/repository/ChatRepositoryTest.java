package de.unidue.palaver.repository;

import android.app.Application;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ChatRepositoryTest {
    private ChatRepository chatRepository;

    @Test
    public void createRepository(){
        Context context = ApplicationProvider.getApplicationContext();
        chatRepository = new ChatRepository((Application) context);

        assertNotNull(chatRepository);
    }
}