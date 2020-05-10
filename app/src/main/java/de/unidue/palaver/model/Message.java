package de.unidue.palaver.model;


import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.unidue.palaver.roomdatabase.DBContract.TableMessage;


@Entity(tableName = TableMessage.TABLE_MESSAGE_NAME)
public class Message implements Comparable<Message>, Serializable {

    @NonNull
    @ColumnInfo(name = "fk_friend")
    private String friendUserName;

    @NonNull
    @SerializedName("Sender")
    @ColumnInfo(name = TableMessage.COLUMN_MESSAGE_SENDER)
    private String sender;

    @NonNull
    @SerializedName("Recipient")
    @ColumnInfo(name = TableMessage.COLUMN_MESSAGE_RECIPIENT)
    private String recipient;

    @NonNull
    @SerializedName("Mimetype")
    @ColumnInfo(name = TableMessage.COLUMN_MESSAGE_MIMETYPE)
    private String mimeType;

    @NonNull
    @SerializedName("Data")
    @ColumnInfo(name = TableMessage.COLUMN_MESSAGE_DATA)
    private String message;

    @NonNull
    @PrimaryKey
    @SerializedName("DateTime")
    @ColumnInfo(name = TableMessage.COLUMN_MESSAGE_DATETIME)
    private String date;


    public Message(String sender, String recipient, String message, String date) {
        this.sender = sender;
        this.recipient = recipient;
        this.mimeType = "text/plain";
        this.message = message;
        this.date = date;
    }

    @Ignore
    public Message(String friendUserName, String sender, String recipient, String message, Date date) {
        this.friendUserName = friendUserName;
        this.sender = sender;
        this.recipient = recipient;
        this.mimeType= "text/plain";
        this.message = message;
        this.date = dateToString(date);
    }

    @NonNull
    public String getFriendUserName() {
        return friendUserName;
    }

    public void setFriendUserName(@NonNull String friendUserName) {
        this.friendUserName = friendUserName;
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
        return "Message{ sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", message='" + message + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String dateToString(Date date){
        String pattern = "yyyy-MM-dd'T'hh:mm:ssZ";
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
}
