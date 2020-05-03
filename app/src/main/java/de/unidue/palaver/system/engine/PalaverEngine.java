package de.unidue.palaver.system.engine;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.CommunicatorResult;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.service.ServiceAddFriend;
import de.unidue.palaver.system.service.ServiceFetchAllChat;
import de.unidue.palaver.system.service.ServiceSendMessage;
import de.unidue.palaver.ui.LoginActivity;

public class PalaverEngine implements IPalaverEngine {

    private Communicator communicator;
    private Authentificator authentificator;
    private Palaver palaver;

    public PalaverEngine() {
        this.communicator = new Communicator();
        this.authentificator = new Authentificator();
        this.palaver = Palaver.getInstance();
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    @Override
    public void handleSendMessage(Context applicationContext, Activity activity, Friend friend, Message message) {
        Palaver.getInstance().getPalaverDB().insertChatItem(friend, message);
        ServiceSendMessage.startIntent(applicationContext, activity, friend, message);
    }

    @Override
    public void handleFetchAllFriendRequestWithNoService(User user) {
        List<Friend> friends = communicator.fetchFriends(user).getData();
        for (Friend friend : friends){
            palaver.getPalaverDB().insertFriend(friend);
        }
    }

    @Override
    public void handleRegisterRequest(Context context, User user) {
        if(communicator.checkConnectivity(context)){
            authentificator.register((Activity) context, user.getUserData().getUserName(),
                    user.getUserData().getPassword());
        } else{
            palaver.getUiManager().showToast(context, StringValue.ErrorMessage.NO_INTERNET);
        }
    }

    @Override
    public void handleLoginRequest(Context applicationContext, LoginActivity loginActivity, User user) {
        if(communicator.checkConnectivity(applicationContext)){
            authentificator.authentificate(applicationContext, loginActivity,  user.getUserData().getUserName(),
                    user.getUserData().getPassword());
        } else{
            palaver.getUiManager().showToast(applicationContext, StringValue.ErrorMessage.NO_INTERNET);
        }
    }

    @Override
    public void handleLogoutRequest(Context applicationContext) {
        SessionManager.getSessionManagerInstance(applicationContext).endSession();
        palaver.getUiManager().openLoginActivity(applicationContext);
        palaver.destroy();
    }

    public void handleIncommingMessage(Friend friend, String message){
        //TODO
    }

    public void handleAddFriendRequest(Context applicationContext, Activity activity, String username) {
        ServiceAddFriend.startIntent(applicationContext, activity, username);
    }

    public void handleFetchAllChatRequestWithNoService(Context applicationContext) {
        List<Friend> friends = palaver.getPalaverDB().getAllFriends();

        for(Friend friend : friends){
            CommunicatorResult<Message> communicatorResult = communicator.getMessage(SessionManager.getSessionManagerInstance(applicationContext).getUser(), friend);
            for(Message message : communicatorResult.getData()){
                palaver.getPalaverDB().insertChatItem(friend, message);
            }
        }
    }

    public void handleFetchAllChatRequestWithService(Context applicationContext, Activity activity) {
        ServiceFetchAllChat.startIntent(applicationContext, activity);
    }
}
