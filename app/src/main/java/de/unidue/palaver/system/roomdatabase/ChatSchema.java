package de.unidue.palaver.system.roomdatabase;

import androidx.room.Entity;

import java.util.Date;

@Entity (tableName = "chat")
public class ChatSchema {

    private String sender;

    private String recipient;

    private String mimeType;

    private String message;

    private boolean isRead;

    private Date date_time;
}
