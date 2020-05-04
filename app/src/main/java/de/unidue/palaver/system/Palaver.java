package de.unidue.palaver.system;

import android.annotation.SuppressLint;
import android.content.Context;

import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.uicontroller.UIController;

public class Palaver implements IPalaver{
    private PalaverEngine palaverEngine;
    private PalaverDB palaverDB;
    private ChatsManager chatsManager;
    private FriendManager friendManager;
    private UIController uiController;
    @SuppressLint("StaticFieldLeak")
    private static Palaver palaverInstance;
    private Context context;

    public static Palaver getInstance(){
        if(palaverInstance==null){
            palaverInstance = new Palaver();
        }
        return palaverInstance;
    }

    @Override
    public void startPalaver(Context applicationContext) {
        this.context = applicationContext;
        this.palaverDB = PalaverDB.getPalaverDBInstace(applicationContext);
    }

    @Override
    public void destroy() {
        palaverDB.deleteAllDataOnDataBase();
    }

    private Palaver() {
        palaverInstance = this;
        this.chatsManager = new ChatsManager();
        this.friendManager = new FriendManager();
        this.palaverEngine = new PalaverEngine(chatsManager, friendManager);
        this.uiController = new UIController();
    }

    public UIController getUiController() {
        return uiController;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public ChatsManager getChatsManager() {
        return chatsManager;
    }

    public PalaverEngine getPalaverEngine() {
        return palaverEngine;
    }

    public PalaverDB getPalaverDB() {
        if(palaverDB==null){
            return PalaverDB.getPalaverDBInstace(context);
        }
        return palaverDB;
    }
}
