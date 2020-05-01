package de.unidue.palaver.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class FriendTest {

    @Test
    public void getUsername() {
        Friend friend = new Friend("friend");
        assertEquals("friend", friend.getUsername());
    }

    @Test
    public void compareTo() {
        Friend friend = new Friend("a");
        Friend friend1 = new Friend("b");

        assertEquals(-1,friend.compareTo(friend1));
    }
}