package de.unidue.palaver.model;

import org.junit.Test;

import java.util.Date;

import de.unidue.palaver.model.ChatItem;
import de.unidue.palaver.model.ChatItemAlignment;
import de.unidue.palaver.model.ChatItemType;
import de.unidue.palaver.model.Friend;

import static org.junit.Assert.*;

public class ChatItemTest {

    @Test
    public void getFriend() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend, ChatItemType.INCOMMING, "Hallo World", new Date());
        assertTrue(chatItem.getFriend().getUsername().equals("Test"));
    }

    @Test
    public void getChatItemType() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend, ChatItemType.INCOMMING, "Hallo World", new Date());
        assertTrue(chatItem.getChatItemType()==ChatItemType.INCOMMING);
    }

    @Test
    public void getMessage() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend, ChatItemType.INCOMMING, "Hallo World", new Date());
        assertTrue(chatItem.getMessage().equals("Hallo World"));
    }

    @Test
    public void getChatItemAlignment() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend, ChatItemType.INCOMMING, "Hallo World", new Date());
        assertTrue(chatItem.getChatItemAlignment()== ChatItemAlignment.LEFT);
    }

    @Test
    public void getIsReadStatus() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend, ChatItemType.INCOMMING, "Hallo World", new Date());
        assertTrue(chatItem.getIsReadStatus()==false);
        chatItem.setIsReadStatus(true);
        assertTrue(chatItem.getIsReadStatus()==true);
    }

    @Test
    public void getDate() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend, ChatItemType.INCOMMING, "Hallo World", new Date());
        assertTrue(chatItem.getDate()!=null);
        System.out.println(chatItem.getDate());
    }
}