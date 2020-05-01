package de.unidue.palaver.system.model;

public class Friend implements Comparable<Friend>{

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
