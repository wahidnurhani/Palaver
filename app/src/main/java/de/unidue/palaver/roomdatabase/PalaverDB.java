package de.unidue.palaver.roomdatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import de.unidue.palaver.model.Chat;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;


@Database(entities = {Friend.class, Message.class},
        version = DBContract.DB_VERSION,
        exportSchema = false, views = {Chat.class})
public abstract class PalaverDB extends RoomDatabase {
    private static String TAG = PalaverDB.class.getSimpleName();

    public abstract PalaverDao palaverDao();
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @SuppressLint("StaticFieldLeak")
    private static volatile PalaverDB palaverDBInstance;

    public synchronized static PalaverDB getDatabase(final Context context) {
        if(palaverDBInstance == null){
            synchronized (PalaverDB.class) {
                Log.i(TAG, "create database");
                palaverDBInstance = Room.databaseBuilder(context.getApplicationContext(),
                         PalaverDB.class, DBContract.DB_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }
        }
        return palaverDBInstance;
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
