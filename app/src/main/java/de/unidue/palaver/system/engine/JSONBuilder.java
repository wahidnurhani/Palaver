package de.unidue.palaver.system.engine;

import org.json.JSONException;
import org.json.JSONObject;

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
}
