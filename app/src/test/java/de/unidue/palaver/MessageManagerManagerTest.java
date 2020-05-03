package de.unidue.palaver;

import org.junit.Test;

import java.util.Date;

import de.unidue.palaver.system.MessageManager;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.resource.MessageType;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.ChatManager;

import static org.junit.Assert.*;

public class MessageManagerManagerTest {

    @Test
    public void getChat() {
        ChatManager chatManager = new ChatManager();
        chatManager.addChat(new MessageManager(new Friend("wahid")));
        chatManager.addChat(new MessageManager(new Friend("jimmy")));
        chatManager.addChat(new MessageManager(new Friend("wahid")));
        chatManager.addChat(new MessageManager(new Friend("nico")));
        assertEquals(3, chatManager.getChatList().size());
    }

    @Test
    public void removeChat() {
        ChatManager chatManager = new ChatManager();
        Friend friend0 = new Friend("Teman");
        Message message0 = new Message(friend0.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "true", new Date());
        MessageManager messageManager0 = new MessageManager(friend0);
        messageManager0.addChatItem(message0);
        chatManager.addChat(messageManager0);
        chatManager.removeChat(messageManager0);
        assertNull(chatManager.getChat(friend0));
    }

    @Test
    public void sort() {
    }

    @Test
    public void search() {
    }

    @Test
    public void testGetChat() {
    }

    @Test
    public void addChat() {
        ChatManager chatManager = new ChatManager();
        chatManager.addChat(new MessageManager(new Friend("wahid")));
        chatManager.addChat(new MessageManager(new Friend("jimmy")));
        assertEquals(2, chatManager.getChatList().size());
    }
}