package de.unidue.palaver.system.model;

public class User {

    private UserData userData;

    public User(UserData userData) {
        this.userData = userData;
    }

    public UserData getUserData() {
        return userData;
    }

}
