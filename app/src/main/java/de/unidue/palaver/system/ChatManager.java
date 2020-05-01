package de.unidue.palaver.system;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unidue.palaver.system.model.Chat;
import de.unidue.palaver.system.model.Friend;

public class ChatManager {

    private List<Chat> chats;

    public ChatManager() {
        this.chats = new ArrayList<>();
    }

    public Chat getChat(Friend friend) {
        for(Chat chat : chats){
            if(chat.getFriend()==friend){
                return chat;
            }
        }
        return null;
    }

    public void addChat(Chat chat) {
        String friendUserName = chat.getFriend().getUsername();
        if(!chatExist(friendUserName)){
            chats.add(chat);
        }
    }

    private boolean chatExist(String userName) {

        for(Chat tmp : chats){
            if(tmp.getFriend().getUsername().equals(userName)){
                return true;
            }
        }
        return false;
    }

    public boolean removeChat(Chat chat){
        return chats.remove(chat);
    }

    public void sort(){
        Collections.sort(chats);
    }

    public void makeNewChat(Friend friend){
        if(getChat(friend)==null){
            Chat chat = new Chat(friend);
            addChat(chat);
            chat.openChat();
        } else {
            getChat(friend).openChat();
        }
    }

    public List<Chat> search(String string){
        List<Chat> result = new ArrayList<>();
        for (Chat chat: chats
        ) {
            if(chat.getFriend().getUsername().contains(string)){
                result.add(chat);
            }
        }
        return result;
    }

    public void refreshView(){

    }

    public List<Chat> getChatList() {
        return chats;
    }
}
