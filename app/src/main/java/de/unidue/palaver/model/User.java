package de.unidue.palaver.model;

public class User {

    private UserData userData;

    public User(UserData userData) {
        this.userData = userData;
    }

    public UserData getUserData() {
        return userData;
    }

}
