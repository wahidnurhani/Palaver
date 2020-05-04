package de.unidue.palaver.system;

import android.annotation.SuppressLint;
import android.content.Context;

import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.engine.PalaverEngine;

public class Palaver implements IPalaver{
    private PalaverEngine palaverEngine;
    private PalaverDB palaverDB;
    private ChatsManager chatsManager;
    private FriendManager friendManager;
    private UIManager uiManager;
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
        palaverInstance = null;
    }

    private Palaver() {
        palaverInstance =this;
        this.palaverEngine = new PalaverEngine();
        this.chatsManager = new ChatsManager();
        this.friendManager = new FriendManager();
        this.uiManager = new UIManager();
    }

    public UIManager getUiManager() {
        return uiManager;
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
