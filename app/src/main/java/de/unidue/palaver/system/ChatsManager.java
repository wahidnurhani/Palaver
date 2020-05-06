package de.unidue.palaver.system;


import android.os.AsyncTask;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.viewmodel.MessageViewModel;

public class ChatsManager extends ViewModel {

    private List<MessageViewModel> messageViewModels;

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

            return null;
        }
    }
}
