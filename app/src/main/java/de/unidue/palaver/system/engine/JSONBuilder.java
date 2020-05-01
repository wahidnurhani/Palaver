package de.unidue.palaver.system.engine;

import org.json.JSONException;
import org.json.JSONObject;

import de.unidue.palaver.system.resource.StringValue;

class JSONBuilder {
    JSONObject formatBodyUserDataToJSON(String username, String password) {
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

    JSONObject formatBodyAddOrRemoveFriendtToJSON(String username, String password, String friendsUsername){
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
}
