package de.unidue.palaver.engine;

import android.app.Activity;
import android.content.Context;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.User;
import de.unidue.palaver.service.ServiceAddFriend;

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
    public void handleSendMessage(Friend friend, String message) {
        //TODO
    }

    @Override
    public void handleFetchAllFriendRequest(User user) {
        communicator.fetchFriends(user);
    }

    @Override
    public void handleRegisterRequest(Context context, User user) {
        if(communicator.checkConnectivity(context)){
            authentificator.register(context, user.getUserData().getUserName(),
                    user.getUserData().getPassword());
        } else{
            palaver.getUiController().showToast(context, "No Internet Connection");
        }
    }

    @Override
    public void handleLoginRequest(Context context, User user) {
        if(communicator.checkConnectivity(context)){
            authentificator.authentificate(context, user.getUserData().getUserName(),
                    user.getUserData().getPassword());
        } else{
            palaver.getUiController().showToast(context, "No Internet Connection");
        }

    }

    public void handleIncommingMessage(Friend friend, String message){
        //TODO
    }


    public void handleAddFriendRequest(Context applicationContext, Activity activity, String username) {
        ServiceAddFriend.startIntent(applicationContext, activity, username);
    }
}
