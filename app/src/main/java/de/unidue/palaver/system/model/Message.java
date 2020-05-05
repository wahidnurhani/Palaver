package de.unidue.palaver.system.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import de.unidue.palaver.system.engine.communicator.Parser;
import de.unidue.palaver.system.resource.DBContract;
import de.unidue.palaver.system.resource.MessageType;


@Entity(tableName = DBContract.TableMessage.TABLE_MESSAGE_NAME)
public class Message implements Comparable<Message>, Serializable {

    private final String sender;

    private final String recipient;


    private final String message;

    private String mimeType;

    private final MessageType messageType;


    private boolean isRead;

    private Date date;


    public Message(String sender, String recipient, MessageType messageType, String message, String isReadStatus, String date) throws ParseException {
        this.sender = sender;
        this.recipient = recipient;
        this.messageType = messageType;
        this.mimeType = "text/plain";
        this.message = message;
        this.isRead = Boolean.parseBoolean(isReadStatus);
        this.date = new Parser().stringToDateFromDataBase(date);
    }

    public Message(String sender, String recipient, MessageType messageType, String message, String isReadStatus, Date date) {
        this.sender = sender;
        this.recipient = recipient;
        this.messageType = messageType;
        this.mimeType="text/plain";
        this.message = message;
        this.isRead = Boolean.parseBoolean(isReadStatus);
        this.date = date;
    }

    @NonNull
    public String getSender() {
        return sender;
    }

    @NonNull
    public String getRecipient() {
        return recipient;
    }

    public String getMimeType() {
        return mimeType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public boolean getIsReadStatus() {
        return isRead;
    }

    public void setIsReadStatus(boolean read) {
        isRead = read;
    }

    @NonNull
    public Date getDate() {
        Parser parser = new Parser();
        return date;
    }

    public String getDateToString(){
        Parser parser = new Parser();
        return parser.dateToString(date);
    }

    @Override
    public int compareTo(Message o) {
        return this.date.compareTo(o.getDate());
    }
}
