package de.unidue.palaver.system.model;

import android.content.Context;

import de.unidue.palaver.system.MessageManager;

public interface IChat {

    void openChat(Context context, MessageManager messageManager);

    Message getLatestMessage();

    void sort();

    boolean isUnReadMessageExist();

    boolean setAllMessageToRead();

    void refreshView();
}
