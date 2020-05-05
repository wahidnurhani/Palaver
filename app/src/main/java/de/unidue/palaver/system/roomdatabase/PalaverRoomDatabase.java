package de.unidue.palaver.system.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {FriendSchema.class}, version = 1)
public abstract class PalaverRoomDatabase extends RoomDatabase {

    public abstract PalaverDao friendsDao();

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
