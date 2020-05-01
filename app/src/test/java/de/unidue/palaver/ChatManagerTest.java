package de.unidue.palaver;

import org.junit.Test;

import java.util.Date;

import de.unidue.palaver.system.model.Chat;
import de.unidue.palaver.system.model.ChatItem;
import de.unidue.palaver.system.resource.ChatItemType;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.ChatManager;
import de.unidue.palaver.system.Palaver;

import static org.junit.Assert.*;

public class ChatManagerTest {

    @Test
    public void getChat() {
        ChatManager chatManager = new ChatManager();
        chatManager.addChat(new Chat(new Friend("wahid")));
        chatManager.addChat(new Chat(new Friend("jimmy")));
        chatManager.addChat(new Chat(new Friend("wahid")));
        chatManager.addChat(new Chat(new Friend("nico")));
        assertEquals(3, chatManager.getChatList().size());
    }

    @Test
    public void removeChat() {
        ChatManager chatManager = new ChatManager();
        Friend friend0 = new Friend("Teman");
        ChatItem chatItem0 = new ChatItem(friend0.getUsername(), "saya", ChatItemType.INCOMMING, "Hallo World", new Date());
        Chat chat0 = new Chat(friend0);
        chat0.addChatItem(chatItem0);
        chatManager.addChat(chat0);
        chatManager.removeChat(chat0);
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
        chatManager.addChat(new Chat(new Friend("wahid")));
        chatManager.addChat(new Chat(new Friend("jimmy")));
        assertEquals(2, chatManager.getChatList().size());
    }
}