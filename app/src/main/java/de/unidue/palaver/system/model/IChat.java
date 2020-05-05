package de.unidue.palaver.system.model;

import android.content.Context;

import de.unidue.palaver.system.MessageViewModel;

public interface IChat {

    Message getLatestMessage();

    void sort();

    boolean setAllMessageToRead();

    void refreshView();
}
