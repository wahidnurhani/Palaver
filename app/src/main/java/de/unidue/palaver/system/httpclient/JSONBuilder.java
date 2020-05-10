package de.unidue.palaver.system.httpclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;

public class JSONBuilder {

    public static class UserAndRecipient {
        @SerializedName("Username")
        @Expose
        private String username;

        @SerializedName("Password")
        @Expose
        private String password;

        @SerializedName("Recipient")
        @Expose
        private String friendUserName;

        public UserAndRecipient(User user, Friend friend) {
            this.username = user.getUserName();
            this.password = user.getPassword();
            this.friendUserName = friend.getUsername();
        }
    }

    public static class SendMessageBody {

        @SerializedName("Username")
        private String username;

        @SerializedName("Password")
        private String password;

        @SerializedName("Recipient")
        private String recipient;

        @SerializedName("Mimetype")
        private String mimeType;

        @SerializedName("Data")
        private String data;

        public SendMessageBody(User user , Friend friend, Message message) {
            this.username = user.getUserName();
            this.password = user.getPassword();
            this.recipient = friend.getUsername();
            this.mimeType = message.getMimeType();
            this.data = message.getMessage();

        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class UserAndFriend {
        @SerializedName("Username")
        @Expose
        private String username;

        @SerializedName("Password")
        @Expose
        private String password;

        @SerializedName("Friend")
        @Expose
        private String friendUserName;

        public UserAndFriend(User user, Friend friend) {
            this.username = user.getUserName();
            this.password = user.getPassword();
            this.friendUserName = friend.getUsername();
        }
    }
}
