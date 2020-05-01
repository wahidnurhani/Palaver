package de.unidue.palaver.system.model;

import org.junit.Test;

import java.util.Date;

import de.unidue.palaver.system.resource.ChatItemAlignment;
import de.unidue.palaver.system.resource.ChatItemType;

import static org.junit.Assert.*;

public class ChatItemTest {

    @Test
    public void getFriend() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend.getUsername(), "saya", ChatItemType.INCOMMING, "Hallo World", new Date());
        assertEquals("Test", chatItem.getSender());
    }

    @Test
    public void getChatItemType() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend.getUsername(), "saya", ChatItemType.INCOMMING, "Hallo World", new Date());
        assertSame(chatItem.getChatItemType(), ChatItemType.INCOMMING);
    }

    @Test
    public void getMessage() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend.getUsername(), "saya", ChatItemType.INCOMMING, "Hallo World", new Date());
        assertEquals("Hallo World", chatItem.getMessage());
    }

    @Test
    public void getChatItemAlignment() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend.getUsername(), "saya", ChatItemType.INCOMMING, "Hallo World", new Date());
        assertSame(chatItem.getChatItemAlignment(), ChatItemAlignment.LEFT);
    }

    @Test
    public void getIsReadStatus() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend.getUsername(), "saya", ChatItemType.INCOMMING, "Hallo World", new Date());
        assertFalse(chatItem.getIsReadStatus());
        chatItem.setIsReadStatus(true);
        assertTrue(chatItem.getIsReadStatus());
    }

    @Test
    public void getDate() {
        Friend friend = new Friend("Test");
        ChatItem chatItem = new ChatItem(friend.getUsername(), "saya", ChatItemType.INCOMMING, "Hallo World", new Date());
        assertNotNull(chatItem.getDate());
        System.out.println(chatItem.getDate());
    }
}