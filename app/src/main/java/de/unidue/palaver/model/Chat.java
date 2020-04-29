package de.unidue.palaver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat implements Comparable<Chat>, IChat {

    private final Friend friend;
    private List<ChatItem> chatItemList;

    public Chat(Friend friend) {
        this.friend = friend;
        this.chatItemList = new ArrayList<>();
    }

    public Friend getFriend() {
        return friend;
    }

    public List<ChatItem> getChatItemList() {
        return chatItemList;
    }

    public void addChatItem(ChatItem chatItem) {
        this.chatItemList.add(chatItem);
    }

    @Override
    public boolean isUnReadMessageExist(){
        for (ChatItem chatItem : chatItemList) {
            if(chatItem.getIsReadStatus()==false){
                return true;
            }
        }
        return false;
    }

    @Override
    public void sort(){
        Collections.sort(chatItemList);
    }

    @Override
    public ChatItem getLatestMessage() {
        sort();
        return chatItemList.get(chatItemList.size()-1);
    }

    @Override
    public int compareTo(Chat o) {
        return this.getLatestMessage().compareTo(o.getLatestMessage());
    }

    @Override
    public void openChat() {
        setAllMessageToRead();
        //TODO open chat activity
    }

    @Override
    public boolean setAllMessageToRead() {
        for (ChatItem chatItem : chatItemList){
            chatItem.setIsReadStatus(true);
        }
        return false;
    }

    @Override
    public void refreshView(){
        //TODO
    }
}
