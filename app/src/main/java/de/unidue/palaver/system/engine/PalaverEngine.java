package de.unidue.palaver.system.engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Date;
import java.util.List;

import de.unidue.palaver.system.ChatRoomManager;
import de.unidue.palaver.system.ChatsManager;
import de.unidue.palaver.system.FriendManager;
import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.engine.communicator.Authentificator;
import de.unidue.palaver.system.engine.communicator.Communicator;
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
    private static final String TAG = PalaverEngine.class.getSimpleName();

    private Communicator communicator;
    private Authentificator authentificator;
    private PalaverDB palaverDB;
    private ChatsManager chatsManager;
    private UIController uiController;
    private FriendManager friendManager;

    public PalaverEngine(ChatsManager chatsManager, FriendManager friendManager) {
        this.communicator = new Communicator();
        this.authentificator = new Authentificator();
        this.uiController = new UIController();
        this.chatsManager = chatsManager;
        this.friendManager = friendManager;
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    @Override
    public void handleSendMessage(Context applicationContext, Activity activity, ChatRoomManager chatRoomManager, String messageText) {
        palaverDB = Palaver.getInstance().getPalaverDB();
        Log.i(TAG, "Check palaverDB SendMessage: "+ (palaverDB!=null));

        Message message = new Message(SessionManager.getSessionManagerInstance(applicationContext).getUser().getUserData().getUserName(),
                chatRoomManager.getFriend().getUsername(), MessageType.OUT, messageText, "true", new Date());

        Friend friend = chatRoomManager.getFriend();

        palaverDB.insertChatItem(friend, message);
        ServiceSendMessage.startIntent(applicationContext, activity, friend, message);
        chatRoomManager.addMessage(message);
    }

    @Override
    public void handleFetchAllFriendRequestWithNoService(User user) {
        Log.i(TAG, "Check communicator FetchAllFriendRequest: "+ (communicator!=null));
        palaverDB = Palaver.getInstance().getPalaverDB();

        List<Friend> friends = communicator.fetchFriends(user).getData();
        for (Friend friend : friends){
            palaverDB.insertFriend(friend);
        }
    }

    @Override
    public void handleRegisterRequest(Context applicationContext, Activity activity, User user) {
        Log.i(TAG, "Check authenticator RegisterRequest: "+ (authentificator!=null));
        Log.i(TAG, "Check uiController RegisterRequest: "+ (uiController!=null));

        if(communicator.checkConnectivity(applicationContext)){
            authentificator.register(applicationContext, activity, user.getUserData().getUserName(),
                    user.getUserData().getPassword());
        } else{
            uiController.showToast(activity, StringValue.ErrorMessage.NO_INTERNET);
        }
    }

    @Override
    public void handleLoginRequest(Context applicationContext, LoginActivity loginActivity, User user) {
        Log.i(TAG, "Check uiController LoginRequest: "+ (uiController!=null));
        Log.i(TAG, "Check authenticator LoginRequest: "+ (authentificator!=null));

        if(communicator.checkConnectivity(applicationContext)){
            authentificator.authentificate(applicationContext, loginActivity,  user.getUserData().getUserName(),
                    user.getUserData().getPassword());
        } else{
            uiController.showToast(applicationContext, StringValue.ErrorMessage.NO_INTERNET);
        }
    }

    @Override
    public void handleLogoutRequest(Context applicationContext) {
        Log.i(TAG, "Check uiController LogoutRequest: "+ (uiController!=null));
        SessionManager.getSessionManagerInstance(applicationContext).endSession();
        uiController.openLoginActivity(applicationContext);
        Palaver.getInstance().destroy();
    }

    public void handleIncommingMessage(Friend friend, String message){
        //TODO
    }

    public void handleAddFriendRequest(Context applicationContext, Activity activity, String username) {
        ServiceAddFriend.startIntent(applicationContext, activity, username);
    }

    public void handleFetchAllChatRequestWithNoService(Context applicationContext) {
        Log.i(TAG, "Check communicator FetchAllAchatWithoutService: "+ (communicator!=null));
        palaverDB = Palaver.getInstance().getPalaverDB();

        List<Friend> friends = palaverDB.getAllFriends();

        for(Friend friend : friends){
            CommunicatorResult<Message> communicatorResult = communicator.getMessage(SessionManager.getSessionManagerInstance(applicationContext).getUser(), friend);
            for(Message message : communicatorResult.getData()){
                palaverDB.insertChatItem(friend, message);
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
        Log.i(TAG, "Check uiController ShowErrorDdialog: "+ (uiController!=null));
        uiController.showErrorDialog(context, message);
    }

    public void handleShowToastRequest(Context applicationContext, String string) {
        Log.i(TAG, "Check uiController ShowToast: "+ (uiController!=null));
        uiController.showToast(applicationContext, string);
    }

    public void handleClickOnFriend(Context context, Friend friend) {
        Log.i(TAG, "Check chatManager ClickOnFriend: "+ (chatsManager!=null));
        Log.i(TAG, "Check uiController ClickOnFriend: "+ (uiController!=null));

        ChatRoomManager chatRoomManager = chatsManager.getChat(friend);

        if(chatRoomManager==null){
            chatRoomManager = new ChatRoomManager(friend);
            chatsManager.addChat(chatRoomManager);
        }
        uiController.openChatRoom(context, chatRoomManager);
    }

    public void handleOpenLoginActivityRequest(Activity activity) {
        Log.i(TAG, "Check uiController openLoginActivity: "+ (uiController!=null));
        uiController.openLoginActivity(activity);
    }

    public void handleOpenFriendManagerActivityRequest(Activity activity) {
        Log.i(TAG, "Check uiController openFriendMAnagerActivity: "+ (uiController!=null));
        uiController.openFriendManagerActivity(activity);
    }

    public void handleOpenChatRoomRequest(Activity activity, ChatRoomManager chatRoomManager) {
        Log.i(TAG, "Check uiController ChatRoomActivity: "+ (uiController!=null));
        uiController.openChatRoom(activity, chatRoomManager);
    }

    public void handleOpenAddFriendDialogRequest(Context applicationContext, Activity activity) {
        Log.i(TAG, "Check uiController open addFriend Dialog: "+ (uiController!=null));
        uiController.openAddFriendDDialog(applicationContext, activity);
    }

    public void handleOpenChatManagerActivityRequest(Activity activity) {
        Log.i(TAG, "Check uiController openChatManagerActivity: "+ (uiController!=null));
        uiController.openChatManagerActivity(activity);
    }

    public void handleOpenRegisterActivityRequest(Context context) {
        Log.i(TAG, "Check uiController openRegisterActivity: "+ (uiController!=null));
        uiController.openRegisterActivity(context);
    }

    public void hadleOpenSplashScreenActivityRequest(Activity activity) {
        uiController.openSplashScreenActivity(activity);
    }

    public void handleStartSessionRequest(Context applicationContext, User user) {
        SessionManager sessionManager = SessionManager.getSessionManagerInstance(applicationContext);
        sessionManager.setUser(user);
        sessionManager.startSession(user.getUserData().getUserName(),
                user.getUserData().getPassword());
    }

    public void handleSendLocalBroadCastRequest(Context applicationContext, String action) {
        Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
    }
}
