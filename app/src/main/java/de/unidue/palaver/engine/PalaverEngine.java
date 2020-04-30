package de.unidue.palaver.engine;

import android.content.Context;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.User;

public class PalaverEngine implements IPalaverEngine {

    private Communicator communicator;
    private Authentificator authentificator;

    public PalaverEngine() {
        this.communicator = new Communicator();
        this.authentificator = new Authentificator();
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
        authentificator.register(context, user.getUserData().getUserName(),
                user.getUserData().getPassword());
    }

    @Override
    public void handleLoginRequest(Context context, User user) {
        authentificator.authentificate(context, user.getUserData().getUserName(),
                user.getUserData().getPassword());
    }

    public void handleIncommingMessage(Friend friend, String message){
        //TODO
    }


}
