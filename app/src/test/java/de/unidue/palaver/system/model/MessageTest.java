package de.unidue.palaver.system.model;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        assertSame(message.getMessageTypeEnum(), MessageType.INCOMMING);
    }

    @Test
    public void getMessage() {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "true", new Date());
        assertEquals("Hallo World", message.getMessage());
    }

    @Test
    public void getIsReadStatus() throws ParseException {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "false", new Date());
        assertFalse(message.getIsReadStatus());
        message.setIsReadStatus(true);
        assertTrue(message.getIsReadStatus());
        System.out.println(message.getDateDate().toString());
        System.out.println(message.getDateToString());
    }

    @Test
    public void getDate() throws ParseException {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "true", "2016-02-12 17:02:38.663");
        assertNotNull(message.getDateDate());
        System.out.println(message.getDateDate().toString());
        System.out.println(message.getDateToString());
    }

    @Test
    public void playWithDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

//Here you say to java the initial timezone. This is the secret
        sdf.setTimeZone(TimeZone.getDefault());
//Will print in UTC
        System.out.println(sdf.format(calendar.getTime()));

//Here you set to your timezone
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5"));
//Will print on your default Timezone
        System.out.println(sdf.format(calendar.getTime()));
    }
}