package de.unidue.palaver.engine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.unidue.palaver.model.Friend;

public class Parser {

    public Parser() {
    }

    public String[] validateAndRegisterReportParser(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt("MsgType");
            String info = jsonObject.getString("Info");
            String data;
            if(jsonObject.get("Data") instanceof String){
                data = jsonObject.getString("Data");
            } else {
                data = null;
            }



            return new String[]{msgType+"",info, data};
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Friend[] getFriendParser(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt("MsgType");
            String info = jsonObject.getString("Info");
            JSONArray contactJSONArray = jsonObject.getJSONArray("Data");
            Friend[] friends = new Friend[contactJSONArray.length()];

            for (int i=0; i<contactJSONArray.length();i++){
                String username = contactJSONArray.getString(i);
                friends[i]= new Friend(username);
            }
            return friends;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] fetchFriendFeedback(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt("MsgType");
            String info = jsonObject.getString("Info");
            String data;
            if(jsonObject.get("Data") instanceof String){
                data = jsonObject.getString("Data");
            } else {
                data = null;
            }

            return new String[]{msgType+"",info, data};
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] addContactReportParser(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt("MsgType");
            String info = jsonObject.getString("Info");
            String data;
            if(jsonObject.get("Data") instanceof String){
                data = jsonObject.getString("Data");
            } else {
                data = null;
            }

            return new String[]{msgType+"",info, data};
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
