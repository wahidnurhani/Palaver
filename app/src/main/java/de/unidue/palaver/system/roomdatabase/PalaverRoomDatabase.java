package de.unidue.palaver.system.roomdatabase;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import de.unidue.palaver.system.model.Chat;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;


@Database(entities = {Friend.class, Message.class},
        version = DBContract.DB_VERSION,
        exportSchema = false, views = {Chat.class})
public abstract class PalaverRoomDatabase extends RoomDatabase {
    private static String TAG = PalaverRoomDatabase.class.getSimpleName();

    public abstract PalaverDao palaverDao();
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @SuppressLint("StaticFieldLeak")
    private static volatile PalaverRoomDatabase palaverRoomInstance;

    public synchronized static PalaverRoomDatabase getDatabase(final Context context) {
        if(palaverRoomInstance == null){
            synchronized (PalaverRoomDatabase.class) {
                palaverRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                         PalaverRoomDatabase.class, DBContract.DB_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }
        }
        return palaverRoomInstance;
    }
    
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}
