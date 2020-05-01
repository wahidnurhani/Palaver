package de.unidue.palaver.model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ChatTest {

    @Test
    public void getFriend() {
        Friend friend = new Friend("Jimmy");
        Chat chat = new Chat(friend);
        assertEquals("Jimmy", chat.getFriend().getUsername());
    }

    @Test
    public void getChatElements() {
        Friend friend = new Friend("Jimmy");
        ChatItem chatItem = new ChatItem("saya", friend.getUsername(), ChatItemType.INCOMMING, "Hallo World", new Date());
        Chat chat = new Chat(friend);
        chat.addChatItem(chatItem);
        assertNotNull(chat.getChatItemList());
        assertSame(chat.getChatItemList().get(0).getChatItemType(), ChatItemType.INCOMMING);
        assertEquals("Hallo World", chat.getChatItemList().get(0).getMessage());
    }

    @Test
    public void isUnReadMessageExist() {
        Friend friend = new Friend("Jimmy");
        Chat chat = new Chat(friend);
        Date date1 = new Date();
        date1.setYear(2010);
        Date date2 = new Date();
        date2.setYear(2011);
        Date date3 = new Date();
        date3.setYear(2012);
        ChatItem chatItem0 = new ChatItem("saya", friend.getUsername(), ChatItemType.INCOMMING, "Hallo World", date1 );
        chatItem0.setIsReadStatus(true);
        ChatItem chatItem1 = new ChatItem("saya", friend.getUsername(), ChatItemType.INCOMMING, "Hallo World", date2 );
        chatItem1.setIsReadStatus(true);
        ChatItem chatItem2 = new ChatItem("saya", friend.getUsername(), ChatItemType.INCOMMING, "Hallo World", date3 );
        chatItem2.setIsReadStatus(false);
        chat.addChatItem(chatItem0);
        chat.addChatItem(chatItem2);
        chat.addChatItem(chatItem1);
        assertTrue(chat.isUnReadMessageExist());
    }

    @Test
    public void sort() {
        Friend friend = new Friend("Jimmy");
        Chat chat = new Chat(friend);
        Date date1 = new Date();
        date1.setYear(2010);
        Date date2 = new Date();
        date2.setYear(2011);
        ChatItem chatItem0 = new ChatItem("saya", friend.getUsername(), ChatItemType.INCOMMING, "Hallo World", date1 );
        ChatItem chatItem1 = new ChatItem("saya", friend.getUsername(), ChatItemType.INCOMMING, "Hallo World", date2);
        chat.addChatItem(chatItem1);
        chat.addChatItem(chatItem0);
        chat.sort();
        assertEquals("Hallo World", chat.getChatItemList().get(0).getMessage());
    }

    @Test
    public void getLatestMessage() {
        Friend friend = new Friend("Jimmy");
        Chat chat = new Chat(friend);
        Date date1 = new Date();
        date1.setYear(2010);
        Date date2 = new Date();
        date2.setYear(2011);
        Date date3 = new Date();
        date3.setYear(2012);
        ChatItem chatItem0 = new ChatItem("saya", friend.getUsername(), ChatItemType.OUT, "first",date1 );
        ChatItem chatItem1 = new ChatItem(friend.getUsername(), "saya", ChatItemType.INCOMMING, "second",date2 );
        ChatItem chatItem2 = new ChatItem(friend.getUsername(), "saya", ChatItemType.INCOMMING, "third",date3 );
        chat.addChatItem(chatItem0);
        chat.addChatItem(chatItem2);
        chat.addChatItem(chatItem1);
        assertEquals("third", chat.getLatestMessage().getMessage());
    }
}