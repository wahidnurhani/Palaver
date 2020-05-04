package de.unidue.palaver.system;

import android.app.Activity;
import android.content.Context;

import java.util.Date;
import java.util.List;

import de.unidue.palaver.system.communicator.Authentificator;
import de.unidue.palaver.system.communicator.Communicator;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.CommunicatorResult;
import de.unidue.palaver.system.resource.MessageType;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.service.ServiceAddFriend;
import de.unidue.palaver.system.service.ServiceFetchAllChat;
import de.unidue.palaver.system.service.ServiceSendMessage;
import de.unidue.palaver.system.uicontroller.UIController;
import de.unidue.palaver.ui.LoginActivity;

public class PalaverEngine implements IPalaverEngine {

    private Communicator communicator;
    private Authentificator authentificator;
    private Palaver palaver;
    private ChatsManager chatsManager;
    private UIController uiController;

    public PalaverEngine() {
        this.palaver = Palaver.getInstance();
        this.communicator = new Communicator();
        this.authentificator = new Authentificator();
        this.uiController = new UIController();
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
    public void handleRegisterRequest(Context applicationContext, Activity activity, User user) {
        if(communicator.checkConnectivity(applicationContext)){
            authentificator.register(applicationContext, activity, user.getUserData().getUserName(),
                    user.getUserData().getPassword());
        } else{
            uiController.showToast(activity, StringValue.ErrorMessage.NO_INTERNET);
        }
    }

    @Override
    public void handleLoginRequest(Context applicationContext, LoginActivity loginActivity, User user) {
        if(communicator.checkConnectivity(applicationContext)){
            authentificator.authentificate(applicationContext, loginActivity,  user.getUserData().getUserName(),
                    user.getUserData().getPassword());
        } else{
            palaver.getUiController().showToast(applicationContext, StringValue.ErrorMessage.NO_INTERNET);
        }
    }

    @Override
    public void handleLogoutRequest(Context applicationContext) {
        SessionManager.getSessionManagerInstance(applicationContext).endSession();
        uiController.openLoginActivity(applicationContext);
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
        uiController.showErrorDialog(context, message);
    }

    public void handleShowToastRequest(Context applicationContext, String string) {
        uiController.showToast(applicationContext, string);
    }

    public void handleClickOnFriend(Context context, Friend friend) {
        chatsManager = Palaver.getInstance().getChatsManager();
        ChatRoomManager chatRoomManager = chatsManager.getChat(friend);

        if(chatRoomManager==null){
            chatRoomManager = new ChatRoomManager(friend);
            chatsManager.addChat(chatRoomManager);
        }
        uiController.openChat(context, chatRoomManager);
    }

    public void handleOpenLoginActivityRequest(Activity activity) {
        uiController.openLoginActivity(activity);
    }

    public void handleOpenFriendManagerActivityRequest(Activity activity) {
        uiController.openFriendManagerActivity(activity);
    }

    public void handleOpenChatRoomRequest(Activity activity, ChatRoomManager chatRoomManager) {
        uiController.openChat(activity, chatRoomManager);
    }

    public void handleOpenAddFriendDialogRequest(Context applicationContext, Activity activity) {
        uiController.openAddFriendDDialog(applicationContext, activity);
    }

    public void handleOpenChatManagerActivityRequest(Activity activity) {
        uiController.openChatManagerActivity(activity);
    }

    public void handleOpenRegisterActivityRequest(Context context) {
        uiController.openRegisterActivity(context);
    }
}
