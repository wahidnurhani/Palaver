package de.unidue.palaver.system.engine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.values.StringValue;

public class JSONBuilder {
    public JSONObject formatBodyUserDataToJSON(String username, String password) {
        JSONObject root = new JSONObject();
        try {
            root.put(StringValue.JSONKeyName.USERNAME,username);
            root.put(StringValue.JSONKeyName.PASSWORD,password);
            return root;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject formatBodyAddOrRemoveFriendtToJSON(String username, String password, String friendsUsername){
        final JSONObject root = new JSONObject();
        try {
            root.put(StringValue.JSONKeyName.USERNAME,username);
            root.put(StringValue.JSONKeyName.PASSWORD,password);
            root.put(StringValue.JSONKeyName.FRIEND, friendsUsername);
            return root;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject formatBodyChangePasswordDataToJSON(String username, String password, String newPassword) {
        final JSONObject root = new JSONObject();
        try {
            root.put(StringValue.JSONKeyName.USERNAME,username);
            root.put(StringValue.JSONKeyName.PASSWORD,password);
            root.put(StringValue.JSONKeyName.NEWPASSWORD, newPassword);
            return root;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject formatBodyPushTokenDataToJSON(String username, String password, String token) {
        final JSONObject root = new JSONObject();
        try {
            root.put(StringValue.JSONKeyName.USERNAME,username);
            root.put(StringValue.JSONKeyName.PASSWORD,password);
            root.put(StringValue.JSONKeyName.PUSH_TOKEN, token);
            return root;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject formatBodySendMessageToJSON(String username, String password, String recipient, String data) {
        final JSONObject root = new JSONObject();
        try {
            root.put(StringValue.JSONKeyName.USERNAME,username);
            root.put(StringValue.JSONKeyName.PASSWORD,password);
            root.put(StringValue.JSONKeyName.RECIPIENT, recipient);
            root.put(StringValue.JSONKeyName.MIME_TYPE, StringValue.JSONKeyName.MIME_TYPE_VALUE);
            root.put(StringValue.JSONKeyName.DATA, data);
            return root;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject formatBodyGetChatToJSON(String username, String password, String recipient) {
        final JSONObject root = new JSONObject();
        try {
            root.put(StringValue.JSONKeyName.USERNAME,username);
            root.put(StringValue.JSONKeyName.PASSWORD,password);
            root.put(StringValue.JSONKeyName.RECIPIENT, recipient);
            return root;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

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
}
