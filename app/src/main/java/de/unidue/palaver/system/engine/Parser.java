package de.unidue.palaver.system.engine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.unidue.palaver.system.model.CommunicatorResult;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.model.Friend;

public class Parser {

    public Parser() {
    }

    String[] validateAndRegisterReportParser(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt(StringValue.JSONKeyName.MSG_TYPE);
            String info = jsonObject.getString(StringValue.JSONKeyName.INFO);
            String data;
            if(jsonObject.get(StringValue.JSONKeyName.DATA) instanceof String){
                data = jsonObject.getString(StringValue.JSONKeyName.DATA);
            } else {
                data = null;
            }

            return new String[]{msgType+"",info, data};
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CommunicatorResult<Friend> getFriendParser(String result) {
        CommunicatorResult<Friend> communicatorResult=null;

        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt(StringValue.JSONKeyName.MSG_TYPE);
            String info = jsonObject.getString(StringValue.JSONKeyName.INFO);
            JSONArray contactJSONArray = jsonObject.getJSONArray(StringValue.JSONKeyName.DATA);

            List<Friend> friends = new ArrayList<>();
            for (int i=0; i<contactJSONArray.length();i++){
                friends.add(new Friend(contactJSONArray.getString(i)));
            }
            communicatorResult = new CommunicatorResult<>(msgType, info, friends);
            return communicatorResult;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return communicatorResult;
    }

    public CommunicatorResult<Friend> addContactReportParser(String result) {

        CommunicatorResult<Friend> communicatorResult=null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt(StringValue.JSONKeyName.MSG_TYPE);
            String info = jsonObject.getString(StringValue.JSONKeyName.INFO);

            communicatorResult = new CommunicatorResult<>(msgType, info, null);

            return communicatorResult;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return communicatorResult;
    }


    public Date stringToDate(String date) {
        String[] dateTime = date.split("T");
        String datum = dateTime[0];
        String zeit = dateTime[1];
        System.out.println(datum);

        String[] ymd= datum.split("-");
        String year= ymd[0];
        String month= ymd[1];
        String day= ymd[2];

        String[] hms = zeit.split(":");
        String hour = hms[0] ;
        String minute = hms[1];
        String second = hms[2];

        Date date1 = new Date();
        date1.setYear(Integer.parseInt(year)-1900);
        date1.setMonth(Integer.parseInt(month)-1);
        date1.setDate(Integer.parseInt(day));
        date1.setHours(Integer.parseInt(hour));
        date1.setMinutes(Integer.parseInt(minute));
        date1.setSeconds(Integer.parseInt(String.valueOf(Math.round(Double.parseDouble(second)))));
        return date1;
    }


    public CommunicatorResult<String> changePasswordResultParser(String result, String newPassWord) {
        CommunicatorResult<String> communicatorResult=null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt(StringValue.JSONKeyName.MSG_TYPE);
            String info = jsonObject.getString(StringValue.JSONKeyName.INFO);
            if(msgType==1){
                List<String> pass = new ArrayList<>();
                pass.add(newPassWord);
                communicatorResult = new CommunicatorResult<>(msgType, info, pass);
            } else {
                communicatorResult = new CommunicatorResult<>(msgType, info, null);
            }

            return communicatorResult;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return communicatorResult;
    }
}
