package de.unidue.palaver.system.engine;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.DataServerResponseList;
import de.unidue.palaver.system.httpclient.NewCommunicator;
import de.unidue.palaver.system.service.ServiceLogin;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.roomdatabase.DatabaseCleaner;
import de.unidue.palaver.system.service.ServiceAddFriend;
import de.unidue.palaver.system.service.ServiceFetchAllChat;
import de.unidue.palaver.system.service.ServiceSendMessage;
import de.unidue.palaver.ui.LoginActivity;
import retrofit2.Response;

public class PalaverEngine implements IPalaverEngine {
    private static final String TAG = PalaverEngine.class.getSimpleName();

    private UIController uiController;
    private static PalaverEngine palaverEngineInstance;

    public static PalaverEngine getPalaverEngineInstance() {
        if(palaverEngineInstance ==null){
            palaverEngineInstance = new PalaverEngine();
        }
        return palaverEngineInstance;
    }

    private PalaverEngine() {
        this.uiController = new UIController();
    }

    public UIController getUiController() {
        return uiController;
    }

    @Override
    public void handleSendMessage(Context applicationContext, Activity activity, Friend friend, String messageText) {
        Message message = new Message(SessionManager.getSessionManagerInstance(applicationContext).getUser().getUserName(),
                friend.getUsername(), messageText, new Date());

        ServiceSendMessage.startIntent(applicationContext, activity, friend, message);
    }

    @Override
    public void handleRegisterRequest(Context applicationContext, Activity activity, User user) {
        NewCommunicator newCommunicator = new NewCommunicator();
        try {
            Response<DataServerResponseList<String>> response = newCommunicator.register(user);
            if (response.body().getMessageType()==1){
                handleShowToastRequest(applicationContext, response.body().getInfo());
                handleOpenLoginActivityRequest(activity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleLoginRequest(Context applicationContext, LoginActivity activity, User user) {
        ServiceLogin.startIntent(applicationContext, activity, user.getUserName(), user.getPassword());
    }

    @Override
    public void handleLogoutRequest(Context applicationContext) {
        Log.i(TAG, "Check uiController LogoutRequest: "+ (uiController!=null));
        SessionManager.getSessionManagerInstance(applicationContext).endSession();
        uiController.openLoginActivity(applicationContext);
        DatabaseCleaner databaseCleaner = new DatabaseCleaner(applicationContext);
        databaseCleaner.cleanDatabase();
    }

    public void handleIncommingMessage(Friend friend, String message){
        //TODO
    }

    public void handleAddFriendRequest(Context applicationContext, Activity activity, String username) {
        ServiceAddFriend.startIntent(applicationContext, activity, username);
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

    public void handleOpenLoginActivityRequest(Activity activity) {
        Log.i(TAG, "Check uiController openLoginActivity: "+ (uiController!=null));
        uiController.openLoginActivity(activity);
    }

    public void handleOpenFriendManagerActivityRequest(Activity activity) {
        Log.i(TAG, "Check uiController openFriendMAnagerActivity: "+ (uiController!=null));
        uiController.openFriendManagerActivity(activity);
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

    public void handleOpenSettingRequest(Context applicationContext, Activity activity) {
        uiController.openSettingActivity(applicationContext, activity);
    }
}
