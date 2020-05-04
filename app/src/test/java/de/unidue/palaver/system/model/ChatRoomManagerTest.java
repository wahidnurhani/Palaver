package de.unidue.palaver.system.model;

import org.junit.Test;

import java.util.Date;

import de.unidue.palaver.system.ChatRoomManager;
import de.unidue.palaver.system.resource.MessageType;

import static org.junit.Assert.*;

public class ChatRoomManagerTest {

    @Test
    public void getFriend() {
        Friend friend = new Friend("Jimmy");
        ChatRoomManager chatRoomManager = new ChatRoomManager(friend);
        assertEquals("Jimmy", chatRoomManager.getFriend().getUsername());
    }

    @Test
    public void getChatElements() {
        Friend friend = new Friend("Jimmy");
        Message message = new Message("saya", friend.getUsername(), MessageType.INCOMMING, "Hallo World", "true", new Date());
        ChatRoomManager chatRoomManager = new ChatRoomManager(friend);
        chatRoomManager.addChatItem(message);
        assertNotNull(chatRoomManager.getMessageList());
        assertSame(chatRoomManager.getMessageList().get(0).getMessageType(), MessageType.INCOMMING);
        assertEquals("Hallo World", chatRoomManager.getMessageList().get(0).getMessage());
    }

    @Test
    public void isUnReadMessageExist() {
        Friend friend = new Friend("Jimmy");
        ChatRoomManager chatRoomManager = new ChatRoomManager(friend);
        Date date1 = new Date();
        date1.setYear(2010);
        Date date2 = new Date();
        date2.setYear(2011);
        Date date3 = new Date();
        date3.setYear(2012);
        Message message0 = new Message("saya", friend.getUsername(), MessageType.INCOMMING, "Hallo World", "true", date1 );
        message0.setIsReadStatus(true);
        Message message1 = new Message("saya", friend.getUsername(), MessageType.INCOMMING, "Hallo World", "true", date2 );
        message1.setIsReadStatus(true);
        Message message2 = new Message("saya", friend.getUsername(), MessageType.INCOMMING, "Hallo World", "true", date3 );
        message2.setIsReadStatus(false);
        chatRoomManager.addChatItem(message0);
        chatRoomManager.addChatItem(message2);
        chatRoomManager.addChatItem(message1);
        assertTrue(chatRoomManager.isUnReadMessageExist());
    }

    @Test
    public void sort() {
        Friend friend = new Friend("Jimmy");
        ChatRoomManager chatRoomManager = new ChatRoomManager(friend);
        Date date1 = new Date();
        date1.setYear(2010);
        Date date2 = new Date();
        date2.setYear(2011);
        Message message0 = new Message("saya", friend.getUsername(), MessageType.INCOMMING, "Hallo World", "true", date1 );
        Message message1 = new Message("saya", friend.getUsername(), MessageType.INCOMMING, "Hallo World", "true", date2);
        chatRoomManager.addChatItem(message1);
        chatRoomManager.addChatItem(message0);
        chatRoomManager.sort();
        assertEquals("Hallo World", chatRoomManager.getMessageList().get(0).getMessage());
    }

    @Test
    public void getLatestMessage() {
        Friend friend = new Friend("Jimmy");
        ChatRoomManager chatRoomManager = new ChatRoomManager(friend);
        Date date1 = new Date();
        date1.setYear(2010);
        Date date2 = new Date();
        date2.setYear(2011);
        Date date3 = new Date();
        date3.setYear(2012);
        Message message0 = new Message("saya", friend.getUsername(), MessageType.OUT, "first", "true",date1 );
        Message message1 = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "second", "true",date2 );
        Message message2 = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "third", "true",date3 );
        chatRoomManager.addChatItem(message0);
        chatRoomManager.addChatItem(message2);
        chatRoomManager.addChatItem(message1);
        assertEquals("third", chatRoomManager.getLatestMessage().getMessage());
    }
}