package de.unidue.palaver.system.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import de.unidue.palaver.system.engine.Parser;
import de.unidue.palaver.system.roomdatabase.DBContract;
import de.unidue.palaver.system.roomdatabase.DBContract.TableMessage;
import de.unidue.palaver.system.resource.MessageType;


@Entity(tableName = TableMessage.TABLE_MESSAGE_NAME, primaryKeys = { TableMessage.COLUMN_FKFRIEND,
        TableMessage.COLUMN_CHAT_SENDER,
        TableMessage.COLUMN_CHAT_DATA,
        TableMessage.COLUMN_CHAT_DATETIME})
public class Message implements Comparable<Message>, Serializable {

    @NonNull
    @ColumnInfo(name = TableMessage.COLUMN_FKFRIEND)
    private String friendName;

    @NonNull
    @ColumnInfo(name = TableMessage.COLUMN_CHAT_SENDER)
    private String sender;

    @ColumnInfo(name = TableMessage.COLUMN_CHAT_RECIPIENT)
    private String recipient;

    @NonNull
    @ColumnInfo(name = TableMessage.COLUMN_CHAT_DATA)
    private String message;

    @ColumnInfo(name = TableMessage.COLUMN_CHAT_MIMETYPE)
    private String mimeType;

    @ColumnInfo(name = DBContract.TableMessage.COLUMN_MESSAGE_TYPE)
    private String messageType;

    @ColumnInfo(name = TableMessage.COLUMN_CHAT_DATA_ISREAD)
    private boolean isRead;

    @NonNull
    @ColumnInfo(name = TableMessage.COLUMN_CHAT_DATETIME)
    private String date;

    public Message() { }

    public Message(String sender, String recipient, MessageType messageType, String message, String isReadStatus, String date) {
        if(messageType == MessageType.INCOMMING){
            friendName = sender;
        } else {
            friendName = recipient;
        }
        this.sender = sender;
        this.recipient = recipient;
        if(messageType ==MessageType.OUT){
            this.messageType = "out";
        } else{
            this.messageType = "in";
        }
        this.mimeType = "text/plain";
        this.message = message;
        this.isRead = Boolean.parseBoolean(isReadStatus);
        this.date = date;
    }

    public Message(String sender, String recipient, MessageType messageType, String message, String isReadStatus, Date date) {
        if(messageType == MessageType.INCOMMING){
            friendName = sender;
        } else {
            friendName = recipient;
        }
        this.sender = sender;
        this.recipient = recipient;
        if(messageType ==MessageType.OUT){
            this.messageType = "out";
        } else{
            this.messageType = "in";
        }
        this.mimeType= "text/plain";
        this.message = message;
        this.isRead = Boolean.parseBoolean(isReadStatus);
        this.date = new Parser().dateToString(date);
    }

    @NonNull
    public String getSender() {
        return sender;
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    @NonNull
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient){
        this.recipient = recipient;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public MessageType getMessageTypeEnum() {
        if (this.messageType.equals("in")){
            return MessageType.INCOMMING;
        }
        return MessageType.OUT;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsReadStatus() {
        return isRead;
    }

    public void setIsReadStatus(boolean read) {
        isRead = read;
    }

    @NonNull
    public Date getDateDate() throws ParseException {
        Parser parser = new Parser();
        return parser.stringToDateFromDataBase(date);
    }

    public String getDate() {
        return date;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getDateToString(){
        return date;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Message o) {
        try {
            return new Parser().stringToDateFromDataBase(date).compareTo(o.getDateDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  0;
    }
}
