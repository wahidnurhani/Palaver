package de.unidue.palaver.system.model;

import android.content.Context;

import de.unidue.palaver.system.ChatRoomManager;

public interface IChat {

    void openChat(Context context, ChatRoomManager chatRoomManager);

    Message getLatestMessage();

    void sort();

    boolean isUnReadMessageExist();

    boolean setAllMessageToRead();

    void refreshView();
}
