package de.unidue.palaver.model;

import org.junit.Test;

import java.util.Date;

import de.unidue.palaver.model.Chat;
import de.unidue.palaver.model.ChatItem;
import de.unidue.palaver.model.ChatItemType;
import de.unidue.palaver.model.Friend;

import static org.junit.Assert.*;

public class ChatTest {

    @Test
    public void getFriend() {
        Friend friend = new Friend("Jimmy");
        Chat chat = new Chat(friend);
        assertTrue(chat.getFriend().getUsername().equals("Jimmy"));
    }

    @Test
    public void getChatElements() {
        Friend friend = new Friend("Jimmy");
        Chat chat = new Chat(friend);
        ChatItem chatItem = new ChatItem(friend, ChatItemType.OUT, "message out",new Date() );
        chat.addChatItem(chatItem);
        assertTrue(chat.getChatItemList()!=null);
        assertTrue(chat.getChatItemList().get(0).getChatItemType()== ChatItemType.OUT);
        assertTrue(chat.getChatItemList().get(0).getMessage().equals("message out"));
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
        ChatItem chatItem0 = new ChatItem(friend, ChatItemType.OUT, "first",date1 );
        chatItem0.setIsReadStatus(true);
        ChatItem chatItem1 = new ChatItem(friend, ChatItemType.INCOMMING, "second",date2 );
        chatItem1.setIsReadStatus(true);
        ChatItem chatItem2 = new ChatItem(friend, ChatItemType.INCOMMING, "third",date3 );
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
        ChatItem chatItem0 = new ChatItem(friend, ChatItemType.OUT, "message out",date1 );
        ChatItem chatItem1 = new ChatItem(friend, ChatItemType.INCOMMING, "message in",date2 );
        chat.addChatItem(chatItem1);
        chat.addChatItem(chatItem0);
        chat.sort();
        assertTrue(chat.getChatItemList().get(0).getMessage().equals("message out"));
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
        ChatItem chatItem0 = new ChatItem(friend, ChatItemType.OUT, "first",date1 );
        ChatItem chatItem1 = new ChatItem(friend, ChatItemType.INCOMMING, "second",date2 );
        ChatItem chatItem2 = new ChatItem(friend, ChatItemType.INCOMMING, "third",date3 );
        chat.addChatItem(chatItem0);
        chat.addChatItem(chatItem2);
        chat.addChatItem(chatItem1);
        assertTrue(chat.getLatestMessage().getMessage().equals("third"));
    }
}