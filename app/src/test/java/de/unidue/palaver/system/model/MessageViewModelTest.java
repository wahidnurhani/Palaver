package de.unidue.palaver.system.model;

import org.junit.Test;

import java.util.Date;

import de.unidue.palaver.system.MessageViewModel;
import de.unidue.palaver.system.resource.MessageType;

import static org.junit.Assert.*;

public class MessageViewModelTest {

    @Test
    public void getFriend() {
        Friend friend = new Friend("Jimmy");
        MessageViewModel messageViewModel = new MessageViewModel(friend);
        assertEquals("Jimmy", messageViewModel.getFriend().getUsername());
    }

    @Test
    public void getChatElements() {
        Friend friend = new Friend("Jimmy");
        Message message = new Message("saya", friend.getUsername(), MessageType.INCOMMING, "Hallo World", "true", new Date());
        MessageViewModel messageViewModel = new MessageViewModel(friend);
        messageViewModel.addChatItem(message);
        assertNotNull(messageViewModel.getMessageList());
        assertSame(messageViewModel.getMessageList().get(0).getMessageType(), MessageType.INCOMMING);
        assertEquals("Hallo World", messageViewModel.getMessageList().get(0).getMessage());
    }

    @Test
    public void isUnReadMessageExist() {
        Friend friend = new Friend("Jimmy");
        MessageViewModel messageViewModel = new MessageViewModel(friend);
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
        messageViewModel.addChatItem(message0);
        messageViewModel.addChatItem(message2);
        messageViewModel.addChatItem(message1);
        assertTrue(messageViewModel.isUnReadMessageExist());
    }

    @Test
    public void sort() {
        Friend friend = new Friend("Jimmy");
        MessageViewModel messageViewModel = new MessageViewModel(friend);
        Date date1 = new Date();
        date1.setYear(2010);
        Date date2 = new Date();
        date2.setYear(2011);
        Message message0 = new Message("saya", friend.getUsername(), MessageType.INCOMMING, "Hallo World", "true", date1 );
        Message message1 = new Message("saya", friend.getUsername(), MessageType.INCOMMING, "Hallo World", "true", date2);
        messageViewModel.addChatItem(message1);
        messageViewModel.addChatItem(message0);
        messageViewModel.sort();
        assertEquals("Hallo World", messageViewModel.getMessageList().get(0).getMessage());
    }

    @Test
    public void getLatestMessage() {
        Friend friend = new Friend("Jimmy");
        MessageViewModel messageViewModel = new MessageViewModel(friend);
        Date date1 = new Date();
        date1.setYear(2010);
        Date date2 = new Date();
        date2.setYear(2011);
        Date date3 = new Date();
        date3.setYear(2012);
        Message message0 = new Message("saya", friend.getUsername(), MessageType.OUT, "first", "true",date1 );
        Message message1 = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "second", "true",date2 );
        Message message2 = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "third", "true",date3 );
        messageViewModel.addChatItem(message0);
        messageViewModel.addChatItem(message2);
        messageViewModel.addChatItem(message1);
        assertEquals("third", messageViewModel.getLatestMessage().getMessage());
    }
}