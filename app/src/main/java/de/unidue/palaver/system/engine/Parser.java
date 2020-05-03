package de.unidue.palaver.system.engine;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.CommunicatorResult;
import de.unidue.palaver.system.resource.MessageType;
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

    CommunicatorResult<Friend> getFriendParser(String result) {
        CommunicatorResult<Friend> communicatorResult;

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
        return null;
    }

    CommunicatorResult<Friend> addAndRemoveFriendReportParser(String result) {

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

    Date stringToDateFromServer(String date) throws ParseException {

        String[] dateTime = date.split("\\.");
        String validDateTime = dateTime[0];

        String[] dateTime1 = validDateTime.split("T");
        String result = dateTime1[0]+" "+dateTime1[1];
        System.out.println(result);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.GERMANY);
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

        return formatter.parse(result);
    }

    public Date stringToDateFromDataBase(String date) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.GERMANY);
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));

        return formatter.parse(date);
    }

    public String dateToString(Date date){
        String pattern = "yyyy-MM-dd hh:mm:ss";
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    CommunicatorResult<String> changePasswordResultParser(String result, String newPassWord) {
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

    public CommunicatorResult<String> pushTokenParser(String result) {
        CommunicatorResult<String> communicatorResult=null;
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

    CommunicatorResult<Date> sendMessageReport(String result) {
        CommunicatorResult<Date> communicatorResult = null;

        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt(StringValue.JSONKeyName.MSG_TYPE);
            String info = jsonObject.getString(StringValue.JSONKeyName.INFO);
            JSONObject data = jsonObject.getJSONObject(StringValue.JSONKeyName.DATA);
            String dateTime = data.getString(StringValue.JSONKeyName.DATE_TIME);
            Date date = stringToDateFromServer(dateTime.split("\\.")[0]);

            List<Date> dateList = new ArrayList<>();
            dateList.add(date);
            communicatorResult = new CommunicatorResult<>(msgType, info, dateList);

            return communicatorResult;
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return communicatorResult;
    }

    CommunicatorResult<Message> getChatDataParser(String result, String isMessageRead, String friendUserName) throws JSONException, ParseException {
        CommunicatorResult<Message> communicatorResult;
        JSONObject jsonObject = new JSONObject(result);
        int msgType = jsonObject.getInt(StringValue.JSONKeyName.MSG_TYPE);
        String info = jsonObject.getString(StringValue.JSONKeyName.INFO);
        JSONArray jsonArray = jsonObject.getJSONArray(StringValue.JSONKeyName.DATA);
        List<Message> messageList = new ArrayList<>();

        if(msgType==1){
            for (int i =0 ; i<jsonArray.length();i++){
                JSONObject oneChat = jsonArray.getJSONObject(i);
                String sender = oneChat.getString(StringValue.JSONKeyName.SENDER);
                String recipient = oneChat.getString(StringValue.JSONKeyName.RECIPIENT);
                String data = oneChat.getString(StringValue.JSONKeyName.DATA);
                String mimeType = oneChat.getString((StringValue.JSONKeyName.MIME_TYPE));
                String dateTime = oneChat.getString(StringValue.JSONKeyName.DATE_TIME);
                String[] dateTimeValid= dateTime.split("\\.");
                MessageType messageType;
                if (sender.equals(friendUserName)){
                    messageType = MessageType.INCOMMING;
                } else {
                    messageType = MessageType.OUT;
                }
                messageList.add(new Message(sender, recipient, messageType, data, isMessageRead, stringToDateFromServer(dateTimeValid[0])));
            }
            communicatorResult = new CommunicatorResult<>(msgType, info, messageList);
        } else {
            communicatorResult = new CommunicatorResult<>(msgType, info, null);
        }
        return communicatorResult;
    }
}
