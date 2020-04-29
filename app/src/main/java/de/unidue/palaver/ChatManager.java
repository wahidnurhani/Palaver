package de.unidue.palaver;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unidue.palaver.model.Chat;
import de.unidue.palaver.model.Friend;

public class ChatManager {

    private List<Chat> chats;

    public ChatManager(Palaver palaver) {
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
        this.chats.add(chat);
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

    public void openChatListActivity(){

    }

    public void refreshView(){

    }
}
