package de.unidue.palaver.system.engine;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.resource.MessageType;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.model.Friend;

public class Parser {

    public Parser() {
    }

    public String[] validateAndRegisterReportParser(String result){
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

    public CommunicatorResult<Friend> addAndRemoveFriendReportParser(String result) {

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

    public String serverDateTimeStringToPalaverDateString(String serverDateTime){
        String[] DateTimeAndTimeZone = serverDateTime.split("\\+");
        String dateTime = DateTimeAndTimeZone[0];
        String realDateTime = dateTime.split("\\.")[0];
        String timeZone = DateTimeAndTimeZone[1];
        String[] timezoneConvert = timeZone.split(":");
        String realTimeZone = timezoneConvert[0]+"00";
        return realDateTime+'+'+realTimeZone;
    }


    public Date stringToDate(String dateString) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        calendar.setTime(sdf.parse(dateString));
        return calendar.getTime();
    }

    public String dateToString(Date date){
        String pattern = "yyyy-MM-dd'T'hh:mm:ssZ";
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
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

    public CommunicatorResult<Date> sendMessageReport(String result) {
        CommunicatorResult<Date> communicatorResult = null;

        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt(StringValue.JSONKeyName.MSG_TYPE);
            String info = jsonObject.getString(StringValue.JSONKeyName.INFO);
            JSONObject data = jsonObject.getJSONObject(StringValue.JSONKeyName.DATA);
            String dateTime = data.getString(StringValue.JSONKeyName.DATE_TIME);
            String dateTimeAppFormat = serverDateTimeStringToPalaverDateString(dateTime);
            Date date = stringToDate(dateTimeAppFormat);

            List<Date> dateList = new ArrayList<>();
            dateList.add(date);
            communicatorResult = new CommunicatorResult<>(msgType, info, dateList);

            return communicatorResult;
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return communicatorResult;
    }

    public CommunicatorResult<Message> getChatDataParser(String result, String isMessageRead, String friendUserName) throws JSONException, ParseException {
        String serverTimeZone = "+0100";
        CommunicatorResult<Message> communicatorResult;
        JSONObject jsonObject = new JSONObject(result);
        int msgType = jsonObject.getInt(StringValue.JSONKeyName.MSG_TYPE);
        String info = jsonObject.getString(StringValue.JSONKeyName.INFO);
        JSONArray jsonArray = jsonObject.getJSONArray(StringValue.JSONKeyName.DATA);
        System.out.println(jsonArray.toString());
        List<Message> messageList = new ArrayList<>();

        if(msgType==1){
            for (int i =0 ; i<jsonArray.length();i++){
                JSONObject oneChat = jsonArray.getJSONObject(i);
                String sender = oneChat.getString(StringValue.JSONKeyName.SENDER);
                String recipient = oneChat.getString(StringValue.JSONKeyName.RECIPIENT);
                String data = oneChat.getString(StringValue.JSONKeyName.DATA);
                String mimeType = oneChat.getString((StringValue.JSONKeyName.MIME_TYPE));
                String dateTime = oneChat.getString(StringValue.JSONKeyName.DATE_TIME)+serverTimeZone;
                MessageType messageType;
                if (sender.equals(friendUserName)){
                    messageType = MessageType.INCOMMING;
                } else {
                    messageType = MessageType.OUT;
                }
                messageList.add(new Message(sender, recipient, messageType, data, isMessageRead, serverDateTimeStringToPalaverDateString(dateTime)));
            }
            for (Message message: messageList){
                System.out.println(message.getDate());
            }
            communicatorResult = new CommunicatorResult<>(msgType, info, messageList);
        } else {
            communicatorResult = new CommunicatorResult<>(msgType, info, null);
        }
        return communicatorResult;
    }
}
