package de.unidue.palaver.system.model;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.unidue.palaver.system.engine.Parser;
import de.unidue.palaver.system.values.MessageType;

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
        System.out.println(message.getDate());
    }

    @Test
    public void getDate() throws ParseException {
        Friend friend = new Friend("Test");
        String serverTime = "2016-02-12T17:01:44.6224075+02:00";
        Parser parser = new Parser();
        Message message = new Message(friend.getUsername(), "saya", MessageType.INCOMMING, "Hallo World", "true", parser.serverDateTimeStringToPalaverDateString(serverTime));
        assertNotNull(message.getDateDate());
        System.out.println(message.getDateDate().toString());
        System.out.println(message.getDate());
    }

    @Test
    public void playWithDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

//Here you say to java the initial timezone. This is the secret
        //sdf.setTimeZone(TimeZone.getDefault());
//Will print in UTC
        System.out.println(sdf.format(calendar.getTime()));

//Here you set to your timezone
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
//Will print on your default Timezone
        System.out.println(sdf.format(calendar.getTime()));
    }

    @Test
    public void newDateParser() throws ParseException {
        String serverDateTime = "2016-02-12T17:01:44.6224075+03:00";

        //ToString
        Calendar calendar = Calendar.getInstance();
        String[] DateTimeAndTimeZone = serverDateTime.split("\\+");
        String dateTime = DateTimeAndTimeZone[0];
        String realDateTime = dateTime.split("\\.")[0];
        String timeZone = DateTimeAndTimeZone[1];
        String[] timezoneConvert = timeZone.split(":");
        String realTimeZone = timezoneConvert[0]+"00";
        String serVerDateTimeInSimpleDateFormat = realDateTime+'+'+realTimeZone;
        System.out.println(serVerDateTimeInSimpleDateFormat);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        calendar.setTime(sdf.parse(serVerDateTimeInSimpleDateFormat));

        System.out.println(sdf.format(sdf.parse(serVerDateTimeInSimpleDateFormat)));

        Date date = new Date();
        System.out.println(calendar.getTime());

    }


}