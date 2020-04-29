package de.unidue.palaver.model;

import java.util.Date;

public class ChatItem implements Comparable<ChatItem>{

    private final Friend friend;
    private final String message;
    private final ChatItemType chatItemType;
    private final ChatItemAlignment chatItemAlignment;
    private boolean isRead;
    private Date date;

    public ChatItem(Friend friend, ChatItemType chatItemType, String message, Date date) {
        this.friend = friend;
        this.chatItemType = chatItemType;
        this.message = message;
        this.date = date;
        if(chatItemType == ChatItemType.INCOMMING){
            chatItemAlignment = ChatItemAlignment.LEFT;
        } else{
            chatItemAlignment = ChatItemAlignment.RIGHT;
        }
        this.isRead = chatItemType == ChatItemType.OUT;
    }

    public Friend getFriend() {
        return friend;
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

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(ChatItem o) {
        return this.date.compareTo(o.getDate());
    }
}
