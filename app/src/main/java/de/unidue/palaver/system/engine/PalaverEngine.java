package de.unidue.palaver.system.engine;

import android.app.Activity;
import android.content.Context;

import java.util.Date;
import java.util.List;

import de.unidue.palaver.system.ChatRoomManager;
import de.unidue.palaver.system.ChatsManager;
import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.UIManager;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.CommunicatorResult;
import de.unidue.palaver.system.resource.MessageType;
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
    private ChatsManager chatsManager;
    private UIManager uiManager;

    public PalaverEngine() {
        this.communicator = new Communicator();
        this.authentificator = new Authentificator();
        this.uiManager = new UIManager();
        this.palaver = Palaver.getInstance();
        this.chatsManager = Palaver.getInstance().getChatsManager();
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    @Override
    public void handleSendMessage(Context applicationContext, Activity activity, ChatRoomManager chatRoomManager, String messageText) {

        Message message = new Message(SessionManager.getSessionManagerInstance(applicationContext).getUser().getUserData().getUserName(),
                chatRoomManager.getFriend().getUsername(), MessageType.OUT, messageText, "true", new Date());

        Friend friend = chatRoomManager.getFriend();

        Palaver.getInstance().getPalaverDB().insertChatItem(friend, message);
        ServiceSendMessage.startIntent(applicationContext, activity, friend, message);
        chatRoomManager.addMessage(message);
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
            uiManager.showToast(context, StringValue.ErrorMessage.NO_INTERNET);
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
        uiManager.openLoginActivity(applicationContext);
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

    public void handleChangePasswordRequest(String newPassword){
        //TODO change password to server . if it success then SessionManager handle the changed Password
    }

    public void handleFetchAllChatRequestWithService(Context applicationContext, Activity activity) {
        ServiceFetchAllChat.startIntent(applicationContext, activity);
    }

    public void handleShowErrorDialogRequest(Context context, String message){
        uiManager.showErrorDialog(context, StringValue.ErrorMessage.USERNAME_PASSWORD_BLANK);
    }

    public void handleShowToastRequest(Context applicationContext, String string) {
        uiManager.showToast(applicationContext, string);
    }

    public void handleClickOnFriend(Context context, Friend friend) {
        ChatRoomManager chatRoomManager = chatsManager.getChat(friend);
        if(chatRoomManager == null){
            chatsManager.addChat(chatRoomManager);
        }
        uiManager.openChat(context, chatRoomManager);
    }
}
