package de.unidue.palaver.engine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import de.unidue.palaver.model.Friend;

public class Parser {

    public Parser() {
    }

    String[] validateAndRegisterReportParser(String result){
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

    Friend[] getFriendParser(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
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

    String[] fetchFriendFeedback(String result) {
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

    String[] addContactReportParser(String result){
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

    public Date stringToDate(String date) {
        String[] dateTime = date.split("T");
        String datum = dateTime[0];
        String zeit = dateTime[1];

        String[] ymd= datum.split("-");
        String year= ymd[0];
        String month= ymd[1];
        String day= ymd[2];

        String[] hms = zeit.split(":");
        String hour = hms[0] ;
        String minute = hms[1];
        String second = hms[2];

        Date date1 = new Date();
        date1.setYear(Integer.parseInt(year));
        date1.setMonth(Integer.parseInt(month));
        date1.setDate(Integer.parseInt(day));
        date1.setHours(Integer.parseInt(hour));
        date1.setMinutes(Integer.parseInt(minute));
        date1.setSeconds(Integer.parseInt(String.valueOf(Math.round(Double.parseDouble(second)))));
        return date1;
    }
}
