package de.unidue.palaver;

import org.junit.Test;

import java.util.Date;

import de.unidue.palaver.system.MessageViewModel;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.resource.MessageType;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.ChatsManager;

import static org.junit.Assert.*;

public class MessageViewModelTest {

    @Test
    public void getChat() {
        ChatsManager chatsManager = new ChatsManager();
        chatsManager.addChat(new MessageViewModel(new Friend("wahid")));
        chatsManager.addChat(new MessageViewModel(new Friend("jimmy")));
        chatsManager.addChat(new MessageViewModel(new Friend("wahid")));
        chatsManager.addChat(new MessageViewModel(new Friend("nico")));
        assertEquals(3, chatsManager.getChatList().size());
    }

    @Test
    public void removeChat() {
        ChatsManager chatsManager = new ChatsManager();
        Friend friend0 = new Friend("Teman");
        Message message0 = new Message(friend0.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "true", new Date());
        MessageViewModel messageViewModel0 = new MessageViewModel(friend0);
        messageViewModel0.addChatItem(message0);
        chatsManager.addChat(messageViewModel0);
        chatsManager.removeChat(messageViewModel0);
        assertNull(chatsManager.getChat(friend0));
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
        ChatsManager chatsManager = new ChatsManager();
        chatsManager.addChat(new MessageViewModel(new Friend("wahid")));
        chatsManager.addChat(new MessageViewModel(new Friend("jimmy")));
        assertEquals(2, chatsManager.getChatList().size());
    }
}