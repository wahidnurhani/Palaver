package de.unidue.palaver.system;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.IChat;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.ui.ChatRoomActivity;
import de.unidue.palaver.ui.arrayadapter.MessageAdapter;

public class MessageManager implements Comparable<MessageManager>, IChat, Serializable {

    private final Friend friend;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    public MessageManager(Friend friend) {
        this.friend = friend;
        this.messageList = new ArrayList<>();
    }

    public Friend getFriend() {
        return friend;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void addChatItem(Message message) {
        this.messageList.add(message);
    }

    @Override
    public boolean isUnReadMessageExist(){
        for (Message message : messageList) {
            if(!message.getIsReadStatus()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void sort(){
        Collections.sort(messageList);
    }

    @Override
    public Message getLatestMessage() {
        sort();
        return messageList.get(messageList.size()-1);
    }

    @Override
    public int compareTo(MessageManager o) {
        return this.getLatestMessage().compareTo(o.getLatestMessage());
    }

    @Override
    public void openChat(Context context, MessageManager messageManager) {
        setAllMessageToRead();
        Intent intent = new Intent(context, ChatRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringValue.IntentKeyName.FRIEND, messageManager);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public boolean setAllMessageToRead() {
        for (Message message : messageList){
            message.setIsReadStatus(true);
        }
        return false;
    }

    @Override
    public void refreshView(){
        //TODO
    }

    public void setChatItems(List<Message> messages) {
        this.messageList = messages;
    }

    public void updateChat(){
        FectchChatFromDB fectchChatFromDB = new FectchChatFromDB();
        fectchChatFromDB.execute();
    }

    public void sendMessage(Context applicationContext, Activity activity, Message message) {
        addChatItem(message);
        messageAdapter.add(message);
        Palaver.getInstance().getPalaverEngine().handleSendMessage(applicationContext, activity, friend, message);
    }

    public void initArrayAdapter(Context context, int layout) {
        messageAdapter = new MessageAdapter(context, layout);
    }

    public ListAdapter getMessageAdapter() {
        return messageAdapter;
    }

    private class FectchChatFromDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            PalaverDB palaverDB = Palaver.getInstance().getPalaverDB();
            messageList.clear();
            messageList.addAll(palaverDB.getAllChatData(friend));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //TODO
            messageAdapter.clear();
            messageAdapter.addAll(messageList);
        }
    }
}
