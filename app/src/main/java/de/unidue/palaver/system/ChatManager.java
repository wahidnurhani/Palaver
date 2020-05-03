package de.unidue.palaver.system;


import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.ui.arrayadapter.ChatArrayAdapter;

public class ChatManager {

    private List<MessageManager> messageManagers;
    private ChatArrayAdapter chatArrayAdapter;

    public ChatManager() {
        this.messageManagers = new ArrayList<>();
    }

    public MessageManager getChat(Friend friend) {
        for(MessageManager messageManager : messageManagers){
            if(messageManager.getFriend()==friend){
                return messageManager;
            }
        }
        return null;
    }

    public void addChat(MessageManager messageManager) {
        String friendUserName = messageManager.getFriend().getUsername();
        if(!chatExist(friendUserName)){
            messageManagers.add(messageManager);
        }
    }

    private boolean chatExist(String userName) {

        for(MessageManager tmp : messageManagers){
            if(tmp.getFriend().getUsername().equals(userName)){
                return true;
            }
        }
        return false;
    }

    public boolean removeChat(MessageManager messageManager){
        return messageManagers.remove(messageManager);
    }

    public void sort(){
        Collections.sort(messageManagers);
    }

    public List<MessageManager> search(String string){
        List<MessageManager> result = new ArrayList<>();
        for (MessageManager messageManager : messageManagers
        ) {
            if(messageManager.getFriend().getUsername().contains(string)){
                result.add(messageManager);
            }
        }
        return result;
    }

    public void initArrayAdapter(Context context, int layout) {
        this.chatArrayAdapter = new ChatArrayAdapter(context, layout);
    }

    public ChatArrayAdapter getChatArrayAdapter() {
        return chatArrayAdapter;
    }

    public void updateChatList() {
        FectchChatListFromDB fectchChatListFromDB = new FectchChatListFromDB();
        fectchChatListFromDB.execute();
    }

    public List<MessageManager> getChatList() {
        return messageManagers;
    }

    private class FectchChatListFromDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            PalaverDB palaverDB = Palaver.getInstance().getPalaverDB();
            messageManagers.clear();
            messageManagers.addAll(palaverDB.getAllChat());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            chatArrayAdapter.clear();
            chatArrayAdapter.addAll(messageManagers);
        }
    }
}
