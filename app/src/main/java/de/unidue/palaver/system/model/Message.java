package de.unidue.palaver.system.model;


import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import de.unidue.palaver.system.engine.Parser;
import de.unidue.palaver.system.resource.MessageType;

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
        this.mimeType = mimeType;
        this.message = message;
        this.isRead = Boolean.parseBoolean(isReadStatus);
        Parser parser = new Parser();
        this.date = parser.stringToDateFromDataBase(date);
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

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMimeType() {
        return mimeType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public boolean getIsReadStatus() {
        return isRead;
    }

    public void setIsReadStatus(boolean read) {
        isRead = read;
    }

    public Date getDate() {
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
