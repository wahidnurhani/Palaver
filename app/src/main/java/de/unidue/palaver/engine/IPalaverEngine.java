package de.unidue.palaver.engine;

import android.content.Context;

import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.User;

public interface IPalaverEngine {

    void handleSendMessage(Friend friend, String message);

    void handleFetchAllFriendRequest(User user);

    void handleRegisterRequest(Context context, User user);

    void handleLoginRequest(Context context, User user);

}
