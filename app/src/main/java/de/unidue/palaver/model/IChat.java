package de.unidue.palaver.model;

public interface IChat {

    void openChat();

    ChatItem getLatestMessage();

    void sort();

    boolean isUnReadMessageExist();

    boolean setAllMessageToRead();

    void refreshView();
}
