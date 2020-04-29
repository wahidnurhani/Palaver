package de.unidue.palaver.engine;

import org.junit.Test;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.model.User;
import de.unidue.palaver.model.UserData;

import static org.junit.Assert.*;

public class CommunicatorTest {

    @Test
    public void validate() {
        Palaver palaver = new Palaver();
        PalaverEngine palaverEngine = palaver.getPalaverEngine();
        User user = new User(new UserData("test1993", "test1993"));
        String[] result = palaverEngine.getCommunicator().registerAndValidate(user, "/api/user/validate");
        assertTrue(3 == result.length);
        assertTrue(result[0].equals("0") || result[0].equals("1"));
        assertTrue(!result[1].equals(""));
        System.out.println(result[1]);
    }

    @Test
    public void register() {
        Palaver palaver = new Palaver();
        PalaverEngine palaverEngine = palaver.getPalaverEngine();
        User user = new User(new UserData("test1993", "test1993"));
        String[] result = palaverEngine.getCommunicator().registerAndValidate(user, "/api/user/register");
        assertTrue(3 == result.length);
        assertTrue(result[0].equals("0") || result[0].equals("1"));
        assertTrue(!result[1].equals(""));
        System.out.println(result[1]);
    }

    @Test
    public void fetchAllContact() {
        Palaver palaver = new Palaver();
        PalaverEngine palaverEngine = palaver.getPalaverEngine();
        User user = new User(new UserData("test1993", "test1993"));
        String[] result = palaverEngine.getCommunicator().registerAndValidate(user, "/api/friends/get");
        assertTrue(3 == result.length);
        assertTrue(result[0].equals("0") || result[0].equals("1"));
        assertTrue(!result[1].equals(""));
        System.out.println(result[1]);
    }

    @Test
    public void addFriend(){
        Palaver palaver = new Palaver();
        PalaverEngine palaverEngine = palaver.getPalaverEngine();
        User user = new User(new UserData("test1993", "test1993"));
        String[] result = palaverEngine.getCommunicator().addContact(user, "test1991");
        assertTrue(3 == result.length);
        assertTrue(result[0].equals("0") || result[0].equals("1"));
        assertTrue(!result[1].equals(""));
        System.out.println(result[1]);
    }
}