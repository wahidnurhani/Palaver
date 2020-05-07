package de.unidue.palaver.system.engine;

import android.app.Activity;
import android.content.Context;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.ui.LoginActivity;

public interface IPalaverEngine {


    void handleSendMessage(Context applicationContext, Activity activity, Friend friend, String messageText);

    void handleFetchAllFriendRequestWithNoService(Context context, User user);

    void handleRegisterRequest(Context applicationContext, Activity activity, User user);

    void handleLoginRequest(Context context, LoginActivity loginActivity, User user);

    void handleLogoutRequest(Context applicationContext);
}
