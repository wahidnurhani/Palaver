package de.unidue.palaver.system.model;

import java.io.Serializable;

public class Friend implements Comparable<Friend>, Serializable {

    private String username;

    public Friend(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int compareTo(Friend o) {
        return username.compareTo(o.getUsername());
    }
}
