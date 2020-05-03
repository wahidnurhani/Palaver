package de.unidue.palaver.system.model;

import org.junit.Test;

import java.util.Date;

import de.unidue.palaver.system.resource.MessageType;

import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void getFriend() {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "true" , new Date());
        assertEquals("Test", message.getSender());
    }

    @Test
    public void getChatItemType() {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "true", new Date());
        assertSame(message.getMessageType(), MessageType.INCOMMING);
    }

    @Test
    public void getMessage() {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "true", new Date());
        assertEquals("Hallo World", message.getMessage());
    }

    @Test
    public void getChatItemAlignment() {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "true", new Date());
        assertSame(message.getChatItemAlignment(), ChatItemAlignment.LEFT);
    }

    @Test
    public void getIsReadStatus() {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "false", new Date());
        assertFalse(message.getIsReadStatus());
        message.setIsReadStatus(true);
        assertTrue(message.getIsReadStatus());
        System.out.println(message.getDate().toString());
        System.out.println(message.getDateToString());
    }

    @Test
    public void getDate() {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "true", "2016-02-12 17:02:38.663");
        assertNotNull(message.getDate());
        System.out.println(message.getDate().toString());
        System.out.println(message.getDateToString());
    }
}