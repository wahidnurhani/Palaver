package de.unidue.palaver.engine;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONBuilder {

    public JSONObject formatBodyUserDataToJSON(String username, String password) {
        JSONObject root = new JSONObject();
        try {
            root.put("Username",username);
            root.put("Password",password);
            return root;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
