package de.unidue.palaver.system.engine.communicator;

import org.junit.Test;
import java.util.Date;

import de.unidue.palaver.system.engine.Communicator;
import de.unidue.palaver.system.engine.CommunicatorResult;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.values.MessageType;

import static org.junit.Assert.*;

public class CommunicatorTest {

    @Test
    public void validate() {
        Communicator communicator = new Communicator();
        User user = new User("test1991", "test1991");
        String[] result = communicator.registerAndValidate(user, "/api/user/validate");
        assertEquals(3, result.length);
        assertTrue(result[0].equals("0") || result[0].equals("1"));
        assertNotEquals("", result[1]);
        System.out.println(result[1]);
    }

    @Test
    public void register() {
        Communicator communicator = new Communicator();
        User user = new User("test1994", "test1994");
        String[] result = communicator.registerAndValidate(user, "/api/user/register");
        assertEquals(3, result.length);
        assertTrue(result[0].equals("0") || result[0].equals("1"));
        assertNotEquals("", result[1]);
        System.out.println(result[1]);
    }

    @Test
    public void fetchAllFriend() {
        Communicator communicator = new Communicator();
        User user = new User("test1991", "test1991");
        CommunicatorResult<Friend> communicatorResult = communicator.fetchFriends(user);
        assertEquals( 1 , communicatorResult.getResponseValue());
        assertNotEquals("", communicatorResult.getMessage());
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void addFriend(){
        Communicator communicator = new Communicator();
        User user = new User("test1991", "test1991");
        CommunicatorResult<Friend> communicatorResult = communicator.addFriend(user, "test1993");

        assertNull(communicatorResult.getData());
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void removeFriend(){
        Communicator communicator = new Communicator();
        User user = new User("test1991", "test1991");
        CommunicatorResult<Friend> communicatorResult = communicator.removeFriend(user, "test1993");

        assertNull(communicatorResult.getData());
        System.out.println(communicatorResult);
    }

    @Test
    public void changePassword(){
        Communicator communicator = new Communicator();

        //For not correct oldPassword
        User user = new User("test1993", "tokenTest");
        CommunicatorResult<String> communicatorResult = communicator.changePassword(user, "test1993");
        assertEquals(0, communicatorResult.getResponseValue());
        System.out.println(communicatorResult);

        //For Correct oldPassword
        User user1 = new User("test1991", "test1991");
        CommunicatorResult<String> communicatorResult2 = communicator.changePassword(user1, "test1991");

        System.out.println(communicatorResult2);
    }

    @Test
    public void pushToken(){
        Communicator communicator = new Communicator();
        User user = new User("test1991", "test1991");
        CommunicatorResult<String> communicatorResult = communicator.pushToken(user, "testToken");
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void sendMessage(){
        Communicator communicator = new Communicator();
        User user = new User("test1992", "test1992");
        Friend friend = new Friend("test1991");
        Message message = new Message(user.getUserName(), friend.getUsername()
                , MessageType.OUT, "test send ", "true", new Date());
        CommunicatorResult<Date> communicatorResult = communicator.sendMessage(user, friend, message);
        System.out.println(communicatorResult.toString());
    }

    @Test
    public void getMessage(){
        Communicator communicator = new Communicator();
        User user = new User("test1991", "test1991");
        Friend friend = new Friend("test1992");
        CommunicatorResult<Message> communicatorResult = communicator.getMessage(user, friend);
        System.out.println(communicatorResult.toString());
    }
}