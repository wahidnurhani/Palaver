package de.unidue.palaver;

import org.junit.Test;

import java.util.Date;

import de.unidue.palaver.model.Chat;
import de.unidue.palaver.model.ChatItem;
import de.unidue.palaver.model.ChatItemType;
import de.unidue.palaver.model.Friend;
import static org.junit.Assert.*;

public class ChatManagerTest {

    @Test
    public void getChat() {
        Palaver palaver = new Palaver();
        ChatManager chatManager = new ChatManager(palaver);
        Friend friend0 = new Friend("Satu");
        ChatItem chatItem0 = new ChatItem(friend0, ChatItemType.INCOMMING, "Hallo World", new Date());
        Chat chat0 = new Chat(friend0);
        chat0.addChatItem(chatItem0);
        chatManager.addChat(chat0);

        assertTrue(chatManager.getChat(friend0)==chat0);

    }

    @Test
    public void removeChat() {
        Palaver palaver = new Palaver();
        ChatManager chatManager = new ChatManager(  palaver);
        Friend friend0 = new Friend("Satu");
        ChatItem chatItem0 = new ChatItem(friend0, ChatItemType.INCOMMING, "Hallo World", new Date());
        Chat chat0 = new Chat(friend0);
        chat0.addChatItem(chatItem0);
        chatManager.addChat(chat0);
        chatManager.removeChat(chat0);

        assertTrue(chatManager.getChat(friend0)==null);
    }

    @Test
    public void sort() {
    }

    @Test
    public void search() {
    }
}