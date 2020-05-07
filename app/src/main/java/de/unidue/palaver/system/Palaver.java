package de.unidue.palaver.system;

import android.annotation.SuppressLint;
import android.content.Context;

import de.unidue.palaver.system.database.PalaverDB;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.roomdatabase.DatabaseCleaner;
import de.unidue.palaver.ui.uicontroller.UIController;

public class Palaver implements IPalaver{
    private PalaverEngine palaverEngine;
    private PalaverDB palaverDB;
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
        DatabaseCleaner databaseCleaner = new DatabaseCleaner(context);
        databaseCleaner.cleanDatabase();
    }

    private Palaver() {
        palaverInstance = this;;
        this.palaverEngine = new PalaverEngine();
        this.uiController = new UIController();
    }

    public UIController getUiController() {
        return uiController;
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
