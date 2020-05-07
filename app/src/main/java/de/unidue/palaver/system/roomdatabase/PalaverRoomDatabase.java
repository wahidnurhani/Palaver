package de.unidue.palaver.system.roomdatabase;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;

@Database(entities = {Friend.class, Message.class}, version = DBContract.DB_VERSION)
public abstract class PalaverRoomDatabase extends RoomDatabase {

    public abstract PalaverDao palaverDao();
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @SuppressLint("StaticFieldLeak")
    private static volatile PalaverRoomDatabase palaverRoomInstance;

    public static PalaverRoomDatabase getDatabase(final Context context) {
        if(palaverRoomInstance == null){
            synchronized (PalaverRoomDatabase.class) {
                palaverRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                         PalaverRoomDatabase.class, DBContract.DB_NAME).build();
            }
        }
        return palaverRoomInstance;
    }
}
