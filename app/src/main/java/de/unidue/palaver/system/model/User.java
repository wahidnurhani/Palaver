package de.unidue.palaver.system.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("Username")
    @Expose
    private String username;

    @SerializedName("Password")
    @Expose
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static class AndFriend {
        @SerializedName("Username")
        @Expose
        private String username;

        @SerializedName("Password")
        @Expose
        private String password;

        @SerializedName("Friend")
        @Expose
        private String friendUserName;

        public AndFriend(String username, String password, String friendUsername) {
            this.username = username;
            this.password = password;
            this.friendUserName = friendUsername;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFriendUserName() {
            return friendUserName;
        }

        public void setFriendUserName(String friendUserName) {
            this.friendUserName = friendUserName;
        }
    }
}
