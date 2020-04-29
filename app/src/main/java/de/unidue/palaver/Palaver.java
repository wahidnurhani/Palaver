package de.unidue.palaver;

import android.content.Context;

import de.unidue.palaver.database.PalaverDB;
import de.unidue.palaver.engine.PalaverEngine;
import de.unidue.palaver.model.User;

public class Palaver {
    private SessionManager sessionManager;
    private PalaverEngine palaverEngine;
    private PalaverDB palaverDB;
    private ChatManager chatManager;
    private FriendManager friendManager;
    private UIController uiController;
    private static Palaver palaverInstance;

    public static Palaver getInstance(){
        if(palaverInstance==null){
            palaverInstance = new Palaver();
        }
        return palaverInstance;
    }

    public Palaver() {
        init();
    }

    public void init(){
        this.palaverEngine = new PalaverEngine(this);
        this.palaverDB = new PalaverDB(this);
        this.sessionManager = new SessionManager(this);
        this.chatManager = new ChatManager(this);
        this.friendManager = new FriendManager(this);
        this.uiController = new UIController(this);
        palaverInstance =this;
    }

    public UIController getUiController() {
        return uiController;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public PalaverEngine getPalaverEngine() {
        return palaverEngine;
    }

    public void logout(){
        deleteDatabase();
        sessionManager.endSession();
    }

    private void deleteDatabase() {

    }

    public User getUser(){
        return this.sessionManager.getUser();
    }

    public void openLoginActivity(Context context) {
        LoginActivity.startIntent(context);
    }
}
