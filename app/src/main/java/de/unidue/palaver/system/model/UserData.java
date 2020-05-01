package de.unidue.palaver.system.model;

public class UserData {

    private String userName;
    private String password;

    public UserData(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
