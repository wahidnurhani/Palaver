package de.unidue.palaver.httpclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.User;

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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
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

    public class ChangePassWord {
        @SerializedName("Username")
        @Expose
        private String username;

        @SerializedName("Password")
        @Expose
        private String password;

        @SerializedName("NewPassword")
        @Expose
        private String newPassword;

        public ChangePassWord(User user, String newPassword) {
            this.username= user.getUserName();
            this.password= user.getPassword();
            this.newPassword = newPassword;

        }
    }

    public class PushToken {
        @SerializedName("Username")
        @Expose
        private String username;

        @SerializedName("Password")
        @Expose
        private String password;

        @SerializedName("PushToken")
        @Expose
        private String token;

        public PushToken(User user, String token) {
            this.username = user.getUserName();
            this.password = user.getPassword();
            this.token = token;
        }
    }

    public class GetMessageOffset {

        @SerializedName("Username")
        @Expose
        private String username;

        @SerializedName("Password")
        @Expose
        private String password;

        @SerializedName("Recipient")
        @Expose
        private String friendUserName;

        @SerializedName("Offset")
        @Expose
        private String offset;

        public GetMessageOffset(User user, Friend friend, String offset) {
            this.username = user.getUserName();
            this.password = user.getPassword();
            this.friendUserName = friend.getUsername();
            this.offset = offset;
        }
    }
}
