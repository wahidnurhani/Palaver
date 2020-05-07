package de.unidue.palaver.system.roomdatabase;

import android.content.Context;
import android.os.AsyncTask;

public class DatabaseCleaner {

    Context context;


    public DatabaseCleaner(Context context) {
        this.context = context;
    }

    public void cleanDatabase(){
        CleanDatabase cleanDatabase = new CleanDatabase();
        cleanDatabase.execute();
    }

    private class CleanDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            PalaverRoomDatabase palaverRoomDatabase = PalaverRoomDatabase.getDatabase(context);
            PalaverDao palaverDao = palaverRoomDatabase.palaverDao();

            palaverDao.deleteAllChat();
            palaverDao.deleteFriend();
            return null;
        }
    }
}
