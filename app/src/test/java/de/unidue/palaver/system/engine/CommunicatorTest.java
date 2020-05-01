package de.unidue.palaver.system.engine;

import org.junit.Test;

import de.unidue.palaver.system.model.CommunicatorResult;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.model.UserData;

import static org.junit.Assert.*;

public class CommunicatorTest {

    @Test
    public void validate() {
        Communicator communicator = new Communicator();
        User user = new User(new UserData("test1993", "test1993"));
        String[] result = communicator.registerAndValidate(user, "/api/user/validate");
        assertEquals(3, result.length);
        assertTrue(result[0].equals("0") || result[0].equals("1"));
        assertNotEquals("", result[1]);
        System.out.println(result[1]);
    }

    @Test
    public void register() {
        Communicator communicator = new Communicator();
        User user = new User(new UserData("test1994", "test1994"));
        String[] result = communicator.registerAndValidate(user, "/api/user/register");
        assertEquals(3, result.length);
        assertTrue(result[0].equals("0") || result[0].equals("1"));
        assertNotEquals("", result[1]);
        System.out.println(result[1]);
    }

    @Test
    public void fetchAllFriend() {
        Communicator communicator = new Communicator();
        User user = new User(new UserData("test1991", "test1991"));
        CommunicatorResult<Friend> communicatorResult = communicator.fetchFriends(user);
        assertEquals( 1 , communicatorResult.getResponseValue());
        assertNotEquals("", communicatorResult.getMessage());
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void addFriend(){
        Communicator communicator = new Communicator();
        User user = new User(new UserData("test1993", "test1993"));
        CommunicatorResult<Friend> communicatorResult = communicator.addContact(user, "test1991");
        assertEquals( 0 , communicatorResult.getResponseValue());
        assertNull(communicatorResult.getData());
        System.out.println(communicatorResult);
    }
}