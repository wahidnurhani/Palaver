package de.unidue.palaver.system.engine;

import android.app.Activity;
import android.content.Context;

import de.unidue.palaver.system.MessageViewModel;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.ui.LoginActivity;

public interface IPalaverEngine {

    void handleSendMessage(Context applicationContext, Activity activity, MessageViewModel messageViewModel, String message);

    void handleFetchAllFriendRequestWithNoService(User user);

    void handleRegisterRequest(Context applicationContext, Activity activity, User user);

    void handleLoginRequest(Context context, LoginActivity loginActivity, User user);

    void handleLogoutRequest(Context applicationContext);
}
