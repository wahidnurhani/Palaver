package de.unidue.palaver.engine;

import android.content.Context;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.User;

public class PalaverEngine implements IPalaverEngine {

    private Communicator communicator;
    private Parser parser;
    private Palaver palaver;
    private Authentificator authentificator;

    public PalaverEngine(Palaver palaver) {
        this.communicator = new Communicator(palaver);
        this.authentificator = new Authentificator(palaver);
        this.parser = new Parser();
        this.palaver = palaver;
    }


    public Communicator getCommunicator() {
        return communicator;
    }

    @Override
    public void handleSendMessage(Friend friend, String message) {
        //TODO
    }

    @Override
    public void handleAddFriendRequest(User user) {
        //TODO
    }

    @Override
    public void handleRegisterRequest(Context context, User user) {
        authentificator.authentificate(context, user.getUserData().getUserName(),
                user.getUserData().getPassword());
    }

    @Override
    public void handleLoginRequest(Context context, User user) {
        authentificator.authentificate(context, user.getUserData().getUserName(), user.getUserData().getPassword());
    }

    public void handleIncommingMessage(Friend friend, String message){
        //TODO
    }


}
