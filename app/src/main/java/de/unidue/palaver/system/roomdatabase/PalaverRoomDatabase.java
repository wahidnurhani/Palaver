package de.unidue.palaver.system.roomdatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;

@Database(entities = {Friend.class, Message.class}, version = 1)
public abstract class PalaverRoomDatabase extends RoomDatabase {

    public abstract PalaverDao palaverDao();
    public static Context context;

    private static volatile PalaverRoomDatabase palaverRoomInstance;

    public static PalaverRoomDatabase getDatabase(final Context context) {
        if(palaverRoomInstance == null){
            synchronized (PalaverRoomDatabase.class) {
                palaverRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                         PalaverRoomDatabase.class, "palaver_database").build();
            }
        }
        return palaverRoomInstance;
    }
}
