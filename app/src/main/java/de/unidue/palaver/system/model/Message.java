package de.unidue.palaver.system.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import de.unidue.palaver.system.engine.Parser;
import de.unidue.palaver.system.roomdatabase.DBContract.TableMessage;


@Entity(tableName = TableMessage.TABLE_MESSAGE_NAME)
public class Message implements Comparable<Message>, Serializable {

    @NonNull
    @SerializedName("Sender")
    @ColumnInfo(name = TableMessage.COLUMN_CHAT_SENDER)
    private String sender;

    @SerializedName("Recipient")
    @ColumnInfo(name = TableMessage.COLUMN_CHAT_RECIPIENT)
    private String recipient;

    @SerializedName("Mimetype")
    @ColumnInfo(name = TableMessage.COLUMN_CHAT_MIMETYPE)
    private String mimeType;

    @NonNull
    @SerializedName("Data")
    @ColumnInfo(name = TableMessage.COLUMN_CHAT_DATA)
    private String message;

    @NonNull
    @PrimaryKey
    @SerializedName("DateTime")
    @ColumnInfo(name = TableMessage.COLUMN_CHAT_DATETIME)
    private String date;

    public Message() { }

    public Message(String sender, String recipient, String message, String date) {
        this.sender = sender;
        this.recipient = recipient;
        this.mimeType = "text/plain";
        this.message = message;
        this.date = date;
    }

    public Message(String sender, String recipient, String message, Date date) {
        this.sender = sender;
        this.recipient = recipient;
        this.mimeType= "text/plain";
        this.message = message;
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

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Message o) {
        return  date.compareTo(o.date);
    }

    @Override
    public String toString() {
        return "Message{" +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", message='" + message + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
