package de.unidue.palaver.system.model;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void getFriend() {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", "Hallo World", new Date());
        assertEquals("Test", message.getSender());
    }


    @Test
    public void getMessage() {
        Friend friend = new Friend("Test");
        Message message = new Message(friend.getUsername(), "saya", "Hallo World", new Date());
        assertEquals("Hallo World", message.getMessage());
    }


    @Test
    public void getDate() {
        Friend friend = new Friend("Test");
        String serverTime = "2016-02-12T17:01:44.6224075+02:00";
        Message message = new Message(friend.getUsername(), "saya", "Hallo World",  serverTime);
        assertNotNull(message.getDate());
        System.out.println(message.getDate());
        System.out.println(message.getDate());
    }

    @Test
    public void playWithDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        System.out.println(sdf.format(calendar.getTime()));

        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));

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

        System.out.println(calendar.getTime());

    }


}