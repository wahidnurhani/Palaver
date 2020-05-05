package de.unidue.palaver.system;


import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.uicontroller.arrayadapter.ChatArrayAdapter;

public class ChatsManager extends ViewModel {

    private List<ChatRoomManager> chatRoomManagers;
    private ChatArrayAdapter chatArrayAdapter;

    public ChatsManager() {
        this.chatRoomManagers = new ArrayList<>();
    }

    public ChatRoomManager getChat(Friend friend) {
        for(ChatRoomManager chatRoomManager : chatRoomManagers){
            if(chatRoomManager.getFriend().getUsername().equals(friend.getUsername())){
                return chatRoomManager;
            }
        }
        return null;
    }

    public void addChat(ChatRoomManager chatRoomManager) {
        String friendUserName = chatRoomManager.getFriend().getUsername();
        if(!chatExist(friendUserName)){
            chatRoomManagers.add(chatRoomManager);
        }
    }

    private boolean chatExist(String userName) {

        for(ChatRoomManager tmp : chatRoomManagers){
            if(tmp.getFriend().getUsername().equals(userName)){
                return true;
            }
        }
        return false;
    }

    public boolean removeChat(ChatRoomManager chatRoomManager){
        return chatRoomManagers.remove(chatRoomManager);
    }

    public void sort(){
        Collections.sort(chatRoomManagers);
    }

    public List<ChatRoomManager> search(String string){
        List<ChatRoomManager> result = new ArrayList<>();
        chatArrayAdapter.clear();
        if(string.equals("")){
            chatArrayAdapter.addAll(chatRoomManagers);
            result = chatRoomManagers;
        } else {
            for (ChatRoomManager chatRoomManager : chatRoomManagers
            ) {
                if(chatRoomManager.getFriend().getUsername().contains(string)){
                    result.add(chatRoomManager);
                }
            }
            chatArrayAdapter.addAll(result);
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

    public List<ChatRoomManager> getChatList() {
        return chatRoomManagers;
    }

    private class FectchChatListFromDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            PalaverDB palaverDB = Palaver.getInstance().getPalaverDB();
            chatRoomManagers.clear();
            chatRoomManagers.addAll(palaverDB.getAllChat());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            chatArrayAdapter.clear();
            chatArrayAdapter.addAll(chatRoomManagers);
        }
    }
}
