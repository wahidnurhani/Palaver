package de.unidue.palaver;

import android.content.Context;

import de.unidue.palaver.database.PalaverDBManager;
import de.unidue.palaver.engine.PalaverEngine;

public class Palaver {
    private PalaverEngine palaverEngine;
    private PalaverDBManager palaverDBManager;
    private ChatManager chatManager;
    private FriendManager friendManager;
    private UIController uiController;
    private static Palaver palaverInstance;
    private Context context;

    public static Palaver getInstance(){
        if(palaverInstance==null){
            palaverInstance = new Palaver();
        }
        return palaverInstance;
    }

    public Palaver() {
        init();
    }

    public void setContext(Context applicationContext) {
        this.context=applicationContext;
    }

    public void init(){
        palaverInstance =this;
        this.palaverEngine = new PalaverEngine();
        this.chatManager = new ChatManager();
        this.friendManager = new FriendManager();
        this.uiController = new UIController();
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

    public PalaverEngine getPalaverEngine() {
        return palaverEngine;
    }

    private void deleteDBManager() {
        this.palaverDBManager=null;
    }

    public PalaverDBManager getPalaverDBManager() {
        return palaverDBManager;
    }

    public void setDBManager(PalaverDBManager palaverDBManager) {
        this.palaverDBManager = palaverDBManager;
    }
}
