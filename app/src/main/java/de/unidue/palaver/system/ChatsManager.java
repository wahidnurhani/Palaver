package de.unidue.palaver.system;


import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.uicontroller.arrayadapter.ChatArrayAdapter;

public class ChatsManager extends ViewModel {

    private List<MessageViewModel> messageViewModels;
    private ChatArrayAdapter chatArrayAdapter;

    public ChatsManager() {
        this.messageViewModels = new ArrayList<>();
    }

    public MessageViewModel getChat(Friend friend) {
        for(MessageViewModel messageViewModel : messageViewModels){
            if(messageViewModel.getFriend().getUsername().equals(friend.getUsername())){
                return messageViewModel;
            }
        }
        return null;
    }

    public void addChat(MessageViewModel messageViewModel) {
        String friendUserName = messageViewModel.getFriend().getUsername();
        if(!chatExist(friendUserName)){
            messageViewModels.add(messageViewModel);
        }
    }

    private boolean chatExist(String userName) {

        for(MessageViewModel tmp : messageViewModels){
            if(tmp.getFriend().getUsername().equals(userName)){
                return true;
            }
        }
        return false;
    }

    public boolean removeChat(MessageViewModel messageViewModel){
        return messageViewModels.remove(messageViewModel);
    }

    public void sort(){
        Collections.sort(messageViewModels);
    }

    public List<MessageViewModel> search(String string){
        List<MessageViewModel> result = new ArrayList<>();
        chatArrayAdapter.clear();
        if(string.equals("")){
            chatArrayAdapter.addAll(messageViewModels);
            result = messageViewModels;
        } else {
            for (MessageViewModel messageViewModel : messageViewModels
            ) {
                if(messageViewModel.getFriend().getUsername().contains(string)){
                    result.add(messageViewModel);
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

    public List<MessageViewModel> getChatList() {
        return messageViewModels;
    }

    private class FectchChatListFromDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
//            PalaverDB palaverDB = Palaver.getInstance().getPalaverDB();
//            messageViewModels.clear();
//            messageViewModels.addAll(palaverDB.getAllChat());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            chatArrayAdapter.clear();
            chatArrayAdapter.addAll(messageViewModels);
        }
    }
}
