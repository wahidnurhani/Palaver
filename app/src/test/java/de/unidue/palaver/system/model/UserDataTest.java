package de.unidue.palaver.system.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserDataTest {

    @Test
    public void changePassword() {
        UserData userData = new UserData("username", "password");
        userData.setPassword("password2");
        assertEquals("password2", userData.getPassword());
    }
}