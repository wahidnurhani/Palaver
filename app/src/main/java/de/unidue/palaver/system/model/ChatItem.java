package de.unidue.palaver.system.model;

import java.util.Date;
import de.unidue.palaver.system.engine.Parser;
import de.unidue.palaver.system.resource.ChatItemAlignment;
import de.unidue.palaver.system.resource.ChatItemType;

public class ChatItem implements Comparable<ChatItem>{

    private final String sender;
    private final String recipient;
    private final String message;
    private String mimeType;
    private final ChatItemType chatItemType;
    private final ChatItemAlignment chatItemAlignment;
    private boolean isRead;
    private Date date;

    public ChatItem(String sender, String recipient, ChatItemType chatItemType, String message, Date date) {
        this.sender = sender;
        this.recipient = recipient;
        this.chatItemType = chatItemType;
        this.mimeType="text/plain";
        this.message = message;
        this.date = date;

        if(chatItemType == ChatItemType.INCOMMING){
            chatItemAlignment = ChatItemAlignment.LEFT;
        } else{
            chatItemAlignment = ChatItemAlignment.RIGHT;
        }
        this.isRead = chatItemType == ChatItemType.OUT;
    }

    public ChatItem(String sender, String recipient, ChatItemType chatItemType, String message, String isReadStatus, String date) {
        this.sender = sender;
        this.recipient = recipient;
        this.chatItemType = chatItemType;
        this.mimeType="text/plain";
        this.message = message;
        this.isRead = Boolean.parseBoolean(isReadStatus);
        if(chatItemType == ChatItemType.INCOMMING){
            chatItemAlignment = ChatItemAlignment.LEFT;
        } else{
            chatItemAlignment = ChatItemAlignment.RIGHT;
        }
        Parser parser = new Parser();
        this.date = parser.stringToDateFromServer(date);
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

    public ChatItemType getChatItemType() {
        return chatItemType;
    }

    public String getMessage() {
        return message;
    }

    public ChatItemAlignment getChatItemAlignment() {
        return chatItemAlignment;
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

    @Override
    public int compareTo(ChatItem o) {
        return this.date.compareTo(o.getDate());
    }
}
